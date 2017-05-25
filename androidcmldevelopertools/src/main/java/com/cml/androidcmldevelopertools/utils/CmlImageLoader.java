package com.cml.androidcmldevelopertools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author：cml on 2017/1/17
 * github：https://github.com/cmlgithub
 */

public class CmlImageLoader {

    private final Context mContext;
    private LruCache<String,Bitmap> mMemoryCache;
    private final String TAG = "CML";
    private DiskLruCache mDiskLruCache;
    private int DISK_CACHE_INDEX = 0;
    private static final int MESSAGE_POST_RESULT = 0;
    private static final int IO_BUFFER_SIZE = 8 *1024;
    private static boolean mIsDiskLruCacheCreated ;
    private int TAG_KEY_URI = 0;

    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoaderResult result = (LoaderResult)msg.obj;
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

    public static CmlImageLoader build(Context context){
        return new CmlImageLoader(context);
    }

    public void bindBitmap(final String uri,final ImageView imageView){
        asyncLoadBitmap(uri,imageView,0,0);
    }

    private CmlImageLoader(Context context){
        mContext = context.getApplicationContext();

        mMemoryCache = CacheUtils.getBitmapLruCacheInstance();

        try {
            mDiskLruCache = CacheUtils.getDiskLruCache(context);
            if(mDiskLruCache !=null){
                mIsDiskLruCacheCreated = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(mMemoryCache.get(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap loadBitmapFromHttp(String url ,int reqWidth,int reqHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("can not visit network from UI Thread.");
        }

        if(mDiskLruCache == null){
            return null;
        }

        String key = EncryptionUtils.hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if(editor != null){
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if(downloadUrlToStream(url,outputStream)){
                editor.commit();
            }else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1){
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG,"downloadBitmap failed."+e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,int reqHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()){
            Log.w(TAG,"load bitmap from UI Thread, it's not recommended!");
        }

        if(mDiskLruCache == null){
            return null;
        }

        Bitmap bitmap = null;
        String key = EncryptionUtils.hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if(snapshot != null){
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = BitmapUtils.decodeSampleBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if(bitmap != null){
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    public Bitmap syncLoadBitmap(String uri, int reqWidth,int reqHeight){
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if(bitmap != null){
            Log.d(TAG,"loadBitmapFromMemoryCache,url:"+uri);
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if(bitmap != null){
                Log.d(TAG,"loadBitmapFromDiskCache,url:"+uri);
                return bitmap;
            }

            bitmap = loadBitmapFromHttp(uri,reqWidth,reqHeight);
            Log.d(TAG,"loadBitmapFromHttp,url:"+uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap == null && !mIsDiskLruCacheCreated){
            Log.w(TAG,"encounter error,DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    public void asyncLoadBitmap(final String uri, final ImageView imageView,final int reqWidth,final int reqHeight){
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return ;
        }
        Runnable loadBitmapTask = new Runnable(){

            @Override
            public void run() {
                Bitmap bitmap = syncLoadBitmap(uri, reqWidth, reqHeight);
                if(bitmap  != null){
                    LoaderResult result = new LoaderResult(imageView,uri,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
                }
            }
        };
        ThreadPoolUtils.THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }


    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG,"Error in downloadBitmap:"+e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String uri) {
        final String key = EncryptionUtils.hashKeyFormUrl(uri);
        Bitmap bitmap = getBitmapFromMemoryCache(key);
        return bitmap;
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
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
