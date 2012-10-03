package com.tapit;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;

import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


@Kroll.proxy(creatableInModule = TapitandroidModule.class)
public class InterstitialAdProxy extends KrollProxy implements OnInterstitialAdDownload{

	private static final String LCAT = "TapItModule";
	private static final int SHOW_MSG = 12345678;

	private AdInterstitialView interstitialAd;

	public InterstitialAdProxy() {
		super();
		interstitialAd = null;
	}

	@Kroll.method
	public void showAd(final String adzone) {
		final InterstitialAdProxy me = this;

		// interstitialAd = new AdInterstitialView(this.getActivity(),adzone);

		Log.d(LCAT, "show Ads: " + adzone);

		// Get the view object from the proxy and set the color

		if (!TiApplication.isUIThread()) {
			TiMessenger.sendBlockingMainMessage(new Handler(TiMessenger.getMainMessenger().getLooper(), new Handler.Callback() {
				
				public boolean handleMessage(Message msg) {
					
					Log.d(LCAT, "Inside thread: " + adzone);
					
					if (msg.what == SHOW_MSG) {
						
						Log.d(LCAT, "Got the message: " + adzone);

						AsyncResult result = (AsyncResult) msg.obj;
						if (interstitialAd == null) {
							interstitialAd = new AdInterstitialView(me.getActivity(), adzone);
							Log.d(LCAT, "Create the View: " + adzone);
							interstitialAd.setOnInterstitialAdDownload(me);
							interstitialAd.load();
							//interstitialAd.showInterstitial();
						}
						result.setResult(null);
						return true;
					}

					return false;
				}
			}).obtainMessage(SHOW_MSG), adzone);
		} else {
			Log.d(LCAT, "Is UI thread: " + adzone);
			if (interstitialAd == null) {
				interstitialAd = new AdInterstitialView(me.getActivity(), adzone);
				interstitialAd.setOnInterstitialAdDownload(me);
				interstitialAd.load();
			}
		}

	}

	@Override
	public void didClose(AdViewCore arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(AdViewCore arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ready(AdViewCore arg0) {
		Log.d(LCAT, "Ready to view...");
		interstitialAd.showInterstitial();
		
	}

	@Override
	public void willLoad(AdViewCore arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void willOpen(AdViewCore arg0) {
		// TODO Auto-generated method stub
		
	}

}
