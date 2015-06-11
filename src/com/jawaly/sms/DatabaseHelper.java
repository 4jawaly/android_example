package com.jawaly.sms;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	SQLiteDatabase db;

	public static final String TAG = "DatabaseHelper";
	public static final String DATABSE_NAME = "jawaly";

	static final String TEMPERORARY_CONTACTS_TABLE = "tempContacts";
	static final String CONTACT_ID = ContactsContract.Contacts._ID;
	static final String PHONELOOKUP_ID = "phoneLookupId";
	static final String PHONE_NUMBER = "phoneNumber";
	static final String NUMBER_TYPE = "numberType";
	static final String DISBLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

	// user contacts and groups
	static final String USER_CONTACTS_GROUPS_TABLE = "user_contacts";
	static final String USER_CONTACTS_GROUP_ID = "group_id";
	static final String USER_CONTACTS_GROUP_NAME = "group_name";

	// sender names
	static final String SENDER_NAMES_TABLE = "sender_names";
	static final String SENDER_NAME_ID = "sender_id";
	static final String SENDER_NAME = "sender_name";
	static final String SENDER_STATE = "state";
	static final String SENDER_ACTIVE_STATE = "active_state";
	static final String SENDER_IS_DEFAULT = "is_default";

	Context context;

	public DatabaseHelper(Context context) {
		super(context, DATABSE_NAME, null, 1);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + TEMPERORARY_CONTACTS_TABLE + "("
				+ CONTACT_ID + " VARCHAR(11)," + DISBLAY_NAME
				+ " VARCHAR(100) , " + PHONELOOKUP_ID + " VARCHAR(10),"
				+ PHONE_NUMBER + " VARCHAR(13)," + NUMBER_TYPE + " int(2))");

		db.execSQL("CREATE TABLE " + USER_CONTACTS_GROUPS_TABLE + "("
				+ USER_CONTACTS_GROUP_ID + " int(11),"
				+ USER_CONTACTS_GROUP_NAME + " VARCHAR(100) )");

		db.execSQL("CREATE TABLE " + SENDER_NAMES_TABLE + "(" + SENDER_NAME_ID
				+ " int(11)," + SENDER_NAME + " VARCHAR(15)," + SENDER_STATE
				+ " VARCHAR(5)," + SENDER_ACTIVE_STATE + " VARCHAR(5),"
				+ SENDER_IS_DEFAULT + " int(1) )");

		// db.close();

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void add_contact(String[] values) {
		db = getWritableDatabase();
		// db.execSQL("INSERT INTO `" + TEMPERORARY_CONTACTS_TABLE +
		// "` VALUES('"
		// + values[0] + "','" + values[1] + "','" + values[2] + "','"
		// + values[3] + "','" + values[4] + "')");
		Log.d("phone number", values[3]);
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_ID, values[0]);
		cv.put(DISBLAY_NAME, values[1]);
		cv.put(PHONELOOKUP_ID, values[2]);
		cv.put(PHONE_NUMBER, values[3]);
		cv.put(NUMBER_TYPE, values[4]);

		Log.d("DB insert", "" + db.insert(TEMPERORARY_CONTACTS_TABLE, null, cv));
	}

	public boolean isSelected(String contactId) {
		boolean isSelected = false;
		db = getReadableDatabase();
		String query = "SELECT " + CONTACT_ID + " FROM  `"
				+ TEMPERORARY_CONTACTS_TABLE + "` WHERE " + CONTACT_ID + " = '"
				+ contactId + "' LIMIT 1";
		Cursor rs = db.rawQuery(query, null);
		if (rs.getCount() > 0) {
			isSelected = true;
		}
		rs.close();
		return isSelected;
	}

	public boolean isSelected_ByPhoneNumber(String phoneNumber) {
		boolean isSelected = false;
		db = getReadableDatabase();
		String query = "SELECT " + CONTACT_ID + " FROM  `"
				+ TEMPERORARY_CONTACTS_TABLE + "` WHERE " + PHONE_NUMBER
				+ " = '" + phoneNumber + "' LIMIT 1";
		Cursor rs = db.rawQuery(query, null);
		if (rs.getCount() > 0) {
			isSelected = true;
		}
		rs.close();
		return isSelected;
	}

	public int getCountNumbers() {
		String query = "SELECT count(*) AS totalSelected FROM  `"
				+ TEMPERORARY_CONTACTS_TABLE + "` ";
		db = getReadableDatabase();
		Cursor rs = db.rawQuery(query, null);
		Integer count = 0;
		if (rs.moveToFirst()) {
			count = rs.getInt(0);
		}

		rs.close();
		return count;
	}

	public void emptyTemp() {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM  `" + TEMPERORARY_CONTACTS_TABLE + "`");
			db.execSQL("DELETE FROM `" + USER_CONTACTS_GROUPS_TABLE + "`");
			db.close();
		} catch (Exception e) {

		}

	}

	public void deleteContact(String contact_id) {
		db = getWritableDatabase();
		db.execSQL("DELETE FROM  `" + TEMPERORARY_CONTACTS_TABLE + "` WHERE "
				+ CONTACT_ID + "=" + contact_id);
	}

	public void deleteContact_byHponeNumber(String phoneNumber) {
		db = getWritableDatabase();
		// db.execSQL("DELETE FROM  `" + TEMPERORARY_CONTACTS_TABLE + "` WHERE "
		// + PHONE_NUMBER + "=" + phoneNumber);
		Log.d("phone number", phoneNumber);
		Log.d("delete row effected",
				""
						+ db.delete(TEMPERORARY_CONTACTS_TABLE, PHONE_NUMBER
								+ "='" + phoneNumber + "'", null));
	}

	public void deleteContactNumber(String contact_id) {
		db = getWritableDatabase();
		db.execSQL("DELETE FROM  `" + TEMPERORARY_CONTACTS_TABLE + "` WHERE "
				+ PHONELOOKUP_ID + "=" + contact_id);
	}

	public ArrayList<String> getContactsIds() {
		ArrayList<String> contactsIds = new ArrayList<String>();
		try {
			SQLiteDatabase db = this.getReadableDatabase();

			// db.openOrCreateDatabase(file, factory)
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery("SELECT " + CONTACT_ID + " FROM "
						+ TEMPERORARY_CONTACTS_TABLE, null);
				while (cursor.moveToNext()) {
					contactsIds.add(cursor.getString(0));

				}
			}
			db.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return contactsIds;
	}

	public ArrayList<String> getNumbersIds(String contact_id) {
		ArrayList<String> contactsIds = new ArrayList<String>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT " + PHONELOOKUP_ID + " FROM "
					+ TEMPERORARY_CONTACTS_TABLE + " WHERE " + CONTACT_ID
					+ "='" + contact_id + "'", null);
			// Log.d("query", "SELECT "+PHONELOOKUP_ID + " FROM "+
			// TEMPERORARY_CONTACTS_TABLE +" WHERE "+CONTACT_ID
			// +"='"+contact_id+"'");
			cursor.moveToFirst();
			while (cursor.isAfterLast() == false) {
				contactsIds.add(cursor.getString(0));
				cursor.moveToNext();

			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactsIds;
	}

	public String getNumbers() {
		StringBuilder numbers = new StringBuilder();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT " + PHONE_NUMBER + " FROM "
					+ TEMPERORARY_CONTACTS_TABLE, null);
			while (cursor.moveToNext()) {
				numbers.append(cursor.getString(0) + ",");

			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numbers.toString();
	}

	public Cursor getSelectedNumbers() {
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT " + CONTACT_ID + " , "
					+ DISBLAY_NAME + "," + PHONE_NUMBER + " FROM "
					+ TEMPERORARY_CONTACTS_TABLE, null);
			// db.close();
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (db != null && db.isOpen()) {
			db.close();
		}

	}

	public void selectAllContactList() {

		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
						ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone._ID }, null,
				null, ContactsContract.CommonDataKinds.Phone.TYPE + " DESC");

		if (cursor.moveToFirst()) {
			do {
				Log.d("contact_id",
						cursor.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
				Log.d("dispaly name", cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME)));

				try {

					add_contact(new String[] {
							cursor.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
							cursor.getString(cursor
									.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
							cursor.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
							cursor.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
							cursor.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) });

				} catch (Exception e) {

				}
			} while (cursor.moveToNext());
		}

	}

	public boolean isSelectedGroup(int groupId) {
		db = getReadableDatabase();
		boolean isSelected = false;
		String query = "SELECT " + USER_CONTACTS_GROUP_ID + " FROM  `"
				+ USER_CONTACTS_GROUPS_TABLE + "` WHERE "
				+ USER_CONTACTS_GROUP_ID + " = '" + groupId + "' LIMIT 1";
		Cursor rs = db.rawQuery(query, null);
		if (rs.getCount() > 0) {
			isSelected = true;
		}
		rs.close();
		return isSelected;
	}

	public void removeGroup(int groupId) {
		db = getWritableDatabase();
		db.execSQL("DELETE FROM " + USER_CONTACTS_GROUPS_TABLE + " WHERE "
				+ USER_CONTACTS_GROUP_ID + " ='" + groupId + "' ");
		db.close();
	}

	public void removeBanks() {
		db = getWritableDatabase();
		db.close();
	}

	public String getGroupsIds() {
		StringBuilder groupsBuilder = new StringBuilder();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT " + USER_CONTACTS_GROUP_ID
					+ " FROM " + USER_CONTACTS_GROUPS_TABLE, null);
			while (cursor.moveToNext()) {
				groupsBuilder.append(cursor.getString(0) + ",");

			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String groups = groupsBuilder.toString();
		if (!TextUtils.isEmpty(groups)) {
			groups = groups.substring(0, groups.length() - 1);
		}
		return groups;

	}

	public String getGroupsNames() {
		StringBuilder groupsBuilder = new StringBuilder();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT " + USER_CONTACTS_GROUP_NAME
					+ " FROM " + USER_CONTACTS_GROUPS_TABLE, null);
			while (cursor.moveToNext()) {
				groupsBuilder.append(cursor.getString(0) + ",");

			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String groups = groupsBuilder.toString();
		if (!TextUtils.isEmpty(groups)) {
			groups = groups.substring(0, groups.length() - 1);
		}
		return groups;

	}
}
