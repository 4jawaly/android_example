package com.jawaly.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class ManageDefaultNumber {
	
	private SharedPreferences sharedPref;
	
	public ManageDefaultNumber(Activity act){
		sharedPref = act.getSharedPreferences("default_numbers", act.MODE_PRIVATE);
	}
	
	public boolean setDefaultNumber(Boolean select_all){
		return sharedPref.edit().putBoolean("default_n", select_all).commit();
	}
	
	public boolean is_DefaultNumber_isexist(){
		boolean value = sharedPref.getBoolean("default_n", false);
		return value;
	}
	
	public boolean get_DefaultNumber(){
		boolean value = sharedPref.getBoolean("default_n", false);
		return value;
	}
	
}
