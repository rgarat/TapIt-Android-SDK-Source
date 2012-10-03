package com.tapit;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import android.util.Log;
import com.tapit.adview.AlertAd;
import com.tapit.adview.AlertAd.AlertAdCallbackListener;

@Kroll.proxy(creatableInModule = TapitandroidModule.class)
public class AlertAdProxy extends KrollProxy {
	
	private static String CAT = "TapItModule";
	

	public AlertAdProxy() {
		super();
	}
	
	@Kroll.method
	public void showAd(String adzone)
	{
		
		Log.d(CAT, "loading Alert ad");
        
		AlertAd alertAd = new AlertAd(this.getActivity(), adzone);
				//new AlertAd(this, adzone);
		
        alertAd.setListener(new AlertAdCallbackListener() {
			
			public void alertAdError(AlertAd ad, String error) {
				Log.d(CAT, "Alert ad failed to load: " +  error);
				//Toast.makeText(getApplicationContext(), "Alert ad failed to load: " +  error, Toast.LENGTH_LONG).show();
			}
			
			public void alertAdDisplayed(AlertAd ad) {
				Log.d(CAT, "Alert ad has been shown");
				
			}
			
			public void alertAdClosed(AlertAd ad, boolean didAccept) {
				Log.d(CAT, "Alert ad was closed using the " + (didAccept ? "CallToAction" : "Decline") + " button");
			}
		});
        alertAd.showAlertAd();
		
	}

}
