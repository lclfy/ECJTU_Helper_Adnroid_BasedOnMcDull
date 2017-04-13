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

public class ECardAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;

    public ECardAdapter(Context context, List<Map<String, String>> list) {
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
        View view = View.inflate(context, R.layout.select_item_materialstyle, null);
        //把header隐藏
        TextView tvHeader = (TextView)view.findViewById(R.id.tv_Header);
        tvHeader.setVisibility(View.GONE);

        TextView tv_consumeAddress = (TextView) view.findViewById(R.id.tv_FirstLine);
        Map<String, String> map = list.get(position);
        //地址(银行充值等没有地址的地方，使用type作为消费名称)
        String consumeAddress = map.get("consumeAddress");
        if(consumeAddress.length()==0){
            consumeAddress = map.get("consumeType");
        }
        tv_consumeAddress.setText(consumeAddress);
        //通过地址找图片
        int addressImageDrawable = 0;
        ImageView addressImage = (ImageView) view.findViewById(R.id.MainImage);
        if (consumeAddress.contains("食") ||
                consumeAddress.contains("餐")){
            addressImageDrawable = R.drawable.ic_local_restaurant;
        }else if (consumeAddress.contains("超") || consumeAddress.contains("商") || consumeAddress.contains("卖")){
            addressImageDrawable = R.drawable.ic_store_mall;
        }else if (consumeAddress.contains("浴") || consumeAddress.contains("澡")){
            addressImageDrawable = R.drawable.ic_local_bathroom;
        }else if (consumeAddress.contains("水")){
            addressImageDrawable = R.drawable.ic_local_drink;
        }else if (consumeAddress.contains("医")){
            addressImageDrawable = R.drawable.ic_local_hospital;
        }else {
            addressImageDrawable = R.drawable.ic_local_bank;
        }
        addressImage.setBackgroundResource(addressImageDrawable);

        //时间
        TextView tv_consumeTime = (TextView) view.findViewById(R.id.tv_SecondLine);
        //切时间
        String [] hhmmdd = map.get("consumeTime").split(" ");
        String [] split_hhmmdd = hhmmdd[1].split(":");
        String hhmm = split_hhmmdd[0] + ":"+split_hhmmdd[1];
        tv_consumeTime.setText(hhmm);

        //金额
        TextView tv_consumeCount = (TextView) view.findViewById(R.id.tv_Right);
        tv_consumeCount.setText(map.get("consumeCount"));
        return view;
    }

    void upDateList(List<Map<String, String>> list){
        this.list = list;
        notifyDataSetChanged();
    }


}
