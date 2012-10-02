package com.tapit.adview;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
public class AdVideoUnitView extends AdInterstitialBaseView {
	
	private Handler handler = new Handler();
	
	private ProgressBar progressBar;
	
	private MediaPlayer player;
	private SurfaceView playerSurfaceView;
	private SurfaceHolder playerSurfaceHolder;
	
	private Button closeButton;
	private String closeButtonText;

	private Button goToWebButton;
	private String goToSiteButtonText;

	private String clickUrl;
	

	public AdVideoUnitView(Context context, String zone) {
		super(context, zone);
		setAdtype("6");
		final AdVideoUnitView adView = this;
		
		
		playerSurfaceView = new SurfaceView(context);
	    playerSurfaceHolder = playerSurfaceView.getHolder();
	    playerSurfaceHolder.addCallback(new SurfaceHolder.Callback(){

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				player.start();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
	    	
	    });
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();
		int screenWidth = display.getWidth();
	    playerSurfaceHolder.setFixedSize(screenWidth, screenHeight);
		
		

		
		player = new MediaPlayer();
//		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//			@Override
//			public void onBufferingUpdate(MediaPlayer mp, int percent) {
////				Log.d("Nick", "buffering: " + percent);
//			}
//		});
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
	        @Override
	        public void onPrepared(MediaPlayer mp) {
	    		isLoaded = true;
	            if(interstitialListener != null) {
	            	interstitialListener.ready(adView);
	            }
//	            progressBar.setMax(player.getDuration());
	        }
	    });
		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return false;
			}
		});
		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return false;
			}
		});
	}

	@Override
	public void end(AdViewCore adView) {
		// no-op. ready() callback will be fired once video is prepared
	}
	
	@Override
	public void showInterstitial() {
		super.showInterstitial();
//		player.start();
	}

	private Runnable progressRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				if(player.isPlaying()) {
					int curPos = player.getCurrentPosition();
					int maxPos = player.getDuration();
					if(progressBar.getMax() != maxPos) {
						progressBar.setMax(maxPos);
					}
					progressBar.setProgress(curPos);
					if(curPos < maxPos){
						handler.postDelayed(progressRunnable, 50);
					}
				}
			} catch (Exception e){
                Log.e("TapIt", "An error occured", e);
			}
		}
	};
	
	private Runnable showButtonsRunnable = new Runnable() {
		
		@Override
		public void run() {
			closeButton.setVisibility(View.VISIBLE);
			goToWebButton.setVisibility(View.VISIBLE);			
		}
	};

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
		this.clickUrl = clickUrl;

		try {
//			player.setDisplay(playerSurfaceHolder);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(url);
			player.prepareAsync();
		} catch (IllegalArgumentException e) {
            Log.e("TapIt", "An error occured", e);
		} catch (IllegalStateException e) {
            Log.e("TapIt", "An error occured", e);
		} catch (IOException e) {
            Log.e("TapIt", "An error occured", e);
		} catch (Exception e) {
            Log.e("TapIt", "An error occured", e);
		}

//		Log.d("Nick", "vid playing!");
		
//		handler.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				Log.d("Nick", "test");
////				PlayerProperties properties = new PlayerProperties();
////				properties.setProperties(false, true, false, false, false,
////						"fullscreen", "exit");
//				
//				// This video definitely works
//				final String myUrl = "http://archive.org/download/superman_1941/superman_1941_512kb.mp4";
//				player.setVideoURI(Uri.parse(myUrl));
//
////				player.setVideoURI(Uri.parse(url));
//				player.start();
//			}
//		});
	}
	
	public void interstitialShowing() {
//		try {
//			final String myUrl = "http://archive.org/download/superman_1941/superman_1941_512kb.mp4";
//			player.setDataSource(myUrl);
//			player.prepare();
//
//			int videoWidth = player.getVideoHeight();
//		    int videoHeight = player.getVideoWidth();
//		    Log.d("Nick", "video dimensions: " + videoWidth + "x" + videoHeight);
		    player.start();
		    handler.post(progressRunnable);
//		} catch (IllegalStateException e) {
//			Log.e("TapIt", "An error occured", e);
//		} catch (IOException e) {
//			Log.e("TapIt", "An error occured", e);
//		}
	}
	
	@Override
	public void interstitialClosing() {
		player.stop();
		player.release();
	}
	
	
	@Override
	protected void interstitialClose() {
		handler.removeCallbacks(progressRunnable);
	}
	
	private void showButtons(){
		handler.post(showButtonsRunnable);
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
	
	@Override
	public View getInterstitialView(Context ctx) {
		removeViews();
		
		RelativeLayout.LayoutParams lp;

		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);

		playerSurfaceView.setLayoutParams(lp);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				showButtons();
			}
		});

		final int barId = 10505;
		progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setId(barId);
		
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		progressBar.setLayoutParams(lp);

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
				closeInterstitial();
			}
		});
		
		goToWebButton = new Button(context);
		goToWebButton.setLayoutParams(llp);
		goToWebButton.setText(goToSiteButtonText != null ? goToSiteButtonText : "Go To Site");
		buttonsLayout.addView(goToWebButton);
		goToWebButton.setVisibility(View.GONE);
		
		goToWebButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				open(clickUrl, false, false, false);
				interstitialClose();
			}
		});
		
		// add views
		interstitialLayout = new RelativeLayout(ctx);
		interstitialLayout.addView(playerSurfaceView);
		interstitialLayout.addView(progressBar);
		interstitialLayout.addView(buttonsLayout);
		
		return interstitialLayout;
	}

//	@Override
//	protected void removeViews() {
//		super.removeViews();
//		
//		RelativeLayout parent = (RelativeLayout)player.getParent();
//		if(parent != null) {
//			(parent).removeAllViews();
//		}
//	}
	
	@Override
	public void destroy() {
		player.release();
	}
}
