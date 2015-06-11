package com.jawaly.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import net.jawaly.sms.api.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListPhonesAdapter extends BaseAdapter{
	
	private Context context;
	private String[] list;
	
	public ListPhonesAdapter(Context context, String[] list){
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_phones_adapter, parent, false);
		}
		
		TextView tv = (TextView)convertView.findViewById(R.id.adapter_select_favortie_txt);
		tv.setText(list[position]);
		
		return convertView;
	}
}
