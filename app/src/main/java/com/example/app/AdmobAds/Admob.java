package com.example.app.AdmobAds;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Admob {

    OnDismiss onDismiss;

    public Admob(OnDismiss onDismiss){
        this.onDismiss = onDismiss;
    }

    public static void loadBannerAd(LinearLayout banner, Context context){
        if (AdsUnit.isAds) {
            MobileAds.initialize(context,initializationStatus -> {
                AdView adView = new AdView(context);
                banner.addView(adView);
                adView.setAdUnitId(AdsUnit.BANNER);
                adView.setAdSize(AdSize.BANNER);

                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            });
        }
    }

    public static void loadInterstitial(Context context){
        if (AdsUnit.isAds){
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(context, AdsUnit.INTERSTITIAL, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);

                            AdsUnit.mInterstitialAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);

                            AdsUnit.mInterstitialAd = interstitialAd;
                        }
                    });
        }
    }

    public static void showInterstitial(Activity activity, boolean isReload){
        if (AdsUnit.mInterstitialAd !=null){

            AdsUnit.mInterstitialAd.show(activity);

            AdsUnit.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    if (isReload){
                        AdsUnit.mInterstitialAd= null;
                        loadInterstitial(activity);
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }
            });

        }
    }

    public static void loadVideoRewarded(Context context){

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {


            }
        });

        AdRequest adRequest =new AdRequest.Builder().build();

        RewardedAd.load(context, AdsUnit.REWARDED,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                        AdsUnit.mInterstitialAd =null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);

                        AdsUnit.mRewardedAd = rewardedAd;
                    }
                });

    }

    public static void showRewarded(Activity activity, boolean isReload){

        if (AdsUnit.mRewardedAd !=null){

            AdsUnit.mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {

                    if (isReload){

                        AdsUnit.mRewardedAd=null;
                        loadVideoRewarded(activity);
                    }


                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {

                    AdsUnit.mRewardedAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }
            });

           AdsUnit.mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });


        }
    }




}
