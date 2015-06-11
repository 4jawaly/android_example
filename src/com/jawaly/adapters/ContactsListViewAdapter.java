package com.jawaly.adapters;

import com.jawaly.fragments.ContactsListFragment;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.MainActivity;
import net.jawaly.sms.api.R;
import com.jawaly.utils.ManageDefaultNumber;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ContactsListViewAdapter extends SimpleCursorAdapter {
	private final String TAG = this.getClass().getSimpleName();
	private MainActivity context;
	private int layout;
	private contactItemHolder viewHolder;
	private Cursor cursor;
	private String[] from;
	DatabaseHelper db;
	ContactsListFragment contactsListFragment;

	public ContactsListViewAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = (MainActivity) context;
		this.layout = layout;
		this.cursor = c;
		this.from = from;
		db = new DatabaseHelper(context);
		contactsListFragment = (ContactsListFragment) ((MainActivity) context)
				.getSupportFragmentManager().findFragmentByTag("contactList");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int currPositon = position;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);

			viewHolder = new contactItemHolder();
			viewHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.phoneBookStatsTextView);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (contactItemHolder) convertView.getTag();
		}

		cursor.moveToPosition(position);
		viewHolder.textView.setText(cursor.getString(cursor
				.getColumnIndex(PhoneLookup.DISPLAY_NAME)));
		
		try{
			viewHolder.mTextView.setText(cursor.getString(2));
		}catch(Exception e){
			viewHolder.mTextView.setText("");
			viewHolder.mTextView.setVisibility(View.GONE);
		}

		viewHolder.textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cursor.moveToPosition(currPositon);
				showDialogForNumbers(getPhoneNumber());
			}
		});
		
		viewHolder.checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(
							final CompoundButton buttonView, boolean isChecked) {
						cursor.moveToPosition(currPositon);
						if (buttonView.isPressed()) {
							try {
								if (isChecked) {
									
									// get phone number

									final Cursor phonesCursor = getPhoneNumber();
									
									if (phonesCursor.getCount() > 1) {
										
										buttonView.setChecked(true);
										
										if(!new ManageDefaultNumber(context).get_DefaultNumber()){
											Log.d("check super primary", "");
											if(isThereSuperPrimary(phonesCursor)){
					
												Cursor all_c = getPhoneNumber();
												if(all_c.moveToFirst()){
													do{
														if(all_c.getString(all_c
																.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)).equals("1")){
															db.add_contact(new String[] {
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
																		cursor.getString(cursor
																				.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });
														
															Log.d("check super primary", " : " + all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
															Log.d("check super primary", " : " + all_c.getString(all_c
																.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)));
														
															contactsListFragment.countSelected++;
															contactsListFragment
																.setSelectedOfTotalTextView();
														}else{
															
														}
													}while(all_c.moveToNext());
												}
												
											}else {
												Cursor all_c = getPhoneNumber();
												if(all_c.moveToFirst()){
													do{
														db.add_contact(new String[] {
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
																		cursor.getString(cursor
																				.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
																all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });
														
														Log.d("check super primary", " : " + all_c.getString(all_c
																		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
														Log.d("check super primary", " : " + all_c.getString(all_c
																.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)));
														
														contactsListFragment.countSelected++;
														contactsListFragment
																.setSelectedOfTotalTextView();
													}while(all_c.moveToNext());
												}
											}
										}else {
											Cursor all_c = getPhoneNumber();
											if(all_c.moveToFirst()){
												do{
													db.add_contact(new String[] {
															all_c.getString(all_c
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
																	cursor.getString(cursor
																			.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
															all_c.getString(all_c
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
															all_c.getString(all_c
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
															all_c.getString(all_c
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });
													
													Log.d("check super primary", " : " + all_c.getString(all_c
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
													Log.d("check super primary", " : " + all_c.getString(all_c
															.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)));
													
													contactsListFragment.countSelected++;
													contactsListFragment
															.setSelectedOfTotalTextView();
												}while(all_c.moveToNext());
											}
										}
										
										//dialog part
//										showDialogForNumbers(phonesCursor, buttonView);
										///////////////////////////////////// dialog end
									} else {
										phonesCursor.moveToFirst();
										
										db.add_contact(new String[] {
												phonesCursor.getString(phonesCursor
														.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
														cursor.getString(cursor
																.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
												phonesCursor.getString(phonesCursor
														.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
												phonesCursor.getString(phonesCursor
														.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
												phonesCursor.getString(phonesCursor
														.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });

										phonesCursor.close();

										contactsListFragment.countSelected++;
										contactsListFragment
												.setSelectedOfTotalTextView();

									}

								} else {
									final Cursor phonesCursor = getPhoneNumber();
									
									if (phonesCursor.getCount() > 1) {
										if(!new ManageDefaultNumber(context).get_DefaultNumber()){
											if(isThereSuperPrimary(phonesCursor)){
												db.deleteContact(cursor.getString(cursor
													.getColumnIndex(ContactsContract.Contacts._ID)));
													contactsListFragment.countSelected--;
													contactsListFragment.setSelectedOfTotalTextView();
											}else{
												phonesCursor.moveToFirst();
												do{
													if(db.isSelected_ByPhoneNumber(phonesCursor.getString(phonesCursor
														.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))))
													{
														contactsListFragment.countSelected--;
														contactsListFragment
															.setSelectedOfTotalTextView();
													}
												}while(phonesCursor.moveToNext());
												db.deleteContact(cursor.getString(cursor
													.getColumnIndex(ContactsContract.Contacts._ID)));
											}
										}else {
											phonesCursor.moveToFirst();
											do{
												if(db.isSelected_ByPhoneNumber(phonesCursor.getString(phonesCursor
													.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))))
												{
													contactsListFragment.countSelected--;
													contactsListFragment
														.setSelectedOfTotalTextView();
												}
											}while(phonesCursor.moveToNext());
											db.deleteContact(cursor.getString(cursor
												.getColumnIndex(ContactsContract.Contacts._ID)));
										}
									}else{
										db.deleteContact(cursor.getString(cursor
											.getColumnIndex(ContactsContract.Contacts._ID)));
										contactsListFragment.countSelected--;
										contactsListFragment.setSelectedOfTotalTextView();
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}

					}
				});

		if (contactsListFragment.selectAll) {
			viewHolder.checkbox.setChecked(true);
		} else if (db.isSelected(cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts._ID)))) {
			viewHolder.checkbox.setChecked(true);
		} else {
			viewHolder.checkbox.setChecked(false);
		}

		return convertView;
	}

	/*
	 * is there super primary
	 */
	private boolean isThereSuperPrimary(Cursor all_c){
		boolean is_there = false;
		
		if(all_c.moveToFirst()){
			do{		
				if(all_c.getString(all_c
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)).equals("1")){
					is_there = true;
				}
				
				Log.d("check super primary", " : " + all_c.getString(all_c
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
				Log.d("check super primary", " : " + all_c.getString(all_c
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)) + " : " + is_there);
				
			}while(all_c.moveToNext());
		}
		
		return is_there;
	}
	
	private Cursor getPhoneNumber(){

		return context
		.getContentResolver()
		.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {
						ContactsContract.CommonDataKinds.Phone.NUMBER,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
						ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone._ID,
						ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY},
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID
						+ " = "
						+ cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts._ID)),
				null,
				ContactsContract.CommonDataKinds.Phone.TYPE
						+ " DESC");
		
	}
	
	private void showDialogForNumbers(final Cursor phonesCursor){
		final Dialog selectNumber = new Dialog(
				context);
		
		selectNumber.setTitle(cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)));
		
		selectNumber.setContentView(R.layout.dialog_select_phone_contact);
		
		final ListView list = (ListView) selectNumber.findViewById(R.id.listView1);
		list.setAdapter(new PhonesListViewAdapter(context,
				R.layout.list_item_phone,
				cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
				phonesCursor, new String[] {},
				new int[] {}));
		
		Button cancelBtn = (Button) selectNumber.findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						selectNumber.dismiss();
					}
				});
//		list.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(
//					AdapterView<?> arg0,
//					View arg1, int position,
//					long arg3) {
//				
//				phonesCursor.moveToPosition(position);
//				
//				Log.d("selected item", "" + phonesCursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				
//				// add to database
//				db.add_contact(new String[] {
//						phonesCursor
//								.getString(phonesCursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
//						cursor.getString(cursor
//								.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
//						phonesCursor
//								.getString(phonesCursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
//						phonesCursor
//								.getString(phonesCursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
//						phonesCursor
//								.getString(phonesCursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });
//				
////				selectNumber.dismiss();
////				buttonView.setChecked(true);
//				
////				phonesCursor.close();
//				/////////////////////////////////////////////// end add to database
//
//				contactsListFragment.countSelected++;
//				contactsListFragment.setSelectedOfTotalTextView();
//
//			}
//		});
		selectNumber.show();
	}
}

class contactItemHolder {
	TextView textView;
	CheckBox checkbox;
	TextView mTextView ;
}
