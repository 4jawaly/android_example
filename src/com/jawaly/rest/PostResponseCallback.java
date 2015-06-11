package com.jawaly.rest;

public abstract class PostResponseCallback {

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     * 
     * @param profile The {@code Profile} that was received from the server.
     */
   public abstract void onDataPosted(String profile);
   
   public abstract void onExceptionThrown();

    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     * 
     */
   
   public void updateProgress(Integer...param){
	   
   }
}
