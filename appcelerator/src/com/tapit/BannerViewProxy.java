package com.tapit;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;

@Kroll.proxy(creatableInModule = TapitandroidModule.class)
public class BannerViewProxy extends TiViewProxy {

	//private static final String LCAT = "TapItModule";

	public BannerViewProxy() {
		super();
	}

	@Override
	public TiUIView createView(Activity activity) {

		BannerView v = new BannerView(this);

		v.getLayoutParams().autoFillsHeight = true;
		v.getLayoutParams().autoFillsWidth = true;

		return v;
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options) {
		// This method is called from handleCreationArgs if there is at least
		// argument specified for the proxy creation call and the first argument
		// is a KrollDict object.

		//Log.d(LCAT, "VIEWPROXY LIFECYCLE EVENT] handleCreationDict " + options);

		// Calling the superclass method ensures that the properties specified
		// in the dictionary are properly set on the proxy object.
		super.handleCreationDict(options);
	}
	
	public void handleCreationArgs(KrollModule createdInModule, Object[] args) 
	{
		// This method is one of the initializers for the proxy class. The arguments
		// for the create call are passed as an array of objects. If your proxy 
		// simply needs to handle a single KrollDict argument, use handleCreationDict.
		// The superclass method calls the handleCreationDict if the first argument
		// to the create method is a dictionary object.

		//Log.d(LCAT, "VIEWPROXY LIFECYCLE EVENT] handleCreationArgs ");

		super.handleCreationArgs(createdInModule, args);
	}
	
	// Proxy properties are forwarded to the view in the propertyChanged
		// notification. If the property update needs to occur on the UI thread
		// then create the setProperty method in the proxy and forward to the view.


		/*
		@Kroll.setProperty(retain=false)
		public void setColor(final String color) 
		{
			Log.d(LCAT,"[VIEWPROXY LIFECYCLE EVENT] Property Set: setColor");

			// Get the view object from the proxy and set the color
			if (view != null) {
				if (!TiApplication.isUIThread()) {
					TiMessenger.sendBlockingMainMessage(new Handler(TiMessenger.getMainMessenger().getLooper(), new Handler.Callback() {
						public boolean handleMessage(Message msg) {
							switch (msg.what) {
								case MSG_SET_COLOR: {
									AsyncResult result = (AsyncResult) msg.obj;
									BannerView v = (BannerView)view;
									v.setColor(color);
									result.setResult(null);
									return true;
								}
							}
							return false;
						}
					}).obtainMessage(MSG_SET_COLOR), color);
				} else {
					BannerView v = (BannerView)view;
					v.setColor(color);
				}
			}

			// Call setProperty if you want the property set on the proxy and
			// to signal the propertyChanged notification
			setProperty("color", color, true);
		}*/

}
