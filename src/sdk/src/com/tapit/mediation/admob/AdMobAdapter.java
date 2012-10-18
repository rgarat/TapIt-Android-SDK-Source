
package com.tapit.mediation.admob;

import java.util.HashMap;
import java.util.Map;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.tapit.adview.AdInterstitialBaseView;
import com.tapit.adview.AdInterstitialView;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * TapIt! Adapter for AdMob Mediation.  This class should not be used directly by publishers.
 */
public final class AdMobAdapter
        implements
        MediationBannerAdapter<AdMobAdapterExtras, AdMobAdapterServerParameters>,
        MediationInterstitialAdapter<AdMobAdapterExtras, AdMobAdapterServerParameters>,
        OnAdDownload, OnInterstitialAdDownload {

    private static final String CLIENT_STRING = "admob-mediation-1.0.0";
        
    /*
     * Callback listeners. This class handles both in-activity (banner) and interstitial ads, so it
     * listens for both
     */
    private MediationBannerListener bannerListener;
    private MediationInterstitialListener interstitialListener;
        
    private AdView bannerAd;
    private AdInterstitialBaseView interstitialAd;

    /*
     * ------------------------------------------------------------------------
     * MediationAdapter Implementation
     * ------------------------------------------------------------------------
     */

    /*
     * These methods let the mediation layer know what data types are used for server-side
     * parameters and publisher "extras"
     */
    @Override
    public Class<AdMobAdapterExtras> getAdditionalParametersType() {
        return AdMobAdapterExtras.class;
    }

    @Override
    public Class<AdMobAdapterServerParameters> getServerParametersType() {
        return AdMobAdapterServerParameters.class;
    }

    /*
     * Ad Requests
     */
    @Override
    public void requestBannerAd(MediationBannerListener listener, Activity activity,
            AdMobAdapterServerParameters serverParameters, AdSize adSize,
            MediationAdRequest mediationAdRequest, AdMobAdapterExtras extras) {
                
        bannerListener = listener;
                
        AdSize supportedSizes[] = {
                // banners
                new AdSize(320, 50),
                new AdSize(300, 50),
                new AdSize(216, 36),
                new AdSize(168, 28),
                new AdSize(120, 20),

                // medium rect
                new AdSize(300, 250),
                                
                // Leaderboard
                new AdSize(728, 90)
        };

        AdSize bestSize = adSize.findBestSize(supportedSizes);
                
        if(bestSize ==  null) {
            listener.onFailedToReceiveAd(AdMobAdapter.this, ErrorCode.INVALID_REQUEST);
            return;
        }
                
        final String zoneId = serverParameters.zoneId;
        bannerAd = new AdView(activity, zoneId);
        Map<String, String> params = new HashMap<String, String>();
        params.put( "client", CLIENT_STRING );
        params.put( "h", Integer.toString(bestSize.getHeight()) );
        params.put( "w", Integer.toString(bestSize.getWidth()) );
        if(mediationAdRequest.isTesting())
        	params.put("mode", "test");
                
        bannerAd.setCustomParameters(params);
        bannerAd.setOnAdDownload(this);
        bannerAd.setUpdateTime(Integer.MAX_VALUE); // defer ad rotation to admob
        bannerAd.update(true); // kick off the ad request
    }

    @Override
    public void requestInterstitialAd(
            MediationInterstitialListener listener, Activity activity,
            AdMobAdapterServerParameters serverParameters, MediationAdRequest mediationAdRequest,
            AdMobAdapterExtras extras) {
        interstitialListener = listener;
                
        final String zoneId = serverParameters.zoneId;
        interstitialAd = new AdInterstitialView(activity, zoneId);
        Map<String, String> params = new HashMap<String, String>();
        params.put( "client", CLIENT_STRING );
        if(mediationAdRequest.isTesting())
        	params.put("mode", "test");
        interstitialAd.setCustomParameters(params);
        interstitialAd.setOnInterstitialAdDownload(this);
        interstitialAd.load();
    }

    @Override
    public void showInterstitial() {
        interstitialAd.showInterstitial();
    }

    @Override
    public void destroy() {
        bannerListener = null;
        interstitialListener = null;
                
        bannerAd = null;
        interstitialAd = null;
    }

    @Override
    public View getBannerView() {
        return bannerAd;
    }

    /**
     * Banner callbacks
     */
        
    @Override
    public void begin(AdViewCore adView) {
        // noop
    }

    /**
     * This event is fired after banner content fully downloaded.
     */
    @Override
    public void end(AdViewCore adView) {
        if(bannerListener != null) {
            if(bannerAd.getParent() == null) {
                // only add the bannerAd to the view once...
                bannerListener.onReceivedAd(AdMobAdapter.this);
            }
        }
    }

    /**
     * This event is fired after fail to download content.
     */
    @Override
    public void error(AdViewCore adView, String error) {
        ErrorCode ec;
        if ("No available creatives".equals(error)) {
            ec = ErrorCode.NO_FILL;
        }
        else {
            ec = ErrorCode.INTERNAL_ERROR;
        }
                
        if (adView == bannerAd) {
            if(bannerListener != null) {
                bannerListener.onFailedToReceiveAd(AdMobAdapter.this, ec);
            }
        }
        else {
            if(interstitialListener != null) {
                interstitialListener.onFailedToReceiveAd(AdMobAdapter.this, ec);
            }
        }
    }

    @Override
    public void clicked(AdViewCore adView) {
        if (adView == bannerAd && bannerListener != null) {
            bannerListener.onClick(AdMobAdapter.this);
        }
    }

    @Override
    public void willPresentFullScreen(AdViewCore adView) {
        if(bannerListener != null) {
            bannerListener.onPresentScreen(AdMobAdapter.this);
        }
    }

    @Override
    public void didPresentFullScreen(AdViewCore adView) {
        // noop
    }

    @Override
    public void willDismissFullScreen(AdViewCore adView) {
        if(bannerListener != null) {
            bannerListener.onDismissScreen(AdMobAdapter.this);
        }
    }

    @Override
    public void willLeaveApplication(AdViewCore adView) {
        if(adView == bannerAd && bannerListener != null) {
            bannerListener.onLeaveApplication(AdMobAdapter.this);
        }
        else if(adView == interstitialAd && interstitialListener != null) {
            interstitialListener.onLeaveApplication(AdMobAdapter.this);
        }
    }
        
    /**
     * Interstitial callbacks
     */
        
    @Override
    public void willLoad(AdViewCore adView) {
        // noop
    }

    @Override
    public void ready(AdViewCore adView) {
        if(interstitialListener != null) {
            interstitialListener.onReceivedAd(AdMobAdapter.this);
        }

    }

    @Override
    public void willOpen(AdViewCore adView) {
        if(interstitialListener != null) {
            interstitialListener.onPresentScreen(AdMobAdapter.this);
        }
    }

    @Override
    public void didClose(AdViewCore adView) {
        if(interstitialListener != null) {
            interstitialListener.onDismissScreen(AdMobAdapter.this);
        }
    }
}
