package com.jawaly.adapters;



import com.jawaly.fragments.ContactsListFragment;
import com.jawaly.sms.DatabaseHelper;
import com.jawaly.sms.MainActivity;
import net.jawaly.sms.api.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PhonesListViewAdapter extends SimpleCursorAdapter {
	private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private int layout;
    private contactItemHolder viewHolder;
    private final Cursor cursor;
    private String[] from;
    private SharedPreferences temp;
    DatabaseHelper db;
    private String name;
    ContactsListFragment contactsListFragment;

	public PhonesListViewAdapter(Context context, int layout, String name,Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
		this.layout = layout;
		this.cursor = c;
		this.from = from;
		this.name = name;
		contactsListFragment = (ContactsListFragment) ((MainActivity) context)
				.getSupportFragmentManager().findFragmentByTag("contactList");
		
		db = new DatabaseHelper(context);
	}
	
	@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
		final ContactNumberHolder viewHolder;
		if(convertView==null){
			viewHolder = new ContactNumberHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout,null);
			viewHolder.numberTextView = (TextView) convertView.findViewById(R.id.phoneNumberTextView);
			viewHolder.numberTypeTextView = (TextView) convertView.findViewById(R.id.phoneTypeTextView);
			viewHolder.chBox = (CheckBox)convertView.findViewById(R.id.selcet_number_chbx);
			convertView.setTag(viewHolder);	
		}else{
			 viewHolder = (ContactNumberHolder) convertView.getTag(); 
		}
		
		cursor.moveToPosition(position);
		viewHolder.numberTextView.setText(cursor.getString(0));
		
		viewHolder.numberTypeTextView.setText(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(cursor.getInt(2)));
		
		if (db.isSelected_ByPhoneNumber(cursor.getString(cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))) {
			viewHolder.chBox.setChecked(true);
			Log.d("is selected", "true");
			
		} else {
			viewHolder.chBox.setChecked(false);
			Log.d("is selected", "false");
		}
		
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor.moveToPosition(position);
				if(viewHolder.chBox.isChecked()){
					db.deleteContact_byHponeNumber( "" + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
					viewHolder.chBox.setChecked(false);
					contactsListFragment.countSelected--;
					contactsListFragment
							.setSelectedOfTotalTextView();
					contactsListFragment.refreshListview();
				}else{
					// add to database
					db.add_contact(new String[] {
							cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
							name,
									cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
											cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
											cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });
					
//					selectNumber.dismiss();
//					buttonView.setChecked(true);
					
//					phonesCursor.close();
					/////////////////////////////////////////////// end add to database
					viewHolder.chBox.setChecked(true);
					contactsListFragment.countSelected++;
					contactsListFragment
							.setSelectedOfTotalTextView();
					contactsListFragment.refreshListview();
				}
			}
		});
		
		
		return convertView;
		}

}
class ContactNumberHolder{
	public TextView numberTextView;
	TextView numberTypeTextView;
	CheckBox chBox;
	
}
