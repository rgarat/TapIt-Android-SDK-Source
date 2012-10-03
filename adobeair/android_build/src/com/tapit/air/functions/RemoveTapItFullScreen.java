package com.tapit.air.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.tapit.air.contexts.TapItContext;

public class RemoveTapItFullScreen implements FREFunction
{
	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{		
		TapItContext tapItContext = (TapItContext) context;
		
		if(tapItContext.fullScreenAd != null)
		{
			tapItContext.fullScreenAd.destroy();
			tapItContext.fullScreenAd=null;
		}
        
        context.dispatchStatusEventAsync("FULLSCREEN_CLOSED", "CLOSED");

       return null;
	}
}
