package com.test.unitymediationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unity3d.mediation.IInitializationListener;
import com.unity3d.mediation.IInterstitialAdLoadListener;
import com.unity3d.mediation.IInterstitialAdShowListener;
import com.unity3d.mediation.IReward;
import com.unity3d.mediation.IRewardedAdLoadListener;
import com.unity3d.mediation.IRewardedAdShowListener;
import com.unity3d.mediation.InitializationConfiguration;
import com.unity3d.mediation.InterstitialAd;
import com.unity3d.mediation.RewardedAd;
import com.unity3d.mediation.UnityMediation;
import com.unity3d.mediation.errors.LoadError;
import com.unity3d.mediation.errors.SdkInitializationError;
import com.unity3d.mediation.errors.ShowError;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private Button rewardLoadBtn, rewardShowBtn, interstitialLoadBtn, interstitialShowBtn, initializeBtn;
    private int counterForRV;
    private TextView description, rewardDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counterForRV = 0;
        rewardLoadBtn = findViewById(R.id.rewardedLoad);
        rewardShowBtn = findViewById(R.id.rewardedShow);
        interstitialLoadBtn = findViewById(R.id.interstitialLoad);
        interstitialShowBtn = findViewById(R.id.interstitialShow);
        initializeBtn = findViewById(R.id.initialize);
        rewardDes = findViewById(R.id.rv_counter);
        description = findViewById(R.id.description);
        rewardLoadBtn.setOnClickListener(this);
        rewardShowBtn.setOnClickListener(this);
        initializeBtn.setOnClickListener(this);
        interstitialLoadBtn.setOnClickListener(this);
        interstitialShowBtn.setOnClickListener(this);

        rewardDes.setText(getString(R.string.reward_text, counterForRV));
        interstitialLoadBtn.setEnabled(false);
        interstitialShowBtn.setEnabled(false);
        rewardLoadBtn.setEnabled(false);
        rewardShowBtn.setEnabled(false);
    }

    private void initialize() {
        InitializationConfiguration configuration = InitializationConfiguration.builder()
                .setGameId(Constant.GAME_ID)
                .setInitializationListener(new IInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                        // Unity Mediation is initialized. Try loading an ad.
                        interstitialLoadBtn.setEnabled(true);
                        rewardLoadBtn.setEnabled(true);
                        System.out.println("Unity Mediation is successfully initialized.");
                        description.setText("Unity Mediation is successfully initialized.");
                    }

                    @Override
                    public void onInitializationFailed(SdkInitializationError sdkInitializationError, String errorMsg) {
                        System.out.println("Unity Mediation Failed to Initialize : " + errorMsg);
                        description.setText("Unity Mediation Failed to Initialize : " + errorMsg);

                    }
                }).build();

        UnityMediation.initialize(configuration);
    }

    private void loadReward() {
        rewardShowBtn.setEnabled(false);
        rewardedAd = new RewardedAd(this, Constant.REWARDED);
        final IRewardedAdLoadListener loadListener = new IRewardedAdLoadListener() {
            @Override
            public void onRewardedLoaded(RewardedAd ad) {
                rewardShowBtn.setEnabled(true);
                description.setText("Rewarded Ad Loaded");
                // Execute logic when the ad successfully loads.
            }

            @Override
            public void onRewardedFailedLoad(RewardedAd ad, LoadError error, String msg) {
                // Execute logic when the ad fails to load.
                description.setText("Rewarded Ad Loaded Error: " + msg);
            }
        };
        rewardedAd.load(loadListener);

    }

    private void showReward() {
        if (rewardedAd != null) {
            final IRewardedAdShowListener showListener = new IRewardedAdShowListener() {
                @Override
                public void onRewardedShowed(RewardedAd ad) {
                    System.out.println("onRewardedShowed");
                    // The ad has started to show.
                }

                @Override
                public void onRewardedClicked(RewardedAd ad) {
                    System.out.println("onRewardedClicked");
                    // The user has clicked on the ad.
                }

                @Override
                public void onRewardedClosed(RewardedAd ad) {
                    System.out.println("onRewardedClosed");
                    // The ad has finished showing.
                    rewardShowBtn.setEnabled(false);
                }

                @Override
                public void onRewardedFailedShow(RewardedAd ad, ShowError error, String msg) {
                    System.out.println("onRewardedFailedShow: " + msg);
                    // An error occurred during the ad playback.
                }

                @Override
                public void onUserRewarded(RewardedAd ad, IReward reward) {
                    // The user should be rewarded for watching the ad.
                    System.out.println("onUserRewarded");
                    rewardDes.setText(getString(R.string.reward_text, ++counterForRV));

                }
            };

            rewardedAd.show(showListener);
        }
    }


    private void loadInterstitial() {
        interstitialShowBtn.setEnabled(false);
        interstitialAd = new InterstitialAd(this, Constant.INTERSTITIAL);
        final IInterstitialAdLoadListener loadListener = new IInterstitialAdLoadListener() {
            @Override
            public void onInterstitialLoaded(InterstitialAd ad) {
                // Execute logic when the ad successfully loads.
                interstitialShowBtn.setEnabled(true);
                description.setText("Interstitial Ad Loaded");
            }

            @Override
            public void onInterstitialFailedLoad(InterstitialAd ad, LoadError error, String msg) {
                // Execute logic when the ad fails to load.
                description.setText("Interstitial Ad Loaded Error: " + msg);
            }
        };

        interstitialAd.load(loadListener);
    }

    private void showInterstitial() {
        if(interstitialAd == null){
            return;
        }

        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
            @Override
            public void onInterstitialShowed(InterstitialAd interstitialAd) {
                System.out.println("onInterstitialShowed");
                // The ad has started to show.
            }

            @Override
            public void onInterstitialClicked(InterstitialAd interstitialAd) {
                System.out.println("onInterstitialClicked");
                // The user has clicked on the ad.
            }

            @Override
            public void onInterstitialClosed(InterstitialAd interstitialAd) {
                System.out.println("onInterstitialClosed");
                // The ad has finished showing.
                description.setText("Interstitial has been closed");
                interstitialShowBtn.setEnabled(false);
            }

            @Override
            public void onInterstitialFailedShow(InterstitialAd interstitialAd, ShowError error, String msg) {
                System.out.println("onInterstitialFailedShow");
                // An error occurred during the ad playback.
                description.setText("Interstitial failed to show due to " + msg);
            }
        };

        interstitialAd.show(showListener);

    }

    @Override
    public void onClick(View view) {
        if (view == initializeBtn) {
            initialize();
        } else if (view == interstitialLoadBtn) {
            loadInterstitial();
        } else if (view == interstitialShowBtn) {
            showInterstitial();
        } else if (view == rewardLoadBtn) {
            loadReward();
        } else if (view == rewardShowBtn) {
            showReward();
        }
    }
}
