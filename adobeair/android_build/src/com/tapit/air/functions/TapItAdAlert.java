package com.tapit.air.functions;

import android.app.Activity;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.tapit.air.contexts.TapItContext;
import com.tapit.adview.AlertAd;
import com.tapit.adview.AlertAd.AlertAdCallbackListener;

public class TapItAdAlert implements FREFunction
{
	private static String ADPROMPT_ZONE_ID = "7984";
	private TapItContext tapItContext;

	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{
		tapItContext = (TapItContext)context;

		Activity activity = context.getActivity();

		try 
		{
			String zone = args[0].getAsString();

			if (!ADPROMPT_ZONE_ID.equals(zone))
				ADPROMPT_ZONE_ID = zone;
		} 
		catch (IllegalStateException e1) { e1.printStackTrace(); } 
		catch (FRETypeMismatchException e1) { e1.printStackTrace(); } 
		catch (FREInvalidObjectException e1) { e1.printStackTrace(); } 
		catch (FREWrongThreadException e1) { e1.printStackTrace(); }

		AlertAd alertAd = new AlertAd(activity, ADPROMPT_ZONE_ID);

		alertAd.setListener(new AlertAdCallbackListener()
		{
			@Override
			public void alertAdError(AlertAd ad, String error) 
			{
				Log.d("TapIt", "Alert ad failed to load: " + error);
				tapItContext.dispatchStatusEventAsync("ALERT_ERROR", "ERROR");
			}

			@Override
			public void alertAdDisplayed(AlertAd ad) 
			{
				Log.d("TapIt", "Alert ad has been shown");
				tapItContext.dispatchStatusEventAsync("ALERT_ADDED", "ADDED");
			}

			@Override
			public void alertAdClosed(AlertAd ad, boolean didAccept) 
			{
				Log.d("TapIt", "Alert ad was closed using the " + (didAccept ? "CallToAction" : "Decline") + " button");
				tapItContext.dispatchStatusEventAsync("ALERT_CLOSED", "CLOSED");
			}
		});

		alertAd.showAlertAd();

		return null;
	}
}
