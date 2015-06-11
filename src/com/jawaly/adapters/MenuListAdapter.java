package com.jawaly.adapters;

import net.jawaly.sms.api.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<String> {
	private final String TAG = getClass().getSimpleName(); 
	String[] items ;
	Context context;
	TypedArray icons;
 	
	public MenuListAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.items = objects;
		this.context = context;
		icons = context.getResources().obtainTypedArray(R.array.menu_icons);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_menu,null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.drawerListItem);
			holder.textView.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/gd_medium.otf"));
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.divider1 = (View)convertView.findViewById(R.id.my_divider);
			holder.divider2 = (View)convertView.findViewById(R.id.my_divider2);
			holder.layout = (View)convertView.findViewById(R.id.drawable_layout);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView.setTextColor(Color.WHITE);
		holder.imageView.setVisibility(View.VISIBLE);
		holder.divider1.setVisibility(View.VISIBLE);
		holder.divider2.setVisibility(View.GONE);
		holder.layout.setBackgroundResource(R.drawable.menu_list_selector);
		holder.layout.setPadding(20, 20, 20, 20);
		
		//((LinearLayout)holder.layout).setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		if(position == 5 || position == 9 || position == 13){
			
			holder.layout.setBackgroundColor(0xff4382db);
			holder.layout.setPadding(15, 15, 15, 15);
			holder.textView.setText(items[position]);
			holder.imageView.setImageResource(icons.getResourceId(position, -1));
			holder.imageView.setVisibility(View.GONE);
			
//		}else if(position == 7 || position == 8 || position == 9){ 
//			holder.textView.setText(items[position]);
//			holder.imageView.setImageResource(icons.getResourceId(position, -1));
//			holder.textView.setTextColor(Color.LTGRAY);
//			holder.divider1.setVisibility(View.GONE);
//			holder.divider2.setVisibility(View.VISIBLE);
		}if(position == 12){
			holder.textView.setText(items[position]);
			holder.layout.setBackgroundColor(Color.RED);
			((LinearLayout)holder.layout).setGravity(Gravity.CENTER);
			holder.imageView.setVisibility(View.GONE);
		} else{
			holder.textView.setText(items[position]);
			holder.imageView.setImageResource(icons.getResourceId(position, -1));
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView textView;
		ImageView imageView;
		View divider1;
		View divider2;
		View layout;
	}
}


