package com.tapit.adview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * Viewer of interstitial advertising.
 */
public class AdInterstitialView extends AdInterstitialBaseView {
	protected static final float CLOSE_BUTTON_SIZE_DP = 50.0f;
	protected static final float CLOSE_BUTTON_PADDING_DP = 8.0f;

	protected ImageButton closeButton;
	
	public AdInterstitialView(Context context, String zone) {
		super(context, zone);
		setAdtype("2");
	}

	@Override
	public View getInterstitialView(Context ctx) {
		callingActivityContext = ctx;
		interstitialLayout = new RelativeLayout(ctx);
        final RelativeLayout.LayoutParams adViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        interstitialLayout.addView(this, adViewLayout);
        showInterstitialCloseButton();
		return interstitialLayout;
	}
	
    protected void showInterstitialCloseButton() {
        StateListDrawable states = new StateListDrawable();

        try {
            states.addState(new int[] {-android.R.attr.state_pressed}, getResources().getDrawable(android.R.drawable.ic_notification_clear_all));
//	        states.addState(new int[] {-android.R.attr.state_pressed},
//	                getResources().getDrawable(getResourceIdByName(context.getPackageName(), "drawable", "tapit_interstitial_close_button_normal")));
//	        states.addState(new int[] {android.R.attr.state_pressed},
//	                getResources().getDrawable(getResourceIdByName(context.getPackageName(), "drawable", "tapit_interstitial_close_button_pressed")));
        } catch(RuntimeException e) {
        	e.printStackTrace();
        }
        closeButton = new ImageButton(context);
        closeButton.setImageDrawable(states);
        closeButton.setBackgroundDrawable(null);
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	closeInterstitial();
            }
        });
        
        final float scale = getResources().getDisplayMetrics().density;
        int buttonSize = (int) (CLOSE_BUTTON_SIZE_DP * scale + 0.5f);
        int buttonPadding = (int) (CLOSE_BUTTON_PADDING_DP * scale + 0.5f);
        RelativeLayout.LayoutParams buttonLayout = new RelativeLayout.LayoutParams(
                buttonSize, buttonSize);
        buttonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonLayout.setMargins(buttonPadding, 0, buttonPadding, 0);
        interstitialLayout.removeView(closeButton);
        interstitialLayout.addView(closeButton, buttonLayout);
    }

    @Override
	public void click(String url) {
    	closeButton.setVisibility(GONE);
    	super.click(url);
    }
	
	@Override
	public void end(AdViewCore adView) {
		super.end(adView);
	}
}
