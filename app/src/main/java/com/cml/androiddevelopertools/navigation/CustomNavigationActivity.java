package com.cml.androiddevelopertools.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cml.androidcmldevelopertools.navigation.CmlFragment;
import com.cml.androidcmldevelopertools.navigation.NavigationChild;
import com.cml.androidcmldevelopertools.navigation.NavigationView;
import com.cml.androiddevelopertools.R;

import java.util.ArrayList;
import java.util.List;

public class CustomNavigationActivity extends AppCompatActivity {


    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_bottom_navigation);

        mNavigationView = (NavigationView)findViewById(R.id.navigationView);

        List<NavigationChild> mList = new ArrayList<>();
        mList.add(new NavigationChild(R.drawable.icon_one_select,R.drawable.icon_one_unselect,"首页", CmlFragment.newInstance("首页")));
        mList.add(new NavigationChild(R.drawable.icon_two_select,R.drawable.icon_two_unselect,"次页",CmlFragment.newInstance("次页")));
        mList.add(new NavigationChild(R.drawable.icon_three_select,R.drawable.icon_three_unselect,"三页",CmlFragment.newInstance("三页")));
        mList.add(new NavigationChild(R.drawable.icon_four_select,R.drawable.icon_four_unselect,"四页",CmlFragment.newInstance("四页")));

        mNavigationView.setNavigationViewChild(mList,getSupportFragmentManager());

    }

    public static void startMe(Activity activity){
        activity.startActivity(new Intent(activity,CustomNavigationActivity.class));
    }
}
