package com.cml.androidcmldevelopertools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.IOException;

/**
 * author：cml on 2017/1/17
 * github：https://github.com/cmlgithub
 */

public class CacheUtils {
    private static final long DISK_CACHE_SIZE = 1024*1024*50;//50MB
    /**
     * A typical LruCache initialization operation
     */
    public static LruCache getBitmapLruCacheInstance(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        return new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                // can ignore
                //Remove old cache callback,so Can do some recycling operations in here
            }
        };
    }

    public static DiskLruCache getDiskLruCache(Context mContext ) throws IOException {
        File diskCacheDir = getBitmapDiskCacheDir(mContext, "bitmap");

        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }

        if(getUsableSpace(diskCacheDir) <= DISK_CACHE_SIZE){
            return null;
        }

        return DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
    }

    private static long getUsableSpace(File diskCacheDir) {
        return diskCacheDir.getUsableSpace();
    }

    private static File getBitmapDiskCacheDir(Context mContext, String bitmap) {
        String cacheFile = null;
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            //  mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);// dir --> /storage/emulated/0/Android/data/app_package_name/files/Pictures
            cacheFile =  mContext.getExternalCacheDir().getPath();//dir--> /storage/emulated/0/Android/data/app_package_name/cache
        }else {
            cacheFile =  mContext.getCacheDir().getPath();//dir --> /data/data/<application package>/cache
        }

        return new File(cacheFile + File.separator + bitmap);//cache bitmap
    }


}
