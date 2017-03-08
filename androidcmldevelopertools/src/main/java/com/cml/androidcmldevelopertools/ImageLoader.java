package com.cml.androidcmldevelopertools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author：cml on 2017/1/19
 * github：https://github.com/cmlgithub
 */

public class ImageLoader {
    private String TAG = "CML";
    private static final int TAG_KEY_URI = R.id.imageLoaderId;
    private static final String DISK_UNIQUENAME = "bitmap";
    private static final long DISK_CACHE_SIZE = 1024*1024*50;//50M
    private static final int DISK_CACHE_INDEX = 0;
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int IO_BUFFER_SIZE = 8*1024;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT +1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;



    private Context mContext;
    private  DiskLruCache mDiskLruCache;
    private  boolean mIsDiskLruCacheCreated;
    private  LruCache<String, Bitmap> mMemoryLruCache;


    private Handler mMainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if(uri.equals(result.uri)){
                imageView.setImageBitmap(result.bitmap);
            }else {
                Log.w(TAG,"set image bitmap,but url has changed,ignored!");
            }
        }
    };

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ImageLoader#"+mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),sThreadFactory);


    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }

    private ImageLoader(Context context){
        mContext = context.getApplicationContext();

        //初始化内存缓存(LruCache)
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory /8;
        mMemoryLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        //初始化sd卡缓存(DiskLruCache)
        File diskCacheDir = getDiskCacheDir(context, DISK_UNIQUENAME);
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }

        if(getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private File getDiskCacheDir(Context context,String uniqueName){
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if(externalStorageAvailable){
            cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator +uniqueName);
    }

    private long getUsableSpace(File path){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        final StatFs statFs = new StatFs(path.getPath());
        return (long)statFs.getBlockSize() * (long)statFs.getAvailableBlocks();
    }

    /**
     * 对url进行md5的处理,防止url中有特殊字符导致不能处理
     */
    private String hashKeyFormUrl(String url){
        String cacheKey = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String byteToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if(hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void bindBitmap(final String url, final ImageView imageView){
        bindBitmap(url,imageView,100,100);
    }

    public void bindBitmap(final String url, final ImageView imageView,final int reqWidth,final int reqHeight){
        imageView.setTag(TAG_KEY_URI,url);
        Bitmap bitmap = loadBitmapFromMemoryCache(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                LoaderResult result = new LoaderResult(imageView,url,bitmap);
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    public Bitmap loadBitmap(String uri,int reqWidth ,int reqHeight){
        Bitmap bitmap = null;
        //从内存中加载图片
        bitmap = loadBitmapFromMemoryCache(uri);
        if(bitmap != null){
            return bitmap;
        }

        //从sd卡中加载图片
        try {
            bitmap = loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if(bitmap != null){
                return bitmap;
            }

            bitmap = loadBitmapFromHttp(uri,reqWidth,reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String uri, int reqWidth, int reqHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("can't visit network from UI Thread.");
        }

        if(mDiskLruCache == null){
            return null;
        }

        String key = hashKeyFormUrl(uri);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if(editor != null){
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if(downloadUrlToStream(uri,outputStream)){
                editor.commit();
            }else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
    }

    private boolean downloadUrlToStream(String uri, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);

            int b;
            while ((b = in.read())!= -1){
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG,"download Bitmap failed."+e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            closeInputStream(in);
            closeOutputStream(out);

        }
        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String uri,int reqWidth,int reqHeight) throws IOException {
        Bitmap bitmap = null;
        if(Looper.myLooper() == Looper.getMainLooper()){
            Log.w(TAG,"不推荐在主线程中加载图片");
        }

        if(mDiskLruCache == null){
            return null;
        }

        String key = hashKeyFormUrl(uri);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if(snapshot != null){
            FileInputStream fileInputStream = (FileInputStream)snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = BitmapUtils.decodeSampleBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
            if(bitmap != null){
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemoryCache(key) == null){
            mMemoryLruCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryLruCache.get(key);
    }

    private Bitmap loadBitmapFromMemoryCache(String url) {
        final String key = hashKeyFormUrl(url);
        return getBitmapFromMemoryCache(key);
    }

    private void closeOutputStream(OutputStream out){
        if(out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeInputStream(InputStream in){
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static class LoaderResult{
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;
        public LoaderResult(ImageView imageView,String uri,Bitmap bitmap){
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
