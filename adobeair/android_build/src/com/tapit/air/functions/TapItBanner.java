package com.tapit.air.functions;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.tapit.air.contexts.TapItContext;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AdViewCore.OnAdDownload;

public class TapItBanner implements FREFunction, OnAdDownload 
{
	public static String BANNER_ZONE_ID = "7979";
	private TapItContext tapItContext;

	@Override
	public FREObject call(FREContext context, FREObject[] args) 
	{
		tapItContext = (TapItContext) context;
		Activity activity = context.getActivity();
		LinearLayout adHolder = new LinearLayout(activity);

		try 
		{
			String zone = args[0].getAsString();
			
			if (!BANNER_ZONE_ID.equals(zone)) BANNER_ZONE_ID = zone;
		}
		catch (IllegalStateException e1) { e1.printStackTrace(); } 
		catch (FRETypeMismatchException e1) { e1.printStackTrace(); } 
		catch (FREInvalidObjectException e1) { e1.printStackTrace(); } 
		catch (FREWrongThreadException e1) { e1.printStackTrace(); }

		AdView bannerAd = new AdView(activity, BANNER_ZONE_ID);

		try 
		{
			String size = args[1].getAsString();
			
			if (size.equals("auto"))
			{
				bannerAd.setAdSize(AdView.BannerAdSize.AUTOSIZE_AD);
			}
			else if (size.equals("120x20"))
			{
				bannerAd.setAdSize(AdView.BannerAdSize.SMALL_BANNER);
			}
			else if (size.equals("168x28"))
			{
				bannerAd.setAdSize(AdView.BannerAdSize.MEDIUM_BANNER);
			}
			else if (size.equals("216x36"))
			{
				bannerAd.setAdSize(AdView.BannerAdSize.LARGE_BANNER);
			}
			else if (size.equals("300x50"))
			{
				bannerAd.setAdSize(AdView.BannerAdSize.XL_BANNER);
			}
			else if (size.equals("320x50")) 
			{
				bannerAd.setAdSize(AdView.BannerAdSize.IPHONE_BANNER);
			}
			else if (size.equals("720x90")) 
			{
				bannerAd.setAdSize(AdView.BannerAdSize.TABLET_BANNER);
			}
			else
			{
				bannerAd.setAdSize(AdView.BannerAdSize.AUTOSIZE_AD);
			}
		} 
		catch (IllegalStateException e1) { e1.printStackTrace(); } 
		catch (FRETypeMismatchException e1) { e1.printStackTrace(); } 
		catch (FREInvalidObjectException e1) { e1.printStackTrace(); } 
		catch (FREWrongThreadException e1) { e1.printStackTrace(); }

		try 
		{
			String position = args[2].getAsString();
			
			if (position.equals("top")) 
			{
				adHolder.setGravity(Gravity.TOP);
			}

			if (position.equals("bottom")) 
			{
				adHolder.setGravity(Gravity.BOTTOM);
			}
		} 
		catch (IllegalStateException e1) { e1.printStackTrace(); } 
		catch (FRETypeMismatchException e1) { e1.printStackTrace(); } 
		catch (FREInvalidObjectException e1) { e1.printStackTrace(); } 
		catch (FREWrongThreadException e1) { e1.printStackTrace(); }

		LayoutParams layParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		activity.addContentView(adHolder, layParams);
		adHolder.addView(bannerAd, params);

		tapItContext.bannerAd = bannerAd;
		tapItContext.currentLayout = adHolder;
		
		bannerAd.setOnAdDownload(this);
       // bannerAd.cancelUpdating();
		return null;
	}

	@Override
	public void begin(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_START", "START");
		Log.d("TapIt", "Requesting banner ad");
	}

	@Override
	public void end(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_ADDED", "ADDED");
		Log.d("TapIt", "Banner ad successfully loaded");
	}

	@Override
	public void error(AdViewCore adView, String error) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_ERROR", "ERROR");
		Log.d("TapIt", "Banner ad failed to load: " + error);
	}

	@Override
	public void clicked(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_CLICKED", "CLICKED");
		Log.d("TapIt", "Ad clicked");
	}

	@Override
	public void willPresentFullScreen(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_START_FULLSCREEN", "START_FULLSCREEN");
		Log.d("TapIt", "willPresentFullScreen");
	}

	@Override
	public void didPresentFullScreen(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_ADDED_FULLSCREEN", "ADDED_FULLSCREEN");
		Log.d("TapIt", "didPresentFullScreen");
	}

	@Override
	public void willDismissFullScreen(AdViewCore adView) 
	{
		tapItContext.dispatchStatusEventAsync("BANNER_DISMISS_FULLSCREEN", "DISMISS_FULLSCREEN");
		Log.d("TapIt", "willDismissFullScreen");
	}

	@Override
	public void willLeaveApplication(AdViewCore adView) 
	{
	}
}
