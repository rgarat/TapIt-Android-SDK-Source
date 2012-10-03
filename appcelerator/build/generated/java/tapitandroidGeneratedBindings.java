package com.tapit;

import java.util.HashMap;

import org.appcelerator.kroll.runtime.rhino.KrollBindings;

public class tapitandroidGeneratedBindings
{
	public static void init()
	{
		KrollBindings.addExternalBinding("com.tapit.AlertAdProxy", com.tapit.AlertAdProxyPrototype.class);
		KrollBindings.addExternalBinding("com.tapit.TapitandroidModule", com.tapit.TapitandroidModulePrototype.class);
		KrollBindings.addExternalBinding("com.tapit.BannerViewProxy", com.tapit.BannerViewProxyPrototype.class);
		KrollBindings.addExternalBinding("com.tapit.InterstitialAdProxy", com.tapit.InterstitialAdProxyPrototype.class);
	}
}