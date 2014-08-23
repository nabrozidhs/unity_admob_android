using UnityEngine;
using System.Collections;

[RequireComponent(typeof(AdMobPlugin))]
public class Menu : MonoBehaviour {

	private const string AD_UNIT_ID = "YOUR_AD_UNIT_ID";
	private const string INTERSTITIAL_ID = "YOUR_AD_UNIT_ID";

	private static Vector2 BUTTON_SIZE = new Vector2(100, 50);

	private Rect buttonPositionShowAds;
	private Rect buttonPositionHideAds;
	private Rect buttonPositionShowInterstitial;

	private AdMobPlugin admob;

	void Start() {
		admob = GetComponent<AdMobPlugin>();
		admob.CreateBanner(adUnitId: AD_UNIT_ID,
		                   adSize: AdMobPlugin.AdSize.SMART_BANNER,
		                   isTopPosition: true,
		                   interstitialId: INTERSTITIAL_ID,
		                   isTestDevice: true);
		admob.RequestAd();

		buttonPositionShowAds = new Rect(
			(Screen.width - BUTTON_SIZE.x) / 2,
			(Screen.height - BUTTON_SIZE.y) / 2,
			BUTTON_SIZE.x, BUTTON_SIZE.y);

		buttonPositionHideAds = new Rect(
			buttonPositionShowAds.x, buttonPositionShowAds.y + BUTTON_SIZE.y * 3 / 2,
			buttonPositionShowAds.width, buttonPositionShowAds.height);

		buttonPositionShowInterstitial = new Rect(
			buttonPositionHideAds.x, buttonPositionHideAds.y + BUTTON_SIZE.y * 3 / 2,
			buttonPositionHideAds.width, buttonPositionHideAds.height);
	}

	void OnEnable() {
		AdMobPlugin.AdClosed += () => { Debug.Log ("AdClosed"); };
		AdMobPlugin.AdFailedToLoad += () => { Debug.Log ("AdFailedToLoad"); };
		AdMobPlugin.AdLeftApplication += () => { Debug.Log ("AdLeftApplication"); };
		AdMobPlugin.AdOpened += () => { Debug.Log ("AdOpened"); };

		AdMobPlugin.InterstitialClosed += () => { Debug.Log ("InterstitialClosed"); };
		AdMobPlugin.InterstitialFailedToLoad += () => { Debug.Log ("InterstitialFailedToLoad"); };
		AdMobPlugin.InterstitialLeftApplication += () => { Debug.Log ("InterstitialLeftApplication"); };
		AdMobPlugin.InterstitialOpened += () => { Debug.Log ("InterstitialOpened"); };

		AdMobPlugin.AdLoaded += HandleAdLoaded;
		AdMobPlugin.InterstitialLoaded += HandleInterstitialLoaded;
	}

	void OnDisable() {
		AdMobPlugin.AdLoaded -= HandleAdLoaded;
		AdMobPlugin.InterstitialLoaded -= HandleInterstitialLoaded;
	}

	void HandleAdLoaded() {
#if !UNITY_EDITOR
		admob.ShowBanner();
#endif
	}

	void HandleInterstitialLoaded() {
#if !UNITY_EDITOR
		admob.ShowInterstitial();
#endif
	}

	void OnGUI() {
		if (GUI.Button(buttonPositionShowAds, "Show Ads")) {
#if !UNITY_EDITOR
			admob.ShowBanner();
#endif
		}

		if (GUI.Button(buttonPositionHideAds, "Hide Ads")) {
#if !UNITY_EDITOR
			admob.HideBanner();
#endif
		}

		if (GUI.Button(buttonPositionShowInterstitial, "Show Interstitial")) {
#if !UNITY_EDITOR
			admob.RequestInterstitial();
#endif
		}
	}
}
