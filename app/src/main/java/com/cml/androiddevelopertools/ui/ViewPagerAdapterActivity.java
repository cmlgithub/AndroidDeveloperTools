package com.cml.androiddevelopertools.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cml.androidcmldevelopertools.adapter.ViewPagerAdapter;
import com.cml.androiddevelopertools.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapterActivity extends AppCompatActivity {

    public static void startMe(Context context){
        context.startActivity(new Intent(context,ViewPagerAdapterActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_adapter);

        List<View> mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setText(i+"");
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mList.add(textView);
        }

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
//        mViewPager.setAdapter(new customeAdapter());
        mViewPager.setAdapter(new ViewPagerAdapter(mList));
    }


    class customeAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TextView textView = new TextView(ViewPagerAdapterActivity.this);
            textView.setText(position+"");
            textView.setTextSize(20);
            container.addView(textView);

            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
