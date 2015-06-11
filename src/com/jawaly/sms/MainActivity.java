package com.jawaly.sms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import net.jawaly.sms.api.R;
import net.jawaly.user.ManageUser;

import org.apache.http.util.ByteArrayBuffer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.jawaly.adapters.MenuListAdapter;
import com.jawaly.fragments.ContactsListFragment;

import com.jawaly.fragments.SendSMSFragment;
import com.jawaly.fragments.SettingsFragment;
import com.jawaly.senders.AddSenderFragment;
import com.jawaly.senders.SenderNamesFragment;
import com.jawaly.utils.ManageLocalization;
import com.jawaly.utils.Utilss;


public class MainActivity extends ActionBarActivity implements
		OnFocusChangeListener {

	final String TAG = getClass().getSimpleName();
	public static Typeface arabicFont;
	public static int gravity = 0;
	public static String language;
	public static final String PROPERTY_USER_NAME = "user_name";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_CREDIT = "user_credit";
	public static final String PROPERTY_FULL_NAME = "full_name";
	public static final String PROPERTY_HAVE_OPEN_SENDER = "have_open_sender";
	public static final String PROPERTY_USER_ID = "user_id";
	public static final String PROPERTY_USER_EMAIL = "user_email";
	public static final String PROPERTY_USER_EMAIL_state = "user_email_state";
	public static final String PROPERTY_USER_MOBILE_NUMBER = "user_mobile_number";
	public static final String PROPERTY_USER_MOBILE_NUMBER_STATE = "user_mobile_number_state";
	public static final String PROPERTY_USER_GROUP_ID = "group_id";
	public static final String PROPERTY_USER_STATE = "user_state";

	public static final String PROPERTY_USER = "user";

	public static final String PROPERTY_TEXT_MESSAGE = "text_message";
	public static final String PROPERTY_SEND_TO = "send_to_numbers";

	protected SharedPreferences pref;
	public static final String PROPERTY_APP_PREF = "4jawaly_sms";

	public ListView menuList;
	public DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	public boolean isTicketClosed = false;

	// field for user account info
	boolean haveOpenSenderName = false;
	int userCredit = 0;
	private String userName;
	private String password;
	private String fullName;

	private ImageView img_actionBar_menu, img_actionBar_back;
	private ImageView img_actionBar_add_sender_name;

	int position = 0;

	private MenuListAdapter myMenuAdapter;

	public String[] menuListItems, menuListItemsForAdmin;
	String currentTitle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			FragmentManager fm = getSupportFragmentManager();
			for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
				fm.popBackStack();
			}
		}

		// /////////////////////////////////////////////////////////////////////////////////girgis
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		// to set localization
		language = new Utilss().getLocalization(this);
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration c = new Configuration(getResources().getConfiguration());
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());
		// /////////////////////////////////////////////////////end localization
		// /////////////////////////////////////////////////////end girgis

		menuListItems = getResources().getStringArray(R.array.side_menu);
		setContentView(R.layout.activity_main);
		pref = getSharedPreferences(MainActivity.PROPERTY_APP_PREF,
				Context.MODE_PRIVATE);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (new ManageLocalization(MainActivity.this).get_userLanguage()
				.equalsIgnoreCase("ar")) {
			drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.END);
		} else {
			drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);
		}
		menuList = (ListView) findViewById(R.id.left_drawer);
		myMenuAdapter = new MenuListAdapter(this, 0, 0, menuListItems);
		menuList.setAdapter(myMenuAdapter);
		menuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 5) {

				} else {
					selectItem(arg2);
				}
			}
		});

		// enable ActionBar app icon to behave as action to toggle nav drawer
		setupActionBarHomeButton(false, false);
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon

		if (new ManageLocalization(MainActivity.this).get_userLanguage()
				.equalsIgnoreCase("ar")) {
			mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
					R.drawable.ic_menu, R.string.open_drawer,
					R.string.close_drawer) {
				@Override
				public boolean onOptionsItemSelected(MenuItem item) {
					if (item != null && item.getItemId() == android.R.id.home) {
						if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
							drawerLayout.closeDrawer(Gravity.RIGHT);
						} else {
							drawerLayout.openDrawer(Gravity.RIGHT);
						}
					}
					return false;
				}
			};
		} else {
			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			drawerLayout, /* DrawerLayout object */
			R.drawable.ic_menu, /* nav drawer image to replace 'Up' caret */
			R.string.open_drawer, /* "open drawer" description for accessibility */
			R.string.close_drawer /* "close drawer" description for accessibility */
			) {
				public void onDrawerClosed(View view) {
					// getSupportActionBar().setTitle(currentTitle);
					getSupportActionBar().setTitle(null);
				}

				public void onDrawerOpened(View drawerView) {
					getSupportActionBar()
							.setTitle(getString(R.string.app_name));
					getSupportActionBar().setTitle(null);
				}
			};
		}

		drawerLayout.setDrawerListener(mDrawerToggle);
		img_actionBar_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawerLayout.isDrawerOpen(menuList)) {
					drawerLayout.closeDrawer(menuList);
				} else {
					drawerLayout.openDrawer(menuList);
				}
			}
		});

		img_actionBar_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		if (savedInstanceState == null) {
			
			userName = pref.getString(MainActivity.PROPERTY_USER_NAME, null);
			password = pref.getString(MainActivity.PROPERTY_PASSWORD, null);
			if (userName == null && password == null) {
				currentTitle = getString(R.string.settings);
				selectItem(2);
			} else {
				currentTitle = getString(R.string.send_sms);
				selectItem(0);
			}

		}

	}

	public void setupActionBarHomeButton(boolean upEnabled, boolean homeButton) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(upEnabled);
		getSupportActionBar().setHomeButtonEnabled(homeButton);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		getSupportActionBar().setDisplayShowCustomEnabled(true);
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar_layout, null);
		getSupportActionBar().setCustomView(actionBarLayout);

		img_actionBar_back = (ImageView) findViewById(R.id.action_bar_back);
		img_actionBar_menu = (ImageView) findViewById(R.id.action_bar_menu);
		img_actionBar_add_sender_name = (ImageView) findViewById(R.id.action_bar_add_sender_name);

		// getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	public void actionBarshowHome() {
		img_actionBar_menu.setVisibility(View.VISIBLE);
		img_actionBar_back.setVisibility(View.INVISIBLE);
		img_actionBar_add_sender_name.setVisibility(View.INVISIBLE);
	}

	public void actionBarshowBack() {
		img_actionBar_menu.setVisibility(View.INVISIBLE);
		img_actionBar_back.setVisibility(View.VISIBLE);
		img_actionBar_add_sender_name.setVisibility(View.INVISIBLE);
	}

	public ImageView setAddSenderNameActionBar() {
		img_actionBar_add_sender_name.setVisibility(View.VISIBLE);
		return img_actionBar_add_sender_name;
	}

	public void populateMenuListView() {

		// Log.d("populateMenuListView", "user group" + " " + group_id);
		menuListItems = getResources().getStringArray(R.array.side_menu);
		myMenuAdapter = new MenuListAdapter(this, 0, 0, menuListItems);
		menuList.setAdapter(myMenuAdapter);

		menuList.setItemChecked(position, true);
		currentTitle = menuListItems[position];

		drawerLayout.closeDrawer(menuList);

	}

	private void selectItem(int position) {
		this.position = position;
		Fragment fragment = null;
		String tag = null;
		switch (position) {
		case 0:
			fragment = new SendSMSFragment();
			tag = SendSMSFragment.fragmentTag;
			break;

		case 1:
			fragment = new SenderNamesFragment();
			tag = SenderNamesFragment.fragmentTag;
			break;

		case 2:
			fragment = new SettingsFragment();
			tag = SettingsFragment.TAG;
			break;

		default:
			fragment = new SettingsFragment();
			tag = SettingsFragment.TAG;
			break;
		}

		Bundle args = new Bundle();
		args.putString(PROPERTY_USER_NAME, userName);
		args.putString(PROPERTY_PASSWORD, password);
		args.putString(PROPERTY_FULL_NAME, fullName);
		args.putInt(PROPERTY_CREDIT, userCredit);
		args.putBoolean(PROPERTY_HAVE_OPEN_SENDER, haveOpenSenderName);
		args.putSerializable(MainActivity.PROPERTY_USER,
				new ManageUser(this).getUser());
		/*
		 * receiving data from other application
		 */
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (action != null) {
			try {
				// in case of action send
				if (Intent.ACTION_SEND.equals(action) && type != null) {
					if ("text/plain".equals(type)) {
						String sharedText = intent
								.getStringExtra(Intent.EXTRA_TEXT);
						args.putString(PROPERTY_TEXT_MESSAGE, sharedText);
					}
				}
				// in case of action == sendto
				else if (Intent.ACTION_SENDTO.equals(action)) {
					Uri data = intent.getData();
					if (data.getEncodedSchemeSpecificPart() != null) {
						String number = data.getEncodedSchemeSpecificPart();
						number = number.replace("-", "");
						number = number.replace("%20", "");
						number = number.replace("#", "");
						// number = number.replace("*","");
						args.putString(PROPERTY_SEND_TO, number);
					}

				} else if (intent.getExtras().containsKey("sms_body")) {
					String sharedText = intent.getStringExtra("sms_body");
					args.putString(PROPERTY_TEXT_MESSAGE, sharedText);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();

		Log.d("fragmenttttttt", "fragmenttttttt" + " false");
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment, tag).commit();

		Log.d("fragmenttttttt",
				"fragmenttttttt " + fragmentManager.getBackStackEntryCount());

		// update selected item and title, then close the drawer
		menuList.setItemChecked(position, true);
		currentTitle = menuListItems[position];

		drawerLayout.closeDrawer(menuList);

	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void openFragment(Fragment newFragment, String fragmentTag,
			String activityTitle, boolean replace, Bundle bundle) {

		Log.d("open fragment", "open fragment start");

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		Fragment f = getVisibleFragment();

		// setDrawerIndicatorEnabled(false);
		fragmentTransaction.addToBackStack(null);
		newFragment.setArguments(bundle);

		// getSupportActionBar().setTitle(activityTitle);
		// setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		fragmentTransaction.add(R.id.content_frame, newFragment, fragmentTag);
		fragmentTransaction.commit();

		Log.d("open fragment", "open fragment end");
	}

	@Override
	public void onBackPressed() {

		if (drawerLayout.isDrawerOpen(menuList)) {
			drawerLayout.closeDrawer(menuList);
		} else {

			FragmentManager fragmentManager = getSupportFragmentManager();

			Log.d("back pressed", "back pressed");
			Fragment f = getVisibleFragment();

			if (f != null && f instanceof ContactsListFragment) {
				super.onBackPressed();
			} else if (f != null && f instanceof AddSenderFragment) {
				super.onBackPressed();
			} else {
				Fragment myFragment = fragmentManager
						.findFragmentByTag(SendSMSFragment.fragmentTag);
				if (myFragment != null && myFragment.isVisible()) {
					super.onBackPressed();
				} else {
					Log.d("fragmenttttttt", "fragmenttttttt" + " false");
					Fragment fragment = new SendSMSFragment();

					Bundle args = new Bundle();
					args.putString(PROPERTY_USER_NAME, userName);
					args.putString(PROPERTY_PASSWORD, password);
					args.putString(PROPERTY_FULL_NAME, fullName);
					args.putInt(PROPERTY_CREDIT, userCredit);
					args.putBoolean(PROPERTY_HAVE_OPEN_SENDER,
							haveOpenSenderName);
					args.putSerializable(MainActivity.PROPERTY_USER,
							new ManageUser(this).getUser());

					fragment.setArguments(args);

					fragmentManager
							.beginTransaction()
							.replace(R.id.content_frame, fragment,
									SendSMSFragment.fragmentTag).commit();

					this.position = 0;

					currentTitle = menuListItems[0];
					getSupportActionBar().setTitle(currentTitle);
					menuList.setItemChecked(0, true);

					drawerLayout.closeDrawer(menuList);
					// setDrawerIndicatorEnabled(true);
					// setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
				}
			}
		}
	}

	/*
	 * return fragment get the current fragment
	 */
	private Fragment getCurrentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();

		if (fragmentManager.getBackStackEntryCount() > 0) {
			String fragmentTag = fragmentManager.getBackStackEntryAt(
					fragmentManager.getBackStackEntryCount() - 1).getName();
			Fragment currentFragment = getSupportFragmentManager()
					.findFragmentByTag(fragmentTag);
			Log.d("getVisibleFragment",
					"fragmenttttttt " + currentFragment.getTag() + " "
							+ currentFragment.toString());
			return currentFragment;
		} else {
			return null;
		}
	}

	/*
	 * return fragment get the current fragment
	 */
	public Fragment getVisibleFragment() {
		try {
			FragmentManager fragmentManager = MainActivity.this
					.getSupportFragmentManager();
			List<Fragment> fragments = fragmentManager.getFragments();
			for (Fragment fragment : fragments) {
				if (fragment != null && fragment.isVisible()) {
					Log.d("getVisibleFragment",
							"fragmenttttttt " + fragment.getTag() + " "
									+ fragment.toString());
					return fragment;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		drawerLayout.isDrawerOpen(menuList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setDrawerLockMode(int mode) {
		drawerLayout.setDrawerLockMode(mode);

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			v.setBackgroundResource(R.drawable.stroked_shape_hover);
		} else {
			v.setBackgroundResource(R.drawable.stroked_shape);
		}

	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
	// v.setBackgroundResource(R.drawable.blue_bg_hover);
	// } else {
	// v.setBackgroundResource(R.drawable.blue_bg);
	// }
	//
	// return false;
	// }

	public void switchButton(Button selectedButton, Button unSelectedButton) {
		selectedButton.setBackgroundResource(R.drawable.orange_bg);
		selectedButton.setTextColor(ColorStateList.valueOf(Color.WHITE));
		unSelectedButton.setBackgroundResource(R.drawable.orange_stroked_shap);
		unSelectedButton.setTextColor(ColorStateList.valueOf(Color
				.parseColor("#e19557")));

	}

	public void setDrawerIndicatorEnabled(boolean indicator) {
		mDrawerToggle.setDrawerIndicatorEnabled(indicator);
	}

	public void downloadImage(String imageURL, String imageName) {
		File fileWithinMyDir = getApplicationContext().getFilesDir();
		String PATH = fileWithinMyDir.getAbsolutePath() + "/" + imageName;
		try {
			URL url = new URL(imageURL);
			File file = new File(PATH);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			Log.d(TAG, "input stream" + is);
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("download", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		DatabaseHelper db = new DatabaseHelper(this);
		db.removeBanks();
		super.onDestroy();
	}

}
