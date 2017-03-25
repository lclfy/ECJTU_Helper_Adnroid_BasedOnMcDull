package com.mcdull.cert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcdull.cert.R;
import com.mcdull.cert.domain.Location;

import java.util.LinkedList;

/**
 * Created by mcdull on 15/7/31.
 */
public class MapMenuAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<Location> list;

    public MapMenuAdapter(Context context,LinkedList<Location> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        tvItemScore.setText(list.get(position).getName());
        return view;
    }

    public void upDateList(LinkedList<Location> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
