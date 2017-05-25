package com.cml.androidcmldevelopertools.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 *
 * Soft keyboard related tools
 */
public class KeyBoardUtils {
	 /**
     * open
     */
    public static void openKeybord(EditText mEditText, Context mContext){
        InputMethodManager imm = (InputMethodManager) mContext  
                .getSystemService(Context.INPUT_METHOD_SERVICE);  
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);  
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,  
                InputMethodManager.HIDE_IMPLICIT_ONLY);  
    }  
  
    /** 
     * close
     */
    public static void closeKeybord(EditText mEditText, Context mContext){
        InputMethodManager imm = (InputMethodManager) mContext  
                .getSystemService(Context.INPUT_METHOD_SERVICE);  
  
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);  
    } 
}
