package com.cml.androidcmldevelopertools.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

    public static void toastShort(Context context,String toast){
        if(context != null && !TextUtils.isEmpty(toast)){
            Toast.makeText(context,toast,Toast.LENGTH_SHORT).show();
        }
    }
    public static void toastLong(Context context,String toast){
        if(context != null && !TextUtils.isEmpty(toast)){
            Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
        }
    }
}
