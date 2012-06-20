package com.tapit.adview.notif;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tapit.adview.AdLog;
import com.tapit.adview.AdRequest;
import com.tapit.adview.AutoDetectedParametersSet;

/*
 * http://stackoverflow.com/questions/4867338/custom-notification-layouts-and-text-colors
 * http://developer.android.com/reference/android/os/SystemClock.html
 * http://developer.android.com/reference/android/os/PowerManager.html
 */
public class AdService extends Service {

	private static final long WAIT_TIMEOUT = 30 * 1000;

	private AdLog adLog = new AdLog(this);

	private AdRequest adRequest;

	public static final String TAG = "AdService";

	private LocationManager locationManager;
	private WhereamiLocationListener listener;

	private PowerManager pm;
	PowerManager.WakeLock wl;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (pm == null)
			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (wl == null)
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
		wl.acquire();

		// scheduling next start
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String appId = preferences.getString(TapIt.APP_ID_PREF, "");
				
		int logLevel = preferences.getInt(TapIt.LOG_LEVEL_PREF, -1);
		if (logLevel >=0 )
			adLog.setLogLevel(logLevel);
		
		adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG,
				"AdService.onStart #" + String.valueOf(startId));
		
		long interval = preferences.getLong(TapIt.INTERVAL_PREF, TapIt.DEFAULT_PERIOD);

		AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		PendingIntent operation = PendingIntent.getService(this, TapIt.GET_ADSERVICE_REQUEST_CODE,
				new Intent("com.tapit.adview.notif.AdService" + appId),
				PendingIntent.FLAG_UPDATE_CURRENT);
		if (operation != null) {
			alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
					+ interval, operation);
			adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG,
					"set next alarm after that interval:" + String.valueOf(interval));
			Editor editor = preferences.edit();
			editor.putLong(TapIt.LAST_SCHEDULING_PREF, SystemClock.elapsedRealtime());
			editor.commit();
		}
		if (!preferences.getBoolean(
				TapIt.NOTIF_ADV_ENABLED_PREF, false)) {
			adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG, 
					"Notification advertisement is disabled... skipped #" + String.valueOf(startId));
			// stopSelf();
			return;
		}

		// creating request
		adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG, 
				"execute ShowNotificationAdvertisementTask");
		new ShowNotificationAdvertisementTask().execute(0);

	}

	private class ShowNotificationAdvertisementTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(AdService.this);

			adRequest = new AdRequest(adLog);
			adRequest.initDefaultParameters(AdService.this);
			adRequest.setZone(preferences.getString(TapIt.ZONE_PREF, "")).setAdtype("4");

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			try {
				new AutoDetectParametersThread(AdService.this, adRequest).start();
				adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG,
						"AutoDetectParametersThread started");
				// see links on top of this file
				Thread.sleep(WAIT_TIMEOUT);

				if ((adRequest.getLatitude() == null) || (adRequest.getLongitude() == null)) {
					AutoDetectedParametersSet autoDetectedParametersSet = AutoDetectedParametersSet
							.getInstance();
					adRequest.setLatitude(autoDetectedParametersSet.getLatitude());
					adRequest.setLongitude(autoDetectedParametersSet.getLongitude());
					adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
							"AutoDetectedParametersSet.Gps/Network=", "("
									+ autoDetectedParametersSet.getLatitude() + ";"
									+ autoDetectedParametersSet.getLongitude() + ")");

				}

			} catch (Exception e) {
				adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_ERROR,
						"sleep in ShowNotificationAdvertisementTask",
						e.getMessage());
			}

			// fetch and show notification
			try {
				URL url = new URL(adRequest.createURL());
				InputStream input = url.openStream();

				BufferedInputStream bufferedInputStream = new BufferedInputStream(input, 8192);
				String responseValue = readInputStream(bufferedInputStream);
				bufferedInputStream.close();
				input.close();
				adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG, "Banner downloaded: " + 
						url.toString());

				adLog.log(AdLog.LOG_LEVEL_3, AdLog.LOG_TYPE_INFO, TAG, "Response: " + responseValue);

				
				JSONObject jsonObject = new JSONObject(responseValue);

				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

				CharSequence adtitle = jsonObject.getString("adtitle");
				CharSequence text = jsonObject.getString("adtext");

				int iconId = preferences.getInt(
						TapIt.ICON_RESOURCE_PREF, -1);
				if (iconId <= 0)
					iconId = android.R.drawable.star_on;
				// android.R.drawable.presence_online
				// android.R.drawable.ic_input_add

				Notification notification = new Notification(iconId, adtitle,
						System.currentTimeMillis());

				if (getPackageManager().checkPermission("android.permission.VIBRATE",
						getApplicationContext().getPackageName()) == 0) {
					long[] vibro = new long[] { 0, 100L, 200L, 300L };
					notification.vibrate = vibro;
				}

				// notification.ledARGB = 0xaaaaaaaa;
				notification.ledOffMS = 300;
				notification.ledOnMS = 300;
				notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject
						.getString("clickurl")));
				PendingIntent contentIntent = PendingIntent.getActivity(AdService.this, 0, i,
						Intent.FLAG_ACTIVITY_NEW_TASK);

				notification.setLatestEventInfo(AdService.this, adtitle, text, contentIntent);

				notificationManager.cancel(0);
				notificationManager.notify(0, notification);
				adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, TAG, "notification showed: " + 
						notification.toString());

				// stopSelf();

			} catch (MalformedURLException e) {
				adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_ERROR, TAG,
						e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_ERROR,
						TAG,
						e.getMessage());
				e.printStackTrace();
			} catch (JSONException e) {
				adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_ERROR,
						TAG,
						e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			wl.release();
		}
	}

	private static String readInputStream(BufferedInputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] buffer = new byte[8192];
		for (int n; (n = in.read(buffer)) != -1;) {
			out.append(new String(buffer, 0, n));
		}
		return out.toString();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class AutoDetectParametersThread extends Thread {
		private Context context;
		private AdRequest adRequest;

		public AutoDetectParametersThread(Context context,
				AdRequest adRequest) {
			this.context = context;
			this.adRequest = adRequest;
		}

		@Override
		public void run() {
			if (adRequest != null) {
				AutoDetectedParametersSet autoDetectedParametersSet = AutoDetectedParametersSet
						.getInstance();

				if ((adRequest.getLatitude() == null) || (adRequest.getLongitude() == null)) {

					int isAccessFineLocation = context
								.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

					boolean checkNetworkProdier = false;
					locationManager = (LocationManager) context
								.getSystemService(Context.LOCATION_SERVICE);
					if (isAccessFineLocation == PackageManager.PERMISSION_GRANTED) {
						boolean isGpsEnabled = locationManager
									.isProviderEnabled(LocationManager.GPS_PROVIDER);

						if (isGpsEnabled) {
							listener = new WhereamiLocationListener(locationManager,
										autoDetectedParametersSet);
							locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER, 0, 0, listener,
										Looper.getMainLooper());
						} else {
							checkNetworkProdier = true;
							adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
										"AutoDetectedParametersSet.Gps", "not avalable");
						}
					} else {
						checkNetworkProdier = true;
						adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
									"AutoDetectedParametersSet.Gps",
									"no permission ACCESS_FINE_LOCATION");
					}

					if (checkNetworkProdier) {
						int isAccessCoarseLocation = context
									.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

						if (isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED) {
							boolean isNetworkEnabled = locationManager
										.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

							if (isNetworkEnabled) {
								listener = new WhereamiLocationListener(locationManager,
											autoDetectedParametersSet);
								locationManager.requestLocationUpdates(
											LocationManager.NETWORK_PROVIDER, 0, 0, listener,
											Looper.getMainLooper());
							} else {
								adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
											"AutoDetectedParametersSet.Network", "not avalable");
							}
						} else {
							adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
										"AutoDetectedParametersSet.Network",
										"no permission ACCESS_COARSE_LOCATION");
						}
					}
				}

				if (adRequest.getUa() == null) {
					if (autoDetectedParametersSet.getUa() == null) {
						String userAgent = getUserAgentString(AdService.this);

						if ((userAgent != null) && (userAgent.length() > 0)) {
							adRequest.setUa(userAgent);
							autoDetectedParametersSet.setUa(userAgent);
						}
					} else {
						adRequest.setUa(autoDetectedParametersSet.getUa());
					}
				}

				if (adRequest.getConnectionSpeed() == null) {
					if (autoDetectedParametersSet.getConnectionSpeed() == null) {
						try {
							Integer connectionSpeed = null;
							ConnectivityManager connectivityManager = (ConnectivityManager) context
									.getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

							if (networkInfo != null) {
								int type = networkInfo.getType();
								int subtype = networkInfo.getSubtype();

								// 0 - low (gprs, edge), 1 - fast (3g, wifi)
								if (type == ConnectivityManager.TYPE_WIFI) {
									connectionSpeed = 1;
								} else if (type == ConnectivityManager.TYPE_MOBILE) {
									if (subtype == TelephonyManager.NETWORK_TYPE_EDGE) {
										connectionSpeed = 0;
									} else if (subtype == TelephonyManager.NETWORK_TYPE_GPRS) {
										connectionSpeed = 0;
									} else if (subtype == TelephonyManager.NETWORK_TYPE_UMTS) {
										connectionSpeed = 1;
									}
								}
							}

							if (connectionSpeed != null) {
								adRequest.setConnectionSpeed(connectionSpeed);
								autoDetectedParametersSet.setConnectionSpeed(connectionSpeed);
							}
						} catch (Exception e) {
						}
					} else {
						adRequest
								.setConnectionSpeed(autoDetectedParametersSet.getConnectionSpeed());
					}
				}
			}
		}
	}

	private class WhereamiLocationListener implements LocationListener {
		private LocationManager locationManager;
		private AutoDetectedParametersSet autoDetectedParametersSet;

		public WhereamiLocationListener(LocationManager locationManager,
				AutoDetectedParametersSet autoDetectedParametersSet) {
			this.locationManager = locationManager;
			this.autoDetectedParametersSet = autoDetectedParametersSet;
		}

		public void onLocationChanged(Location location) {
			locationManager.removeUpdates(this);

			try {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();

				adRequest.setLatitude(Double.toString(latitude));
				adRequest.setLongitude(Double.toString(longitude));
				autoDetectedParametersSet.setLatitude(Double.toString(latitude));
				autoDetectedParametersSet.setLongitude(Double.toString(longitude));
				adLog.log(AdLog.LOG_LEVEL_3, AdLog.LOG_TYPE_INFO, "LocationChanged=",
						"(" + autoDetectedParametersSet.getLatitude() + ";"
								+ autoDetectedParametersSet.getLongitude() + ")");

			} catch (Exception e) {
				adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_ERROR, "LocationChanged",
						e.getMessage());
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public static String getUserAgentString(Context context) {
		try {
			Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(
					Context.class, WebView.class);
			constructor.setAccessible(true);
			try {
				WebSettings settings = constructor.newInstance(context, null);
				return settings.getUserAgentString();
			} finally {
				constructor.setAccessible(false);
			}
		} catch (Exception e) {
			return new WebView(context).getSettings().getUserAgentString();
		}
	}
}
