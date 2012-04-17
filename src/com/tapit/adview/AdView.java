package com.tapit.adview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;

/**
 * Viewer of advertising. Following parametres are defined automatically, if
 * they are equal NULL: latitude - Latitude. longitude - Longitude. ua - The
 * browser user agent of the device making the request.
 */
public class AdView extends AdViewCore {
	private AutoDetectParametersThread autoDetectParametersThread;
	private LocationManager locationManager;
	private WhereamiLocationListener listener;

	/**
	 * Creation of viewer of advertising.
	 * 
	 * @param context
	 *            - The reference to the context of Activity.
	 * @param zone
	 *            - The id of the zone of publisher site.
	 */
	public AdView(Context context, String zone) {
		super(context, zone);
		initialize(context);
	}

	/**
	 * Creation of advanced viewer of advertising. It is used for element
	 * creation in a XML template.
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	/**
	 * Creation of advanced viewer of advertising. It is used for element
	 * creation in a XML template.
	 * 
	 * @param context
	 * @param attrs
	 */
	public AdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	/**
	 * Creation of advanced viewer of advertising. It is used for element
	 * creation in a XML template.
	 * 
	 * @param context
	 */
	public AdView(Context context) {
		super(context);
		initialize(context);
	}

	private void initialize(Context context) {
		if (adRequest != null) {
			AutoDetectedParametersSet autoDetectedParametersSet = AutoDetectedParametersSet
					.getInstance();

			if (adRequest.getUa() == null) {
				if (autoDetectedParametersSet.getUa() == null) {
					String userAgent = getSettings().getUserAgentString();

					if ((userAgent != null) && (userAgent.length() > 0)) {
						adRequest.setUa(userAgent);
						autoDetectedParametersSet.setUa(userAgent);
					}
				} else {
					adRequest.setUa(autoDetectedParametersSet.getUa());
				}
			}
		}
		
		autoDetectParametersThread = new AutoDetectParametersThread(context, this, adRequest);
		
	}

	@Override
	protected void onAttachedToWindow() {
		adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, "AttachedToWindow", "");
		if ((autoDetectParametersThread != null)
				&& (autoDetectParametersThread.getState().equals(Thread.State.NEW))) {
			autoDetectParametersThread.start();
		}
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, "DetachedFromWindow", "");
		super.onDetachedFromWindow();
	}

	@Override
	protected void cancelUpdating() {
		if ((locationManager != null) && (listener != null)) {
			locationManager.removeUpdates(listener);
		}

		if (autoDetectParametersThread != null) {
			try {
				autoDetectParametersThread.interrupt();
			} catch (Exception e) {
			}
		}
		super.cancelUpdating();
	}
	
	private class AutoDetectParametersThread extends Thread {
		private Context context;
		private AdViewCore adserverView;
		private AdRequest adRequest;

		public AutoDetectParametersThread(Context context,
				AdViewCore adserverView, AdRequest adRequest) {
			this.context = context;
			this.adserverView = adserverView;
			this.adRequest = adRequest;
		}

		@Override
		public void run() {
			if (adRequest != null) {
				AutoDetectedParametersSet autoDetectedParametersSet = AutoDetectedParametersSet
						.getInstance();

				if (adRequest.getUa() == null) {
					if (autoDetectedParametersSet.getUa() == null) {
						String userAgent = adserverView.getSettings().getUserAgentString();

						if ((userAgent != null) && (userAgent.length() > 0)) {
							adRequest.setUa(userAgent);
							autoDetectedParametersSet.setUa(userAgent);
						}
					} else {
						adRequest.setUa(autoDetectedParametersSet.getUa());
					}
				}
				
				if ((adRequest.getLatitude() == null) || (adRequest.getLongitude() == null)) {
					if ((autoDetectedParametersSet.getLatitude() == null)
							|| (autoDetectedParametersSet.getLongitude() == null)) {
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
					} else {
						adRequest.setLatitude(autoDetectedParametersSet.getLatitude());
						adRequest.setLongitude(autoDetectedParametersSet.getLongitude());
						adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_WARNING,
								"AutoDetectedParametersSet.Gps/Network=", "("
										+ autoDetectedParametersSet.getLatitude() + ";"
										+ autoDetectedParametersSet.getLongitude() + ")");
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

}
