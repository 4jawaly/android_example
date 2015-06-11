package com.jawaly.fragments;

import java.util.Locale;

import net.jawaly.sms.api.R;
import net.jawaly.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jawaly.customview.MyProgressDialog;
import com.jawaly.rest.GetTask;
import com.jawaly.rest.RestTaskCallback;
import com.jawaly.rest.RestfulUrl;
import com.jawaly.sms.MainActivity;
import com.jawaly.sms.Utils;
import com.jawaly.utils.Utilss;

public class SettingsFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {
	public static String TAG = "SettingActivity";

	private MainActivity context;

	private String language;
	private EditText userNameEditText, passwordEditText, specialApiEditText;
	private Button saveBtn;

	private User user;
	private GetTask task;

	String userName;
	String password;
	String specialApi;

	GetTask loginTask;
	MyProgressDialog dialog;
	SharedPreferences pref;

	public SettingsFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (MainActivity) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (task != null) {
			task.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = getArguments();

		user = (User) bundle.getSerializable(MainActivity.PROPERTY_USER);

		language = new Utilss().getLocalization(getActivity());

		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration c = new Configuration(getResources().getConfiguration());
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());

		((MainActivity) getActivity()).actionBarshowHome();

		View rootView = inflater.inflate(R.layout.fragment_settings_layout,
				container, false);

		pref = getActivity().getSharedPreferences(
				MainActivity.PROPERTY_APP_PREF, Context.MODE_PRIVATE);

		userNameEditText = (EditText) rootView
				.findViewById(R.id.settings_userName);
		userNameEditText.setOnFocusChangeListener(this);

		passwordEditText = (EditText) rootView
				.findViewById(R.id.settings_userPassword);
		passwordEditText.setOnFocusChangeListener(this);

		specialApiEditText = (EditText) rootView
				.findViewById(R.id.special_api_edt);
		specialApiEditText.setOnFocusChangeListener(this);

		if (!user.getUserName().equals("x")) {
			userNameEditText.setText(user.getUserName());
		}
		if (!user.getPassWord().equals("x")) {
			passwordEditText.setText(user.getPassWord());
		}

		saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);

		language = new Utilss().getLocalization((Activity) context);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.saveBtn:
			onsaveClicked();
			break;

		default:
			break;
		}
	}

	private void onsaveClicked() {

		userName = userNameEditText.getText().toString();
		password = passwordEditText.getText().toString();
		specialApi = specialApiEditText.getText().toString();

		if (TextUtils.isEmpty(userName) || userName.length() < 5) {
			userNameEditText.setError(Utils
					.setupErrorSpan(getString(R.string.error_username_length)));
			Utils.setupErrorAppMsg(getActivity(), getActivity().getResources()
					.getString(R.string.error_app_msg));

		} else if (TextUtils.isEmpty(password) || password.length() < 6) {
			passwordEditText.setError(Utils
					.setupErrorSpan(getString(R.string.error_password_length)));
			Utils.setupErrorAppMsg(getActivity(), getActivity().getResources()
					.getString(R.string.error_app_msg));

		} else if (TextUtils.isEmpty(specialApi)) {

			specialApiEditText.setError(Utils
					.setupErrorSpan(getString(R.string.error_special_api)));
			Utils.setupErrorAppMsg(getActivity(), getActivity().getResources()
					.getString(R.string.error_app_msg));

		} else {
			Utils.removeAppMsg(getActivity());
			// do login
			loginTask = new GetTask(RestfulUrl.getUserUrl(userName, password,
					specialApi), new RestTaskCallback() {

				@Override
				public void onTaskNotComplete() {
					// progress.hide();
					dialog.dismiss();
				}

				@Override
				public void onTaskComplete(String result) {
					// progress.hide();

					try {
						dialog.dismiss();
					} catch (Exception e) {

					}

					Log.d(TAG, result);
					try {

						JSONObject response = new JSONObject(result);
						int code = response.getInt("Code");
						switch (code) {
						case 101:
							Utils.setupErrorAppMsg(
									getActivity(),
									getString(R.string.error_all_fiels_required));
							break;
						case 102:
							Utils.setupErrorAppMsg(getActivity(),
									getString(R.string.error_username_incorect));

							userNameEditText.setError(Utils
									.setupErrorSpan(getString(R.string.error_username_incorect)));
							break;
						case 103:
							Utils.setupErrorAppMsg(
									getActivity(),
									getString(R.string.error_password_incorrect));

							passwordEditText.setError(Utils
									.setupErrorSpan(getString(R.string.error_password_incorrect)));
							break;
						case 104:

							if (new Utilss().validateUser(getActivity(),
									userName, password, result)) {
								SharedPreferences.Editor editor = pref.edit();
								editor.putString(
										MainActivity.PROPERTY_USER_NAME,
										userName);
								editor.putString(
										MainActivity.PROPERTY_PASSWORD,
										password);
								JSONArray user = response.getJSONArray("user");
								editor.putString(
										MainActivity.PROPERTY_FULL_NAME,
										user.getString(1));
								editor.putInt(MainActivity.PROPERTY_CREDIT,
										user.getInt(11));
								editor.putInt(MainActivity.PROPERTY_USER_ID,
										user.getInt(0));
								editor.putString(
										MainActivity.PROPERTY_USER_EMAIL,
										user.getString(4));
								editor.putBoolean(
										MainActivity.PROPERTY_USER_EMAIL_state,
										user.getString(5).equalsIgnoreCase(
												"yes"));
								editor.putString(
										MainActivity.PROPERTY_USER_MOBILE_NUMBER,
										user.getString(6));
								editor.putBoolean(
										MainActivity.PROPERTY_USER_MOBILE_NUMBER_STATE,
										user.getString(7).equalsIgnoreCase(
												"yes"));
								editor.putInt(
										MainActivity.PROPERTY_USER_GROUP_ID,
										user.getInt(8));
								editor.putBoolean(
										MainActivity.PROPERTY_USER_STATE,
										user.getString(9).equalsIgnoreCase(
												"yes"));
								editor.putInt(
										MainActivity.PROPERTY_HAVE_OPEN_SENDER,
										user.getInt(12));

								editor.commit();

								Utils.setupSuccessAppMsg(
										getActivity(),
										getActivity().getResources().getString(
												R.string.success_app_msg));

								// Intent aIntent = new Intent(
								// getActivity(),
								// MainActivity.class);
								// aIntent.putExtra("fromApp", true);
								// startActivity(aIntent);

								// finish();
							}

							break;
						case 105:

							Utils.setupErrorAppMsg(getActivity(),
									getString(R.string.error_site_closed));
							break;
						case 113:
							Utils.setupErrorAppMsg(getActivity(),
									getString(R.string.error_account_inactive));
							break;
						case 114:
							Utils.setupErrorAppMsg(getActivity(),
									getString(R.string.error_account_banned));
							break;
						case 115:

							new Utilss().validateUser(getActivity(), userName,
									password, result);
							break;

						default:
							Utils.setupErrorAppMsg(getActivity(),
									response.getString("ErrorMsg"));
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Utils.setupErrorAppMsg(getActivity(),
								getString(R.string.invalid_api));
					}

				}
			});
			if (Utils.isNetworkAvailable(getActivity())) {

				if (dialog != null) {
					if (!dialog.isShowing())
						dialog = MyProgressDialog.show(getActivity(), null,
								null, false, true);
				} else {
					dialog = MyProgressDialog.show(getActivity(), null, null,
							false, true);
				}
				// progress.show();
				loginTask.execute();

			} else {
				Utils.setupErrorAppMsg(getActivity(),
						getString(R.string.error_no_connection));
			}
		}

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
