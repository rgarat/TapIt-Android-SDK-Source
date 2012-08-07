package com.tapit.adview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnAdClickListener;

public abstract class AdInterstitialBaseView extends AdView implements OnAdDownload, OnAdClickListener {

	public enum FullscreenAdSize {
		AUTOSIZE_AD		( -1,  -1),
		MEDIUM_RECTANGLE(300, 250);
		
		public final int width;
		public final int height;
		FullscreenAdSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
	
	
	protected final Context context;
	protected Context callingActivityContext;
	protected RelativeLayout interstitialLayout;
	protected boolean isLoaded = false;
	protected OnInterstitialAdDownload interstitialListener;
	
	
	public AdInterstitialBaseView(Context ctx, String zone) {
		super(ctx, zone);
		context = ctx;
		setAdSize(FullscreenAdSize.AUTOSIZE_AD); // default to auto-sizing banner
		setOnAdDownload(this);
		setOnAdClickListener(this);
		setUpdateTime(0); // disable add cycling

	}

	public void setAdSize(FullscreenAdSize adSize) {
		int width = adSize.width;
		int height = adSize.height;
		
		if(width <= 0) {
			Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
			int screenHeight = display.getHeight();
			int screenWidth = display.getWidth();

			int adWidth = getWidth();
			if(adWidth <= 0) {
				adWidth = screenWidth;
			}
			int adHeight = getHeight();
			if(adHeight <= 0) {
				adHeight = screenHeight;
			}
			for(FullscreenAdSize fsas : FullscreenAdSize.values()) {
				if(fsas.width <= adWidth && fsas.height <= adHeight 
						&& (fsas.width > width || fsas.height > height)) {
					width = fsas.width;
					height = fsas.height;
				}
			}
		}

		if ((adRequest != null)) {
			adRequest.setHeight(height);
			adRequest.setWidth(width);
		}
	}
	
	public abstract View getInterstitialView(Context ctx);
	
	protected void removeViews() {
		RelativeLayout parent = (RelativeLayout)this.getParent();
		if(parent != null) {
			(parent).removeAllViews();
		}
	}

	
	public void closeInterstitial() {
		final AdInterstitialBaseView adView = this;
		handler.post(new Runnable() {
			@Override
			public void run() {
				if(callingActivityContext == null) {
					// interstitial was never displayed; nothing to do here...
					return;
				}
				((Activity)callingActivityContext).finish();
				if(interstitialListener != null) {
					interstitialListener.didClose(adView);
				}
				
				removeViews();
			}

		});

		
	}

	public boolean isLoaded() {
		return isLoaded;
	}
	
	public void load() {
		update(true);
	}
	
	public void showInterstitial() {
    	if(interstitialListener != null) {
    		interstitialListener.willOpen(this);
    	}
		AdActivity.adView = this;
        Intent i = new Intent(context, AdActivity.class);
        ((Activity)context).startActivityForResult(i,1);
	}

	/**
	 * This event is fired before banner download begins.
	 */
	public void begin(AdViewCore adView) {
		isLoaded = false;
		if(interstitialListener != null) {
			interstitialListener.willLoad(adView);
		}
	}

	/**
	 * This event is fired after banner content fully downloaded.
	 */
	public void end(AdViewCore adView) {
		isLoaded = true;
		if(interstitialListener != null) {
			interstitialListener.ready(adView);
		}
	}

	/**
	 * This event is fired after fail to download content.
	 */
	public void error(AdViewCore adView, String error) {
		if(interstitialListener != null) {
			interstitialListener.error(adView, error);
		}
	}
	
	public void click(String url) {
		Log.d("NickTest", "AdInterstitialBaseView.click(" + url + ")");
		if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    	Activity thisActivity = ((Activity)callingActivityContext);
	    	thisActivity.startActivityForResult(intent,2);
	    	closeInterstitial();
		}
		else {
			loadUrl(url);
		}
	}
	
	public void setOnInterstitialAdDownload(OnInterstitialAdDownload listener) {
		interstitialListener = listener;
	}
	
	/**
	 * setUpdateTime(Integer) is not supported in AdFullscreenView
	 */
	@Override
	public final void setUpdateTime(int updateTime) {
		// not supported for interstitials
	}
	
	
	/**
	 * called once the interstitial action is full-screened
	 */
	public void interstitialShowing() {
		// no-op
	}
	
	/**
	 * called once the interstitial action is full-screened
	 */
	public void interstitialClosing() {
		// no-op
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		// Close interstitial properly on back button press
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	closeInterstitial();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Allows lookup of resource id's from jars at runtime
	 * http://stackoverflow.com/questions/1995004/packaging-android-resource-files-within-a-distributable-jar-file/2825174#7117422
	 * @param packageName the package name of your app. e.g. context.getPackageName()
	 * @param className e.g. layout, string, drawable
	 * @param name the name of the resource you're looking for
	 * @return the id of the resource
	 */
	public static int getResourceIdByName(String packageName, String className, String name) {
	   Class<?> r = null;
	   int id = 0;
	   try {
		   r = Class.forName(packageName + ".R");

		   Class<?>[] classes = r.getClasses();
		   Class<?> desiredClass = null;

		   for (int i = 0; i < classes.length; i++) {
			   if(classes[i].getName().split("\\$")[1].equals(className)) {
				   desiredClass = classes[i];

				   break;
			   }
		   }

		   if(desiredClass != null)
			   id = desiredClass.getField(name).getInt(desiredClass);
	   } catch (ClassNotFoundException e) {
		   e.printStackTrace();
	   } catch (IllegalArgumentException e) {
		   e.printStackTrace();
	   } catch (SecurityException e) {
		   e.printStackTrace();
	   } catch (IllegalAccessException e) {
		   e.printStackTrace();
	   } catch (NoSuchFieldException e) {
		   e.printStackTrace();
	   }

	   return id;
	}
}
