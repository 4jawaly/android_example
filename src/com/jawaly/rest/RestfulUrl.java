package com.jawaly.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class RestfulUrl {
	public static final String SPECIAL_API = "apiSjawaly/";

	public static final String BASE_URL = "http://www.4jawaly.net/apiSjawaly/";

	public static String serviceLocation = "http://www.4jawaly.net/";

	public static void setServiceLocation(String serviceLocation) {
		RestfulUrl.serviceLocation = serviceLocation;
	}

	

	

	public static String getUserUrl(String userName, String password,String api) {
		if (userName == null || password == null) {
			return "";
		}
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append(api+"/getUser.php?return=json&");
		restUrl.append("password=");
		try {
			restUrl.append(URLEncoder.encode(password, "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	public static String getSendSmsUrl() {
		return serviceLocation + SPECIAL_API + "/ApiSendGroup.php";
	}


	public static String getCalcPoints() {
		return serviceLocation + "calcPointsPrice.php";
	}

	public static String getRemotelyAddSenderName() {
		return serviceLocation + "/add-sender.php";
	}


	public static String senderListNew(String userName, String password) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append(SPECIAL_API);
		restUrl.append("/GetAllSenders.php?return=json&");
		
		restUrl.append("password=");
		try {
			restUrl.append(URLEncoder.encode(password,  "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	public static String senderListActive(String userName, String password) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append(SPECIAL_API);

		restUrl.append("/GetAllSenders.php?return=json&");
		restUrl.append("SnderType=Yes&");

		restUrl.append("password=");
		try {
			restUrl.append(URLEncoder.encode(password, "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	
	public static String senderMobileConfirm() {
		return serviceLocation + "/ActiveSendername.php";
	}

	public static String getUserContactsGroupsUrl(String userName,
			String password) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append("/getmaingroups.php?return=json&password=");
		try {
			restUrl.append(URLEncoder.encode(password, "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	public static String senderAdd() {
		return serviceLocation +SPECIAL_API+ "/addSender.php";
	}

	public static String getUserSubGroupsUrl(String userName, String password,
			int mainGroupId) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append(SPECIAL_API);
		restUrl.append("/getsubgroups.php?return=json&password=");
		try {
			restUrl.append(URLEncoder.encode(password, "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
			restUrl.append("&gid=" + mainGroupId);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	public static String sendGetRequest(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		StringBuffer sb = null;
		try {
			request.setURI(new URI(url));
			// Log.d("restUrl",mRestUrl);
			HttpResponse response = client.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			sb = new StringBuffer("");
			String line = "";
			// String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			// Log.d("rest respone", sb.toString());
			return sb.toString();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			// Log.e("RestRequest", "URISyntaxException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String bankListGet(String userName, String password) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append("/bank_accounts_get.php?return=json&password=");
		try {
			restUrl.append(URLEncoder.encode(password, "UTF-8"));
			restUrl.append("&username=");
			restUrl.append(URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return restUrl.toString();
	}

	public static String requestRechargePost() {
		return serviceLocation + "/askforcharge.php";
	}


}
