package com.jawaly.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import net.jawaly.sms.api.R;
import net.jawaly.user.ManageUser;
import net.jawaly.user.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jawaly.customview.MyProgressDialog;
import com.jawaly.fragments.SendSMSFragment;
import com.jawaly.rest.PostTask;
import com.jawaly.rest.RestTaskCallback;
import com.jawaly.rest.RestfulUrl;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.Utils;
import com.jawaly.utils.Utilss;

public class ManageConnection {

	public void sendMessages(final Context context, User user, String message,
			String numbers, String time, String groups,
			final SendSMSFragment obj, final String sender) {

		final MyProgressDialog dialog = MyProgressDialog.show(context, null,
				null, false, true);

		PostTask sendTask = new PostTask(RestfulUrl.getSendSmsUrl(),
				new RestTaskCallback() {

					@Override
					public void onTaskNotComplete() {
						// TODO Auto-generated method stub
						// progress.dismiss();
						dialog.dismiss();
					}

					@Override
					public void onTaskComplete(String result) {
						final DatabaseHelper db = new DatabaseHelper(context);
						dialog.dismiss();
						try {

							JSONObject response = new JSONObject(result);
							int code = response.getInt("Code");
							if (code == 100) {
								// update credit value text view
//								obj.updateCredit(response
//										.getString("currentuserpoints"));

								new Utilss().setlocalization(context);

								final Dialog successDialog = new Dialog(context);
								successDialog
										.setContentView(R.layout.dialog_send_report);
								successDialog.setTitle(context
										.getString(R.string.success_send_sms));

								TextView validNumbers = (TextView) successDialog
										.findViewById(R.id.numbersValueTextView);
								TextView invalid = (TextView) successDialog
										.findViewById(R.id.invalidNumbersValue);
								TextView blocked = (TextView) successDialog
										.findViewById(R.id.blockedNumbersValue);
								TextView smsCount = (TextView) successDialog
										.findViewById(R.id.messageCountValueTV);
								TextView totalCount = (TextView) successDialog
										.findViewById(R.id.totalPointsValue);
								TextView numberRepeated = (TextView) successDialog
										.findViewById(R.id.repeatedNumbersValue);
								Button close = (Button) successDialog
										.findViewById(R.id.closeBtn);
								close.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										successDialog.dismiss();

									}
								});
								Log.d("response", response.toString());
								// validNumbers.setText(result.getString("valid"));
								invalid.setText(Html.fromHtml(response
										.getString("nvalid")));
								blocked.setText(response.getString("Blocked"));
								smsCount.setText(response
										.getString("SMSNUmber"));
								totalCount.setText(response
										.getString("totalcout"));
								validNumbers.setText(response
										.getString("totalsentnumbers"));
								numberRepeated.setText(response
										.getString("Repeated"));
								successDialog.show();

								/*
								 * empty user numbers & messages
								 */

								db.emptyTemp();
								obj.msgText.setText("");
								obj.numbersEditText.setText("");
								obj.countSelectedTextView.setText("0");

								obj.sendMessageLater = false;

							} else {

								Utils.showToastMsg(context,
										response.getString("MessageIs"));

							}
						} catch (Exception e) {
							Utils.showToastMsg(context,
									" No response From sit !!");
							dialog.dismiss();
							e.printStackTrace();
						}

					}
				}, dialog, context);
		// Log.d("sub groups", subGroups.toString());
		// Log.d(TAG, "username: "+userName+
		// " password: "+password+ " message: "+message+
		// " numbers: "+numbers);
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				2);
		nameValuePairs.add(new BasicNameValuePair("return", "json"));
		nameValuePairs.add(new BasicNameValuePair("username", user
				.getUserName()));
		nameValuePairs.add(new BasicNameValuePair("password", user
				.getPassWord()));
		nameValuePairs.add(new BasicNameValuePair("Rmduplicated", "1"));
		nameValuePairs.add(new BasicNameValuePair("message", message));

		nameValuePairs.add(new BasicNameValuePair("numbers", numbers.replace(
				"-", "").replace(" ", "")));
		nameValuePairs.add(new BasicNameValuePair("os", " android "
				+ android.os.Build.VERSION.RELEASE));
		if (obj.sendMessageLater) {
			nameValuePairs.add(new BasicNameValuePair("datetime", time));

			TimeZone defaultt = TimeZone.getDefault();

			Log.d("getDisplayName", defaultt.getDisplayName());
			Log.d("getRawOffset", (defaultt.getRawOffset() / 1000) + "");

			nameValuePairs.add(new BasicNameValuePair("offset", String
					.valueOf((defaultt.getRawOffset() / 1000))));
			nameValuePairs.add(new BasicNameValuePair("zoneis", defaultt
					.getDisplayName()));
		}

		nameValuePairs.add(new BasicNameValuePair("gid", groups));
		nameValuePairs.add(new BasicNameValuePair("sender", sender));
		sendTask.execute(nameValuePairs);

	}

	
	private void errorToastMessage(Context context) {
		Toast.makeText(context,
				context.getString(R.string.change_password_update_failed),
				Toast.LENGTH_LONG).show();
	}


	}
