package com.tapit.air.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.tapit.air.contexts.TapItContext;

public class TapItAirExtension implements FREExtension
{
	@Override
	public FREContext createContext(String contextType)
	{
		return new TapItContext();
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void initialize()
	{
	}
}
