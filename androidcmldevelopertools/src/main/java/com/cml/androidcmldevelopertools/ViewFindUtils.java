package com.cml.androidcmldevelopertools;

import android.util.SparseArray;
import android.view.View;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 */

public class ViewFindUtils {


    /**
     * ViewHolder concise wording, avoid duplicate definition ViewHolder adapter, to reduce the amount of code
     * @param view
     * @param id
     * @param <T>
     * @return
     *
     * eg:
     * if (convertView == null) {
     * 	convertView = View.inflate(context, R.layout.demo, null);
     * }
     * TextView tv_demo = ViewFindUtils.get(convertView, R.id.tv_demo);
     */
    public static <T extends View> T hold(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);

        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    /**
     * Alternative findviewById
     */
    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

}
