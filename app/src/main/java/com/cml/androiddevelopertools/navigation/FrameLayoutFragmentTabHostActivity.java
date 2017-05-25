package com.cml.androiddevelopertools.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.cml.androidcmldevelopertools.navigation.CmlFragment;
import com.cml.androiddevelopertools.R;

public class FrameLayoutFragmentTabHostActivity extends AppCompatActivity {

    private FragmentTabHost mFragmentTabHost;
    private String[] names ;
    private int[] imgSelectors = new int[]{R.drawable.bottom_drawable_selector1, R.drawable.bottom_drawable_selector2,
            R.drawable.bottom_drawable_selector3, R.drawable.bottom_drawable_selector4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout_fragment_tab_host);

        mFragmentTabHost = (FragmentTabHost) findViewById(R.id.fragmentTabHost);

        mFragmentTabHost.setup(this,getSupportFragmentManager(),R.id.frameLayout);

        names = new String[]{getResources().getString(R.string.main_bottom_text_one),
                getResources().getString(R.string.main_bottom_text_two), getResources().getString(R.string.main_bottom_text_three),
                getResources().getString(R.string.main_bottom_text_four)};

        for (int i = 0; i < names.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_tab, null);

            ImageView ivImg = (ImageView) view.findViewById(R.id.iv_img);
            TextView tvText = (TextView) view.findViewById(R.id.tv_text);
            ivImg.setImageResource(imgSelectors[i]);
            tvText.setText(names[i]);

            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(names[i])
                    .setIndicator(view);

            Bundle bundle = new Bundle();
            bundle.putString(CmlFragment.TEXT, names[i]);

            mFragmentTabHost.addTab(tabSpec, CmlFragment.class, bundle);
        }

    }

    public static void startMe(Activity activity){
        activity.startActivity(new Intent(activity,FrameLayoutFragmentTabHostActivity.class));
    }
}
