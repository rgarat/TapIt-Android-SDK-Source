package com.tapit.adview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Viewer of OfferWall advertising. Presents list of offer-advertising list in
 * full screen mode
 */
public class AdOfferWallView extends AdView {

	public static final String TAG = "AdOfferWallView";

	private Boolean isShowPhoneStatusBar;

	private Dialog dialog;

	private Handler handler = new Handler();

	private Context context;
	
	private Button closeButton;

	public AdOfferWallView(Context context, String zone) {
		super(context, zone);
		this.context = context;
		setAdtype("7");
	}

	/**
	 * Show OfferWall ad.
	 */
	public void show() {
		openOfferWall(context, isShowPhoneStatusBar);
	}

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

	private void openOfferWall(final Context context, Boolean isShowPhoneStatusBar) {
		if (isShowPhoneStatusBar == null) {
			isShowPhoneStatusBar = true;
		}
		
		setUpdateTime(0);

		// show dialog
		final Dialog dialog;

		if (isShowPhoneStatusBar) {
			dialog = new Dialog(context, android.R.style.Theme_NoTitleBar);
		} else {
			dialog = new Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
		}

		this.dialog = dialog;

		dialog.setCancelable(false);

		if (closeButton == null){
			closeButton = new Button(context);
			closeButton.setId(1000);
			closeButton.setText("Close");
			RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			closeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			closeButton.setLayoutParams(closeLayoutParams);
		}

		RelativeLayout mainLayout = new RelativeLayout(context);
		mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

		mainLayout.addView(this);
		mainLayout.addView(closeButton);

		dialog.setContentView(mainLayout);
		dialog.show();

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				interstitialClose();
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				interstitialClose();
			}
		});
	}
	
	/**
	 * setUpdateTime(Integer) is not supported in AdOfferWallView
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
		handler.post(closeDialogRunnable);
	}

	/**
	 * Get whether to show phone status bar or not.
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
	
	/**
	 * Get Object for customization close button view.
	 * 
	 * @return close button
	 */
	public Button getCloseButton() {
		return closeButton;
	}

	/**
	 * Set Button for customization close button view. closeButton will be added
	 * into RelativeLayout so you have customize it like this:
	 * 
	 * <pre class="prettyprint">
	 * closeButton.setText(&quot;Close&quot;);
	 * RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(
	 * 		RelativeLayout.LayoutParams.WRAP_CONTENT,
	 * 		RelativeLayout.LayoutParams.WRAP_CONTENT);
	 * closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	 * closeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	 * closeButton.setLayoutParams(closeLayoutParams);
	 * </pre>
	 * 
	 * @param closeButton
	 */
	public void setCloseButton(Button closeButton) {
		this.closeButton = closeButton;
	}

	@Override
	protected String wrapToHTML(String data, String bridgeScriptPath, String scriptPath) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (!jsonObject.has("type") || !jsonObject.getString("type").equals("offerwall"))
				return blank;
			String title = jsonObject.getString("title");

			String content = "";

			JSONArray array = jsonObject.getJSONArray("offers");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				content += String.format(bannerItem, object.getString("clickurl"),
						object.getString("imageurl"), object.getString("adtitle"),
						object.getString("adtext"));
			}

			return String.format(blank, title, content);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return blank;
	}

	private static final String bannerItem = "			<li>"
			+ "				<a href=\"%s\">"
			+ "					<div class=\"offer\">"
			+ "					<div class=\"icon\"><img src=\"%s\" width=\"57\" height=\"57\"/></div>"
			+ "						<div class=\"desc\">"
			+ "							<h3>%s</h3>"
			+ "							<h4>%s</h4>"
			+ "						</div>"
			+ "					<div class=\"button\"><img src=\"http://d2bgg7rjywcwsy.cloudfront.net/offerwall/img/arrow.png\" width=\"57\" height=\"57\"/></div>"
			+ "					</div>"
			+ "				</a>"
			+ "			</li>"
			+ "";

	private static final String blank = "<html>"
			+ ""
			+ "<head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
			+ "<meta name=\"HandheldFriendly\" content=\"true\" />"
			+ "<meta name=\"MobileOptimized\" content=\"320\" />"
			+ "<meta name=\"viewport\" content=\"width=device-width\" />"
			+ "<meta name=\"viewport\" content=\"initial-scale=1.0\" />"
			+ "<meta name=\"viewport\" content=\"user-scalable=no\" />"
			+ "<title>TapIt!</title>"
			+ "<link type=\"text/css\" rel=\"stylesheet\" href=\"http://d2bgg7rjywcwsy.cloudfront.net/offerwall/css/style.css\"/>"
			+ "</head>"
			+ "<body>"
			+ "<div id=\"page\">"
			+ "<div id=\"action-wrapper\">"
			+ "	<div id=\"action\">"
			+ "		<h2>%s</h2>"
			+ "	</div>"
			+ "</div> <!-- end action-wrapper -->"
			+ "<div id=\"list-wrapper\">"
			+ "	<div id=\"list\">"
			+ "		<ul>"
			+ "%s"
			+ "		</ul>"
			+ "	</div>"
			+ "</div> <!-- end list-wrapper -->"
			+ ""
			+ "</div> <!-- end page -->"
			+ ""
			+ "</body>"
			+ ""
			+ "</html>";
}
