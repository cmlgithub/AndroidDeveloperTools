package com.cml.androidcmldevelopertools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chenmingliang on 2017/11/6.
 */

public abstract class HolderAdapter<T,H> extends ListViewAdapter {


    private final LayoutInflater layoutInflater;

    public HolderAdapter(Context context, List<T> list) {
        super(context, list);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        H holder = null;
        T t =  (T) mList.get(position);

        if(view == null){
            view = buildConverView(layoutInflater,t,position);
            holder = buildHolder(view,t,position);
        }else {
            holder = (H) view.getTag();
        }
        bindViewDatas(holder,t,position);
        return view;
    }

    protected abstract void bindViewDatas(H holder, T t, int position);

    protected abstract H buildHolder(View view, T t, int position);

    public abstract View buildConverView(LayoutInflater layoutInflater, T t, int position) ;
}
