package com.yourcompany;

import java.util.Hashtable;

import com.tapit.adview.AdFullscreenView;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdOfferWallView;
import com.tapit.adview.AdVideoUnitView;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;
import com.tapit.adview.AlertAd;
import com.tapit.adview.AlertAd.AlertAdCallbackListener;
import com.tapit.adview.track.InstallTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TapItTestActivity extends Activity implements OnAdDownload, OnInterstitialAdDownload {

	public final static String BANNER_ZONE_ID = "7979";
	public final static String VIDEO_ZONE_ID = "7981";
	public final static String MED_RECT_ZONE_ID = "7982";
	public final static String INTRS_ZONE_ID = "7983";
	public final static String ADPROMPT_ZONE_ID = "7984";
	
	private Button loadButton;
	private Button showButton;
	private Button showAlertAdButton;
	
	private AdInterstitialView interstitialAd;
//	private AdFullscreenView interstitialAd;
//	private AdOfferWallView interstitialAd;
//	private AdVideoUnitView interstitialAd;
	
	private AdView bannerAd;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InstallTracker tracker = InstallTracker.getInstance();
        tracker.reportInstall(this, "offer_txt");
        
        bannerAd = (AdView)findViewById(R.id.bannerAd);
//        Hashtable<String, String> params = new Hashtable<String, String>();
//        params.put("mode", "test");
//        bannerAd.setCustomParameters(params);
        bannerAd.setOnAdDownload(this);

        setupButtons();
    }

    public void setupButtons() {
		loadButton = (Button)findViewById(R.id.loadInterstitialButton);
		loadButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View button) {
		    	setupInterstitial();
		    	interstitialAd.load();
		    	loadButton.setEnabled(false);
		    }    		
		});
		showButton = (Button)findViewById(R.id.showInterstitialButton);
		showButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View button) {
		    	interstitialAd.showInterstitial();
		    }
		});
	 	showButton.setEnabled(false);
	 	
	 	final TapItTestActivity me = this;
	 	showAlertAdButton = (Button)findViewById(R.id.showAlertAdButton);
	 	showAlertAdButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				me.fireAlertAd();
			}
		});
	}
    
    public void fireAlertAd() {
    	Log.d("TapIt!", "loading Alert ad");
        AlertAd alertAd = new AlertAd(this, ADPROMPT_ZONE_ID);
//        Hashtable<String, String> params = new Hashtable<String, String>(); 
//        params.put("mode", "test");
//        alertAd.setCustomParameters(params);
        alertAd.setListener(new AlertAdCallbackListener() {
			
			@Override
			public void alertAdError(AlertAd ad, String error) {
				Log.d("TapIt!", "Alert ad failed to load: " +  error);
				Toast.makeText(getApplicationContext(), "Alert ad failed to load: " +  error, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void alertAdDisplayed(AlertAd ad) {
				Log.d("TapIt!", "Alert ad has been shown");
				
			}
			
			@Override
			public void alertAdClosed(AlertAd ad, boolean didAccept) {
				Log.d("TapIt!", "Alert ad was closed using the " + (didAccept ? "CallToAction" : "Decline") + " button");
			}
		});
        alertAd.showAlertAd();
    }
    
    
    public void setupInterstitial() {
    	interstitialAd = new AdInterstitialView(this, INTRS_ZONE_ID);
//    	interstitialAd = new AdFullscreenView(this, INTRS_ZONE_ID);
//    	interstitialAd = new AdOfferWallView(this, INTRS_ZONE_ID);
//    	interstitialAd = new AdVideoUnitView(this, VIDEO_ZONE_ID);
//        Hashtable<String, String> params = new Hashtable<String, String>();
//        params.put("cid", "1");
//        interstitialAd.setCustomParameters(params);

    	interstitialAd.setOnInterstitialAdDownload(this);
    }

	@Override
	public void begin(AdViewCore adView) {
		// Called just before an ad request is made
        Log.d("TapIt!", "Requesting banner ad");
        Toast.makeText(getApplicationContext(), "Requesting banner ad", Toast.LENGTH_LONG).show();
	}

	@Override
	public void end(AdViewCore adView) {
		// Called after an ad is successfully loaded... show ad
		Log.d("TapIt!", "Banner ad successfully loaded");
		Toast.makeText(getApplicationContext(), "Banner ad successfully loaded", Toast.LENGTH_LONG).show();
	}

	@Override
	public void error(AdViewCore adView, String error) {
		Log.d("TapIt!", "Ad failed to load: " + error);
		if(adView == interstitialAd) {
			Toast.makeText(getApplicationContext(), "Failed to load interstitial: " + error, Toast.LENGTH_LONG).show();
			showButton.setEnabled(false);
			loadButton.setEnabled(true);
		}
		else if(adView == bannerAd) {
			Toast.makeText(getApplicationContext(), "Failed to load banner: " + error, Toast.LENGTH_LONG).show();
			// Called when bannerAd fails to load an ad... hide ad
			Log.d("TapIt!", "Banner ad failed to load: " + error);
		}
	}

	@Override
	public void willLoad(AdViewCore adView) {
		// interstitial is about to load
		Log.d("TapIt!", "WillLoad");
	}

	@Override
	public void ready(AdViewCore adView) {
		// interstitial is loaded and ready for display
		Log.d("TapIt!", "ready!");
		showButton.setEnabled(true);
	}

	@Override
	public void willOpen(AdViewCore adView) {
		// interstitial is about to cover the screen. minimize your app footprint
		Log.d("TapIt!", "WillOpen");
	}

	@Override
	public void didClose(AdViewCore adView) {
		// interstitial is no longer covering the screen
		Log.d("TapIt!", "didClose");
		
		destroyInterstitial();
		loadButton.setEnabled(true);
	 	showButton.setEnabled(false);
	}
	
	private void destroyInterstitial() {
		if(interstitialAd != null) {
			interstitialAd.destroy();
			interstitialAd = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		AdView bannerAd = (AdView)findViewById(R.id.bannerAd);
		if(bannerAd != null) {
			// Shutdown ad internals
			bannerAd.destroy();
		}
		destroyInterstitial();
		super.onDestroy();
	}
}