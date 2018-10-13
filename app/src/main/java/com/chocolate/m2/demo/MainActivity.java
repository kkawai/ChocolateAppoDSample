package com.chocolate.m2.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.vdopia.ads.lw.Chocolate;
import com.vdopia.ads.lw.ChocolateAdException;
import com.vdopia.ads.lw.InitCallback;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.LVDOInterstitialAd;
import com.vdopia.ads.lw.LVDOInterstitialListener;
import com.vdopia.ads.lw.LVDORewardedAd;
import com.vdopia.ads.lw.RewardedAdListener;
import com.vdopia.ads.lw.VdopiaLogger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RewardedAdListener, LVDOInterstitialListener {

    public static final String TAG = "KevinAppodeal";

    private final static String CHOCOLATE_APP_KEY = "XqjhRR";
    private final static String APPODEAL_APP_KEY = "543259f795c32f48f68f8bcc0c527ed5569c643b83e76a0f";

    private LVDOAdRequest adRequest = new LVDOAdRequest(this);
    private LVDOInterstitialAd interstitialAd;
    private LVDORewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        VdopiaLogger.enable(true);
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, APPODEAL_APP_KEY, Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO);
        if (!Chocolate.isInitialized(this)) {
            Chocolate.init(this, CHOCOLATE_APP_KEY, new InitCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Chocolate.init success");
                    setPartners(adRequest);
                    LVDORewardedAd.prefetch(MainActivity.this, CHOCOLATE_APP_KEY, adRequest);
                }

                @Override
                public void onError(String s) {
                    Log.d(TAG, "Chocolate.init error: " + s);
                }

                @Override
                public void onAlreadyInitialized() {
                    Log.d(TAG, "Chocolate.init already initialized");
                }
            });
        } else {
            Log.d(TAG, "Chocolate is already initialized");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
                Log.d(TAG, "Appodeal onInterstitialLoaded");
            }

            @Override
            public void onInterstitialFailedToLoad() {
                Log.d(TAG, "Appodeal onInterstitialFailedToLoad");
            }

            @Override
            public void onInterstitialShown() {
                Log.d(TAG, "Appodeal onInterstitialShown");
            }

            @Override
            public void onInterstitialClicked() {
                Log.d(TAG, "Appodeal onInterstitialClicked");
            }

            @Override
            public void onInterstitialClosed() {
                Log.d(TAG, "Appodeal onInterstitialClosed");
            }
        });

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded() {
                Log.d(TAG, "Appodeal onRewardedVideoLoaded");
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                Log.d(TAG, "Appodeal onRewardedVideoFailedToLoad");
            }

            @Override
            public void onRewardedVideoShown() {
                Log.d(TAG, "Appodeal onRewardedVideoShown");
            }

            @Override
            public void onRewardedVideoFinished(int amount, String name) {
                Log.d(TAG, "Appodeal onRewardedVideoFinished");
            }

            @Override
            public void onRewardedVideoClosed(boolean finished) {
                Log.d(TAG, "Appodeal onRewardedVideoClosed");
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
                    dialog("isLimitAdTrackingEnabled: " + info.isLimitAdTrackingEnabled() + "\nAd Id: " + info.getId());
                } catch (Throwable t) {
                    Log.e(TAG, "AdvertisingIdClient.getAdvertisingIdInfo failed: " + t);
                    dialog("failed to get ad info: " + t);
                }

            }
        }.start();
    }

    private void dialog(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this).setMessage(string).show();
            }
        });
    }

    private void toast(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                //new AlertDialog.Builder(MainActivity.this).setMessage(string).show();
            }
        });
    }

    public void onLoadInterstitialAd(View view) {
        clearM2();
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL) && Appodeal.canShow(Appodeal.INTERSTITIAL)) {
            Log.d(TAG, "appodeal is loaded and can show Interstitial");
            Appodeal.show(this, Appodeal.INTERSTITIAL);
        } else {
            Log.d(TAG, "appodeal is not loaded and cannot show Interstitial");
        }
    }

    public void onLoadRewardAd(View view) {
        clearM2();
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO) && Appodeal.canShow(Appodeal.REWARDED_VIDEO)) {
            Log.d(TAG, "appodeal is loaded and can show Rewarded");
            Appodeal.show(this, Appodeal.REWARDED_VIDEO);
        } else {
            Log.d(TAG, "appodeal is not loaded and cannot show Rewarded");
        }
    }

    public void onLoadInterstitialChocolateAd(View view) {
        clearM2();
        setPartners(adRequest);
        interstitialAd.loadAd(adRequest);
    }

    public void onLoadRewardChocolateAd(View view) {
        clearM2();
        setPartners(adRequest);
        rewardedAd.loadAd(adRequest);
    }

    @Override
    public void onRewardedVideoLoaded(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "chocolate onRewardedVideoLoaded");
        try {
            this.rewardedAd.showRewardAd("secret234", "myUserId", "coins", "4.00");
        } catch (ChocolateAdException e) {
            Log.e(TAG, "chocolate  onRewardedVideoLoaded: "+e);
        }
    }

    @Override
    public void onRewardedVideoFailed(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "chocolate onRewardedVideoFailed");
        toast("Chocolate No-Fill for rewarded");
    }

    @Override
    public void onRewardedVideoShown(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "chocolate onRewardedVideoShown");
    }

    @Override
    public void onRewardedVideoShownError(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "chocolate onRewardedVideoShownError");
    }

    @Override
    public void onRewardedVideoDismissed(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "chocolate onRewardedVideoFinished: "+getWinner(lvdoRewardedAd));
        toast("Chocolate Dismissed: " + getWinner(lvdoRewardedAd));
        setPartners(adRequest);
        LVDORewardedAd.prefetch(MainActivity.this, CHOCOLATE_APP_KEY, adRequest);
    }

    private String getWinner(LVDORewardedAd lvdoRewardedAd) {
        return TextUtils.isEmpty(lvdoRewardedAd.getWinningPartnerName()) ? "AppoDeal" : lvdoRewardedAd.getWinningPartnerName();
    }

    private String getWinner(LVDOInterstitialAd lvdoInterstitialAd) {
        return TextUtils.isEmpty(lvdoInterstitialAd.getWinningPartnerName()) ? "AppoDeal" : lvdoInterstitialAd.getWinningPartnerName();
    }


    @Override
    public void onRewardedVideoCompleted(LVDORewardedAd lvdoRewardedAd) {
        Log.d(TAG, "chocolate onRewardedVideoCompleted");
    }

    @Override
    public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "chocolate onInterstitialLoaded");
        try {
            this.interstitialAd.show();
        } catch (ChocolateAdException e) {
            Log.e(TAG, "chocolate onInterstitialLoaded: " + e);
        }
    }

    @Override
    public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "chocolate onInterstitialFailed");
        toast("Chocolate No-Fill for Interstitial");
    }

    @Override
    public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "chocolate onInterstitialShown");
    }

    @Override
    public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "chocolate chocolate onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {
        Log.d(TAG, "chocolate onInterstitialDismissed");
        toast("Chocolate Dismissed: " + getWinner(lvdoInterstitialAd));
        setPartners(adRequest);
        LVDOInterstitialAd.prefetch(MainActivity.this, CHOCOLATE_APP_KEY, adRequest);
    }

    public void onLoadM2InterstitialChocFirst(View view) {
        setPartners(adRequest);
        adRequest.setSecondaryMediationApiKey(APPODEAL_APP_KEY);
        adRequest.setSecondaryMediationRunOrder(LVDOAdRequest.SecondaryMediationRunOrder.CHOCOLATE_MEDIATION_RUN_FIRST);
        interstitialAd.loadAd(adRequest);
    }

    public void onLoadM2InterstitialAppodealFirst(View view) {
        setPartners(adRequest);
        adRequest.setSecondaryMediationApiKey(APPODEAL_APP_KEY);
        adRequest.setSecondaryMediationRunOrder(LVDOAdRequest.SecondaryMediationRunOrder.SECONDARY_MEDIATION_RUN_FIRST);
        interstitialAd.loadAd(adRequest);
    }

    public void onLoadM2RewardedChocFirst(View view) {
        setPartners(adRequest);
        adRequest.setSecondaryMediationApiKey(APPODEAL_APP_KEY);
        adRequest.setSecondaryMediationRunOrder(LVDOAdRequest.SecondaryMediationRunOrder.CHOCOLATE_MEDIATION_RUN_FIRST);
        rewardedAd.loadAd(adRequest);
    }

    public void onLoadM2RewardedAppodealFirst(View view) {
        setPartners(adRequest);
        adRequest.setSecondaryMediationApiKey(APPODEAL_APP_KEY);
        adRequest.setSecondaryMediationRunOrder(LVDOAdRequest.SecondaryMediationRunOrder.SECONDARY_MEDIATION_RUN_FIRST);
        rewardedAd.loadAd(adRequest);
    }

    private void clearM2() {
        adRequest.setSecondaryMediationApiKey(null);
    }

    static String[] partners = new String[13];
    static {
        partners[0] = LVDOConstants.PARTNER.ADCOLONY.name();
        partners[1] = LVDOConstants.PARTNER.APPLOVIN.name();
        partners[2] = LVDOConstants.PARTNER.BAIDU.name();
        partners[3] = LVDOConstants.PARTNER.CHOCOLATE.name();
        partners[4] = LVDOConstants.PARTNER.FACEBOOK.name();
        partners[5] = LVDOConstants.PARTNER.GOOGLEADMOB.name();
        partners[6] = LVDOConstants.PARTNER.INMOBI.name();
        partners[7] = LVDOConstants.PARTNER.MOPUB.name();
        partners[8] = LVDOConstants.PARTNER.OGURY.name();
        partners[9] = LVDOConstants.PARTNER.TAPJOY.name();
        partners[10] = LVDOConstants.PARTNER.UNITY.name();
        partners[11] = LVDOConstants.PARTNER.VUNGLE.name();
        partners[12] = LVDOConstants.PARTNER.YOUAPPI.name();
    }

    static boolean[] selected = new boolean[13];
    static {
        for (int i=0;i<13;i++) {
            selected[i]=true;
        }
    }

    private List<LVDOConstants.PARTNER> setPartners(LVDOAdRequest adRequest) {
        List<LVDOConstants.PARTNER> list = new ArrayList<>(13);
        for (int i=0;i<13;i++) {
            if (selected[i]) {
                list.add(LVDOConstants.PARTNER.valueOf(partners[i]));
            }
        }
        adRequest.setPartnerNames(list);
        return list;
    }

    public void choosePartners(View view) {
        //w/out the listener, even though it does nothing, things don't work so well
        //so we need to pass a listener
        new AlertDialog.Builder(this).setMultiChoiceItems(partners, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        }).setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

}
