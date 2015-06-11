package com.jawaly.connection;

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

import com.jawaly.rest.RestfulUrl;

public class RestfulUrls {
	//FixMe
	public static  String serviceLocation = RestfulUrl.serviceLocation;

	
	

	
	public static String getCountriesUrl() {
		return serviceLocation + "getCountries.php?return=json";
	}

	public static String getCities(String countryCode) {
		return serviceLocation + "getCities.php?return=json&code="
				+ countryCode;
	}



	public static String getUserUrl(String userName, String password) {
		if(userName==null || password==null){
			return "";
		}
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append("/getUser.php?return=json&");
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
		return serviceLocation + "/ApiSendGroup.php";
	}




	
	public static String getSetDefaultSenderUrl(){
		return serviceLocation + "/setDefultSender.php";
	}

	public static String senderListNew(String userName, String password,
			String senderType) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
		restUrl.append("/GetAllSenders.php?return=json&");
		if (senderType != null) {
			restUrl.append("SnderType=new&");
		}
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

	public static String senderListActive(String userName, String password) {
		StringBuffer restUrl = new StringBuffer(serviceLocation);
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


	public static String senderAdd() {
		return serviceLocation + "addSender.php";
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

	
}
