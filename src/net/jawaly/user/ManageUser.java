package net.jawaly.user;

import com.jawaly.sms.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageUser {
	
	private Context context;
	private SharedPreferences user_sp;
	private User user;
	
	public ManageUser(Context context){
		this.context = context;
		user_sp = context.getSharedPreferences(MainActivity.PROPERTY_APP_PREF,
				Context.MODE_PRIVATE);
	}
	
	public void updateUser(User user){
		user_sp.edit().putInt(MainActivity.PROPERTY_CREDIT, user.getCredit())
		.putInt(MainActivity.PROPERTY_USER_GROUP_ID, user.getGroup_id())
		.commit();
	}
	
	public User getUser(){
		user = new User();
		
		user.setUser_id(user_sp.getInt(MainActivity.PROPERTY_USER_ID, -1));
		user.setUserName(user_sp.getString(MainActivity.PROPERTY_USER_NAME, "x"));
		user.setPassWord(user_sp.getString(MainActivity.PROPERTY_PASSWORD, "x"));
		user.setFullName(user_sp.getString(MainActivity.PROPERTY_FULL_NAME, "x"));
		user.setCredit(user_sp.getInt(MainActivity.PROPERTY_CREDIT, -1));
		user.setEmail(user_sp.getString(MainActivity.PROPERTY_USER_EMAIL, "x"));
		user.setEmail_verification(user_sp.getBoolean(MainActivity.PROPERTY_USER_EMAIL_state, false));
		user.setMobile_number(user_sp.getString(MainActivity.PROPERTY_USER_MOBILE_NUMBER, "x"));
		user.setMobile_active_state(user_sp.getBoolean(MainActivity.PROPERTY_USER_MOBILE_NUMBER_STATE, false));
		user.setGroup_id(user_sp.getInt(MainActivity.PROPERTY_USER_GROUP_ID, -1));
		user.setUser_open_sender_name(user_sp.getInt(MainActivity.PROPERTY_HAVE_OPEN_SENDER, -1));
		
		return user;
	}
	
	public void updatePassword(String password){
		user_sp.edit().putString(MainActivity.PROPERTY_PASSWORD, password).commit();
	}
	
}
