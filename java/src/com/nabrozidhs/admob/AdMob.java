package com.nabrozidhs.admob;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.unity3d.player.UnityPlayer;

public final class AdMob extends AdListener {

    private static final String TAG = AdMob.class.getSimpleName();

    private final Activity mActivity;
    private final AdView mAdView;
    private final String mCallbackName;
    
    /**
     * Constructor for the AdMob class that holds the ads.
     * 
     * @param activity
     *            The activity to place the {@link AdView}.
     * @param adUnitId
     *            Your AdUnit ID from the AdMob console.
     * @param adSize
     *            A string ad size constant representing the desired ad size.
     * @param isTopPosition
     *            True to position the ad at the top of the screen. False to
     *            position the ad at the bottom of the screen.
     * @param callbackName
     *            the game object that should listen for ad events.
     */
    public AdMob(final Activity activity, final String adUnitId, final String adSize,
            final boolean isTopPosition, final String callbackName) {
        mActivity = activity;
        mCallbackName = callbackName;
        
        mAdView = new AdView(activity);
        mAdView.setAdSize(parseAdSize(adSize));
        mAdView.setAdUnitId(adUnitId);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(this);
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                final LinearLayout layout = new LinearLayout(activity);
                final LayoutParams layoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = isTopPosition ? Gravity.TOP : Gravity.BOTTOM;
                
                mActivity.addContentView(layout, layoutParams);
                layout.addView(mAdView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        });
    }

    /**
     * Request an ad.
     */
    public void requestAd() {
        Log.d(TAG, "Requesting an ad.");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    
    /**
     * Shows the banner to the user.
     */
    public void showBanner() {
        Log.d(TAG, "Show banner to the user");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Hides the banner from the user.
     */
    public void hideBanner() {
        Log.d(TAG, "Hides banner from the user");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mAdView.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onAdLoaded() {
        if (mCallbackName != null) {
            UnityPlayer.UnitySendMessage(mCallbackName, "OnAdLoaded", "");
        }
    }
    
    /**
     * Parses the ad size string obtained from Unity.
     * 
     * @param adSize
     * 
     * @return the resulting {@link AdSize}
     */
    private static AdSize parseAdSize(final String adSize) {
        if ("BANNER".equals(adSize)) {
            return AdSize.BANNER;
        } else if ("MEDIUM_RECTANGLE".equals(adSize)) {
            return AdSize.MEDIUM_RECTANGLE;
        } else if ("FULL_BANNER".equals(adSize)) {
            return AdSize.FULL_BANNER;
        } else if ("LEADERBOARD".equals(adSize)) {
            return AdSize.LEADERBOARD;
        } else if ("SMART_BANNER".equals(adSize)) {
            return AdSize.SMART_BANNER;
        }

        return null;
    }
}
