package com.jawaly.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.jawaly.customview.MyProgressDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class PostTask extends AsyncTask<List<? extends NameValuePair> , String, String> {
    private String mRestUrl;
    private RestTaskCallback mCallback;
    MyProgressDialog dialog = null;
    Context c;
    
    public PostTask(String mRestUrl,RestTaskCallback mCallback){
    	this.mRestUrl = mRestUrl;
    	this.mCallback = mCallback;
    }
    
    public PostTask(String mRestUrl,RestTaskCallback mCallback, MyProgressDialog dialog, Context c) {
		this(mRestUrl, mCallback);
		this.dialog = dialog;
		this.c = c;
	}
    
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	if(dialog != null){
//    		dialog.show(c, null, null, false, true);
    	}
    }
    
	@Override
	protected String doInBackground(List<? extends NameValuePair>... param) {
	  Log.d("PostTask",mRestUrl);
  	  DefaultHttpClient hc=new DefaultHttpClient();  
  	  HttpConnectionParams.setConnectionTimeout(hc.getParams(), 15000);
  	  HttpConnectionParams.setSoTimeout(hc.getParams(), 15000);
	  ResponseHandler <String> res=new BasicResponseHandler();  
	  HttpPost postMethod=new HttpPost(mRestUrl);
	  try {
		postMethod.setEntity(new UrlEncodedFormEntity(param[0],"UTF-8"));
		 String responses=hc.execute(postMethod,res);
		 return responses;
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	  
		return null;
	}
	
    @Override
    protected void onPostExecute(String result) {
    	
    	if(!isCancelled()){
    		if(result!=null){
    			try{
    				if(dialog != null){
    					dialog.dismiss();
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			super.onPostExecute(result);
    			mCallback.onTaskComplete(result);
    		}else{
    			mCallback.onTaskNotComplete();
    		}
    	}else{
    		Log.d("task", "is Canceled true");
    	}
    

    }


}
