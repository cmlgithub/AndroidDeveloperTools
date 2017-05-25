package com.cml.androidcmldevelopertools.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 */

public class AppUtils {

    /**
     * get App versionCode
     *
     * fail return -1
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(packInfo != null){
            return packInfo.versionCode;
        }else {
            return -1;
        }
    }

    /**
     * get App versionName
     *
     * fail return ""
     */
    public static String getVersionName(Context context)  {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(packInfo != null){
            return packInfo.versionName;
        }else {
            return "";
        }

    }

    /**
     * install apk
     */
    public static  void installApk(Context context, String apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
