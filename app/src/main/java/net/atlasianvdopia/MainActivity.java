package net.atlassianvdopia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;

public class MainActivity extends AppCompatActivity {
    
    public static final String TAG = "KevinAppodeal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String appoDealKey = "ccbd57eeec72f3baa5a081bc26d8e1ad9b7bbac0f1a4c273";
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, appoDealKey, Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO | Appodeal.MREC);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
                Log.d(TAG, "onInterstitialLoaded");
            }
            @Override
            public void onInterstitialFailedToLoad() {
                Log.d(TAG, "onInterstitialFailedToLoad");
            }
            @Override
            public void onInterstitialShown() {
                Log.d(TAG, "onInterstitialShown");
            }
            @Override
            public void onInterstitialClicked() {
                Log.d(TAG, "onInterstitialClicked");
            }
            @Override
            public void onInterstitialClosed() {
                Log.d(TAG, "onInterstitialClosed");
            }
        });

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded() {
                Log.d(TAG, "onRewardedVideoLoaded");
            }
            @Override
            public void onRewardedVideoFailedToLoad() {
                Log.d(TAG, "onRewardedVideoFailedToLoad");
            }
            @Override
            public void onRewardedVideoShown() {
                Log.d(TAG, "onRewardedVideoShown");
            }
            @Override
            public void onRewardedVideoFinished(int amount, String name) {
                Log.d(TAG, "onRewardedVideoFinished");
            }
            @Override
            public void onRewardedVideoClosed(boolean finished) {
                Log.d(TAG, "onRewardedVideoClosed");
            }
        });
        
    }

    public void onLoadInterstitialAd(View view) {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void onLoadRewardAd(View view) {
        Appodeal.show(this, Appodeal.REWARDED_VIDEO);
    }
}
