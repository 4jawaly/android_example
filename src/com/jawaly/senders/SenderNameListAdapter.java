package com.jawaly.senders;


import java.util.ArrayList;
import java.util.List;

import net.jawaly.sms.api.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jawaly.rest.PostTask;
import com.jawaly.rest.RestTaskCallback;
import com.jawaly.rest.RestfulUrl;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.MainActivity;
import com.jawaly.sms.Utils;


public class SenderNameListAdapter extends ArrayAdapter<SenderName> {

	private final String TAG = this.getClass().getSimpleName();
	private MainActivity context;

	private SenderName[] items;
	DatabaseHelper db;
	
	
	public SenderNameListAdapter(Context context, int resource,
			int textViewResourceId, SenderName[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = (MainActivity) context;
		this.items = objects;

	}

	@Override
	public View getView(final int position,  View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_sender_item, null);

			viewHolder = new ViewHolder();

			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textView1);
			viewHolder.statusIconIV = (ImageView) convertView
					.findViewById(R.id.statusIcon);
			viewHolder.confirmButton = (Button) convertView.findViewById(R.id.confirmButton);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
	
		if (items[position].getState().equals("new")) {
			// sender name is new
			if (items[position].getActiveState().equals("No")||items[position].getActiveState().equals("Done")) {
				// not need confirmation 
				convertView.setBackgroundResource(R.drawable.yellow_box);
				viewHolder.statusIconIV.setImageResource(R.drawable.mobile);
				viewHolder.confirmButton.setVisibility(View.GONE);
			} else {
				// need confirmation
				convertView.setBackgroundResource(R.drawable.red_box);
				viewHolder.statusIconIV.setImageResource(R.drawable.pending);
				viewHolder.confirmButton.setVisibility(View.VISIBLE);
			}
		} else if (items[position].getState().equals("Yes")) {
			// sender name is active
			convertView.setBackgroundResource(R.drawable.green_box);
			viewHolder.statusIconIV.setImageResource(R.drawable.verified);
			viewHolder.confirmButton.setVisibility(View.GONE);
		} else {
			// sender name is refused
			convertView.setBackgroundResource(R.drawable.bg_container);
			viewHolder.statusIconIV.setImageResource(R.drawable.ban);
			viewHolder.confirmButton.setVisibility(View.GONE);
			
		}

		viewHolder.textView.setText(items[position].getSenderName());
		
		// set confirm button action 
		viewHolder.confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_confirm_sendername);
				TextView title = (TextView) dialog.findViewById(R.id.textView1);
				Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/gd_medium.otf");
				title.setTypeface(tf);
				final EditText codeEt = (EditText) dialog.findViewById(R.id.editText1);
				Button sendButton = (Button) dialog.findViewById(R.id.button1);
				sendButton.setTypeface(tf);
				//sendButton.setOnTouchListener(new OrangeButtonsListeners());
				sendButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final String code = codeEt.getText().toString().trim();
						if(TextUtils.isEmpty(code)){
							Utils.showToastMsg(context, context.getString(R.string.plz_enter_confirmation_code));
							return;
						}
						final ProgressDialog progress = Utils.setupProgress(context, context.getString(R.string.sending_confirmation_code));
						
						PostTask confirmPostTask = new PostTask(RestfulUrl.senderMobileConfirm(), new RestTaskCallback() {
							
							@Override
							public void onTaskNotComplete() {
								progress.hide();
								Utils.showToastMsg(context,context.getString(R.string.failed_response_format));
							}
							
							@Override
							public void onTaskComplete(String result) {
								progress.hide();
								try {
									JSONObject response = new JSONObject(result);
									int code = response.getInt("Code");
									if(code==110){
										Utils.showToastMsg(context,response.getString("MessageIs") );
										viewHolder.confirmButton.setVisibility(View.GONE);
										//listItemView.setBackgroundResource(R.drawable.yellow_box);
										items[position].setActiveState("Done");
										dialog.dismiss();
									}else{
										Utils.showToastMsg(context,context.getString(R.string.failed_activate_sender_name)+" "+ response.getString("MessageIs") );
									}
								} catch (JSONException e) {
									Utils.showToastMsg(context,context.getString(R.string.failed_response_format));
									e.printStackTrace();
								}
								
							}
						});
						List<NameValuePair> nameValuePairList = new  ArrayList<NameValuePair>(2);
						SharedPreferences pref = context.getSharedPreferences(MainActivity.PROPERTY_APP_PREF,Context.MODE_PRIVATE);
						nameValuePairList.add(new BasicNameValuePair("username",pref.getString(MainActivity.PROPERTY_USER_NAME, "")));
						nameValuePairList.add(new BasicNameValuePair("password",pref.getString(MainActivity.PROPERTY_PASSWORD, "")));
						nameValuePairList.add(new BasicNameValuePair("Snderid",items[position].getSenderId()+""));
						nameValuePairList.add(new BasicNameValuePair("sender_name",items[position].getSenderName()));
						nameValuePairList.add(new BasicNameValuePair("Activecode",code));
						nameValuePairList.add(new BasicNameValuePair("return","json"));
						progress.show();
						confirmPostTask.execute(nameValuePairList);
					}
				});
				dialog.show();
				
			}
		});
		viewHolder.confirmButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()== MotionEvent.ACTION_DOWN){
					viewHolder.confirmButton.setBackgroundResource(R.drawable.red_box);
				}else{
					viewHolder.confirmButton.setBackgroundResource(R.drawable.red_bg);
					
				}
				return false;
			}
		});
		
		
		
		return convertView;
	}

}

class ViewHolder {
	TextView textView;
	ImageView statusIconIV;
	Button confirmButton;

}
