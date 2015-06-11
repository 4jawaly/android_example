package com.jawaly.fragments;




import android.widget.SimpleCursorAdapter;
import com.jawaly.adapters.ContactsListViewAdapter;
import com.jawaly.customview.MyProgressDialog;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.MainActivity;
import net.jawaly.sms.api.R;
import com.jawaly.sms.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsListFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();
	private Cursor cursor;
	private int totalContacts;
	public int countSelected = 0;
	
	//running select all contact task
	AsyncTask task;
	
	//ui fields
	private TextView selectedOfTotalTV ;
	private ListView contactsList;
	private Button searchBtn;
	private EditText searchEditText;
	
	public boolean selectAll=false;
	ImageView showSelectedImage;
	MainActivity context;
	
	public DatabaseHelper db;
	ProgressDialog progressDialog ;
	SendSMSFragment sendSMSFragment;
	
	OnClickListener showSelectedListener;
	OnClickListener showAllListener ;
	  
	public  ContactsListFragment() {
		this.context = (MainActivity) getActivity();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (MainActivity) activity;
		
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	
	public void refreshListview(){
		((SimpleCursorAdapter)contactsList.getAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		((MainActivity)getActivity()).actionBarshowBack();
		
		View rootView = inflater.inflate(R.layout.activity_contactslist, null);
		
	    sendSMSFragment= (SendSMSFragment)((MainActivity) context).getSupportFragmentManager().findFragmentByTag(SendSMSFragment.fragmentTag);

		
		// query contacts 
		cursor = getContacts();
		totalContacts = cursor.getCount();
		
		selectedOfTotalTV = (TextView) rootView.findViewById(R.id.selectOfTotalText);
		selectedOfTotalTV.setText("0/"+totalContacts);
		db = new DatabaseHelper(context);
		
		// set list ui
		contactsList = (ListView) rootView.findViewById(R.id.contactsListView);
		// set empty view 
		TextView emptyView = new TextView(context);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText(R.string.no_contacts);
		emptyView.setTextSize(20);
		emptyView.setVisibility(View.GONE);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		((ViewGroup)contactsList.getParent()).addView(emptyView);
		contactsList.setEmptyView(emptyView);
		
//		Button back = (Button) rootView.findViewById(R.id.button1);
//		back.setOnTouchListener(context);
//		back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				 context.onBackPressed();
//			}
//		});
//		
		
	    // the desired columns to be bound
		final String[] columns = new String[] {ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID};
		// the XML defined views which the data will be bound to
		final int[] to = new int[] { R.id.phoneBookStatsTextView};
        // create the adapter using the cursor pointing to the desired data as well as the layout information
		final ContactsListViewAdapter listAdapter = new ContactsListViewAdapter(context,R.layout.list_item_contact , cursor, columns, to);
       // set this adapter as your ListActivity's adapter
		contactsList.setAdapter(listAdapter);	
		
		countSelected = db.getCountNumbers();
		setSelectedOfTotalTextView();
	
		// select all behavior 
		CheckBox selectAllCheckBox = (CheckBox) rootView.findViewById(R.id.checkBox1);
		
		selectAllCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(buttonView.isPressed()){
					if(isChecked){
						
						countSelected = cursor.getCount();
						selectAll = true;
						setSelectedOfTotalTextView();
						contactsList.setAdapter(listAdapter);
						
						task = new AsyncTask<Object, Object, Object>(){
							Dialog dialog;
							
							@Override
							protected void onPreExecute() {
								super.onPreExecute();
								dialog = MyProgressDialog.show(getActivity(), null, null, false, false);
							}
							@Override
							protected void onPostExecute(Object result) {
								super.onPostExecute(result);
								dialog.dismiss();
							}
							@Override
							protected Object doInBackground(Object... params) {
								db.selectAllContactList();
								return null;
							}
						}.execute();
						
					}else{
						selectAll = false;
						db.emptyTemp();
						countSelected = 0;
						setSelectedOfTotalTextView();
						contactsList.setAdapter(listAdapter);
					}
					
				}
				
			}
		});
		////////////////////////////////////////////////////// end select all
		
		// selectedOfTotalTV 
		// when click on selectedOfTotalTV the items in list changed to display only selected items 
		//countSelected
		showSelectedImage = (ImageView) rootView.findViewById(R.id.showSelected);
		showSelectedImage.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN){
					v.setBackgroundResource(R.drawable.stroked_shape_hover);
				}else{
					v.setBackgroundResource(R.drawable.stroked_shape);
				}
			
				return false;
			}
		});
		showSelectedListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contactsList.setAdapter(new ContactsListViewAdapter(context,R.layout.list_item_contact , db.getSelectedNumbers(), columns, to));
				showSelectedImage.setImageResource(R.drawable.up);
				showSelectedImage.setOnClickListener(showAllListener);
				context.setTitle(getString(R.string.selected_contacts));
			}
		};
		  showAllListener = new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				contactsList.setAdapter(new ContactsListViewAdapter(context,R.layout.list_item_contact , cursor, columns, to));
				showSelectedImage.setImageResource(R.drawable.down);
				showSelectedImage.setOnClickListener(showSelectedListener);
				context.setTitle(getString(R.string.phone_book));
				
			}
		};
		 

		showSelectedImage.setOnClickListener(showSelectedListener);

		//searchBtn = (Button) rootView.findViewById(R.id.searchBtn);
		searchEditText = (EditText) rootView.findViewById(R.id.searchEdit);
		searchEditText.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				   String name = searchEditText.getText().toString();
				  
				    cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				    		new String[] {ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME},
						   "  `"+ContactsContract.Contacts.DISPLAY_NAME + "` LIKE '%"+name+"%' AND "+ PhoneLookup.HAS_PHONE_NUMBER + "= 1",
						   null,
						   ContactsContract.Contacts.DISPLAY_NAME);
				  contactsList.setAdapter(new ContactsListViewAdapter(context,R.layout.list_item_contact , cursor, columns, to));	
				    
				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				

			});
		
		
		return rootView;
	}


	private Cursor getContacts(){
		
		return  context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				new String[] {ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME},
				PhoneLookup.HAS_PHONE_NUMBER + "= 1", null, ContactsContract.Contacts.DISPLAY_NAME);
	}
	
	
	public void setSelectedOfTotalTextView(){
		selectedOfTotalTV.setText(countSelected+ "/"+totalContacts);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		context.onBackPressed();
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().setTitle(getString(R.string.send_sms));
		context.setDrawerIndicatorEnabled(true);
	//	((MainActivity)getActivity()).setupActionBarHomeButton(true, true);
		//((MainActivity)getActivity()).getSupportActionBar().show();
		((MainActivity)getActivity()).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	
		sendSMSFragment.updateSelectedContacts();
		((MainActivity)getActivity()).actionBarshowHome();
	}



	


	
	
}
