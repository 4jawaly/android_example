package com.jawaly.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.os.AsyncTask;
import android.util.Log;



/**
 * An AsyncTask implementation for performing GETs on the Hypothetical REST APIs.
 */
public class GetTask extends AsyncTask<String, String, String>{

    private String mRestUrl;
    private RestTaskCallback mCallback;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     * 
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     * 
     */
    public GetTask(String restUrl, RestTaskCallback callback){
    	//Log.d("GETTASK","initialize get task method");
        this.mRestUrl = restUrl;
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        //Use HTTP Client APIs to make the call.
        //Return the HTTP Response body here.
    	//Log.d("BACKGROUND","begin doing background thread");
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
    	HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
    	HttpGet request = new HttpGet();
    	StringBuffer sb = null;
    	try {
			request.setURI(new URI(mRestUrl));
			Log.d("restUrl",mRestUrl);
			HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            sb = new StringBuffer("");
            String line = "";
            //String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            //Log.d("rest respone", sb.toString());
           return sb.toString();
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			//Log.e("RestRequest", "URISyntaxException");
		} catch(Exception e){
			e.printStackTrace();
		}
    	return null;

    
    }

    @Override
    protected void onPostExecute(String result) {
    	if(isCancelled()){
        	result = null;
        }
    	
    	if(result!=null){
	        super.onPostExecute(result);
	        mCallback.onTaskComplete(result);
    	}else{
    		mCallback.onTaskNotComplete();
    	}
    	
    

    }


}

