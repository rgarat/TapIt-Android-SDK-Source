package com.tapit.adview;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class AlertAd {
	private static final String AD_TYPE_DIALOG = "10";
	
	protected Context context;
	protected AdRequest adRequest;
	
	private AlertAdCallbackListener listener;
	private LoadContentTask contentTask;

	
	public AlertAd(Context context, String zone) {
		this.context = context;
		adRequest = new AdRequest(zone);
		adRequest.setAdtype(AD_TYPE_DIALOG);
		adRequest.initDefaultParameters(context);
	}
	
	/**
	 * Optional. Set user location longtitude value (given in degrees.decimal
	 * degrees).
	 * 
	 * @param longitude
	 */
	public void setLongitude(String longitude) {
		if ((adRequest != null) && (longitude != null)) {
			adRequest.setLongitude(longitude);
		}
	}

	/**
	 * Optional. Get user location longtitude value (given in degrees.decimal
	 * degrees).
	 */
	public String getLongitude() {
		if (adRequest != null) {
			String longitude = adRequest.getLongitude();

			if (longitude != null) {
				return longitude;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Optional. Set Custom Parameters.
	 * 
	 * @param customParameters
	 */
	public void setCustomParameters(Hashtable<String, String> customParameters) {
		if (adRequest != null) {
			adRequest.setCustomParameters(customParameters);
		}
	}

	/**
	 * Set listener for alert ad callbacks
	 * 
	 * @param listener
	 */
	public void setListener(AlertAdCallbackListener listener) {
		this.listener = listener;
	}
	
	public void showAlertAd() {
		contentTask = new LoadContentTask(this);
		contentTask.execute(0);
	}
	
	private void displayAlertAd(String title, String callToAction, String declineStr, final String clickUrl) {
		final Activity theActivity = (Activity)context;
		final AlertAdCallbackListener theListener = listener;
		final AlertAd theAlertAd = this;
		
		try {
    		AlertDialog alertDialog = new AlertDialog.Builder(context)
    			.setPositiveButton(callToAction, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					// do the thing!
    					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
    					theActivity.startActivityForResult(intent,2);
    					if(theListener != null) {
    						theListener.alertAdClosed(theAlertAd, true);
    					}
    				}
    			})
    			.setNegativeButton(declineStr, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				// cancel out...
    				if(theListener != null) {
    					theListener.alertAdClosed(theAlertAd, false);
    				}
    			}
    			}).create();
    		alertDialog.setTitle(title);
    		
    		alertDialog.show();
		} catch(Exception e) {
		    if(listener != null) {
		        listener.alertAdError(this, e.getMessage());
		    }
		    e.printStackTrace();
		}
	}
	
	private String requestGet(String url) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
//		Log.d("TapIt", url);
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 8192);
		String responseValue = readInputStream(bufferedInputStream);
//		Log.d("TapIt", responseValue);
		bufferedInputStream.close();
		inputStream.close();
		return responseValue;
	}

	private static String readInputStream(BufferedInputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] buffer = new byte[8192];
		for (int n; (n = in.read(buffer)) != -1;) {
			out.append(new String(buffer, 0, n));
		}
		return out.toString();
	}
	
	private class LoadContentTask extends AsyncTask<Integer, Integer, String>{
		
		private AlertAd theAd;
		
		public LoadContentTask(AlertAd alertAd) {
			theAd = alertAd;
		}

		@Override
		protected String doInBackground(Integer... params) {
			
			String url = adRequest.createURL();
			Log.d("TapIt", url);
			String data;
			try {
				data = requestGet(url);
			} catch (IOException e) {
				data = "{\"error\": \"" + e.getMessage() + "\"}";
			}
			return data;
		}
		
		@Override
		protected void onPostExecute(String jsonStr) {
			String error = null;
			try {
				Log.d("TapIt", jsonStr);
				JSONObject jsonObject = new JSONObject(jsonStr);
				if(jsonObject.has("error")) {
					// failed to retrieve an ad, abort and call the error callback
					if(listener != null) {
						listener.alertAdError(theAd, jsonObject.getString("error"));
					}
				}
				else if (jsonObject.has("type") && "alert".equals(jsonObject.getString("type"))) {
					
					String title = jsonObject.getString("adtitle");
					String callToAction = jsonObject.getString("calltoaction");
					String declineStr = jsonObject.getString("declinestring");
					String clickUrl = jsonObject.getString("clickurl");
//					String clickUrl = "http://play.google.com/store/apps/details?id=com.tinyco.village&hl=en";
//					String clickUrl = "market://details?id=com.tinyco.village&hl=en";
					
					theAd.displayAlertAd(title, callToAction, declineStr, clickUrl);
					if(listener != null) {
						listener.alertAdDisplayed(theAd);
					}
				}
				else {
					if(listener != null) {
						listener.alertAdError(theAd, "Server returned an incompatible ad");
					}
				}
			
			} catch (JSONException e) {
				if("".equals(jsonStr)) {
					error = "server returned an empty response";
				}
				else {
					error = e.getMessage();
				}
				if(listener != null) {
					listener.alertAdError(theAd, error);
				}
			}
		}
	}
	
	/**
	 * Callbacks for alert ads.
	 */
	public interface AlertAdCallbackListener {
		/**
		 * This event is fired after alert is displayed.
		 */
		public void alertAdDisplayed(AlertAd ad);

		/**
		 * This event is fired if alert failed to load.
		 */
		public void alertAdError(AlertAd ad, String error);
		
		/**
		 * This event is fired if the alert was closed.
		 * @param didAccept true if user pressed the call to action button, false otherwise
		 */
		public void alertAdClosed(AlertAd ad, boolean didAccept);
	}
}
