package com.cml.androidcmldevelopertools.utils;

import android.os.Environment;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 */

public class SdCardUtils {

    /**
     *  SD card is mounted
     */
    public static boolean isMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
