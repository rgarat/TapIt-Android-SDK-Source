package com.tapit.test;

import com.tapit.adview.notif.TapIt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @hide
 * @author synergy
 *
 */
public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		new TapIt(context, "2", "01010", true);
	}
}
