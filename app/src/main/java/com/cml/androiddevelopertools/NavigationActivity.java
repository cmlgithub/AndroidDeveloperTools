package com.cml.androiddevelopertools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cml.androiddevelopertools.navigation.CustomNavigationActivity;
import com.cml.androiddevelopertools.navigation.FrameLayoutFragmentTabHostActivity;
import com.cml.androiddevelopertools.navigation.RadioGroupViewPagerActivity;

public class NavigationActivity extends AppCompatActivity {

    public static void startMe(Activity activity){
        activity.startActivity(new Intent(activity,NavigationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    public void clickOne(View view){
        RadioGroupViewPagerActivity.startMe(this);
    }

    public void clickTwo(View view){
        FrameLayoutFragmentTabHostActivity.startMe(this);
    }

    public void clickThree(View view){
        CustomNavigationActivity.startMe(this);
    }
}
