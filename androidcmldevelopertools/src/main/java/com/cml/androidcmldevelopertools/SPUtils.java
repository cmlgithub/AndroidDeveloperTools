package com.cml.androidcmldevelopertools;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 */
public class SPUtils {

    /**
     * File name stored in the phone
     */
    public static final String FILE_NAME = "sp_data";
    public static final String FIRST = "first";


    /**
     * Save int type parameters
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * Get int type parameters
     *
     * default 0
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * Save String type parameters
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * Get String type parameters
     *
     * default ""
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * Save boolean type parameters
     *
     * For storage is the first time to enter the program records
     */
    public static void putIsFirstBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(FIRST,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * Get boolean type parameters
     *
     * default true
     */
    public static boolean getIsFirstBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(FIRST,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
    /**
     * Save boolean type parameters
     */
    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * Get boolean type parameters
     *
     * default false
     */
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }


}
