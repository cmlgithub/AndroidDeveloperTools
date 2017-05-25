package com.cml.androiddevelopertools.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.cml.androiddevelopertools.R;

/**
 * author：cml on 2017/5/19
 * github：https://github.com/cmlgithub
 */

public class ImageRadioButton extends AppCompatRadioButton {

    private float mImgHeight, mImgWidth;

    public ImageRadioButton(Context context) {
        this(context,null);
    }

    public ImageRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable drawableTop = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageRadioButton);

        mImgWidth = typedArray.getDimension(R.styleable.ImageRadioButton_drawableTopWidth,24);
        mImgHeight = typedArray.getDimension(R.styleable.ImageRadioButton_drawableTopHeight,24);
        drawableTop = typedArray.getDrawable(R.styleable.ImageRadioButton_drawableTop);

        typedArray.recycle();

        setCompoundDrawablesWithIntrinsicBounds(null,drawableTop,null,null);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (top != null) {
            top.setBounds(0, 0, dp2px(mImgWidth), dp2px(mImgHeight));
        }

        setCompoundDrawables(left, top, right, bottom);
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal, getResources().getDisplayMetrics());
    }


}
