package com.cml.androidcmldevelopertools.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cml.androidcmldevelopertools.R;
import com.cml.androidcmldevelopertools.utils.ScreenUtils;

import java.util.List;

/**
 * author：cml on 2017/5/23
 * github：https://github.com/cmlgithub
 */

public class NavigationView extends LinearLayout {

    public static final int TOP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int DEFAULT_NAVIGATION_HEIGHT = 56;
    public static final int DEFAULT_NAVIGATION_WIDTH = 56;
    public static final int DEFAULT_NAVIGATION_ICON_HEIGHT = 25;
    public static final int DEFAULT_NAVIGATION_ICON_WIDTH = 25;
    private static final String DEFAULT_SELECT_TEXT_COLOR = "#d81e06";
    private static final String DEFAULT_UNSELECT_TEXT_COLOR = "#000000";
    private static final String DEFAULT_NAVIGATION_BACKGROUND = "#f0f0f0";

    private Context mContext;
    private int defaultPosition;
    private int mNavigationIconHeight;
    private int mNavigationIconWidth;
    private int mNavigationUnselectTextColor;
    private int mNavigationSelectTextColor;
    private int mNavigationHeight;
    private int mNavigationWidth;
    private int mNavigationLocation;
    private int mNavigationFrameLayoutBackground;
    private int mNavigationLinearLayoutBackground;
    private LinearLayout mLinearLayout;
    private FrameLayout mFrameLayout;
    private List<NavigationChild> mList;
    private FragmentManager mFragmentManager;

    public NavigationView(Context context) {
        this(context,null);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAttrValue(attrs);

        createFrameLayout();

        createLinerLayout();
    }

    private void getAttrValue(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.NavigationView);
        mNavigationLocation = typedArray.getInt(R.styleable.NavigationView_navigationViewLocation, BOTTOM);
        mNavigationHeight = typedArray.getDimensionPixelSize(R.styleable.NavigationView_navigationViewHeight, ScreenUtils.dp2px(mContext, DEFAULT_NAVIGATION_HEIGHT));
        mNavigationWidth = typedArray.getDimensionPixelSize(R.styleable.NavigationView_navigationViewWidth,ScreenUtils.dp2px(mContext, DEFAULT_NAVIGATION_WIDTH));
        mNavigationIconHeight = typedArray.getDimensionPixelSize(R.styleable.NavigationView_navigationViewHeight,ScreenUtils.dp2px(mContext, DEFAULT_NAVIGATION_ICON_HEIGHT));
        mNavigationIconWidth = typedArray.getDimensionPixelSize(R.styleable.NavigationView_navigationViewWidth,ScreenUtils.dp2px(mContext, DEFAULT_NAVIGATION_ICON_WIDTH));
        mNavigationFrameLayoutBackground = typedArray.getColor(R.styleable.NavigationView_navigationFrameLayoutBackground, Color.TRANSPARENT);
        mNavigationLinearLayoutBackground = typedArray.getColor(R.styleable.NavigationView_navigationLinearLayoutBackground, Color.parseColor(DEFAULT_NAVIGATION_BACKGROUND));
        mNavigationUnselectTextColor = typedArray.getColor(R.styleable.NavigationView_navigationUnselectTextColor, Color.parseColor(DEFAULT_UNSELECT_TEXT_COLOR));
        mNavigationSelectTextColor = typedArray.getColor(R.styleable.NavigationView_navigationSelectTextColor, Color.parseColor(DEFAULT_SELECT_TEXT_COLOR));
        typedArray.recycle();
    }


    private void createLinerLayout() {
        mLinearLayout = new LinearLayout(mContext);
        LayoutParams linearLayoutLp = null;
        switch (mNavigationLocation){
            case TOP:
                linearLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mNavigationHeight);
                setOrientation(LinearLayout.VERTICAL);
                this.addView(mLinearLayout);
                this.addView(mFrameLayout);
                break;
            case LEFT:
                linearLayoutLp = new LayoutParams(mNavigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                setOrientation(LinearLayout.HORIZONTAL);
                mLinearLayout.setOrientation(VERTICAL);
                this.addView(mLinearLayout);
                this.addView(mFrameLayout);
                break;
            case RIGHT:
                linearLayoutLp = new LayoutParams(mNavigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                setOrientation(LinearLayout.HORIZONTAL);
                mLinearLayout.setOrientation(VERTICAL);
                this.addView(mFrameLayout);
                this.addView(mLinearLayout);
                break;
            case BOTTOM:
                linearLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mNavigationHeight);
                setOrientation(LinearLayout.VERTICAL);
                this.addView(mFrameLayout);
                this.addView(mLinearLayout);
                break;
        }
        mLinearLayout.setBackgroundColor(mNavigationLinearLayoutBackground);
        mLinearLayout.setLayoutParams(linearLayoutLp);
    }

    private void createFrameLayout() {
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setId(R.id.frameLayout_container_id);
        LayoutParams frameLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayoutLp.weight = 1;
        mFrameLayout.setLayoutParams(frameLayoutLp);
        mFrameLayout.setBackgroundColor(mNavigationFrameLayoutBackground);
    }


    public void setNavigationViewChild(@NonNull List<NavigationChild> mList, @NonNull FragmentManager fragmentManager){
        this.mList = mList;
        this.mFragmentManager = fragmentManager;
        switchSelectItem(defaultPosition);
        mFragmentManager.beginTransaction().replace(R.id.frameLayout_container_id,mList.get(defaultPosition).getFragment()).commit();

    }

    class NavigationViewOnClickListener implements OnClickListener {

        private int mPosition;

        public NavigationViewOnClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            NavigationChild navigationChild = mList.get(mPosition);
            mFragmentManager.beginTransaction().replace(R.id.frameLayout_container_id,navigationChild.getFragment()).commit();
            switchSelectItem(mPosition);
        }
    }

    private void switchSelectItem(int position) {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            NavigationChild navigationChild = mList.get(i);

            LinearLayout linearLayout = createNavigationChildLinearLayout();


            ImageView imageView = createNavigationChildChildImageView();

            TextView textView = createNavigationChildChildTextView();
            textView.setText(navigationChild.getText());

            if(i == position){
                imageView.setImageResource(navigationChild.getSelectIcon());
                textView.setTextColor(mNavigationSelectTextColor);
            }else {
                imageView.setImageResource(navigationChild.getNoSelectIcon());
                textView.setTextColor(mNavigationUnselectTextColor);
            }

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(new NavigationViewOnClickListener(i));
            mLinearLayout.addView(linearLayout);
        }
    }

    private TextView createNavigationChildChildTextView() {
        LayoutParams childTextViewLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(childTextViewLp);
        return textView;
    }

    private ImageView createNavigationChildChildImageView() {
        ImageView imageView = new ImageView(mContext);
        LayoutParams imageViewLp = new LayoutParams(mNavigationIconWidth, mNavigationIconHeight);
        imageView.setLayoutParams(imageViewLp);
        return imageView;
    }

    private LinearLayout createNavigationChildLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams childRootLp = null;
        if(mNavigationLocation == TOP || mNavigationLocation == BOTTOM){
            childRootLp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        }else if(mNavigationLocation == LEFT || mNavigationLocation == RIGHT){
            childRootLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        }
        childRootLp.weight = 1;
        linearLayout.setLayoutParams(childRootLp);
        return linearLayout;
    }

}
