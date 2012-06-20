package com.tapit.adview.notif;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.tapit.adview.AdLog;

public class TapIt {

	static final String NOTIF_ADV_ENABLED_PREF = "notif_adv_enabled";
	static final String ICON_RESOURCE_PREF = "icon_resource";
	static final String INTERVAL_PREF = "interval";
	static final String APP_ID_PREF = "app_id";
	static final String ZONE_PREF = "zone";
	static final String LAST_SCHEDULING_PREF = "last_scheduling";
	static final String LOG_LEVEL_PREF = "log_level";

	static final String TAG = "TapIt";

	static final int GET_ADSERVICE_REQUEST_CODE = 1;

	static final long DEFAULT_PERIOD = AlarmManager.INTERVAL_HALF_DAY;

	private Context context;

	private AlarmManager alarmManager;

	private PendingIntent operation;

	private AdLog adLog = new AdLog(this);

	/**
	 * Starts notification advertisement. To set up library right it is need to
	 * add service AdService into AndroidManifest.xml:
	 * 
	 * <pre>
	 * &lt;service android:name="com.tapit.adview.notif.AdService"&gt;
	 *    &lt;intent-filter&gt;
	 *       &lt;action android:name="com.tapit.adview.notif.AdService&lt;APP_ID&gt;" /&gt;
	 *    &lt;/intent-filter&gt;
	 * &lt;/service&gt;
	 * </pre>
	 * 
	 * replacing &lt;APP_ID&gt; by appId, when appId must be unique for all
	 * installed on phone applications.
	 * 
	 * <p>
	 * e.g. if your app has packagename "com.example.test" when you can replace
	 * &lt;APP_ID&gt; from AndroidManifest.xml by "com.example.test" and use
	 * {@link Context#getPackageName() getPackageName()} as appId.
	 * </p>
	 * 
	 * In this case action in intent-filter will be like this:
	 * 
	 * <pre>
	 * &lt;action android:name="com.tapit.adview.notif.AdServicecom.example.test" /&gt;
	 * </pre>
	 * 
	 * and constructor like this:
	 * 
	 * <pre>
	 * new TapIt(this, getPackageName(), false);
	 * </pre>
	 * 
	 * @param context
	 *            - context
	 * @param zone
	 *            - zone
	 * @param appId
	 *            -
	 * @param isStartFromBootReceiver
	 *            - if you instantiate this class from BootReceiver then set
	 *            true, otherwise false
	 */
	public TapIt(Context context, String zone, String appId, boolean isStartFromBootReceiver) {
		this(context, zone, appId, isStartFromBootReceiver, -1);
	}

	/**
	 * Starts notification advertisement. To set up library right it is need to
	 * add service AdService into AndroidManifest.xml:
	 * 
	 * <pre>
	 * &lt;service android:name="com.tapit.adview.notif.AdService"&gt;
	 *    &lt;intent-filter&gt;
	 *       &lt;action android:name="com.tapit.adview.notif.AdService&lt;APP_ID&gt;" /&gt;
	 *    &lt;/intent-filter&gt;
	 * &lt;/service&gt;
	 * </pre>
	 * 
	 * replacing &lt;APP_ID&gt; by appId, when appId must be unique for all
	 * installed on phone applications.
	 * 
	 * <p>
	 * e.g. if your app has packagename "com.example.test" when you can replace
	 * &lt;APP_ID&gt; from AndroidManifest.xml by "com.example.test" and use
	 * {@link Context#getPackageName() getPackageName()} as appId.
	 * </p>
	 * 
	 * In this case action in intent-filter will be like this:
	 * 
	 * <pre>
	 * &lt;action android:name="com.tapit.adview.notif.AdServicecom.example.test" /&gt;
	 * </pre>
	 * 
	 * and constructor like this:
	 * 
	 * <pre>
	 * new TapIt(this, getPackageName(), false);
	 * </pre>
	 *
	 * As logLever you can set one of these parameters:<br>
	 * 
	 * {@link com.tapit.adview.AdLog#LOG_LEVEL_NONE}<br>
	 * {@link com.tapit.adview.AdLog#LOG_LEVEL_1}<br>
	 * {@link com.tapit.adview.AdLog#LOG_LEVEL_2}<br>
	 * {@link com.tapit.adview.AdLog#LOG_LEVEL_3}<br> 
	 *
	 * @param context
	 *            - context
	 * @param zone
	 *            - zone
	 * @param appId
	 *            -
	 * @param isStartFromBootReceiver
	 *            - if you instantiate this class from BootReceiver then set
	 *            true, otherwise false
	 * @param logLever
	 *            - set logLevel for all operations in this class
	 */
	public TapIt(Context context, String zone, String appId, boolean isStartFromBootReceiver,
			int logLevel) {
		this.context = context;


		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(APP_ID_PREF, appId);
		editor.putString(ZONE_PREF, zone);
		editor.commit();
		
		if (logLevel >= 0) {
			adLog.setLogLevel(logLevel);
			editor.putInt(LOG_LEVEL_PREF, logLevel);
			editor.commit();
		}


		long lastScheduling = preferences.getLong(LAST_SCHEDULING_PREF, 0);
		if (lastScheduling == 0)
			isStartFromBootReceiver = true;
		long interval = preferences.getLong(INTERVAL_PREF, DEFAULT_PERIOD);
		System.err.println("interval is " + interval);
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		operation = PendingIntent.getService(context, GET_ADSERVICE_REQUEST_CODE, new Intent(
				"com.tapit.adview.notif.AdService" + appId), PendingIntent.FLAG_UPDATE_CURRENT);
		if (operation != null
				&& (lastScheduling + interval < SystemClock.elapsedRealtime() || isStartFromBootReceiver)) {
			alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
					+ interval, operation);
			adLog.log(AdLog.LOG_LEVEL_3, AdLog.LOG_TYPE_INFO, "TapIt",
					"Set next alarm after interval: " + String.valueOf(interval));
			editor.putLong(LAST_SCHEDULING_PREF, SystemClock.elapsedRealtime());
			editor.commit();
		} else {
			adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_ERROR, "TapIt",
					"Can't set next alarm because operation is null");
		}

		if (!preferences.contains(NOTIF_ADV_ENABLED_PREF)) {
			setEnabled(true);
		}
	}

	/**
	 * Enable or disable notification advertisement
	 */
	public void setEnabled(boolean b) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean(NOTIF_ADV_ENABLED_PREF, b);
		editor.commit();
	}

	/**
	 * Can get status about notification advertisement
	 * 
	 * @return true if notification advertisement is enable, false in other case
	 */
	public boolean isEnabled() {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				NOTIF_ADV_ENABLED_PREF, false);
	}

	/**
	 * Put this id of your custom icon. Or put 0, if you want use default icon.
	 * 
	 * @param iconResourceId
	 */
	public void setIconResource(int iconResourceId) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putInt(ICON_RESOURCE_PREF, iconResourceId);
		editor.commit();
	}

	/**
	 * Set update interval
	 * 
	 * @param interval
	 *            in seconds
	 */
	public void setUpdateInterval(int interval) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putLong(INTERVAL_PREF, interval * 1000);
		editor.commit();

		if (operation != null) {
			alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
					+ interval * 1000, operation);
			adLog.log(AdLog.LOG_LEVEL_3, AdLog.LOG_TYPE_INFO, "TapIt",
					"Set next alarm after interval: " + String.valueOf(interval));
			editor.putLong(LAST_SCHEDULING_PREF, SystemClock.elapsedRealtime());
			editor.commit();
		}
	}

	/**
	 * Get updater interval
	 * 
	 * @return interval in seconds
	 */
	public int getUpdateInterval() {
		return (int) (PreferenceManager.getDefaultSharedPreferences(context).getLong(INTERVAL_PREF,
				DEFAULT_PERIOD) / 1000);
	}
}
