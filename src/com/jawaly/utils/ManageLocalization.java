package com.jawaly.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class ManageLocalization {
	
	private SharedPreferences sharedPref; 
	
	public ManageLocalization(Activity act){
		sharedPref = act.getSharedPreferences("user", act.MODE_PRIVATE);
	}
	
	public boolean setLanguage(String userLanguage){
		return sharedPref.edit().putString("user_language", userLanguage).commit();
	}
	
	public boolean user_language_isexist(){
		String value = sharedPref.getString("user_language", "x");
		if(value.equals("x")){
			return false;
		}else{
			return true;
		}
	}
	
	public String get_userLanguage(){
		String value = sharedPref.getString("user_language", "x");
		return value;
	}
}
