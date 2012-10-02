package com.tapit.adview;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Viewer of OfferWall advertising. Presents list of offer-advertising list in
 * full screen mode
 */
public class AdOfferWallView extends AdInterstitialBaseView {

    private Button closeButton;

    public AdOfferWallView(Context context, String zone) {
        super(context, zone);
        setAdtype("7");
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
            Log.e("TapIt", "An error occured while processing JSON", e);
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

    @Override
    public View getInterstitialView(Context ctx) {
        callingActivityContext = ctx;
        interstitialLayout = new RelativeLayout(ctx);
        final RelativeLayout.LayoutParams adViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        interstitialLayout.addView(this, adViewLayout);
        initCloseButton();
        return interstitialLayout;
    }

    private void initCloseButton() {
        closeButton = new Button(context);
//        closeButton.setId(1000);
        closeButton.setText("Close");
        RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        closeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        closeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        closeButton.setLayoutParams(closeLayoutParams);

        interstitialLayout.addView(closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeInterstitial();
            }
        });
    }

    public void loadUrl(final String url) {
        super.loadUrl(url);
        closeButton.setVisibility(GONE);
    }
}
