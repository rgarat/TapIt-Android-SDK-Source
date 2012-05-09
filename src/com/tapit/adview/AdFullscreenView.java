package com.tapit.adview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Viewer of full screen advertising. Presents full screen advertising during
 * some time, cannot be closed before timeout expiration
 */
public class AdFullscreenView extends AdInterstitialBaseView {
	
	private ImageButton transparentButton;
	
	
	public AdFullscreenView(Context context, String zone) {
		super(context, zone);
		setAdtype("2");
	}

	@Override
	public View getInterstitialView(Context ctx) {
		removeViews();
		callingActivityContext = ctx;
		interstitialLayout = new RelativeLayout(ctx);
        final RelativeLayout.LayoutParams adViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        interstitialLayout.addView(this, adViewLayout);
        initNavigation();
		return interstitialLayout;
	}

    /**
     * add listeners so that when the screen is tapped, go/close buttons appear
     */
    protected void initNavigation() {
    	// cover screen in transparent button
    	ImageButton transparentButton = new ImageButton(context);
    	transparentButton.setBackgroundDrawable(null);
        RelativeLayout.LayoutParams buttonLayout = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.FILL_PARENT, 
        		RelativeLayout.LayoutParams.FILL_PARENT);
    	
        transparentButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			showNavigation();
    		}
    	});

        interstitialLayout.addView(transparentButton,buttonLayout);
    }

    protected void showNavigation() {
    	interstitialLayout.removeView(transparentButton);
    	Button myGoButton = new Button(context);
    	myGoButton.setText("See More");
    	myGoButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    	myGoButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			loadAdAction();
    		}
    	});
    	
    	Button myCloseButton = new Button(context);
    	myCloseButton.setText("Skip");
    	myCloseButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    	myCloseButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			closeInterstitial();
    		}
    	});

    	LinearLayout ll = new LinearLayout(context);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	ll.addView(myGoButton);
    	ll.addView(myCloseButton);
    	LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	
    	RelativeLayout rel = new RelativeLayout(context);
    	RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
    		    RelativeLayout.LayoutParams.FILL_PARENT, 
    		    RelativeLayout.LayoutParams.WRAP_CONTENT);
    	rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	
    	rel.addView(ll,rl);
    	interstitialLayout.addView(rel, llParams);
    }
    
    protected void loadAdAction() {
    	final String clickURL = this.getClickURL();
    	
    	loadUrl(clickURL);
    }
}
