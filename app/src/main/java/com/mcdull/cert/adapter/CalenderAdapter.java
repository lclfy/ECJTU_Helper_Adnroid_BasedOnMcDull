package com.mcdull.cert.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcdull.cert.R;

/**
 * Created by mcdull on 15/7/11.
 */

public class CalenderAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;

    public CalenderAdapter(Context context, List<Map<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list==null) {
            return 0;
        }else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.school_calender_display, null);
        TextView tv_FirstLine = (TextView) view.findViewById(R.id.tv_FirstLine);
        TextView tv_SecondLine = (TextView) view.findViewById(R.id.tv_SecondLine);
        TextView tv_ClassTime = (TextView) view.findViewById(R.id.tv_classTime);

        Map<String,String> item = list.get(position);
        if (item.get("course").length()>14){
            //太长了显示不下
            tv_FirstLine.setText(item.get("course").substring(0,14)+"…");
        }else {
            tv_FirstLine.setText(item.get("course"));
        }
        tv_ClassTime.setText(item.get("classString"));
        tv_SecondLine.setText(item.get("classRoom"));

        ImageView MainImage = (ImageView)view.findViewById(R.id.MainImage);
        switch (position%5){
            case 0:
                MainImage.setImageResource(R.drawable.ic_today_color1_48dp);
                break;
            case 1:
                MainImage.setImageResource(R.drawable.ic_today_color2_48dp);
                break;
            case 2:
                MainImage.setImageResource(R.drawable.ic_today_color3_48dp);
                break;
            case 3:
                MainImage.setImageResource(R.drawable.ic_today_color4_48dp);
                break;
            case 4:
                MainImage.setImageResource(R.drawable.ic_today_color5_48dp);
                break;
        }

        return view;
    }

    void upDateList(List<Map<String, String>> list){
        this.list = list;
        notifyDataSetChanged();
    }


}

