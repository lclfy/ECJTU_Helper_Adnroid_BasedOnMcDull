package com.mcdull.cert.adapter;

/**
 * Created by mcdull on 15/7/11.
 */

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcdull.cert.R;

public class ExamAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,String>> list;

    public ExamAdapter(Context context, List<Map<String,String>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
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
        View view = View.inflate(context, R.layout.select_item_materialstyle, null);

        //把header隐藏
        TextView tvHeader = (TextView)view.findViewById(R.id.tv_Header);
        tvHeader.setVisibility(View.GONE);

        TextView tv_FirstLine = (TextView) view.findViewById(R.id.tv_FirstLine);
        TextView tv_SecondLine = (TextView) view.findViewById(R.id.tv_SecondLine);
        TextView tv_Right = (TextView) view.findViewById(R.id.tv_Right);
        ImageView MainImage = (ImageView)view.findViewById(R.id.MainImage);
        MainImage.setBackgroundResource(R.drawable.ic_img_examtime);

        Map<String,String> item = list.get(position);
        String kcmc = item.get("kcmc").split("（")[0].split("\\(")[0];
        if (kcmc.length()>=10){
            tv_FirstLine.setText(kcmc.substring(0,10) + "…-" + item.get("kcxz"));
        }else {
            tv_FirstLine.setText(kcmc+"-" + item.get("kcxz"));
        }

        tv_SecondLine.setText(item.get("kssj"));
        tv_Right.setText(item.get("ksdd"));

        return view;
    }



}
