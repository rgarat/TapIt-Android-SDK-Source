package com.tapit.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.api.PluginResult;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

import org.apache.cordova.api.*;
import com.tapit.adview.AdFullscreenView;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdOfferWallView;
import com.tapit.adview.AdVideoUnitView;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AlertAd;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;
import com.tapit.adview.AlertAd.AlertAdCallbackListener;

public class TapItAndroid extends Plugin implements OnAdDownload,
		OnInterstitialAdDownload {
	private String TAG = "TAPIT Android Plugin";
	private String TYPE_INTERSTITIAL = "AdInterstitial";
	private String TYPE_VIDEO = "AdVideo";
	private String TYPE_FULLSCREEN = "Adfullscreen";
	private String TYPE_BANNER = "AdOfferWall";
	private String TYPE_ALERT = "AlertAd";

	private String CallbackID;

	public static String ZONE_ID = "";

	// Ad views
	private AdInterstitialView interstitialAd;
	private AdFullscreenView fullscreenAd;
	private AdOfferWallView offerwallAd;
	private AdVideoUnitView videoAd;
	private AlertAd alertAd;
	private AdView bannerAd;

	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId) {
		this.CallbackID = callbackId;

		try {
			ZONE_ID = data.getString(0);
		} catch (JSONException e) {
			Log.i(TAG, "Error parsing JSON params");
			e.printStackTrace();
			this.error(new PluginResult(PluginResult.Status.ERROR),
					this.CallbackID);
		}

		Log.i(TAG, "LOADED");

		bannerAd = (AdView) cordova.getActivity().findViewById(2323);
		bannerAd.setOnAdDownload(this);

		// Setup all the ads
		/**
		 * Due to the Looper.loop error (Cannot update UI thread from worked
		 * thread we had to do this via another thread!)
		 * */
		setupAds();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*interstitialAd.setOnInterstitialAdDownload(this);
		fullscreenAd.setOnInterstitialAdDownload(this);
		offerwallAd.setOnInterstitialAdDownload(this);
		videoAd.setOnInterstitialAdDownload(this);

		interstitialAd.load();
		fullscreenAd.load();
		offerwallAd.load();
		videoAd.load();*/

		// Interstital ad
		if (action.equals(TYPE_INTERSTITIAL)) {
			Log.i(TAG, TYPE_INTERSTITIAL);
			interstitialAd.setOnInterstitialAdDownload(this);
			interstitialAd.load();
			interstitialAd.showInterstitial();
		}

		// Full screen ad
		else if (action.equals(TYPE_FULLSCREEN)) {
			Log.i(TAG, TYPE_FULLSCREEN);
			fullscreenAd.setOnInterstitialAdDownload(this);
			fullscreenAd.load();
			fullscreenAd.showInterstitial();
		}

		// Video ad
		else if (action.equals(TYPE_VIDEO)) {
			Log.i(TAG, TYPE_VIDEO);
			videoAd.setOnInterstitialAdDownload(this);
			videoAd.load();
			videoAd.showInterstitial();
		}

		// Banner ad
		else if (action.equals(TYPE_BANNER)) {
			Log.i(TAG, TYPE_BANNER);
			offerwallAd.setOnInterstitialAdDownload(this);
			offerwallAd.show();
			offerwallAd.showInterstitial();

		}

		else if (action.equals(TYPE_ALERT)) {
			Log.i(TAG, TYPE_ALERT);
			alertAd.setListener(new AlertAdCallbackListener() {

				public void alertAdError(AlertAd ad, String error) {
					Log.d(TAG, "Alert ad failed to load: " + error);
				}

				public void alertAdDisplayed(AlertAd ad) {
					Log.d(TAG, "Alert ad has been shown");

				}

				public void alertAdClosed(AlertAd ad, boolean didAccept) {
					Log.d(TAG, "Alert ad was closed using the "
							+ (didAccept ? "CallToAction" : "Decline")
							+ " button");
				}
			});

			alertAd.showAlertAd();
		}

		else {
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		}

		PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
		r.setKeepCallback(true);
		return r;
	}

	public void begin(AdViewCore adView) {
		// Called just before an ad request is made
		Log.d(TAG, "Requesting banner ad");
	}

	public void end(AdViewCore adView) {
		// Called after an ad is successfully loaded... show ad
		Log.d(TAG, "Banner ad successfully loaded");
	}

	public void error(AdViewCore adView, String error) {
		Log.d(TAG, "Ad failed to load: " + error);
		if (adView == bannerAd) {
			// Called when bannerAd fails to load an ad... hide ad
			Log.d(TAG, "Banner ad failed to load: " + error);
		}

		else {
			Log.d(TAG, adView.toString() + "Ad failed to load: " + error);
			this.error(new PluginResult(PluginResult.Status.ERROR),
					this.CallbackID);
		}
	}

	public void willLoad(AdViewCore adView) {
		// Ad is about to load
		Log.d(TAG, "WillLoad");
	}

	public void ready(AdViewCore adView) {
		// Ad is loaded and ready for display
		Log.d(TAG, "ready!");
	}

	public void willOpen(AdViewCore adView) {
		// Ad is about to cover the screen. minimize your app footprint
		Log.d(TAG, "WillOpen");
	}

	public void didClose(AdViewCore adView) {
		// Ad is no longer covering the screen
		Log.d(TAG, "didClose");
		destroyAds();
	}

	private void destroyAds() {
		if (interstitialAd != null) {
			interstitialAd.destroy();
			interstitialAd = null;
		}

		else if (fullscreenAd != null) {
			fullscreenAd.destroy();
			fullscreenAd = null;
		}

		else if (offerwallAd != null) {
			offerwallAd.destroy();
			offerwallAd = null;
		}

		else if (videoAd != null) {
			videoAd.destroy();
			videoAd = null;
		}

	}

	@Override
	public void onDestroy() {
		AdView bannerAd = (AdView) cordova.getActivity().findViewById(2323);
		if (bannerAd != null) {
			// Shutdown ad internals
			bannerAd.destroy();
		}
		destroyAds();
		super.onDestroy();
	}

	public void setupAds() {
		// Create dialog in new thread
		Runnable runnable = new Runnable() {
			public void run() {
				// Toast.makeText(cordova.getContext(), "Hello",
				// Toast.LENGTH_SHORT).show();
				interstitialAd = new AdInterstitialView(cordova.getContext(),
						ZONE_ID);
				fullscreenAd = new AdFullscreenView(cordova.getContext(),
						ZONE_ID);
				offerwallAd = new AdOfferWallView(cordova.getContext(), ZONE_ID);
				videoAd = new AdVideoUnitView(cordova.getContext(), ZONE_ID);
				alertAd = new AlertAd(cordova.getContext(), ZONE_ID);

				Log.i(TAG, "Ads created!");
			}
		};
		this.cordova.getActivity().runOnUiThread(runnable);
	}
}
