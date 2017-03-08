package com.cml.androidcmldevelopertools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * author：cml on 2017/1/13
 * github：https://github.com/cmlgithub
 */

public class BitmapUtils {



    /**
     * inJustDecodeBounds = true; BitmapFactory.decodeResource() Do not load the image into memory just to read the picture wide and high
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeResource(res,resId,options);
    }

    public static Bitmap decodeSampleBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        int inSampleSize = 1;
        if(outWidth > reqHeight || outHeight > reqHeight){
            final int halfWidth = outWidth /2;
            final int halfHeight = outHeight /2;
            while ((halfWidth/inSampleSize) >= reqWidth || (halfHeight/inSampleSize) >= reqHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }



}
