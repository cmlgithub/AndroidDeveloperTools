package com.cml.androidcmldevelopertools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by chenmingliang on 2017/11/6.
 */

public abstract class ListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mList;

    public ListViewAdapter(Context context, List<T> list){
        this.mContext = context;
        this.mList = list;

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
