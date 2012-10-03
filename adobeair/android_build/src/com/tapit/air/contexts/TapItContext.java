package com.tapit.air.contexts;

import java.util.HashMap;
import java.util.Map;
import android.widget.LinearLayout;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.tapit.air.functions.*;
import com.tapit.adview.AdView;
import com.tapit.adview.AlertAd;
import com.tapit.adview.AdFullscreenView;

public class TapItContext extends FREContext
{
	public AdView bannerAd;
	public AlertAd alertAd;
	public AdFullscreenView fullScreenAd;
	public LinearLayout currentLayout;
	
	@Override
	public void dispose()
	{
	}

	@Override
	public Map<String, FREFunction> getFunctions()
	{
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();
		functionMap.put("addBanner", new TapItBanner());
		functionMap.put("addAlert", new TapItAdAlert());
		functionMap.put("addFullScreen", new TapItFullScreen());
		functionMap.put("removeBanner", new RemoveTapItBanner());
		functionMap.put("removeFullScreen", new RemoveTapItFullScreen());
	    return functionMap;
	}
}
