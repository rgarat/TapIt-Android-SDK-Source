package com.tapit;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import org.appcelerator.kroll.common.Log;

import com.tapit.adview.AdView;

public class BannerView extends TiUIView {

	private static final String LCAT = "TapItModule";
	private static final String PROPERTY_COLOR = "color";
	private static final String PROPERTY_ADZONE = "adzone";
	
	private AdView bannerAd;

	public BannerView(TiViewProxy proxy) {
		super(proxy);

		Log.d(LCAT, "[VIEW LIFECYCLE EVENT] view");

		//TiCompositeLayout view = new TiCompositeLayout(proxy.getActivity());
		
		//bannerAd = new AdView(TiApplication.getInstance(), proxy.getActivity());
		
		this.bannerAd = new AdView(proxy.getActivity(),"");
		//bannerAd.setZone("7979");

		setNativeView(this.bannerAd);
		
	}

	@Override
	public void processProperties(KrollDict props) {
		super.processProperties(props);

		Log.d(LCAT, "[VIEW LIFECYCLE EVENT] processProperties " + props);

		if (props.containsKey(PROPERTY_COLOR)) {
			/*View square = getNativeView();

			square.setBackgroundColor(TiConvert.toColor(props, PROPERTY_COLOR));
			notifyOfColorChange(props.getString(PROPERTY_COLOR));
			square.invalidate();*/

		}
		else if (props.containsKey(PROPERTY_ADZONE)) {
			AdView v = (AdView)getNativeView();
			v.setZone(props.getString(PROPERTY_ADZONE));
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue,
			KrollProxy proxy) {

		super.propertyChanged(key, oldValue, newValue, proxy);

		Log.d(LCAT, "[VIEW LIFECYCLE EVENT] propertyChanged: " + key + ' '
				+ oldValue + ' ' + newValue);
	}	

}
