package com.mcdull.cert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcdull.cert.R;

import java.util.List;

/**
 * Created by mcdull on 15/8/16.
 */
public class MySpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> list;

    public MySpinnerAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list==null||list.size()==0){
            return 0;
        }else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.select_item, null);
        TextView tvItemScore = (TextView) view.findViewById(R.id.tv_select_item);
        tvItemScore.setText(list.get(position));
        return view;
    }
}
