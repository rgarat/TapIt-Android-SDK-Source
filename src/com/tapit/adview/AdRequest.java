package com.tapit.adview;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;

public class AdRequest {
	public static final String TAG = "AdRequest";

	private Map<String, String> parameters = Collections
			.synchronizedMap(new HashMap<String, String>());
	private static final String PARAMETER_ZONE = "zone";
	private static final String PARAMETER_ADTYPE = "adtype";
	private static final String PARAMETER_USER_AGENT = "ua";
	private static final String PARAMETER_LATITUDE = "lat";
	private static final String PARAMETER_LONGITUDE = "long";
	private static final String PARAMETER_BACKGROUND = "paramBG";
	private static final String PARAMETER_LINK = "paramLINK";
	private static final String PARAMETER_MIN_SIZE_X = "min_size_x";
	private static final String PARAMETER_MIN_SIZE_Y = "min_size_y";
	private static final String PARAMETER_SIZE_X = "size_x";
	private static final String PARAMETER_SIZE_Y = "size_y";
	private static final String PARAMETER_HEIGHT = "h";
	private static final String PARAMETER_WIDTH = "w";
	private static final String PARAMETER_CONNECTION_SPEED = "connection_speed";
	public final static String PARAMETER_DEVICE_ID = "udid";

	private String adserverURL = "http://r.tapit.com/adrequest.php";

	private Hashtable<String, String> customParameters;

	private AdLog adLog;

	public AdRequest(AdLog adLog) {
		this.adLog = adLog;
	}

	public AdRequest(String zone) {
		setZone(zone);
	}

	public void initDefaultParameters(Context context) {
		String deviceIdMD5 = Utils.getDeviceIdMD5(context);
		adLog.log(AdLog.LOG_LEVEL_2, AdLog.LOG_TYPE_INFO, "deviceIdMD5", deviceIdMD5);

		parameters.put("format", "json");
		
		if ((deviceIdMD5 != null) && (deviceIdMD5.length() > 0)) {
			parameters.put(PARAMETER_DEVICE_ID, deviceIdMD5);
		}
	}

	/**
	 * Get URL of ad server.
	 * 
	 * @return
	 */
	public synchronized String getAdserverURL() {
		return adserverURL;
	}

	/**
	 * Overrides the URL of ad server.
	 * 
	 * @param adserverURL
	 */
	public synchronized void setAdserverURL(String adserverURL) {
		if ((adserverURL != null) && (adserverURL.length() > 0)) {
			this.adserverURL = adserverURL;
		}
	}

	/**
	 * Optional. Set the browser user agent of the device making the request.
	 * 
	 * @param ua
	 * @return
	 */
	public AdRequest setUa(String ua) {
		if (ua != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_USER_AGENT, ua);
			}
		}
		return this;
	}

	/**
	 * Required. Set the id of the zone of publisher site.
	 * 
	 * @param zone
	 * @return
	 */
	public AdRequest setZone(String zone) {
		if (zone != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_ZONE, zone);
			}
		}
		return this;
	}

	/**
	 * Required. Set the adtype of the advertise.
	 * 
	 * @param adtype
	 * @return
	 */
	public AdRequest setAdtype(String adtype) {
		if (adtype != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_ADTYPE, adtype);
			}
		}
		return this;
	}

	/**
	 * Optional. Set Latitude.
	 * 
	 * @param latitude
	 * @return
	 */
	public AdRequest setLatitude(String latitude) {
		if (latitude != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_LATITUDE, latitude);
			}
		}
		return this;
	}

	/**
	 * Optional. Set Longitude.
	 * 
	 * @param longitude
	 * @return
	 */
	public AdRequest setLongitude(String longitude) {
		if (longitude != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_LONGITUDE, longitude);
			}
		}
		return this;
	}

	/**
	 * Optional. Set Background color in borders.
	 * 
	 * @param paramBG
	 * @return
	 */
	public AdRequest setParamBG(String paramBG) {
		if (paramBG != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_BACKGROUND, paramBG);
			}
		}
		return this;
	}

	/**
	 * Optional. Set Text color.
	 * 
	 * @param paramLINK
	 * @return
	 */
	public AdRequest setParamLINK(String paramLINK) {
		if (paramLINK != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_LINK, paramLINK);
			}
		}
		return this;
	}

	/**
	 * @deprecated
	 * Optional. Set minimum width of advertising.
	 * 
	 * @param minSizeX
	 * @return
	 */
	public AdRequest setMinSizeX(Integer minSizeX) {
		if ((minSizeX != null) && (minSizeX > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_MIN_SIZE_X, String.valueOf(minSizeX));
			}
		}
		return this;
	}

	/**
	 * @deprecated
	 * Optional. Set minimum height of advertising.
	 * 
	 * @param minSizeY
	 * @return
	 */
	public AdRequest setMinSizeY(Integer minSizeY) {
		if ((minSizeY != null) && (minSizeY > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_MIN_SIZE_Y, String.valueOf(minSizeY));
			}
		}
		return this;
	}

	/**
	 * @deprecated
	 * Optional. Set maximum width of advertising.
	 * 
	 * @param sizeX
	 * @return
	 */
	public AdRequest setSizeX(Integer sizeX) {
		if ((sizeX != null) && (sizeX > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_SIZE_X, String.valueOf(sizeX));
			}
		}
		return this;
	}

	/**
	 * @deprecated
	 * Optional. Set maximum height of advertising.
	 * 
	 * @param sizeY
	 * @return
	 */
	public AdRequest setSizeY(Integer sizeY) {
		if ((sizeY != null) && (sizeY > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_SIZE_Y, String.valueOf(sizeY));
			}
		}
		return this;
	}

	public AdRequest setHeight(Integer height) {
		if ((height != null) && (height > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_HEIGHT, String.valueOf(height));
			}
		}
		return this;
	}

	public Integer getHeight() {
		synchronized (parameters) {
			String height = parameters.get(PARAMETER_HEIGHT);
			return getIntParameter(height);
		}
	}
	
	public AdRequest setWidth(Integer width) {
		if ((width != null) && (width > 0)) {
			synchronized (parameters) {
				parameters.put(PARAMETER_WIDTH, String.valueOf(width));
			}
		}
		return this;
	}
	
	public Integer getWidth() {
		synchronized (parameters) {
			String width = parameters.get(PARAMETER_WIDTH);
			return getIntParameter(width);
		}
	}

	/**
	 * Optional. Set connection speed. 0 - low (gprs, edge), 1 - fast (3g,
	 * wifi).
	 * 
	 * @param connectionSpeed
	 * @return
	 */
	public AdRequest setConnectionSpeed(Integer connectionSpeed) {
		if (connectionSpeed != null) {
			synchronized (parameters) {
				parameters.put(PARAMETER_CONNECTION_SPEED, String.valueOf(connectionSpeed));
			}
		}
		return this;
	}

	public String getAdtype() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_ADTYPE);
		}
	}

	public String getUa() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_USER_AGENT);
		}
	}

	public String getZone() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_ZONE);
		}
	}

	public String getLatitude() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_LATITUDE);
		}
	}

	public String getLongitude() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_LONGITUDE);
		}
	}

	public String getParamBG() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_BACKGROUND);
		}
	}

	public String getParamLINK() {
		synchronized (parameters) {
			return parameters.get(PARAMETER_LINK);
		}
	}

	/**
	 * @deprecated
	 */
	public Integer getMinSizeX() {
		synchronized (parameters) {
			String minSizeX = parameters.get(PARAMETER_MIN_SIZE_X);
			return getIntParameter(minSizeX);
		}
	}

	/**
	 * @deprecated
	 */
	public Integer getMinSizeY() {
		synchronized (parameters) {
			String minSizeY = parameters.get(PARAMETER_MIN_SIZE_Y);
			return getIntParameter(minSizeY);
		}
	}

	/**
	 * @deprecated
	 */
	public Integer getSizeX() {
		synchronized (parameters) {
			String sizeX = parameters.get(PARAMETER_SIZE_X);
			return getIntParameter(sizeX);
		}
	}

	/**
	 * @deprecated
	 */
	public Integer getSizeY() {
		synchronized (parameters) {
			String sizeY = parameters.get(PARAMETER_SIZE_Y);
			return getIntParameter(sizeY);
		}
	}

	public Integer getConnectionSpeed() {
		synchronized (parameters) {
			String connectionSpeed = parameters.get(PARAMETER_CONNECTION_SPEED);
			return getIntParameter(connectionSpeed);
		}
	}

	/**
	 * Optional. Set Custom parameters.
	 * 
	 * @param customParameters
	 * @return
	 */
	public void setCustomParameters(Hashtable<String, String> customParameters) {
		this.customParameters = customParameters;
	}

	public Hashtable<String, String> getCustomParameters() {
		return customParameters;
	}

	private Integer getIntParameter(String stringValue) {
		if (stringValue != null) {
			return Integer.parseInt(stringValue);
		} else {
			return null;
		}
	}

	/**
	 * Creates URL with given parameters.
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             if all the required parameters are not present.
	 */
	public synchronized String createURL() throws IllegalStateException {
		return this.toString();
	}

	public synchronized String toString() {
		StringBuilder builderToString = new StringBuilder();
		String adserverURL = this.adserverURL + "?";
		builderToString.append(adserverURL);
		parameters.put("sdk", "android-v" + AdViewCore.VERSION);
		appendParameters(builderToString, parameters);
		appendParameters(builderToString, customParameters);
		
		return builderToString.toString();// builderToString.toString().equals(adserverURL)
											// ? this.adserverURL :
											// builderToString.toString();
	}

	private void appendParameters(StringBuilder builderToString, Map<String, String> parameters) {

		if (parameters != null) {
			synchronized (parameters) {
				Set<String> keySet = parameters.keySet();

				for (Iterator<String> parameterNames = keySet.iterator(); parameterNames.hasNext();) {
					String param = parameterNames.next();
					String value = parameters.get(param);

					if (value != null) {
						try {
							builderToString.append("&" + URLEncoder.encode(param, "UTF-8") + "="
									+ URLEncoder.encode(value, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
