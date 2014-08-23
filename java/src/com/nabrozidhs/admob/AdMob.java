package com.nabrozidhs.admob;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.unity3d.player.UnityPlayer;

public final class AdMob extends AdListener {

    private static final String TAG = AdMob.class.getSimpleName();

    private final Activity mActivity;
    private final AdView mAdView;
    private final InterstitialAd mInterstitial;
    private final String mCallbackName;
    private final boolean isTestingDevice;
    
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
     * @param interstitialId
     *            Your interstitial ID from the AdMob console.
     * @param callbackName
     *            the game object that should listen for ad events.
     * @param isTestDevice
     * 			  Tell if the device is a testing device to avoid being banned from
     * 			  AdMob.
     */
    public AdMob(final Activity activity, final String adUnitId, final String adSize,
            final boolean isTopPosition, final String interstitialId, final String callbackName,
            final boolean isTestDevice) {
        mActivity = activity;
        mCallbackName = callbackName;
        isTestingDevice = isTestDevice;
        
        mAdView = new AdView(activity);
        mAdView.setAdSize(parseAdSize(adSize));
        mAdView.setAdUnitId(adUnitId);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            
            @Override
            public void onAdClosed() {
                if (mCallbackName != null) {
                    UnityPlayer.UnitySendMessage(mCallbackName, "OnAdClosed", "");
                }
            }
            
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mCallbackName != null) {
                    UnityPlayer.UnitySendMessage(mCallbackName, "OnAdFailedToLoad",
                            Integer.toString(errorCode));
                }
            }
            
            @Override
            public void onAdLeftApplication() {
                if (mCallbackName != null) {
                    UnityPlayer.UnitySendMessage(mCallbackName, "OnAdLeftApplication", "");
                }
            }
            
            @Override
            public void onAdLoaded() {
                if (mCallbackName != null) {
                    UnityPlayer.UnitySendMessage(mCallbackName, "OnAdLoaded", "");
                }
            }
            
            @Override
            public void onAdOpened() {
                if (mCallbackName != null) {
                    UnityPlayer.UnitySendMessage(mCallbackName, "OnAdOpened", "");
                }
            }
        });
        
        if (interstitialId != null && interstitialId.length() != 0) {
            mInterstitial = new InterstitialAd(activity);
            mInterstitial.setAdUnitId(interstitialId);
            mInterstitial.setAdListener(new AdListener() {
                
                @Override
                public void onAdClosed() {
                    if (mCallbackName != null) {
                        UnityPlayer.UnitySendMessage(mCallbackName, "OnInterstitialClosed", "");
                    }
                }
                
                @Override
                public void onAdFailedToLoad(final int errorCode) {
                    if (mCallbackName != null) {
                        UnityPlayer.UnitySendMessage(mCallbackName, "OnInterstitialFailedToLoad",
                                Integer.toString(errorCode));
                    }
                }
                
                @Override
                public void onAdLeftApplication() {
                    if (mCallbackName != null) {
                        UnityPlayer.UnitySendMessage(mCallbackName, "OnInterstitialLeftApplication", "");
                    }
                }
                
                @Override
                public void onAdLoaded() {
                    if (mCallbackName != null) {
                        UnityPlayer.UnitySendMessage(mCallbackName, "OnInterstitialLoaded", "");
                    }
                }
                
                @Override
                public void onAdOpened() {
                    if (mCallbackName != null) {
                        UnityPlayer.UnitySendMessage(mCallbackName, "OnInterstitialOpened", "");
                    }
                }
            });
        } else {
            mInterstitial = null;
        }
        
        final CountDownLatch latch = new CountDownLatch(1);
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                final LinearLayout layout = new LinearLayout(activity);
                final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = isTopPosition ? Gravity.TOP : Gravity.BOTTOM;
                
                mActivity.addContentView(layout, layoutParams);
                layout.addView(mAdView, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                
                latch.countDown();
            }
        });
        
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Request an ad.
     */
    public void requestAd() {
        Log.d(TAG, "Requesting an ad.");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                //mAdView.loadAd(new AdRequest.Builder().build());
                
                // Create an ad request. Check logcat output for the hashed device ID to
                // get test ads on a physical device.
                final AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                
                if (isTestingDevice) {
                    adRequestBuilder.addTestDevice(getAdmobDeviceId(
                            mActivity.getApplicationContext()));
                }

                // Start loading the ad in the background.
                mAdView.loadAd(adRequestBuilder.build());
            }
        });
    }
    
    /**
     * Request an interstitial ad.
     */
    public void requestInterstitial() {
        if (mInterstitial == null) {
            return;
        }
        
        Log.d(TAG, "Requesting an interstitial ad.");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                
                // Create an ad request. Check logcat output for the hashed device ID to
                // get test ads on a physical device.
                final AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                
                if (isTestingDevice) {
                    adRequestBuilder.addTestDevice(getAdmobDeviceId(
                            mActivity.getApplicationContext()));
                }
                
                mInterstitial.loadAd(adRequestBuilder.build());
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
     * Shows the interstitial ad to the user.
     */
    public void showInterstitial() {
        if (mInterstitial == null) {
            return;
        }
        
        Log.d(TAG, "Show interstitial ad to the user.");
        
        mActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
            }
        });
    }
    
    /**
     * Parses the ad size string obtained from Unity.
     * 
     * @param adSize
     * 
     * @return the parsed {@link AdSize}.
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
    
    private static final String getAdmobDeviceId(final Context context) {
        
        final String android_id = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final String deviceId = md5(android_id).toUpperCase(Locale.US);
        
        return deviceId;
    }
    
    private static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        
        return "";
    }
}
