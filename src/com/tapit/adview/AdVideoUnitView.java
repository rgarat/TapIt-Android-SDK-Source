package com.tapit.adview;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/*
 * how to handle change orientation:
 * http://stackoverflow.com/questions/3821423/background-task-progress-dialog-orientation-change-is-there-any-100-working/3821998#3821998
 * http://stackoverflow.com/questions/151777/how-do-i-save-an-android-applications-state
 * http://stackoverflow.com/questions/3611457/android-temporarily-disable-orientation-changes-in-an-activity
 * 
 * lock orientation:
 * http://eigo.co.uk/Lock-Screen-Orientation-in-Android.aspx
 */

/**
 * Viewer of video advertising. Presents full screen video advertising during
 * some time, cannot be closed before timeout expiration
 */
public class AdVideoUnitView extends AdView {
	
	public static final String TAG = "AdVideoUnitView";	
	
	private Integer autoCloseTime;
	private Boolean isShowPhoneStatusBar;

	private Dialog dialog;
	
	private Handler handler = new Handler();
	
	private ProgressBar bar;

	private VideoView player;
	
//	private TextView countdownTimer;
//	private RelativeLayout.LayoutParams countdownTimerParams;
	
	private Button closeButton;
	private Button goToWebButton;
	
	private String closeButtonText;

	private String goToSiteButtonText;


	public AdVideoUnitView(Context context, String zone) {
		super(context, zone);
		setAdtype("6");
	}

	/**
	 * Show video ad.
	 */
	public void show() {
		openVideoUnit(getContext(), autoCloseTime, isShowPhoneStatusBar, this);
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
					autoCloseTime--;
//					countdownTimer.setText(autoCloseTime.toString());
					bar.setSecondaryProgress(bar.getMax() - autoCloseTime*1000);
					if (autoCloseTime > 0 && dialog.isShowing()){
						handler.postDelayed(countdownRunnable, 1000);
					}
					if (autoCloseTime <= 0){
						showButtons();
					}
//					Log.d(TAG, "countdown: " + autoCloseTime.toString());
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	};
	
	private Runnable seekRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				if (dialog != null) {
					bar.setProgress(bar.getMax()*player.getCurrentPosition()/player.getDuration());
					if (autoCloseTime > 0 && dialog.isShowing()){
						handler.postDelayed(seekRunnable, 50);
					}
					//Log.d(TAG, "seekRunnable: " + bar.getSecondaryProgress());
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	};
	
	private Runnable showButtonsRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (dialog == null) 
				return;
			closeButton.setVisibility(View.VISIBLE);
			goToWebButton.setVisibility(View.VISIBLE);			
		}
	};

	private void openVideoUnit(Context context, Integer autoCloseFullscreenTime,
			Boolean isShowPhoneStatusBar, AdView adView) {
		if ((autoCloseFullscreenTime == null) || (autoCloseFullscreenTime < 0)) {
			autoCloseFullscreenTime = 0;
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

		((AdVideoUnitView) adView).dialog = dialog;

		dialog.setCancelable(false);

		if (adView.getParent() != null) {
			((ViewGroup) adView.getParent()).removeAllViews();
		}
		
		adView.setUpdateTime(0);
		adView.update(true);

		RelativeLayout mainLayout = new RelativeLayout(context);
		mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//		adView.setLayoutParams(new ViewGroup.LayoutParams(
//				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


		// create CountdownTimer
//		countdownTimer = new TextView(context);
//		countdownTimer.setText(autoCloseFullscreenTime.toString());
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
		
		// create ProgressBar
		final int barId = 10505;
		bar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		bar.setMax(autoCloseFullscreenTime*1000);
		bar.setId(barId);
		
		RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		barParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		bar.setLayoutParams(barParams);
		
		if (autoCloseFullscreenTime > 0) {
//			handler.postDelayed(closeDialogRunnable, autoCloseFullscreenTime * 1000);
			handler.postDelayed(countdownRunnable, 0);
			handler.post(seekRunnable);
		}
		
		// create VideoPlayer
		player = new VideoView(context);
		
		RelativeLayout.LayoutParams lp;
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		player.setLayoutParams(lp);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				showButtons();
			}
		});

		// create LinearLayout for buttons
		LinearLayout buttonsLayout = new LinearLayout(context);
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ABOVE, barId);
		buttonsLayout.setLayoutParams(lp);
		
		// create close Button
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		
		closeButton = new Button(context);
		closeButton.setLayoutParams(llp);
		closeButton.setText(closeButtonText != null ? closeButtonText : "Close");
		buttonsLayout.addView(closeButton);
		closeButton.setVisibility(View.GONE);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				interstitialClose();
			}
		});
		
		goToWebButton = new Button(context);
		goToWebButton.setLayoutParams(llp);
		goToWebButton.setText(goToSiteButtonText != null ? goToSiteButtonText : "Go To Site");
		buttonsLayout.addView(goToWebButton);
		goToWebButton.setVisibility(View.GONE);
		
		// add views
		mainLayout.addView(player);
//		mainLayout.addView(countdownTimer);
		mainLayout.addView(bar);
		mainLayout.addView(buttonsLayout);
		
		dialog.setContentView(mainLayout);
		dialog.show();		
	}
	
	/**
	 * Play video
	 * 
	 * @param url
	 *            - video URL
	 * @param audioMuted
	 *            - should audio be muted
	 * @param autoPlay
	 *            - should video play immediately
	 * @param controls
	 *            - should native player controls be visible
	 * @param loop
	 *            - should video start over again after finishing
	 * @param d
	 *            - inline area dimensions
	 * @param startStyle
	 *            - normal/fullscreen; full screen if video should play in full
	 *            screen
	 * @param stopStyle
	 *            - normal/exit; exit if video should exit after video stops
	 */
	@Override
	public void playVideo(final String url, final String clickUrl, boolean audioMuted, boolean autoPlay,
			boolean controls, boolean loop, Dimensions d, String startStyle, String stopStyle) {
//		Log.v("Nick", "playVid: " + url);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
//				PlayerProperties properties = new PlayerProperties();
//				properties.setProperties(false, true, false, false, false,
//						"fullscreen", "exit");
				
				player.setVideoURI(Uri.parse(url));
				player.start();
				
				goToWebButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						open(clickUrl, false, false, false);
						interstitialClose();
					}
				});
			}
		});
	}
	
	/**
	 * setUpdateTime(Integer) is not supported in AdVideoUnitView
	 */
	@Override
	public void setUpdateTime(int updateTime) {
//		if (getUpdateTime() == 0 || updateTime != 0)
//			throw new UnsupportedOperationException("setUpdateTime(Integer) is not supported in AdVideoUnitView");

		super.setUpdateTime(0);
	}

	@Override
	protected void interstitialClose() {
		handler.removeCallbacks(countdownRunnable);
		handler.removeCallbacks(seekRunnable);
		handler.post(closeDialogRunnable);
	}
	
	private void showButtons(){
		handler.post(showButtonsRunnable);
	}
	
	/**
	 * get auto-close time.
	 * 
	 * @return auto close time in sec.
	 */
	public Integer getAutoCloseTime() {
		return autoCloseTime;
	}

	/**
	 * Set auto-close time.
	 * 
	 * @param autoCloseTime in sec.
	 */
	public void setAutoCloseTime(Integer autoCloseTime) {
		this.autoCloseTime = autoCloseTime;
	}
	
	/**
	 * Set close button text
	 * 
	 * @param closeButtonText
	 */
	public void setCloseButtonText(String closeButtonText){
		this.closeButtonText = closeButtonText;
	}
	
	/**
	 * Set GoToSite button text 
	 * 
	 * @param goToSiteButtonText
	 */
	public void setGoToSiteButtonText(String goToSiteButtonText){
		this.goToSiteButtonText = goToSiteButtonText;
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
	 * Get whether to show Phone Status Bar or not.
	 * 
	 * @return true if status bar showing is enabled
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
