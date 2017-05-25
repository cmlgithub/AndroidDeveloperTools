package com.cml.androiddevelopertools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cml.androiddevelopertools.navigation.CustomNavigationActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickOne(View view){
        ImageLoaderActivity.startMe(this);
    }

    public void clickTwo(View view){
        NavigationActivity.startMe(this);
    }

    public void clickThree(View view){
        CustomNavigationActivity.startMe(this);
    }
}
