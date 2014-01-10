package com.nabrozidhs.admob;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public final class AdMob {

    private static final String TAG = AdMob.class.getSimpleName();

    private final Activity mActivity;
    private final AdView mAdView;

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
     */
    public AdMob(final Activity activity, final String adUnitId,
            final String adSize, final boolean isTopPosition) {
        mActivity = activity;

        mAdView = new AdView(activity);
        mAdView.setAdSize(parseAdSize(adSize));
        mAdView.setAdUnitId(adUnitId);

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final LinearLayout layout = new LinearLayout(activity);
                final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                final LinearLayout.LayoutParams adParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                layoutParams.gravity = isTopPosition ? Gravity.TOP : Gravity.BOTTOM;

                activity.addContentView(layout, layoutParams);
                layout.addView(mAdView, adParams);

                // Request an Ad (it won't show).
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
