package com.cgranule.dropdots.services;

import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;

import java.util.HashMap;

/**
 * Created by Damar on 8/17/2015.
 */
public interface GameServices {
    //Google Play Game Services
    public void signIn();
    public void submitScore(long score);
    public void unlockingAchivement(String keyId);
    public void incrementingAchievement(HashMap<String, Integer> achMap);
    public void incrementingAchievement(String key, int step);
    public void showLeaderboard();
    public void showAchievement();
    public boolean isSignedIn();

    //IAP
    public void requestPurchase(String productString);
    public void requestPurchaseRestore();

    //Admob
    public void showBannerAds();
    public void hideBannerAds();
    public void showInterstitial();
    public boolean isBannerVisible();

    //Analytics
    public void trackScreen(String screenName);
    public void trackEvent(String screenName, String category, String action, String label);

    //Share
    public void shareScore(long score);

    //Rate
    public void rateMe();

    //Utils
    public boolean isInternetAvailable();

}
