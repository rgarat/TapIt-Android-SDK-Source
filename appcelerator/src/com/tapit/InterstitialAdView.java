package com.tapit;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

public class InterstitialAdView extends TiUIView {
	
	private static final String LCAT = "TapItModule";

	public InterstitialAdView(TiViewProxy proxy) {
		super(proxy);
		Log.d(LCAT, "Init real InterstitialAdView...");
	}

}
