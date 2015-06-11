package com.jawaly.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import net.jawaly.sms.api.R;
import net.jawaly.user.ManageUser;
import net.jawaly.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jawaly.customview.MyProgressDialog;
import com.jawaly.rest.GetTask;
import com.jawaly.rest.JsonParser;
import com.jawaly.rest.RestTaskCallback;
import com.jawaly.rest.RestfulUrl;
import com.jawaly.senders.SenderName;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.MainActivity;
import com.jawaly.sms.Utils;
import com.jawaly.utils.Utilss;

public class SendSMSFragment extends Fragment implements OnFocusChangeListener {

	private final String TAG = this.getClass().getSimpleName();

	private ArrayList<String> list;

	public static final String fragmentTag = "sendsms";
	// ui views
	// public TextView creditTextView;
	Spinner chooseSenderSpinenr;
	EditText senderEditText;
	// Spinner chooseMainGroupSpinner;
	Button phoneBookBtn;
	TextView phoneBookStatsTextView;
	Button sendBtn;
	public EditText msgText;
	public EditText numbersEditText;
	TextView messageCountTextView;
	public TextView countSelectedTextView;
	// private TextView favorite_msgs;

	// properties for ui formating
	Typeface arabicTypeface;
	StringBuilder englishText;

	/**
	 * laterButton is for detecting the time when send sms
	 */
	Button laterButton;
	/**
	 * nowButton detect that sending sms is now
	 */
	Button nowButton;

	// user setting properties
	String userName;// user name
	String password;// password
	boolean haveOpenSenderName;
	int userCredit, Group_id;

	/**
	 * user sender names list and have value in case of user haven't opened
	 * sender name
	 */
	String[] senderNames;
	ArrayList<SenderName> sender_list = new ArrayList<SenderName>();

	private int spinnerSendersDefault;

	// ContactListGroup[] mainGroups;
	// MainGroupsSpinnerAdapter mainGroupadapter;

	DatabaseHelper db;

	private TextView totalMessageCharacters;
	private TextView remainingMessagesCharaters;
	private TextView time_value;

	/**
	 * numbers is numbers that will receive messages
	 */
	String numbers;
	/**
	 * sender of message and it's value will be set from senders list and sender
	 * edit text
	 */
	String senderName;
	/**
	 * messageText is the content of messages
	 */
	String message;
	/**
	 * groups is user contacts groups ids registerd in site and groups is
	 * sperated by comma
	 */
	String groups, groups_name;

	MainActivity context;

	GetTask getGroupsList;
	GetTask authenticate;
	GetTask getSenders;

	MyProgressDialog dialog;
	ViewGroup addMessageToFavorite;

	/**
	 * detect is message send later or now
	 */
	public boolean sendMessageLater = false;
	/**
	 * in case of user choose send MessageLater this property will have value of
	 * date time in this format y-m-d H:i:s
	 */
	int datePickerDay = 0;
	int datePickerYear = 0;
	int datePickerMonth = 0;
	int hour = 0;
	int minut = 0;

	Bundle bundle;
	User user;

	/**
	 * constructor
	 */
	public SendSMSFragment() {
		context = (MainActivity) getActivity();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String language = new Utilss().getLocalization(getActivity());

		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration c = new Configuration(getResources().getConfiguration());
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());

		bundle = getArguments();
		user = (User) bundle.getSerializable(MainActivity.PROPERTY_USER);

		// userName = bundle.getString(MainActivity.PROPERTY_USER_NAME);
		userName = user.getUserName();
		// password = bundle.getString(MainActivity.PROPERTY_PASSWORD);
		password = user.getPassWord();

		userCredit = bundle.getInt(MainActivity.PROPERTY_CREDIT);
		haveOpenSenderName = bundle
				.getBoolean(MainActivity.PROPERTY_HAVE_OPEN_SENDER);

		authenticateUser();
		db = new DatabaseHelper(context);

		((MainActivity) getActivity()).actionBarshowHome();

		View rootView = inflater.inflate(R.layout.activity_sendsms, null);

		arabicTypeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/gd_medium.otf");

		time_value = (TextView) rootView
				.findViewById(R.id.sendingTimeTextView_value);

		TextView sendTo = (TextView) rootView.findViewById(R.id.textView1);
		sendTo.setTypeface(arabicTypeface);

		TextView smsDetailsTV = (TextView) rootView
				.findViewById(R.id.smsDetails);
		smsDetailsTV.setTypeface(arabicTypeface);

		TextView sendingTimeTV = (TextView) rootView
				.findViewById(R.id.sendingTimeTextView);
		sendingTimeTV.setTypeface(arabicTypeface);

		chooseSenderSpinenr = (Spinner) rootView
				.findViewById(R.id.chooseSenderSpinner);
		chooseSenderSpinenr
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (senderNames != null) {
							senderName = senderNames[position];
							Log.d(TAG, "selected sender name: " + senderName);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

						senderName = chooseSenderSpinenr.getSelectedItem()
								.toString();
						Log.d(TAG, "on nothing selected sender name: "
								+ senderName);
					}
				});
		// set sender adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.custome_spiner_item,
				new String[] { getString(R.string.sender_name) });
		adapter.setDropDownViewResource(R.layout.custome_spiner_item);
		chooseSenderSpinenr.setAdapter(adapter);

		senderEditText = (EditText) rootView
				.findViewById(R.id.senderNameEditText);
		senderEditText.setOnFocusChangeListener(context);

		senderEditText.setFilters(setMaxLength(12));
		senderEditText.addTextChangedListener(new TextWatcher() {
			public boolean isPhoneNumber(String editTextStr, boolean required) {
				String PHONE_REGEX = "^([1-9][0-9]{0,2})?(\\.[0-9]?)?$";
				String text = editTextStr;
				// pattern doesn't match so returning false
				if (required && !Pattern.matches(PHONE_REGEX, text)) {
					// editText.setError(errMsg);
					return false;
				}
				return true;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String msg = s.toString();
				englishText = new StringBuilder();
				Boolean isArabic = false;

				for (char currentChar : msg.toCharArray()) {
					Log.d("char", "" + currentChar);

					if (Pattern.matches("[\u0100-\uFFFF]", "" + currentChar)
							|| Character.isWhitespace(currentChar)) {
						isArabic = true;
					}

					else
						englishText.append(currentChar);

				}

				if (isArabic) {
					senderEditText.setError(Utils
							.setupErrorSpan(getString(R.string.error_sendername_lang)));
					senderEditText.setText(englishText.toString());

					senderEditText.setSelection(senderEditText.getText()
							.length());
				}

				else if (isPhoneNumber(englishText.toString(), true)) {
					senderEditText.setFilters(setMaxLength(15));
				}
			}
		});
		// set main group spinner and its adapter
		// mainGroups = new ContactListGroup[] { new ContactListGroup(0,
		// getString(R.string.choose_main_group), 0) };

		// update selected phone book number
		countSelectedTextView = (TextView) rootView
				.findViewById(R.id.countSelectedTextView);
		updateSelectedContacts();

		phoneBookBtn = (Button) rootView.findViewById(R.id.phoneBook);

		phoneBookBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				openFragment(new ContactsListFragment(), "contactList",
						getString(R.string.phone_book), false);
			}

		});

		sendBtn = (Button) rootView.findViewById(R.id.sendBtn);
		sendBtn.setTypeface(arabicTypeface);
		// sendBtn.setOnTouchListener(context);
		msgText = (EditText) rootView.findViewById(R.id.msgTextEditText);
		msgText.setOnFocusChangeListener(this);

		if (bundle.getString(MainActivity.PROPERTY_TEXT_MESSAGE) != null) {
			msgText.setText(bundle
					.getString(MainActivity.PROPERTY_TEXT_MESSAGE));
		}
		msgText.setOnFocusChangeListener(this);

		messageCountTextView = (TextView) rootView
				.findViewById(R.id.messageCountTextView);
		totalMessageCharacters = (TextView) rootView
				.findViewById(R.id.totalMessagesCharacters);
		remainingMessagesCharaters = (TextView) rootView
				.findViewById(R.id.remainingChars);
		setMessageStats(0, 0, 0);
		msgText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// Log.d("after text ",s.toString());
				int textCount = 0;
				Boolean isArabic = false;
				String msg = s.toString();
				for (char currentChar : msg.toCharArray()) {
					Log.d("char", "" + currentChar);
					if (Pattern.matches("\n", "" + currentChar)
							|| currentChar == '\n') {
						textCount += 2;
					} else {
						textCount++;
					}
					if (Pattern.matches("[\u0100-\uFFFF]", "" + currentChar)) {
						isArabic = true;
					}
					// [\u0100-\uFFFF]
				}
				int firstMessagePart = 160; // 160
				int secondMessagepart = 153;// 153
				if (isArabic) {
					firstMessagePart = 70;
					secondMessagepart = 67;
					msgText.setGravity(Gravity.RIGHT);
				} else {
					msgText.setGravity(Gravity.LEFT);
				}
				int currMessagePart = 1;
				int remainingChar = firstMessagePart - textCount;
				if (textCount > firstMessagePart) {

					currMessagePart = (textCount / secondMessagepart) + 1;

					remainingChar = (currMessagePart * secondMessagepart)
							- textCount;
					// currMessagePart +=1;
				}
				setMessageStats(currMessagePart, remainingChar, textCount);

			}
		});
		numbersEditText = (EditText) rootView
				.findViewById(R.id.numbersEditText);
		numbersEditText.setOnFocusChangeListener(this);
		if (bundle.getString(MainActivity.PROPERTY_SEND_TO) != null) {
			numbersEditText.setText(bundle
					.getString(MainActivity.PROPERTY_SEND_TO));
		}

		// setup sending times options
		laterButton = (Button) rootView.findViewById(R.id.sendLater);
		laterButton.setTypeface(arabicTypeface);
		nowButton = (Button) rootView.findViewById(R.id.now);
		nowButton.setTypeface(arabicTypeface);
		laterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectLaterButton();
				// open dialog to choose date
				openChooseLaterMessageDialog();
			}
		});
		nowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectNowButton();
				sendMessageLater = false;
				time_value.setText("");
			}
		});

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String sender_name = "";

				message = msgText.getText().toString().trim();
				// first validate message is empty or not
				if (TextUtils.isEmpty(message)) {
					Utils.setupErrorAppMsg(context,
							getString(R.string.error_enter_message));
					// msgText.setError(getString(R.string.error_enter_message));
				} else {
					// check if user select number or not
					numbers = db.getNumbers();
					numbers += numbersEditText.getText().toString().trim();

					// get inserted groups
					groups = db.getGroupsIds();
					groups_name = db.getGroupsNames();
					Log.d(groups, "groups:" + groups);
					Log.d(groups, "groups_name:" + groups_name);
					if (TextUtils.isEmpty(numbers) && TextUtils.isEmpty(groups)) {
						Utils.setupErrorAppMsg(context,
								getString(R.string.error_empty_number));
					} else {
						if (TextUtils.isEmpty(senderName)) {
							senderName = senderEditText.getText().toString()
									.trim();
						}
						if (TextUtils.isEmpty(senderName)) {
							Utils.setupErrorAppMsg(context,
									getString(R.string.error_enter_sender_name));
						} else {
							String date = "";
							if (sendMessageLater) {
								date = datePickerYear + "-" + datePickerMonth
										+ "-" + datePickerDay + " " + hour
										+ ":" + minut;
							} else {
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat format1 = new SimpleDateFormat(
										"dd/MM/yyyy hh:mm");
								date = format1.format(cal.getTime());
							}

							if (haveOpenSenderName) {
								senderName = senderEditText.getText()
										.toString().trim();
								sender_name = senderName;
							} else {
								sender_name = chooseSenderSpinenr
										.getSelectedItem().toString();
							}

							Log.d("SenderName", senderName);

							new com.jawaly.utils.Utilss()
									.ShowDialogConfirmSendSms(getActivity(),
											new ManageUser(getActivity())
													.getUser(), sender_name,
											date, msgText.getText().toString(),
											numbers, messageCountTextView
													.getText().toString(),
											groups, groups_name,
											SendSMSFragment.this, senderName);
							// sendSms();
						}
					}
				}

			}
		});

		return rootView;
	}

	private void getSenderNamesList() {
		getSenders = new GetTask(
				RestfulUrl.senderListActive(userName, password),
				new RestTaskCallback() {

					@Override
					public void onTaskNotComplete() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTaskComplete(String result) {
						try {
							JSONObject response = new JSONObject(result);
							// SenderArchive
							if (response.getInt("Code") == 117) {
								JSONArray sendersJsonArray = response
										.getJSONArray("SenderArchive");
								int countSenders = sendersJsonArray.length();
								senderNames = new String[countSenders];
								for (int i = 0; i < countSenders; i++) {
									JSONObject j = sendersJsonArray
											.getJSONObject(i);
									senderNames[i] = j.getString("SenderName");
									if (j.getInt("ISDefult") == 1) {
										spinnerSendersDefault = i;
									}

									SenderName sender = new SenderName();
									sender.setDefault(j.getInt("ISDefult"));
									sender.setSenderName(j
											.getString("SenderName"));

									sender_list.add(sender);
								}
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										context, R.layout.custome_spiner_item,
										senderNames);
								adapter.setDropDownViewResource(R.layout.custome_spiner_item);
								// adapter.setDropDownViewResource(R.layout.custome_spiner_item);
								chooseSenderSpinenr.setAdapter(adapter);
								chooseSenderSpinenr.setSelection(
										spinnerSendersDefault, true);

								// getGroupList();
							} else {
								// TODO
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		getSenders.execute();
	}

	private void setMessageStats(int messageCount, int remainingChars,
			int txtCount) {
		remainingMessagesCharaters.setText(String.valueOf(remainingChars));
		messageCountTextView.setText(String.valueOf(messageCount));
		totalMessageCharacters.setText(String.valueOf(txtCount));
	}

	private void authenticateUser() {
		authenticate = new GetTask(RestfulUrl.getUserUrl(userName,
				new ManageUser(getActivity()).getUser().getPassWord(),
				RestfulUrl.SPECIAL_API), new RestTaskCallback() {

			@Override
			public void onTaskNotComplete() {

			}

			@Override
			public void onTaskComplete(String result) {
				try {
					JSONObject response = new JSONObject(result);
					int code = response.getInt("Code");
					if (code == 104) {
						if (new Utilss().validateUser(getActivity(), userName,
								password, result)) {
							JSONArray user = response.getJSONArray("user");
							Group_id = user.getInt(8);
							Log.d("user group", "user group" + " " + Group_id);
							// context.populateMenuListView(Group_id);
							userCredit = user.getInt(11);
							User u = new User();
							u.setGroup_id(Group_id);
							u.setCredit(userCredit);
							new ManageUser(getActivity()).updateUser(u);

							getSenderNamesList();

							// updateCredit(userCredit + "");
						} else {
							// getActivity().finish();
						}

					}
				} catch (Exception e) {

				}
			}
		});
		authenticate.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (authenticate != null
				&& authenticate.getStatus() == AsyncTask.Status.RUNNING) {
			authenticate.cancel(true);
		}
		if (getSenders != null
				&& getSenders.getStatus() == AsyncTask.Status.RUNNING) {
			getSenders.cancel(true);
		}
		if (getGroupsList != null
				&& getGroupsList.getStatus() == AsyncTask.Status.RUNNING) {
			getGroupsList.cancel(true);
		}
		// db.emptyTemp();

	}

	public void updateSelectedContacts() {
		countSelectedTextView.setText(" " + db.getCountNumbers());
	}

	private void openChooseLaterMessageDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setTitle(getString(R.string.choose_sending_time));
		dialog.setContentView(R.layout.dialog_choose_send_date);
		Button done = (Button) dialog.findViewById(R.id.ok);
		// done.setOnTouchListener(context);
		Button cancel = (Button) dialog.findViewById(R.id.cancel);
		// cancel.setOnTouchListener(new OrangeButtonsListeners());

		final DatePicker datePicker = (DatePicker) dialog
				.findViewById(R.id.datePicker1);
		final TimePicker timePicker = (TimePicker) dialog
				.findViewById(R.id.timePicker1);
		if (datePickerDay != 0 && datePickerMonth != 0 && datePickerYear != 0) {
			datePicker.init(datePickerYear, datePickerMonth - 1, datePickerDay,
					null);
		}
		Log.d(TAG, "hour" + hour + " minut:" + minut);
		if (hour != 0) {
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minut);
		}

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datePickerDay = datePicker.getDayOfMonth();
				datePickerMonth = datePicker.getMonth() + 1;
				datePickerYear = datePicker.getYear();
				hour = timePicker.getCurrentHour();
				minut = timePicker.getCurrentMinute();

				if (isTimeLater(datePickerYear + "/" + datePickerMonth + "/"
						+ datePickerDay + " " + hour + ":" + minut)) {
					time_value.setText(datePickerYear + "/" + datePickerMonth
							+ "/" + datePickerDay + " " + hour + ":" + minut);
					sendMessageLater = true;
				} else {
					selectNowButton();
					time_value.setText("");
					sendMessageLater = false;
				}

				dialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}
		});

		dialog.show();
	}

	private void selectLaterButton() {
		laterButton.setBackgroundResource(R.drawable.orange_bg);
		laterButton.setTextColor(ColorStateList.valueOf(Color.WHITE));
		nowButton.setBackgroundResource(R.drawable.orange_stroked_shap);
		nowButton.setTextColor(ColorStateList.valueOf(Color
				.parseColor("#e19557")));

	}

	private void selectNowButton() {
		laterButton.setBackgroundResource(R.drawable.orange_stroked_shap);
		laterButton.setTextColor(ColorStateList.valueOf(Color
				.parseColor("#e19557")));
		nowButton.setBackgroundResource(R.drawable.orange_bg);
		nowButton.setTextColor(ColorStateList.valueOf(Color.WHITE));

	}

	private void openFragment(Fragment newFragment, String fragmentTag,
			String activityTitle, boolean replace) {
		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.hide(SendSMSFragment.this);
		// getActivity().setTitle(activityTitle);
		if (!replace) {
			((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);
			fragmentTransaction.addToBackStack(null);
		} else {
			newFragment.setArguments(bundle);
		}
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				activityTitle);
		((MainActivity) getActivity())
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		fragmentTransaction.replace(R.id.content_frame, newFragment,
				fragmentTag);
		fragmentTransaction.commit();
	}

	private void openFragmentFor(Fragment newFragment, String fragmentTag,
			String activityTitle, boolean replace) {
		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				activityTitle);
		fragmentTransaction.replace(R.id.content_frame, newFragment,
				fragmentTag);
		fragmentTransaction.commit();

		((MainActivity) getActivity()).menuList.setItemChecked(1, true);

		((MainActivity) getActivity()).drawerLayout
				.closeDrawer(((MainActivity) getActivity()).menuList);
	}

	public void updateSelectedMessage(String message) {
		msgText.setText(message);
	}

	private boolean isTimeLater(String datetime) {
		try {
			Calendar date = Calendar.getInstance();
			date.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm")
					.parse(datetime)); // Parse into Date object
			Calendar now = Calendar.getInstance();
			if (date.getTimeInMillis() > now.getTimeInMillis()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}


	// set the max number of digits the user can enter
	public InputFilter[] setMaxLength(int length) {
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(length);
		return FilterArray;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Utils.removeAppMsg(getActivity());
	}

}
