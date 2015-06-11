package com.jawaly.sms;

import net.jawaly.sms.api.R;



import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends Activity {
	/**
	 * this constant used for debug by Logging
	 */
	public static final String TAG = "SplashScreen";
	private Handler splashHandler;
	private Runnable splashThread;
	private long splash_time = 2000;



	private ImageView image_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splash);
		image_view = (ImageView) findViewById(R.id.imageView2);

		Animation logoMoveAnimation = AnimationUtils.loadAnimation(this,
				R.anim.my_logo_anim);
		image_view.startAnimation(logoMoveAnimation);

	
		runSplash();
		
	}

	/**
	 * this function s
	 */
	public void runSplash() {
		Log.d("run splash", "splash --> login");
		splashHandler = new Handler();
		splashThread = new Runnable() {
			public void run() {

				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));

			}
		};
		splashHandler.postDelayed(splashThread, splash_time);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void onBackPressed() {
	}

	


}
