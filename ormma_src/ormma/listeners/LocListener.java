package com.tapit.adview.ormma.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.tapit.adview.ormma.OrmmaLocationController;

/**
 * The listener interface for receiving location events.
 * The class that is interested in processing a loc
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLocListener<code> method. When
 * the loc event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LocEvent
 */
public class LocListener implements LocationListener {

	/**
	 * The m ormma location controller.
	 */
	OrmmaLocationController mOrmmaLocationController;
	
	/**
	 * The m loc man.
	 */
	private LocationManager mLocMan;
	// private Location cLoc;
	/**
	 * The m provider.
	 */
	private String mProvider;

	// private long mInterval;

	/**
	 * Instantiates a new loc listener.
	 *
	 * @param c the c
	 * @param interval the interval
	 * @param ormmaLocationController the ormma location controller
	 * @param provider the provider
	 */
	public LocListener(Context c, int interval, OrmmaLocationController ormmaLocationController, String provider) {
		mOrmmaLocationController = ormmaLocationController;
		mLocMan = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		mProvider = provider;
		// mInterval = interval;
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	public void onProviderDisabled(String provider) {
		mOrmmaLocationController.fail();
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (status == 0) {
			mOrmmaLocationController.fail();
		}
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location) {
		mOrmmaLocationController.success(location);
	}

	/**
	 * Stop.
	 */
	public void stop() {
		mLocMan.removeUpdates(this);
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * Start.
	 */
	public void start() {
		mLocMan.requestLocationUpdates(mProvider, 0, 0, this);
	}

}
