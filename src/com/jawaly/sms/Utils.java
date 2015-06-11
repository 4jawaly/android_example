package com.jawaly.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import net.jawaly.sms.api.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {

	public static void showToastMsg(Context context, String msg) {
		Toast message = Toast.makeText(context, msg, 20000);
		message.setGravity(Gravity.CENTER, 0, 0);
		message.show();
	}

	public static String getPage(String url) {
		Log.d("requested page: ", url);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		StringBuffer sb = null;
		try {
			request.setURI(new URI(url));
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


	public static ProgressDialog setupProgress(Context context, String message) {
		ProgressDialog progress = new ProgressDialog(context);
		progress.setMessage(message);
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return progress;
	}



	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static SpannableStringBuilder setupErrorSpan(String message) {
		// setup seterror view
		int ecolor = Color.RED;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
		ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
		return ssbuilder;
	}

	// show success in top of screen
	public static void setupSuccessAppMsg(Activity activity, String msg) {
		TextView AppMsg = (TextView) activity.findViewById(R.id.appMsg);

		AppMsg.setVisibility(View.VISIBLE);
		AppMsg.setBackgroundColor(activity.getResources().getColor(
				R.color.green_color));
		// AppMsg.setText(activity.getResources().getString(
		// R.string.success_app_msg));
		AppMsg.setText(msg);
	}

	// show error in top of screen
	public static void setupErrorAppMsg(Activity activity, String msg) {
		TextView AppMsg = (TextView) activity.findViewById(R.id.appMsg);

		AppMsg.setVisibility(View.VISIBLE);
		AppMsg.setBackgroundColor(Color.RED);
		AppMsg.setText(msg);
	}

	// hide error from top of screen
	public static void removeAppMsg(Activity activity) {
		TextView AppMsg = (TextView) activity.findViewById(R.id.appMsg);

		if (AppMsg.getVisibility() == View.VISIBLE) {
			AppMsg.setVisibility(View.GONE);
		}
	}
}
