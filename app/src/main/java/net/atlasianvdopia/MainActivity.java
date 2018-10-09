package net.atlassianvdopia;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.vdopia.ads.lw.Chocolate;
import com.vdopia.ads.lw.InitCallback;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.LVDOInterstitialAd;
import com.vdopia.ads.lw.LVDOInterstitialListener;
import com.vdopia.ads.lw.LVDORewardedAd;
import com.vdopia.ads.lw.RewardedAdListener;
import com.vdopia.ads.lw.VdopiaLogger;

public class MainActivity extends AppCompatActivity implements RewardedAdListener, LVDOInterstitialListener {

    public static final String TAG = "KevinAppodeal";

    private final static String CHOCOLATE_APP_KEY = "XqjhRR";

    LVDOAdRequest adRequest = new LVDOAdRequest(this);
    LVDOInterstitialAd interstitialAd;
    LVDORewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        VdopiaLogger.enable(true);
        adRequest.addPartnerName(LVDOConstants.PARTNER.TAPJOY);
        String appoDealKey = "ccbd57eeec72f3baa5a081bc26d8e1ad9b7bbac0f1a4c273";
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, appoDealKey, Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO | Appodeal.MREC);
        Chocolate.init(this, CHOCOLATE_APP_KEY, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Chocolate.init success");
                LVDORewardedAd.prefetch(MainActivity.this, CHOCOLATE_APP_KEY, adRequest);
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "Chocolate.init error: " + s);
            }
        });

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

        interstitialAd = new LVDOInterstitialAd(this, CHOCOLATE_APP_KEY, this);
        rewardedAd = new LVDORewardedAd(this, CHOCOLATE_APP_KEY, this);

    }

    public void onCheckOptInSettings(final View view) {

        new Thread() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(view.getContext());
                    Log.d(TAG, "AdvertisingIdClient.getAdvertisingIdInfo. isLimitAdTrackingEnabled: " + info.isLimitAdTrackingEnabled());
                    toast("isLimitAdTrackingEnabled: " + info.isLimitAdTrackingEnabled() + "\nAd Id: " + info.getId());
                } catch (Throwable t) {
                    Log.e(TAG, "AdvertisingIdClient.getAdvertisingIdInfo failed: " + t);
                    toast("failed to get ad info: " + t);
                }

            }
        }.start();
    }

    private void toast(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this).setMessage(string).show();
            }
        });
    }

    public void onLoadInterstitialAd(View view) {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void onLoadRewardAd(View view) {
        Appodeal.show(this, Appodeal.REWARDED_VIDEO);
    }

    public void onLoadInterstitialChocolateAd(View view) {
        interstitialAd.loadAd(adRequest);
    }

    public void onLoadRewardChocolateAd(View view) {
        rewardedAd.loadAd(adRequest);
    }

    @Override
    public void onRewardedVideoLoaded(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "onRewardedVideoLoaded");
        try {
            lvdoRewardedAd.showRewardAd("secret234", "myUserId", "coins", "4.00");
        } catch (Exception e) {
            Log.e(TAG, "onRewardedVideoLoaded");
        }
    }

    @Override
    public void onRewardedVideoFailed(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "onRewardedVideoFailed");
    }

    @Override
    public void onRewardedVideoShown(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "onRewardedVideoShown");
    }

    @Override
    public void onRewardedVideoShownError(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "onRewardedVideoShownError");
    }

    @Override
    public void onRewardedVideoDismissed(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "onRewardedVideoFinished");
    }

    @Override
    public void onRewardedVideoCompleted(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "onRewardedVideoCompleted");
    }

    @Override
    public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "onInterstitialLoaded");
        try {
            interstitialAd.show();
        } catch (Exception e) {
            Log.e(TAG, "onInterstitialLoaded: " + e);
        }
    }

    @Override
    public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "onInterstitialFailed");
    }

    @Override
    public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "onInterstitialShown");
    }

    @Override
    public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "onInterstitialDismissed");
    }

}
