using System;
using UnityEngine;

public sealed class AdMobPlugin : MonoBehaviour {

	private const string CLASS_NAME = "com.nabrozidhs.admob.AdMob";

	private const string CALL_SHOW_BANNER = "showBanner";
	private const string CALL_HIDE_BANNER = "hideBanner";
	private const string CALL_SHOW_INSTERTITIAL = "showInterstitial";
	private const string CALL_REQUEST_AD = "requestAd";
	private const string CALL_REQUEST_INTERSTITIAL = "requestInterstitial";

	public enum AdSize {BANNER, MEDIUM_RECTANGLE, FULL_BANNER, LEADERBOARD, SMART_BANNER};

	public static event Action AdClosed = delegate{};
	public static event Action AdFailedToLoad = delegate{};
	public static event Action AdLeftApplication = delegate{};
	public static event Action AdLoaded = delegate{};
	public static event Action AdOpened = delegate{};

	public static event Action InterstitialClosed = delegate{};
	public static event Action InterstitialFailedToLoad = delegate{};
	public static event Action InterstitialLeftApplication = delegate{};
	public static event Action InterstitialLoaded = delegate{};
	public static event Action InterstitialOpened = delegate{};
	
#if UNITY_ANDROID && !UNITY_EDITOR
	private AndroidJavaObject plugin;
#endif

	/// <summary>
	/// Bind this instance.
	/// </summary>
	public void CreateBanner(string adUnitId, AdSize adSize, bool isTopPosition=true, string interstitialId="", bool isTestDevice=false) {
		if (String.IsNullOrEmpty(interstitialId)) {
			interstitialId = "";
		}

#if UNITY_ANDROID && !UNITY_EDITOR
		plugin = new AndroidJavaObject(
			CLASS_NAME,
			new AndroidJavaClass("com.unity3d.player.UnityPlayer")
				.GetStatic<AndroidJavaObject>("currentActivity"),
			adUnitId,
			adSize.ToString(),
			isTopPosition,
			interstitialId,
			gameObject.name,
			isTestDevice);
#endif
	}

	/// <summary>
	/// Requests a banner ad. This method should be called
	/// after we have created a banner.
	/// </summary>
	public void RequestAd() {
#if UNITY_ANDROID && !UNITY_EDITOR
		if (plugin != null) {
			plugin.Call(CALL_REQUEST_AD, new object[0]);
		}
#endif
	}
	
	/// <summary>
	/// Requests an interstitial ad.
	/// </summary>
	public void RequestInterstitial() {
#if UNITY_ANDROID && !UNITY_EDITOR
		if (plugin != null) {
			plugin.Call(CALL_REQUEST_INTERSTITIAL, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Shows the banner to the user.
	/// </summary>
	public void ShowBanner() {
#if UNITY_ANDROID && !UNITY_EDITOR
		if (plugin != null) {
			plugin.Call(CALL_SHOW_BANNER, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Hides the banner from the user.
	/// </summary>
	public void HideBanner() {
#if UNITY_ANDROID && !UNITY_EDITOR
		if (plugin != null) {
			plugin.Call(CALL_HIDE_BANNER, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Shows the interstitial ad to the user.
	/// </summary>
	public void ShowInterstitial() {
#if UNITY_ANDROID && !UNITY_EDITOR
		if (plugin != null) {
			plugin.Call(CALL_SHOW_INSTERTITIAL, new object[0]);
		}
#endif
	}

	public void OnAdClosed() {
		AdClosed();
	}

	public void OnAdFailedToLoad() {
		AdFailedToLoad();
	}

	public void OnAdLeftApplication() {
		AdLeftApplication();
	}

	public void OnAdLoaded() {
		AdLoaded();
	}

	public void OnAdOpened() {
		AdOpened();
	}
	
	public void OnInterstitialClosed() {
		InterstitialClosed();
	}
	
	public void OnInterstitialFailedToLoad() {
		InterstitialFailedToLoad();
	}
	
	public void OnInterstitialLeftApplication() {
		InterstitialLeftApplication();
	}
	
	public void OnInterstitialLoaded() {
		InterstitialLoaded();
	}
	
	public void OnInterstitialOpened() {
		InterstitialOpened();
	}
}
