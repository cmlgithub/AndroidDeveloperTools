package com.cml.androiddevelopertools.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cml.androidcmldevelopertools.adapter.HolderAdapter;
import com.cml.androidcmldevelopertools.adapter.ListViewAdapter;
import com.cml.androiddevelopertools.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapterActivity extends AppCompatActivity {

    public static void startMe(Context context){
        context.startActivity(new Intent(context,ListViewAdapterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_adapter);
        ListView mListView = (ListView) findViewById(R.id.listView);


        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");

        //原生的adapter方式
//        mListView.setAdapter(new customeAdapter());

        //封装一层之后的adapter
//        mListView.setAdapter(new ListAdapter(this,list));

        //封装两层之后的adapter
        mListView.setAdapter(new holderAdapter(this,list));

    }

     class holderAdapter extends HolderAdapter<String,holderAdapter.ViewHolder>{

         public holderAdapter(Context context, List<String> list) {
             super(context, list);
         }

         @Override
         protected void bindViewDatas(ViewHolder holder, String s, int position) {
             holder.textView.setText(position+s+"");
         }

         @Override
         protected ViewHolder buildHolder(View view, String s, int position) {
             ViewHolder viewHolder = new ViewHolder();
             viewHolder.textView = view.findViewById(R.id.textView);
             return viewHolder;
         }

         @Override
         public View buildConverView(LayoutInflater layoutInflater, String s, int position) {
             return layoutInflater.inflate(R.layout.item,null);
         }

         public  class ViewHolder{
            TextView textView;
        }
    }

    class ListAdapter extends ListViewAdapter<String>{

        public ListAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ListViewAdapterActivity.this);
            textView.setText(i+"");
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            return textView;
        }
    }

    class customeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView textView = new TextView(ListViewAdapterActivity.this);
            textView.setText(i+"");
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));

            return textView;
        }
    }
}
