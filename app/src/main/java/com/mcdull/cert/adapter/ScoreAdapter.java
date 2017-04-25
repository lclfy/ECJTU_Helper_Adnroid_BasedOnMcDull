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

public class ScoreAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,String>> list;

	public ScoreAdapter(Context context, List<Map<String,String>> list) {
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

		Map<String,String> item = list.get(position);
		//及格和不及格放不一样的图
		if (item.get("ckcj").length()==0){
			//没有补考
			try {
				//试试考试成绩是不是分数，是的话按60换图
				if (Integer.parseInt(item.get("kscj")) < 60){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else {
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}catch (Exception e){
				//不是的话按及格不及格换图
				if (item.get("kscj").contains("不及格")){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else{
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}
			tv_Right.setText(item.get("kscj"));
		}else if (item.get("cxcj").length()==0){
			//有补考无重修
			//试试考试成绩是不是分数，是的话按60换图
			try {
				if (Integer.parseInt(item.get("ckcj")) < 60){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else {
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}	catch (Exception e){
				//不是的话按及格不及格换图
				if (item.get("ckcj").contains("不及格")){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else{
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}
			tv_Right.setText("补考："+item.get("ckcj"));
		}else{
			//重修
			//试试考试成绩是不是分数，是的话按60换图
			try {
				if (Integer.parseInt(item.get("cxcj")) < 60){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else {
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}	catch (Exception e){
				//不是的话按及格不及格换图
				if (item.get("cxcj").contains("不及格")){
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore_failed1);
				}else{
					MainImage.setBackgroundResource(R.drawable.ic_img_examscore);
				}
			}
			tv_Right.setText("重修："+item.get("cxcj"));
		}
		String kcmc = item.get("kcmc").split("（")[0].split("\\(")[0];
		if (kcmc.length()>10){
			tv_FirstLine.setText(kcmc.substring(0,9) + "…-" + item.get("khfs"));
		}else {
			tv_FirstLine.setText(kcmc + "-" + item.get("khfs"));
		}

		tv_SecondLine.setText("本课程有"+item.get("kcxf")+"个学分");


		return view;
	}



}
