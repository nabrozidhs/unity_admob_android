using UnityEngine;
using System.Collections;

[RequireComponent(typeof(AdMobPlugin))]
public class Menu : MonoBehaviour {

	private const string AD_UNIT_ID = "YOUR_AD_UNIT_ID";

    private static Vector2 BUTTON_SIZE = new Vector2(100, 50);
    
	private Rect buttonPositionShowAds;
	private Rect buttonPositionHideAds;

    private AdMobPlugin admob;

	void Start() {
#if !UNITY_EDITOR
		admob = GetComponent<AdMobPlugin>();
		admob.CreateBanner(AD_UNIT_ID, AdMobPlugin.AdSize.SMART_BANNER, true);
		admob.RequestAd();
#endif

		buttonPositionShowAds = new Rect(
			(Screen.width - BUTTON_SIZE.x) / 2,
			(Screen.height - BUTTON_SIZE.y) / 2,
			BUTTON_SIZE.x, BUTTON_SIZE.y);

		buttonPositionHideAds = new Rect(
			buttonPositionShowAds.x, buttonPositionShowAds.y + BUTTON_SIZE.y * 3 / 2,
			buttonPositionShowAds.width, buttonPositionShowAds.height);
    }

	void OnEnable() {
		AdMobPlugin.AdLoaded += HandleAdLoaded;
	}

	void OnDisable() {
		AdMobPlugin.AdLoaded -= HandleAdLoaded;
	}

	void HandleAdLoaded() {
#if !UNITY_EDITOR
		admob.ShowBanner();
#endif
	}

    void OnGUI () {
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
    }
}
