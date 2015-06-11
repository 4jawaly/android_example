package com.jawaly.senders;

import net.jawaly.sms.api.R;
import net.jawaly.user.User;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jawaly.rest.RestfulUrl;
import com.jawaly.sms.MainActivity;
import com.jawaly.sms.Utils;

public class SenderNamesFragment extends Fragment {

	// ui
	Button showAllBtn;
	Button showNewSendersBtn;
	ListView listView;

	TextView emptyView;

	MainActivity context;

	SenderName[] sendersList;

	int selectedTabIndex = 0;// show new sender list

	AsyncTask<Object, Object, Object> getSenders;
	private String userName;
	private String password;

	Bundle bundle;

	private ImageView img_actionBar_add_sender_name;

	public static final String fragmentTag = "sendernametag";

	public SenderNamesFragment() {
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
	public void onDestroy() {
		super.onDestroy();
		context.actionBarshowHome();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bundle = getArguments();

		User user = (User) bundle.getSerializable(MainActivity.PROPERTY_USER);

		userName = user.getUserName();
		password = user.getPassWord();

		img_actionBar_add_sender_name = context.setAddSenderNameActionBar();

		View rootView = inflater.inflate(R.layout.fragment_sender_name, null);
		// setup ui
		Typeface arabicTypeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/gd_medium.otf");
		TextView titlTv = (TextView) rootView.findViewById(R.id.textView1);
		titlTv.setTypeface(arabicTypeface);

		// showAll button
		showAllBtn = (Button) rootView.findViewById(R.id.allSendersBtn);
		showAllBtn.setTypeface(arabicTypeface);

		// show new sender button
		showNewSendersBtn = (Button) rootView.findViewById(R.id.newSendersBtn);
		showNewSendersBtn.setTypeface(arabicTypeface);

		// sender names List
		listView = (ListView) rootView.findViewById(R.id.listView1);
		// listView.setAdapter(new SenderNameListAdapter(context, 0,
		// 0,getSendersList(0,true)));
		populateSenderList();
		// setting empty view for list
		// set empty view
		emptyView = new TextView(context);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText(R.string.no_sender_names);
		emptyView.setTextSize(14);
		emptyView.setVisibility(View.GONE);
		emptyView.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);

	
		img_actionBar_add_sender_name
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
						android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();

						fragmentTransaction.hide(SenderNamesFragment.this);

						// ((MainActivity)
						// getActivity()).setupActionBarHomeButton(false,
						// false);
						((MainActivity) getActivity())
								.setDrawerIndicatorEnabled(false);
						fragmentTransaction.addToBackStack(null);
						AddSenderFragment newFragment = new AddSenderFragment();
						newFragment.setArguments(bundle);
						((MainActivity) getActivity())
								.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
						fragmentTransaction.replace(R.id.content_frame,
								newFragment);
						fragmentTransaction.commit();
					}
				});

		showAllBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectedTabIndex == 1) {
					return;
				}
				selectedTabIndex = 1;
				showAllBtn.setBackgroundResource(R.drawable.active_gray_bg);
				showNewSendersBtn
						.setBackgroundResource(R.drawable.inactive_gray_bg);
				// listView.setAdapter(new SenderNameListAdapter(context, 0,
				// 0,getSendersList(0,false)));
				populateSenderList();
			}
		});

		showNewSendersBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectedTabIndex == 0) {
					return;
				}
				selectedTabIndex = 0;
				showNewSendersBtn
						.setBackgroundResource(R.drawable.active_gray_bg);
				showAllBtn.setBackgroundResource(R.drawable.inactive_gray_bg);
				populateSenderList();
			}
		});

		return rootView;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.hide(SenderNamesFragment.this);

		// ((MainActivity) getActivity()).setupActionBarHomeButton(false,
		// false);
		((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);
		fragmentTransaction.addToBackStack(null);
		AddSenderFragment newFragment = new AddSenderFragment();
		newFragment.setArguments(bundle);
		;
		((MainActivity) getActivity())
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		fragmentTransaction.replace(R.id.content_frame, newFragment);
		fragmentTransaction.commit();

		return true;
	}

	// for test

	private SenderName[] getSendersList(int index, boolean isNew) {

		SenderName[] senders = new SenderName[4];
		if (isNew) {
			senders[0] = new SenderName("not required confirm", 1, "new", "No",
					0);
			senders[1] = new SenderName("required confirmation ", 1, "new",
					"Yes", 1);
			senders[2] = new SenderName("active ", 1, "Yes", "Yes", 1);
			senders[3] = new SenderName("refused", 1, "No", "Yes", 1);
		} else {
			senders[0] = new SenderName("not required confirm", 1, "new", "No",
					0);
			senders[1] = new SenderName("required confirmation ", 1, "new",
					"Yes", 1);
			senders[2] = new SenderName("all ", 1, "Yes", "Yes", 1);
			senders[3] = new SenderName("all", 1, "No", "Yes", 1);
		}
		return senders;
	}

	private void populateSenderList() {
		final ProgressDialog progress = new ProgressDialog(context);
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage(getString(R.string.loading_all_senders));
		progress.show();
		getSenders = new AsyncTask<Object, Object, Object>() {

			@Override
			protected void onPostExecute(Object result) {
				progress.dismiss();
				if (sendersList != null) {
					((ViewGroup) listView.getParent()).removeView(emptyView);
					listView.setEmptyView(getActivity().findViewById(
							android.R.id.empty));
					listView.setAdapter(new SenderNameListAdapter(context, 0,
							0, sendersList));
				} else {
					sendersList = new SenderName[0];
					listView.setAdapter(new SenderNameListAdapter(context, 0,
							0, sendersList));

				}
			}

			@Override
			protected Object doInBackground(Object... params) {
				String senderType = "new";
				if (selectedTabIndex == 1) {
					senderType = null;
				}
				String results = Utils.getPage(RestfulUrl.senderListNew(
						userName, password));

				if (results != null) {
					try {
						// sendersList
						JSONObject response = new JSONObject(results);
						int totalResults = response.getInt("totalresults");
						int code = response.getInt("Code");
						if (code == 117) {
							sendersList = new SenderName[0];
						}
						if (totalResults > 0) {
							sendersList = new SenderName[totalResults];
							JSONArray sendersJsonArray = response
									.getJSONArray("SenderArchive");
							for (int i = 0; i < sendersJsonArray.length(); i++) {
								JSONObject senderJsonObject = sendersJsonArray
										.getJSONObject(i);
								sendersList[i] = new SenderName(
										senderJsonObject
												.getString("SenderName"),
										senderJsonObject.getInt("SenderID"),
										senderJsonObject.getString("State"),
										senderJsonObject
												.getString("ActiveState"),
										senderJsonObject.getInt("ISDefult"));
							}
						}
					} catch (Exception e) {
						sendersList = null;
						e.printStackTrace();
					}
				}
				return null;
			}
		};

		getSenders.execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		selectedTabIndex = 0;
		//populateSenderList();

	}

}
