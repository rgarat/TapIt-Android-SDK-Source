package com.yourcompany;

import java.util.Hashtable;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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
	
	private Button gLoadButton;
	private Button gShowButton;

	private AdInterstitialView interstitialAd;
//	private AdFullscreenView interstitialAd;
//	private AdOfferWallView interstitialAd;
//	private AdVideoUnitView interstitialAd;
	
	private AdView bannerAd;

	private com.google.ads.AdView googAd;
	private com.google.ads.InterstitialAd googInterstitial;
	
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
        bannerAd.cancelUpdating();

        setupButtons();
        setupGoog();
    }
    
    
    private void setupGoog() {
        googAd = new com.google.ads.AdView(this, com.google.ads.AdSize.BANNER, "903dbd7178b049f7");
        googAd.setAdListener(new AdListener() {

            @Override
            public void onDismissScreen(Ad gAd) {
                Log.d("TapIt", "googAd->onDismissScreen");
            }

            @Override
            public void onFailedToReceiveAd(Ad gAd, ErrorCode errCode) {
                Log.d("TapIt", "googAd->onFailedToReceiveAd: " + errCode);
            }

            @Override
            public void onLeaveApplication(Ad gAd) {
                Log.d("TapIt", "googAd->onLeaveApplication");
            }

            @Override
            public void onPresentScreen(Ad gAd) {
                Log.d("TapIt", "googAd->onPresentScreen");
            }

            @Override
            public void onReceiveAd(Ad gAd) {
                Log.d("TapIt", "googAd->onReceiveAd");
            }
            
        });
        
        com.google.ads.AdRequest googAdRequest = new com.google.ads.AdRequest();
        googAdRequest.addTestDevice(com.google.ads.AdRequest.TEST_EMULATOR);
        googAdRequest.addTestDevice("ED9A71101B1CD7741894D3D1B181D51B");
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.googLayout);

        // Add the adView to it
        layout.addView(googAd);

        // Initiate a generic request to load it with an ad
        googAd.loadAd(googAdRequest);
    }
    
    private void setupGoogInterstitial() {
        com.google.ads.AdRequest googAdRequest = new com.google.ads.AdRequest();
        googAdRequest.addTestDevice(com.google.ads.AdRequest.TEST_EMULATOR);
        googAdRequest.addTestDevice("ED9A71101B1CD7741894D3D1B181D51B");

        googInterstitial = new com.google.ads.InterstitialAd(this, "3027767e23364ccf");
        googInterstitial.loadAd(googAdRequest);
        googInterstitial.setAdListener(new AdListener() {

            @Override
            public void onDismissScreen(Ad arg0) {
                Log.d("TapIt", "googInterstitial->onDismissScreen");
                gShowButton.setEnabled(false);
                gLoadButton.setEnabled(true);
            }

            @Override
            public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
                Log.d("TapIt", "googInterstitial->onFailedToReceiveAd: " + arg1);
                gShowButton.setEnabled(false);
                gLoadButton.setEnabled(true);
            }

            @Override
            public void onLeaveApplication(Ad arg0) {
                Log.d("TapIt", "googInterstitial->onLeaveApplication");
            }

            @Override
            public void onPresentScreen(Ad arg0) {
                Log.d("TapIt", "googInterstitial->onPresentScreen");
            }

            @Override
            public void onReceiveAd(Ad arg0) {
                Log.d("TapIt", "googInterstitial->onReceiveAd");
                gShowButton.setEnabled(true);
            }
        });
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

    
        gLoadButton = (Button)findViewById(R.id.gLoadInterstitialButton);
        gLoadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View button) {
                setupGoogInterstitial();
            }           
        });
        gShowButton = (Button)findViewById(R.id.gShowInterstitialButton);
        gShowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View button) {
                googInterstitial.show();
            }
        });
        gShowButton.setEnabled(false);
    }
    
    public void fireAlertAd() {
    	Log.d("TapIt", "loading Alert ad");
        AlertAd alertAd = new AlertAd(this, ADPROMPT_ZONE_ID);
//        Hashtable<String, String> params = new Hashtable<String, String>(); 
//        params.put("mode", "test");
//        alertAd.setCustomParameters(params);
        alertAd.setListener(new AlertAdCallbackListener() {
			
			@Override
			public void alertAdError(AlertAd ad, String error) {
				Log.d("TapIt", "Alert ad failed to load: " +  error);
				Toast.makeText(getApplicationContext(), "Alert ad failed to load: " +  error, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void alertAdDisplayed(AlertAd ad) {
				Log.d("TapIt", "Alert ad has been shown");
				
			}
			
			@Override
			public void alertAdClosed(AlertAd ad, boolean didAccept) {
				Log.d("TapIt", "Alert ad was closed using the " + (didAccept ? "CallToAction" : "Decline") + " button");
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
		Log.d("TapIt", "Banner ad successfully loaded");
		Toast.makeText(getApplicationContext(), "Banner ad successfully loaded", Toast.LENGTH_LONG).show();
	}

	@Override
	public void error(AdViewCore adView, String error) {
		Log.d("TapIt", "Ad failed to load: " + error);
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
		Log.d("TapIt", "WillLoad");
        Toast.makeText(getApplicationContext(), "WillLoad", Toast.LENGTH_LONG).show();
	}

	@Override
	public void ready(AdViewCore adView) {
		// interstitial is loaded and ready for display
		Log.d("TapIt", "ready!");
		showButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), "ready!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void willOpen(AdViewCore adView) {
		// interstitial is about to cover the screen. minimize your app footprint
		Log.d("TapIt", "WillOpen");
        Toast.makeText(getApplicationContext(), "WillOpen", Toast.LENGTH_LONG).show();
	}

	@Override
	public void didClose(AdViewCore adView) {
		// interstitial is no longer covering the screen
		Log.d("TapIt", "didClose");
        Toast.makeText(getApplicationContext(), "didClose", Toast.LENGTH_LONG).show();

		destroyInterstitial();
		loadButton.setEnabled(true);
	 	showButton.setEnabled(false);
	}


    @Override
    public void clicked(AdViewCore adView) {
        Log.d("TapIt", "Ad clicked");
        Toast.makeText(getApplicationContext(), "Ad clicked", Toast.LENGTH_LONG).show();
    }


    @Override
    public void willPresentFullScreen(AdViewCore adView) {
      Log.d("TapIt", "willPresentFullScreen");
      Toast.makeText(getApplicationContext(), "willPresentFullScreen", Toast.LENGTH_LONG).show();
    }


    @Override
    public void didPresentFullScreen(AdViewCore adView) {
      Log.d("TapIt", "didPresentFullScreen");
      Toast.makeText(getApplicationContext(), "didPresentFullScreen", Toast.LENGTH_LONG).show();
    }


    @Override
    public void willDismissFullScreen(AdViewCore adView) {
      Log.d("TapIt", "willDismissFullScreen");
      Toast.makeText(getApplicationContext(), "willDismissFullScreen", Toast.LENGTH_LONG).show();
    }


//    @Override
//    public void didDismissFullScreen(AdViewCore adView) {
//      Log.d("TapIt", "didDismissFullScreen");
//      Toast.makeText(getApplicationContext(), "didDismissFullScreen", Toast.LENGTH_LONG).show();
//    }
//
//
    @Override
    public void willLeaveApplication(AdViewCore adView) {
      Log.d("TapIt", "Leaving Application!");
      Toast.makeText(getApplicationContext(), "Leaving Application!", Toast.LENGTH_LONG).show();
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