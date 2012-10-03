package com.tapit.air.functions;

import android.app.Activity;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdViewCore;
import com.tapit.air.contexts.TapItContext;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;

public class TapItFullScreen implements FREFunction, OnAdDownload, OnInterstitialAdDownload
{
	private static String INTRS_ZONE_ID = "7983";
	private TapItContext tapItContext;

	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{		
		tapItContext = (TapItContext) context;
		Activity activity = tapItContext.getActivity();
		
		try 
		{
			String zone = args[0].getAsString();

			if (!INTRS_ZONE_ID.equals(zone))
				INTRS_ZONE_ID = zone;
		} 
		catch (IllegalStateException e1) { e1.printStackTrace(); } 
		catch (FRETypeMismatchException e1) { e1.printStackTrace(); } 
		catch (FREInvalidObjectException e1) { e1.printStackTrace(); } 
		catch (FREWrongThreadException e1) { e1.printStackTrace(); }
		
		AdInterstitialView bannerAd = new AdInterstitialView(activity, INTRS_ZONE_ID);
      
        tapItContext.bannerAd = bannerAd;
        
        bannerAd.setOnInterstitialAdDownload(this);
        bannerAd.load();
        bannerAd.showInterstitial();

       return null;
	}

	@Override
	public void begin(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_START", "START");
		Log.d("TapIt", "Requesting banner ad");
	}

	@Override
	public void end(AdViewCore adView)
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_ADDED", "ADDED");
		Log.d("TapIt", "Banner ad successfully loaded");
	}

	@Override
	public void error(AdViewCore adView, String error) 
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_ERROR", "ERROR");
		Log.d("TapIt", "Banner ad failed to load: " + error);
	}

	@Override
	public void clicked(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_CLICKED", "CLICKED");
		Log.d("TapIt", "Ad clicked");
	}

	@Override
	public void willPresentFullScreen(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("WILL_PRESENT_FULLSCREEN", "WILL_PRESENT");
		Log.d("TapIt", "willPresentFullScreen");
	}

	@Override
	public void didPresentFullScreen(AdViewCore adView)
	{
		tapItContext.dispatchStatusEventAsync("PRESENTED_FULLSCREEN", "PRESENTED");
		Log.d("TapIt", "didPresentFullScreen");
	}

	@Override
	public void willDismissFullScreen(AdViewCore adView)
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_DISMISSED", "DISMISS_FULLSCREEN");
		Log.d("TapIt", "willDismissFullScreen");
	}
	
	@Override
	public void willLoad(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_LOADING", "START");
		Log.d("TapIt", "WillLoad");
	}

	@Override
	public void ready(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_ADDED", "READY");
		Log.d("TapIt", "ready!");
	}

	@Override
	public void willOpen(AdViewCore adView)
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_READY", "OPEN");
		Log.d("TapIt", "WillOpen");
	}
	
	@Override
	public void didClose(AdViewCore adView)
	{
		tapItContext.dispatchStatusEventAsync("FULLSCREEN_CLOSED", "CLOSED");
		Log.d("TapIt", "didClose");
	}

	@Override
    public void willLeaveApplication(AdViewCore adView)
    {
    }
}
