package com.tapit.air.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.tapit.air.contexts.TapItContext;

public class RemoveTapItBanner implements FREFunction
{
	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{		
		TapItContext tapItContext = (TapItContext) context;
		
		if(tapItContext.bannerAd != null)
		{
			tapItContext.bannerAd.destroy();
			tapItContext.currentLayout.removeAllViews();
			tapItContext.bannerAd=null;
		}
        
        context.dispatchStatusEventAsync("BANNER_CLOSED", "CLOSED");

       return null;
	}
}
