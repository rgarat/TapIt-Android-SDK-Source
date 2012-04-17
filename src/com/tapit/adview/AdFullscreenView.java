package com.tapit.adview;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Viewer of full screen advertising. Presents full screen advertising during
 * some time, cannot be closed before timeout expiration
 */
public class AdFullscreenView extends AdView {
	
	public static final String TAG = "AdFullscreenView";	
	
	private Integer autoCloseTime;
	private Boolean isShowPhoneStatusBar;

	private Dialog dialog;
	
	private Handler handler = new Handler();
	
	private ProgressBar bar;
	
//	private TextView countdownTimer;
//	private RelativeLayout.LayoutParams countdownTimerParams;

	public AdFullscreenView(Context context, String zone) {
		super(context, zone);
		setAdtype("2");
	}

	/**
	 * Show fullscreen ad.
	 */
	public void show() {
		openFullscreenForm(getContext(), autoCloseTime, isShowPhoneStatusBar, this);
	}

	private Runnable closeDialogRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				if (dialog != null) {
					dialog.dismiss();
					destroy();
					Log.d(TAG, "destroy();");
				}
			} catch (Exception e) {

			}
		}
	};
	
	private Runnable countdownRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				if (dialog != null) {
					autoCloseTime -= 50;
//					countdownTimer.setText(autoCloseTime.toString());
					bar.setProgress(bar.getMax() - autoCloseTime);
					if (autoCloseTime > 0 && dialog.isShowing()){
						handler.postDelayed(countdownRunnable, 50);
					}
//					Log.d(TAG, "countdown: " + autoCloseTime.toString());
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	};

	private void openFullscreenForm(Context context, Integer autoCloseTime,
			Boolean isShowPhoneStatusBar, AdView adView) {
		if ((autoCloseTime == null) || (autoCloseTime < 0)) {
			autoCloseTime = 0;
		}
		if (isShowPhoneStatusBar == null) {
			isShowPhoneStatusBar = true;
		}

		// show dialog
		final Dialog dialog;

		if (isShowPhoneStatusBar) {
			dialog = new LockedOrientationDialog(context, android.R.style.Theme_NoTitleBar);
		} else {
			dialog = new LockedOrientationDialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
		}

		this.dialog = dialog;

		dialog.setCancelable(false);

		// FIXME: strange code:
		if (adView.getParent() != null) {
			((ViewGroup) adView.getParent()).removeAllViews();
		}
		
		super.setUpdateTime(0);

		RelativeLayout mainLayout = new RelativeLayout(context);
		mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		adView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		mainLayout.addView(adView);


//		countdownTimer = new TextView(context);
//		countdownTimer.setText(autoCloseTime.toString());
//		
//		if (countdownTimerParams == null){
//			countdownTimerParams = new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.WRAP_CONTENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT);
//			
//			countdownTimerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//			countdownTimerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//			int margin = (int) (10 * context.getResources().getDisplayMetrics().density);
//			countdownTimerParams.topMargin = margin;
//			countdownTimerParams.rightMargin = margin;
//		}
//		countdownTimer.setLayoutParams(countdownTimerParams);
		
		bar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		bar.setMax(autoCloseTime);
		
		RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		barParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		bar.setLayoutParams(barParams);
		
//		mainLayout.addView(countdownTimer);
		mainLayout.addView(bar);

		dialog.setContentView(mainLayout);
		dialog.show();
		
		adView.setOnAdDownload(new OnAdDownload() {

			@Override
			public void begin() {
				// noop
			}

			@Override
			public void end() {
				adLog.log(AdLog.LOG_LEVEL_3, AdLog.LOG_TYPE_INFO, "openFullscreenForm", "Displaying ad");
				Integer autoCloseTime = getAutoCloseTime();
				if (autoCloseTime > 0) {
					handler.postDelayed(closeDialogRunnable, autoCloseTime * 1000);
					handler.postDelayed(countdownRunnable, 50);
				}
			}

			@Override
			public void error(String error) {
				adLog.log(AdLog.LOG_LEVEL_1, AdLog.LOG_TYPE_WARNING, "openFullscreenForm", error);
				handler.post(closeDialogRunnable);
			}
			
		});
	}

	/**
	 * setUpdateTime(Integer) is not supported in AdFullscreenView
	 */
	@Override
	public void setUpdateTime(int updateTime) {
//		if (getUpdateTime() == 0 || updateTime != 0)
//			throw new UnsupportedOperationException("setUpdateTime(Integer) is not supported in AdFullscreenView");

		super.setUpdateTime(0);
	}

	@Override
	protected void interstitialClose() {
		handler.removeCallbacks(closeDialogRunnable);
		handler.removeCallbacks(countdownRunnable);
		handler.post(closeDialogRunnable);
	}
	
	/**
	 * get auto-close time.
	 * 
	 * @return auto close time in sec.
	 */
	public Integer getAutoCloseTime() {
		return autoCloseTime / 1000;
	}

	/**
	 * Set auto-close time.
	 * 
	 * @param autoCloseTime in sec.
	 */
	public void setAutoCloseTime(Integer autoCloseTime) {
		this.autoCloseTime = autoCloseTime * 1000;
	}

	/**
	 * Get whether to show phone status bar or not.
	 * 
	 * @return true if status bar showing is enabled
	 */
	public Boolean getIsShowPhoneStatusBar() {
		return isShowPhoneStatusBar;
	}
	
//	/**
//	 * Set RelativeLayout.LayoutParams layout params for countdown timer.
//	 * 
//	 * @param countdownTimerParams
//	 */
//	public void setCountdownTimerParams(RelativeLayout.LayoutParams countdownTimerParams) {
//		this.countdownTimerParams = countdownTimerParams;
//	}
//
//	/**
//	 * Get RelativeLayout.LayoutParams layout params for countdown timer.
//	 * 
//	 * @return
//	 */
//	public RelativeLayout.LayoutParams getCountdownTimerParams() {
//		return countdownTimerParams;
//	}

	/**
	 * Set whether to show Phone Status Bar or not.
	 * 
	 * @param isShowPhoneStatusBar
	 */
	public void setIsShowPhoneStatusBar(Boolean isShowPhoneStatusBar) {
		this.isShowPhoneStatusBar = isShowPhoneStatusBar;
	}

	protected String wrapToHTML(String data, String bridgeScriptPath, String scriptPath){
		return "<html><head>"
			+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />"
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
