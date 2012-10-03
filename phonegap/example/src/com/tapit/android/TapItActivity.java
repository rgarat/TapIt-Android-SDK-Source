package com.tapit.android;

import org.apache.cordova.DroidGap;
import android.os.Bundle;
import android.widget.LinearLayout;


import com.tapit.adview.AdView;

public class TapItActivity extends DroidGap {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");
		setupTapit();
	}
	
	/**
	 * Setup Tapit 
	 * */
	public void setupTapit(){
		AdView ad;
		ad= new AdView(this,"");
		ad.setId(2323);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		ad.setLayoutParams(layoutParams);
		LinearLayout layout = super.root;
		layout.addView(ad);
	}
}



