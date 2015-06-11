package com.jawaly.utils;

import java.util.ArrayList;
import java.util.Locale;

import net.jawaly.sms.api.R;
import net.jawaly.user.User;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.jawaly.adapters.ListPhonesAdapter;
import com.jawaly.connection.ManageConnection;
import com.jawaly.customview.ExpandableHeightListView;
import com.jawaly.fragments.SendSMSFragment;

public class Utilss {

	String selected_message = "";

	public boolean validateUser(Context context, String userName,
			String password, String result) {
		boolean b = true;
		String isEmailRequired = "";
		String isMobileRequired = "";

		Log.d("validate user", userName + " , " + password);

		try {
			JSONObject jo = new JSONObject(result);

			JSONObject jo2 = jo.getJSONObject("registeration_setting");
			isEmailRequired = jo2.getString("required_email_activation");
			isMobileRequired = jo2.getString("required_mobile_activation");

			JSONArray ja = jo.getJSONArray("user");

			Log.d("validate user", ja.getString(9));

			boolean adminBlock = ja.getString(9).equalsIgnoreCase("no");
			Log.d("validate user", adminBlock + "");
			if (adminBlock) {
				showUnAuthorizedUser(context);
				return false;
			}

		} catch (Exception e) {

		}

		return b;
	}

	private void showUnAuthorizedUser(final Context context) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			TextView title = new TextView(context);
			// You Can Customise your Title here
			//title.setText(context.getString(R.string.dialog_closed_by_admin));
			title.setBackgroundColor(Color.WHITE);
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.BLACK);
			title.setTextSize(20);
			builder.setCustomTitle(title);
			//builder.setMessage(R.string.dialog_closed_by_admin_txt);
			builder.setPositiveButton(R.string.dialog_closed_by_admin_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((Activity) context).finish();
						}
					});

			AlertDialog dialog = builder.show();
			TextView messageText = (TextView) dialog
					.findViewById(android.R.id.message);

			messageText.setGravity(Gravity.CENTER);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void dialogConfirmCloseSite(final Context context, final
	// SettingsFragment setting){
	// try{
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// TextView title = new TextView(context);
	// // You Can Customise your Title here
	// title.setText(context.getString(R.string.dialog_closed_by_admin));
	// title.setBackgroundColor(Color.WHITE);
	// title.setPadding(10, 10, 10, 10);
	// title.setGravity(Gravity.CENTER);
	// title.setTextColor(Color.BLACK);
	// title.setTextSize(20);
	// builder.setCustomTitle(title);
	// builder.setMessage(R.string.dialog_confirm_close_site);
	// builder.setPositiveButton(R.string.dialog_closed_by_admin_ok, new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// setting.close_website.setChecked(true);
	// setting.sendRequest();
	// }
	// });
	//
	// AlertDialog dialog = builder.show();
	// TextView messageText =
	// (TextView)dialog.findViewById(android.R.id.message);
	//
	// messageText.setGravity(Gravity.CENTER);
	// dialog.show();
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }

	/*
	 * get device name
	 */
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}




	public void ShowDialogConfirmSendSms(final Context context,
			final User user, final String sender_name, final String time,
			final String message, final String numbers, String messagesNumber,
			final String groups, final String groups_name,
			final SendSMSFragment obj, final String sender) {

		final Dialog myDialog = new Dialog(context);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setlocalization(context);

		myDialog.setContentView(R.layout.send_sms_confirm_dialog);

		final String newNumbers = checkEmptyNumbers(numbers);

		String[] list = newNumbers.split(",");
		String[] group_list = groups.split(",");
		String[] groups_name_list = groups_name.split(",");

		TextView name = (TextView) myDialog
				.findViewById(R.id.confirm_dialog_messsge_confirmation_name_value);
		TextView date = (TextView) myDialog
				.findViewById(R.id.confirm_dialog_messsge_confirmation_date);
		TextView message_tv = (TextView) myDialog
				.findViewById(R.id.confirm_dialog_messsge_txt44);
		TextView messages_count = (TextView) myDialog
				.findViewById(R.id.confirm_dialog_messages_count);
		TextView send_btn = (TextView) myDialog
				.findViewById(R.id.confirm_dialog_send_btn);

		send_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new ManageConnection().sendMessages(context, user, message,
						newNumbers, time, groups, obj, sender);
				myDialog.dismiss();
			}
		});

		ExpandableHeightListView listView = (ExpandableHeightListView) myDialog
				.findViewById(R.id.confirm_dialog_phones_lisrview);
		// ExpandableHeightListView group_listView =
		// (ExpandableHeightListView)myDialog.findViewById(R.id.confirm_dialog_groups_lisrview);
		listView.setExpanded(true);

		listView.setAdapter(new ListPhonesAdapter(context, list));

		name.setText(sender_name);
		date.setText(time);
		message_tv.setText(message);
		messages_count.setText(messagesNumber);

		myDialog.show();

	}

	public void setlocalization(Context context) {
		String language = new Utilss().getLocalization((Activity) context);

		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration c = new Configuration(context.getResources()
				.getConfiguration());
		c.locale = locale;
		context.getResources().updateConfiguration(c,
				context.getResources().getDisplayMetrics());
	}

	public String getLocalization(Activity context) {
		Locale currentLocale;
		String language;

		ManageLocalization ml = new ManageLocalization(context);
		if (ml.user_language_isexist()) {
			language = ml.get_userLanguage();
			Log.d("language", language);
		} else {
			currentLocale = Locale.getDefault();
			String lng = currentLocale.getLanguage();
			if (lng.equals("ar")) {
				language = lng;
				ml.setLanguage(language);
				Log.d("language", language);
			} else {
				language = "en";
				ml.setLanguage(language);
				Log.d("language", language);
			}
		}
		return language;
	}

	/*
	 * method to check empty numbers
	 */
	private String checkEmptyNumbers(String data) {
		StringBuffer sb = new StringBuffer();
		String[] d = data.split(",");
		for (int i = 0; i < d.length; i++) {
			if (d[i].length() > 0) {
				if (i == data.length() - 1) {
					sb.append(d[i]);
				} else {
					sb.append(d[i]);
					sb.append(",");
				}
			} else {

			}
		}
		return sb.toString();
	}
}
