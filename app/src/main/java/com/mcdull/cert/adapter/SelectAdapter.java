package com.mcdull.cert.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcdull.cert.R;

public class SelectAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	
	public SelectAdapter(Context context ,List<String> list) {
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
		View view = View.inflate(context, R.layout.select_item, null);
		TextView tvItemScore = (TextView) view.findViewById(R.id.tv_select_item);
		tvItemScore.setText(list.get(position));
		return view;
	}
	
	void upDateList(List<String> list){
		this.list = list;
		notifyDataSetChanged();
	}
	

}
