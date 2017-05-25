package com.cml.androidcmldevelopertools.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * author：cml on 2017/5/19
 * github：https://github.com/cmlgithub
 */

public class CmlFragment extends Fragment {

    private TextView mTextView;
    public static final String TEXT = "text";

    public static CmlFragment newInstance(@NonNull String text){
        CmlFragment cmlFragment = new CmlFragment();
        Bundle args = new Bundle();
        args.putString(TEXT,text);
        cmlFragment.setArguments(args);
        return cmlFragment;
    }
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTextView = new TextView(mContext);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(20);
        return mTextView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView.setText(getArguments().getString(TEXT));
    }
}
