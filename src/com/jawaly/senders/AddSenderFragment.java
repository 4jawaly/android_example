package com.jawaly.senders;

import java.util.ArrayList;
import java.util.List;

import net.jawaly.sms.api.R;
import net.jawaly.user.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jawaly.rest.PostTask;
import com.jawaly.rest.RestTaskCallback;
import com.jawaly.rest.RestfulUrl;
import com.jawaly.sms.MainActivity;
import com.jawaly.sms.Utils;
import com.jawaly.utils.Utilss;

public class AddSenderFragment extends Fragment implements
		OnFocusChangeListener {

	private final String TAG = getClass().getSimpleName();

	MainActivity context;

	// ui views
	Button addButton;
	EditText senderET;

	public AddSenderFragment() {
		this.context = (MainActivity) getActivity();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (MainActivity) activity;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		new Utilss().setlocalization(getActivity());
		context.actionBarshowBack();

		View rootView = inflater.inflate(R.layout.fragment_add_sender, null);
		addButton = (Button) rootView.findViewById(R.id.add);
		// addButton.setOnTouchListener(context);

		senderET = (EditText) rootView.findViewById(R.id.senderNameET);
		senderET.setOnFocusChangeListener(this);
		senderET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Log.d(TAG,"start: "+ start +" befor: "+before+
				// " count: "+count+ " text: "+s.toString());
				if (s.toString().length() > 12) {
					senderET.removeTextChangedListener(this);
					senderET.setText(s.toString().substring(0, 12));
					senderET.addTextChangedListener(this);
					senderET.setSelection(12);
				}
				try {
					if (Character.isWhitespace(s.charAt(start))) {
						senderET.removeTextChangedListener(this);
						senderET.setText(s.toString().replace(" ", ""));
						if (start != 0) {
							senderET.setSelection(start);
						}
						senderET.addTextChangedListener(this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String senderName = senderET.getText().toString().trim();
				if (TextUtils.isEmpty(senderName)) {
					senderET.setError(Utils
							.setupErrorSpan(getString(R.string.plz_enter_sender_name)));
					
					Utils.setupErrorAppMsg(context,
							getString(R.string.plz_enter_sender_name));
				} else {
					if (!Utils.isNetworkAvailable(context)) {
						Utils.setupErrorAppMsg(context,
								getString(R.string.no_connection));
						return;
					}
					sendRequest(senderName);
				}

			}
		});
		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		context.onBackPressed();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// context.setupActionBarHomeButton(true, true);
		context.setDrawerIndicatorEnabled(true);
		context.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

		context.actionBarshowHome();
	}

	private void sendRequest(String senderName) {
		Bundle bundle = getArguments();
		User user = (User) bundle.getSerializable(MainActivity.PROPERTY_USER);

		String userName = user.getUserName();
		String password = user.getPassWord();

		final ProgressDialog progress = new ProgressDialog(context);
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage(getString(R.string.process_sending_request));

		PostTask requestSenderPost = new PostTask(RestfulUrl.senderAdd(),
				new RestTaskCallback() {

					@Override
					public void onTaskNotComplete() {
						progress.hide();
						Utils.showToastMsg(
								context,
								getString(R.string.failed_add_sender_connection));
					}

					@Override
					public void onTaskComplete(String result) {
						progress.hide();
						try {
							JSONObject resposnse = new JSONObject(result);
							int code = resposnse.getInt("Code");
							if (code == 110 || code == 117) {
								Utils.setupSuccessAppMsg(context,
										resposnse.getString("MessageIs"));
								senderET.setText("");
								// context.onBackPressed();

							} else {
								Utils.setupErrorAppMsg(
										context,
										getString(R.string.failed_add_sender_service_reply)
												+ resposnse
														.getString("MessageIs"));
							}
						} catch (JSONException e) {
							try {
								Utils.setupErrorAppMsg(
										context,
										getString(R.string.failed_response_format));
							} catch (Exception ee) {

							}
							e.printStackTrace();
						} catch (Exception e) {
							try {
								Utils.setupErrorAppMsg(
										context,
										getString(R.string.failed_response_format));
							} catch (Exception r) {

							}
							e.printStackTrace();
						}
					}
				});
		// prepare posted parameters
		final List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(
				2);
		nameValuePairList.add(new BasicNameValuePair("return", "json"));
		nameValuePairList.add(new BasicNameValuePair("username", userName));
		nameValuePairList.add(new BasicNameValuePair("password", password));
		nameValuePairList.add(new BasicNameValuePair("Sendername", senderName));
		progress.show();
		requestSenderPost.execute(nameValuePairList);

	}

	@Override
	public void onPause() {
		super.onPause();
		Utils.removeAppMsg(getActivity());

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Utils.removeAppMsg(getActivity());

	}

}
