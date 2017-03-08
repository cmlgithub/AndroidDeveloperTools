package com.cml.androidcmldevelopertools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 */

public class ScreenUtils {

    /**
     * Get DisplayMetrics
     * eg DisplayMetrics displayMetrics = getScreenMetrics();
     *          metrics.heightPixels
     *          metrics.widthPixels
     */
    public static DisplayMetrics getScreenMetrics(){
        return new DisplayMetrics();
    }

    /**
     * Get Screen Width
     */
    public static int getScreeningWidth(Activity activity){
        WindowManager windowManager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * Get Screen Height
     */
    public static int getScreeningHeight(Activity activity){
        WindowManager windowManager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * px 2 dip/dp
     */
    public static int px2dp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }

    /**
     * dip/dp 2 px
     */
    public static int dp2px(Context context ,float dpValue){
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int)(dpValue*scale+0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }

    /**
     * px 2 sp
     */
    public static int px2sp(Context context,float pxValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue/fontScale+0.5f);
    }

    /**
     * sp 2 px
     */
    public static int sp2px(Context context,float spValue){
//        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return (int)(spValue*fontScale+0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,context.getResources().getDisplayMetrics());
    }

}
