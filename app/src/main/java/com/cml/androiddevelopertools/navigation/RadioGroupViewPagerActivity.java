package com.cml.androiddevelopertools.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.cml.androidcmldevelopertools.navigation.CmlFragment;
import com.cml.androiddevelopertools.R;

import java.util.ArrayList;
import java.util.List;

public class RadioGroupViewPagerActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton mNav1;
    private RadioButton mNav2;
    private RadioButton mNav3;
    private RadioButton mNav4;
    private ViewPager mRadioGroupViewPager_viewPager;
    private List<Fragment> mFragments ;
    private List<RadioButton> mRadioButtons ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_group_view_pager);

        mNav1 = (RadioButton) findViewById(R.id.nav1);
        mNav2 = (RadioButton) findViewById(R.id.nav2);
        mNav3 = (RadioButton) findViewById(R.id.nav3);
        mNav4 = (RadioButton) findViewById(R.id.nav4);
        mRadioButtons = new ArrayList<>();
        mRadioButtons.add(mNav1);
        mRadioButtons.add(mNav2);
        mRadioButtons.add(mNav3);
        mRadioButtons.add(mNav4);

        mRadioGroupViewPager_viewPager = (ViewPager) findViewById(R.id.radioGroupViewPager_ViewPager);

        mNav1.setOnClickListener(this);
        mNav2.setOnClickListener(this);
        mNav3.setOnClickListener(this);
        mNav4.setOnClickListener(this);

        mFragments = new ArrayList<>();
        mFragments.add(CmlFragment.newInstance(getString(R.string.main_bottom_text_one)));
        mFragments.add(CmlFragment.newInstance(getString(R.string.main_bottom_text_two)));
        mFragments.add(CmlFragment.newInstance(getString(R.string.main_bottom_text_three)));
        mFragments.add(CmlFragment.newInstance(getString(R.string.main_bottom_text_four)));

        mRadioGroupViewPager_viewPager.setAdapter(new RadioGroupViewPagerAdapter(getSupportFragmentManager()));
        mRadioGroupViewPager_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchSelectRadioButton(mRadioButtons.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav1:
                switchCurrentItem(0);
                break;
            case R.id.nav2:
                switchCurrentItem(1);
                break;
            case R.id.nav3:
                switchCurrentItem(2);
                break;
            case R.id.nav4:
                switchCurrentItem(3);
                break;
        }
    }

    private void switchCurrentItem(int position){
        mRadioGroupViewPager_viewPager.setCurrentItem(position,false);
    }

    private void switchSelectRadioButton(RadioButton radioButton){
        radioButton.setChecked(true);
    }

    public static void startMe(Activity activity){
        activity.startActivity(new Intent(activity,RadioGroupViewPagerActivity.class));
    }

    class RadioGroupViewPagerAdapter extends FragmentPagerAdapter {

        public RadioGroupViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
