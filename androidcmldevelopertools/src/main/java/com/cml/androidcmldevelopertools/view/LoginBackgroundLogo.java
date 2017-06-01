package com.cml.androidcmldevelopertools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cml.androidcmldevelopertools.R;

import static com.cml.androidcmldevelopertools.utils.ScreenUtils.dp2px;
import static com.cml.androidcmldevelopertools.utils.ScreenUtils.sp2px;

/**
 * author：cml on 2017/5/31
 * github：https://github.com/cmlgithub
 */

public class LoginBackgroundLogo extends FrameLayout {

    private int mLogoCopyrightTextColor;
    private int mLogoNameTextColor;
    private float mLogoCopyrightTextSize;
    private float mLogoNameToLogoDistance;
    private float mLogoNameTextSize;
    private float mLogoWidth;
    private float mLogoHeight;
    private Drawable mLogoDrawable;
    private Bitmap mLogoBitmap;
    private String mLogoName;
    private String mLogoCopyright;

    private static final String DEFAULT_LOGO_NAME = "Logo";
    private static final String DEFAULT_LOGO_COPYRIGHT = "TM";
    private static final float DEFAULT_LOGO_WIDTH = 80;
    private static final float DEFAULT_LOGO_HEIGHT = 80;
    private static final float DEFAULT_LOGO_NAME_TEXTSIZE = 16;
    private static final float DEFAULT_LOGO_Copyright_TEXTSIZE = 8;
    private static final float DEFAULT_LOGO_NAME_TO_LOGO_DISTANCE = 20;
    private Paint mPaint;

    public LoginBackgroundLogo(@NonNull Context context) {
        this(context,null);
    }

    public LoginBackgroundLogo(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginBackgroundLogo(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginBackgroundLogo);
        mLogoName = typedArray.getString(R.styleable.LoginBackgroundLogo_logoName);
        mLogoWidth = typedArray.getDimension(R.styleable.LoginBackgroundLogo_logoWidth,dp2px(context,DEFAULT_LOGO_WIDTH));
        mLogoHeight = typedArray.getDimension(R.styleable.LoginBackgroundLogo_logoWidth,dp2px(context,DEFAULT_LOGO_HEIGHT));
        mLogoNameTextSize = typedArray.getDimension(R.styleable.LoginBackgroundLogo_logoNameTextSize,sp2px(context, DEFAULT_LOGO_NAME_TEXTSIZE));
        mLogoNameTextColor = typedArray.getColor(R.styleable.LoginBackgroundLogo_logoNameTextColor, Color.WHITE);
        mLogoCopyrightTextSize = typedArray.getDimension(R.styleable.LoginBackgroundLogo_logoCopyrightTextSize,sp2px(context,DEFAULT_LOGO_Copyright_TEXTSIZE));
        mLogoCopyrightTextColor = typedArray.getColor(R.styleable.LoginBackgroundLogo_logoCopyrightTextColor, Color.WHITE);
        mLogoNameToLogoDistance = typedArray.getDimension(R.styleable.LoginBackgroundLogo_logoNameToLogoDistance,dp2px(context,DEFAULT_LOGO_NAME_TO_LOGO_DISTANCE));
        mLogoCopyright = typedArray.getString(R.styleable.LoginBackgroundLogo_logoCopyright);
        mLogoDrawable = typedArray.getDrawable(R.styleable.LoginBackgroundLogo_logoCopyright);

        if(TextUtils.isEmpty(mLogoName)){
            mLogoName = DEFAULT_LOGO_NAME;
        }
        if(TextUtils.isEmpty(mLogoCopyright)){
            mLogoCopyright = DEFAULT_LOGO_COPYRIGHT;
        }
        if(null == mLogoDrawable){
            mLogoBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        }else {
            BitmapDrawable bd = (BitmapDrawable) mLogoDrawable;
            mLogoBitmap = bd.getBitmap();
        }
        typedArray.recycle();

        mLogoBitmap = zoomBitmap(mLogoBitmap,mLogoWidth,mLogoHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }


    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
        mHeightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mLogoBitmap,mWidthMeasureSpec/2-mLogoBitmap.getWidth()/2,mHeightMeasureSpec/2-mLogoBitmap.getHeight()/2,mPaint);

        mPaint.setTextSize(mLogoNameTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mLogoNameTextColor);
        canvas.drawText(mLogoName,mWidthMeasureSpec/2,mHeightMeasureSpec/2+mLogoBitmap.getHeight()/2+mLogoNameToLogoDistance,mPaint);

        mPaint.setColor(mLogoCopyrightTextColor);
        mPaint.setTextSize(mLogoCopyrightTextSize);
        canvas.drawText(mLogoCopyright,mWidthMeasureSpec/2 + mLogoWidth/2 - mLogoNameToLogoDistance/2,
                mHeightMeasureSpec/2+mLogoHeight/2+mLogoNameToLogoDistance/2,mPaint);

    }


    public Bitmap zoomBitmap(Bitmap bm, float newWidth , float newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ( newWidth) / width;
        float scaleHeight = ( newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
