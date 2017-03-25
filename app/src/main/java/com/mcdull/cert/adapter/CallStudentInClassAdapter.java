package com.mcdull.cert.adapter;

/**
 * Created by mcdull on 15/7/11.
 */

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcdull.cert.Bean.CalledPersonBean;
import com.mcdull.cert.R;

public class CallStudentInClassAdapter extends BaseAdapter {

    private Context context;
    private List<CalledPersonBean> list;
    //用来判断是否点名已经结束
    boolean isDone;

    public CallStudentInClassAdapter(Context context, List<CalledPersonBean> list,boolean isDone) {
        super();
        this.context = context;
        //此处传入的list已经是CalledPersonBean类型，有姓名，班级编号与是否被点三项
        this.list = list;
        this.isDone = isDone;


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
        return position;
    }

    public void notifyDataSetChanged(List<CalledPersonBean> list){
        this.list = list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.select_item_callpeople, null);
        //把header隐藏
        TextView tvHeader = (TextView)view.findViewById(R.id.tv_Header);
        tvHeader.setVisibility(View.GONE);

        TextView tv_FirstLine = (TextView) view.findViewById(R.id.tv_calledStudentId);
        TextView tv_SecondLine = (TextView) view.findViewById(R.id.tv_calledStudentName);

        TextView tv_Right = (TextView) view.findViewById(R.id.tv_Right);
        ImageView MainImage = (ImageView)view.findViewById(R.id.MainImage);
        //设置头像
        switch (position%9){
            case 0:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color1_48dp);
                break;
            case 1:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color2_48dp);
                break;
            case 2:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color3_48dp);
                break;
            case 3:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color4_48dp);
                break;
            case 4:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color5_48dp);
                break;
            case 5:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color6_48dp);
                break;
            case 6:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color7_48dp);
                break;
            case 7:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color8_48dp);
                break;
            case 8:
                MainImage.setBackgroundResource(R.drawable.ic_account_circle_color9_48dp);
                break;
        }
        CalledPersonBean students = list.get(position);
        tv_FirstLine.setText(students.sn + " - ");
        tv_SecondLine.setText(students.xm);
            if (students.hasCome){
                tv_Right.setText("√已签到");
                //字体换色
                tv_Right.setTextColor(context.getResources().getColor(R.color.caolv));
            }else {
                tv_Right.setText("未到");
                tv_Right.setTextColor(context.getResources().getColor(R.color.zhuhong));

            }



        return view;
    }





}
