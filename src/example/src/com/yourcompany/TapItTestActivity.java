package com.yourcompany;

import com.tapit.adview.AdFullscreenView;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdOfferWallView;
import com.tapit.adview.AdVideoUnitView;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;
import com.tapit.adview.notif.TapIt;
import com.tapit.adview.track.InstallTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TapItTestActivity extends Activity implements OnAdDownload, OnInterstitialAdDownload {

	public final static String ZONE_ID = "1";
	
	private Button loadButton;
	private Button showButton;
	
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
	}
    
    public void setupInterstitial() {
    	interstitialAd = new AdInterstitialView(this, ZONE_ID);
//    	interstitialAd = new AdFullscreenView(this, ZONE_ID);
//    	interstitialAd = new AdOfferWallView(this, ZONE_ID);
//    	interstitialAd = new AdVideoUnitView(this, ZONE_ID);
    	interstitialAd.setOnInterstitialAdDownload(this);
    }

	@Override
	public void begin(AdViewCore adView) {
		// Called just before an ad request is made
        Log.d("TapIt!", "Requesting banner ad");
	}

	@Override
	public void end(AdViewCore adView) {
		// Called after an ad is successfully loaded... show ad
		Log.d("TapIt!", "Banner ad successfully loaded");
	}

	@Override
	public void error(AdViewCore adView, String error) {
		Log.d("TapIt!", "Ad failed to load: " + error);
		if(adView == interstitialAd) {
			showButton.setEnabled(false);
			loadButton.setEnabled(true);
		}
		else if(adView == bannerAd) {
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