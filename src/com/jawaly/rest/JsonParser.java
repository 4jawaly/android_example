package com.jawaly.rest;

import java.util.ArrayList;

import net.jawaly.sms.api.R;
import net.jawaly.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.jawaly.senders.SenderName;
import com.jawaly.sms.Utils;

public class JsonParser {
	
	Context context;
	
	public JsonParser(Context context){
		this.context = context;
	}
	
	public String getMessageParsing(String result) throws JSONException {
		
		JSONObject response = new JSONObject(result);
		int code = response.getInt("Code");
		
		switch (code) {
		case 101:
			Utils.showToastMsg( context, context.getString(R.string.error_all_fiels_required));
			break;
			
		case 102:
			Utils.showToastMsg( context, context.getString(R.string.error_username_incorect));
			break;
		case 103:
			Utils.showToastMsg( context, context.getString(R.string.error_password_incorrect));
			break;
		case 117:
				return result;

		default:
			break;
		}
		
		return "";
	}
	
	public User parseUser(String result) throws JSONException{
		User user = new User();
		
		JSONObject jo = new JSONObject(result);
		JSONArray ja = jo.getJSONArray("user");
		
		user.setGroup_id(ja.getInt(8)); 
		
		return user;
	}
	
	
	
	public ArrayList<String> getListOfFavoriteMasseges(String message) throws JSONException{
		ArrayList<String> list = new ArrayList<String>();
		
		JSONObject jo = new JSONObject(message);
		String msg = jo.getString("messages");
		JSONArray ja = new JSONArray(msg);
		for(int i=0; i<ja.length(); i++){
			JSONObject jo1 = ja.getJSONObject(i);
			JSONObject jo2 = jo1.getJSONObject("message_node");
			list.add(jo2.getString("msg"));
		}
		
		return list;
	}
	
	
	public ArrayList<SenderName> getListOfSenderNames(String result) throws JSONException{
		ArrayList<SenderName> list = new ArrayList<SenderName>();
		
		JSONObject jo = new JSONObject(result);
		JSONArray ja = jo.getJSONArray("SenderArchive");
		for(int i=0; i<ja.length(); i++){
			SenderName sn = new SenderName();
			
			JSONObject jo1 = ja.getJSONObject(i);
			sn.setSenderName(jo1.getString("SenderName"));
			sn.setSenderId(Integer.parseInt(jo1.getString("SenderID")));
			sn.setActiveState(jo1.getString("ActiveState"));
			sn.setDefault(jo1.getInt("ISDefult"));
			sn.setState(jo1.getString("State"));
			
			list.add(sn);
		}
		
		return list;
	}
}
