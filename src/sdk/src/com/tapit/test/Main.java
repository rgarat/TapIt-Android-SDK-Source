package com.tapit.test;

import java.util.Hashtable;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tapit.adview.AdFullscreenView;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdLog;
import com.tapit.adview.AdOfferWallView;
import com.tapit.adview.AdVideoUnitView;
import com.tapit.adview.AdView;
import com.tapit.adview.R;
import com.tapit.adview.notif.TapIt;
import com.tapit.adview.track.EventTracker;
import com.tapit.adview.track.InstallTracker;

/**
 * @hide
 * @author synergy
 * 
 */
public class Main extends Activity {

	private static final String FIRST_START_PREF = "first_start_pref";
	
	public static final int BANNER_HEIGHT = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		AdView adView = (AdView) findViewById(R.id.adViewer1);
		adView.setUpdateTime(10);
		adView.setDefaultImage(R.drawable.thumb_72x72_blue);

		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("cid", "1");
		adView.setCustomParameters(hashtable);
		adView.setLogLevel(AdLog.LOG_LEVEL_3);
		adView.update(true);
		
		//AdLog.setFileLog("/sdcard/AdService.txt");
		TapIt tapIt = new TapIt(this, "2", "01010", false);
		InstallTracker.getInstance().setLogLevel(AdLog.LOG_LEVEL_3);
		InstallTracker.getInstance().reportInstall(this);
		EventTracker.getInstance().setLogLevel(AdLog.LOG_LEVEL_3);
		EventTracker.getInstance().reportEvent(this, "crash fixing");

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.getBoolean(FIRST_START_PREF, true)) {
			tapIt.setIconResource(R.drawable.tapit_72x72_blue);
			tapIt.setUpdateInterval(60 * 60);
			Editor editor = preferences.edit();
			editor.putBoolean(FIRST_START_PREF, false);
			editor.commit();
		}
		
		// tapIt.setIconResource(0);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
//		case R.id.button1:
//			showInterstitial();
//			break;
//			
//		case R.id.button2:
//			showFullscreen();
//			
//			break;
//			
//		case R.id.button3:
//			showOfferWall();
//			break;
//
//		case R.id.button4:
//			showVideo();
//			break;
//
//		case R.id.buttonSendRequest:
//			sendRequest();
//			break;
//			
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AdView adView = (AdView) findViewById(R.id.adViewer1);
		if (adView != null)
			adView.destroy();
	}
	
	private void showInterstitial(){
//		AdInterstitialView interstitialView = new AdInterstitialView(this, "3");
//		interstitialView.setLogLevel(AdLog.LOG_TYPE_INFO);
//		interstitialView.setUpdateTime(5);
//		interstitialView.setShowCloseButtonTime(2);
//		interstitialView.setAutoCloseInterstitialTime(0);
//		
//		interstitialView.setIsShowPhoneStatusBar(false);
//		
//		Hashtable<String, String> hashtable = new Hashtable<String, String>();
//		hashtable.put("cid", "1");
//		interstitialView.setCustomParameters(hashtable);
//		
//		interstitialView.show();
	}
	
	private void showFullscreen(){
//		AdFullscreenView fullscreenView = new AdFullscreenView(this, "3");
//		fullscreenView.setLogLevel(AdLog.LOG_TYPE_INFO);
////		fullscreenView.setUpdateTime(5);
//		fullscreenView.setIsShowPhoneStatusBar(false);
//		fullscreenView.setAutoCloseTime(10);
//		
////		RelativeLayout.LayoutParams countdownTimerParams = new RelativeLayout.LayoutParams(
////				RelativeLayout.LayoutParams.WRAP_CONTENT,
////				RelativeLayout.LayoutParams.WRAP_CONTENT);
////		
////		countdownTimerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
////		countdownTimerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
////		int margin = (int) (12 * getResources().getDisplayMetrics().density);
////		countdownTimerParams.bottomMargin = margin;
////		countdownTimerParams.leftMargin = margin;
////		
////		fullscreenView.setCountdownTimerParams(countdownTimerParams);
//
//		Hashtable<String, String> hash = new Hashtable<String, String>();
//		hash.put("cid", "1");
//		fullscreenView.setCustomParameters(hash);
//		
//		fullscreenView.show();
	}
	
	private void showOfferWall(){
//		AdOfferWallView offerWallView = new AdOfferWallView(this, "3");
//		offerWallView.setIsShowPhoneStatusBar(false);
//		
////		Button closeButton = new Button(this);
////		closeButton.setId(1000);
////		closeButton.setText("Close2");
////		RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(
////				RelativeLayout.LayoutParams.WRAP_CONTENT,
////				RelativeLayout.LayoutParams.WRAP_CONTENT);
////		closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
////		closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
////		closeButton.setLayoutParams(closeLayoutParams);
////		offerWallView.setCloseButton(closeButton);
//		
//		offerWallView.show();
	}
	
	private void showVideo(){
//		AdVideoUnitView adVideoUnitView = new AdVideoUnitView(this, "3");
//		adVideoUnitView.setLogLevel(AdLog.LOG_TYPE_INFO);
////		adVideoUnitView.setUpdateTime(5);
//		adVideoUnitView.setIsShowPhoneStatusBar(false);
//		adVideoUnitView.setAutoCloseTime(45);
////		adVideoUnitView.setCloseButtonText("clise3");
////		adVideoUnitView.setGoToSiteButtonText("go");
//		adVideoUnitView.show();
	}
	
	private void sendRequest(){
		LinearLayout layout = (LinearLayout) findViewById(R.id.frameAdContent);
		layout.removeAllViews();
		
		String zone = ((EditText)findViewById(R.id.editTextZone)).getText().toString();
		String CID = ((EditText)findViewById(R.id.editTextCID)).getText().toString();
		
		AdView adView = new AdView(this, zone);
		
		adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				BANNER_HEIGHT));
		
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put("cid", CID);
		adView.setCustomParameters(h);
		
		layout.addView(adView);
	}
}
