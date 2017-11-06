package com.cml.androiddevelopertools.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cml.androiddevelopertools.R;

public class TotalUseAdapterActivity extends AppCompatActivity {

    public static void startMe(Context context){
        context.startActivity(new Intent(context,TotalUseAdapterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_use_adapter);
    }

    public void viewPagerAdapter(View view){
        ViewPagerAdapterActivity.startMe(this);
    }

    public void AbstractAdapter(View view){
        ListViewAdapterActivity.startMe(this);
    }

}
