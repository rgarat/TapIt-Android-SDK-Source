package com.tapit.adview;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Viewer of interstitial advertising.
 */
public class AdInterstitialView extends AdView {
	private Integer showCloseButtonTime;
	private Integer autoCloseInterstitialTime;
	private Boolean isShowPhoneStatusBar;
	private Button closeButton;

	private Dialog dialog;

	public AdInterstitialView(Context context, String zone) {
		super(context, zone);
		setAdtype("2");
	}

	/**
	 * Show interstitial ad.
	 */
	public void show() {
		openInterstitialForm(getContext(), showCloseButtonTime,
				autoCloseInterstitialTime, isShowPhoneStatusBar, this, closeButton);
	}

	private Button tempCloseButton;

	private Runnable closeDialogRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				if (dialog != null) {
					dialog.dismiss();
					destroy();
				}
			} catch (Exception e) {

			}
		}
	};

	private void openInterstitialForm(Context context,
			Integer showCloseButtonTime, Integer autoCloseInterstitialTime,
			Boolean isShowPhoneStatusBar, AdView adView, Button closeButton) {
		if ((showCloseButtonTime == null) || (showCloseButtonTime < 0)) {
			showCloseButtonTime = 0;
		}
		if ((autoCloseInterstitialTime == null) || (autoCloseInterstitialTime < 0)) {
			autoCloseInterstitialTime = 0;
		}
		if (isShowPhoneStatusBar == null) {
			isShowPhoneStatusBar = true;
		}

		tempCloseButton = closeButton;

		// show dialog
		final Dialog dialog;

		if (isShowPhoneStatusBar) {
			dialog = new Dialog(context, android.R.style.Theme_NoTitleBar);
		} else {
			dialog = new Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
		}

		((AdInterstitialView) adView).dialog = dialog;

		dialog.setCancelable(false);

		if (adView.getParent() != null) {
			((ViewGroup) adView.getParent()).removeAllViews();
		}

		RelativeLayout mainLayout = new RelativeLayout(context);
		mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		adView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		mainLayout.addView(adView);

		if (tempCloseButton == null) {
			tempCloseButton = new Button(context);
			tempCloseButton.setText("Close");
			RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			closeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			tempCloseButton.setLayoutParams(closeLayoutParams);
		}
		tempCloseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				destroy();
			}
		});
		mainLayout.addView(tempCloseButton);

		Handler handler = new Handler();

		if (showCloseButtonTime <= 0) {
			tempCloseButton.setVisibility(View.VISIBLE);
		} else {
			tempCloseButton.setVisibility(View.INVISIBLE);

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (tempCloseButton != null) {
						tempCloseButton.setVisibility(View.VISIBLE);
					}
				}
			}, showCloseButtonTime * 1000);
		}

		if (autoCloseInterstitialTime > 0) {
			handler.postDelayed(closeDialogRunnable, autoCloseInterstitialTime * 1000);
		}

		dialog.setContentView(mainLayout);
		dialog.show();
	}

	@Override
	protected void interstitialClose() {
		handler.removeCallbacks(closeDialogRunnable);
		handler.post(closeDialogRunnable);
	}

	/**
	 * Get show close button after delay.
	 * 
	 * @return time in sec 
	 */
	public Integer getShowCloseButtonTime() {
		return showCloseButtonTime;
	}

	/**
	 * Set show close button after delay.
	 * 
	 * @param showCloseButtonTime
	 */
	public void setShowCloseButtonTime(Integer showCloseButtonTime) {
		this.showCloseButtonTime = showCloseButtonTime;
	}

	/**
	 * get auto-close interstitial time.
	 * 
	 * @return auto close time in sec.
	 */
	public Integer getAutoCloseInterstitialTime() {
		return autoCloseInterstitialTime;
	}

	/**
	 * Set auto-close interstitial time.
	 * 
	 * @param autoCloseInterstitialTime
	 */
	public void setAutoCloseInterstitialTime(Integer autoCloseInterstitialTime) {
		this.autoCloseInterstitialTime = autoCloseInterstitialTime;
	}

	/**
	 * Get whether to show Phone Status Bar or not.
	 * 
	 * @return true if status bar showing enabled 
	 */
	public Boolean getIsShowPhoneStatusBar() {
		return isShowPhoneStatusBar;
	}

	/**
	 * Set whether to show Phone Status Bar or not.
	 * 
	 * @param isShowPhoneStatusBar
	 */
	public void setIsShowPhoneStatusBar(Boolean isShowPhoneStatusBar) {
		this.isShowPhoneStatusBar = isShowPhoneStatusBar;
	}

	/**
	 * Get Object for customization close button view.
	 * 
	 * @return close button
	 */
	public Button getCloseButton() {
		return closeButton;
	}

	/**
	 * Set Object for customization close button view.
	 * 
	 * @param closeButton
	 */
	public void setCloseButton(Button closeButton) {
		this.closeButton = closeButton;
	}

	protected String wrapToHTML(String data, String bridgeScriptPath, String scriptPath){
		return "<html><head>"
			+ "<meta name='viewport' content='user-scalable=no initial-scale=1.0' />"
			+ "<title>Advertisement</title> " 
//			+ "<script src=\"file:/" + bridgeScriptPath + "\" type=\"text/javascript\"></script>"
//			+ "<script src=\"file:/" + scriptPath + "\" type=\"text/javascript\"></script>"
			+ "</head>"
			+ "<body style=\"margin:0; padding:0; overflow:hidden; background-color:transparent;\">"
			+ "<table height=\"100%\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"text-align:center;vertical-align:middle;\">"
			+ data 
			+ "</td></tr></table>"
			+ "</body> "
			+ "</html> ";
	}
}
