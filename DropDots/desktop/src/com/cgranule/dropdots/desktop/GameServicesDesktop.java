package com.cgranule.dropdots.desktop;

import com.cgranule.dropdots.services.GameServices;

import java.util.HashMap;

/**
 * Created by Damar on 8/18/2015.
 */
public class GameServicesDesktop implements GameServices {
    @Override
    public void signIn() {

    }

    @Override
    public void submitScore(long score) {

    }

    @Override
    public void unlockingAchivement(String keyId) {

    }

    @Override
    public void incrementingAchievement(HashMap<String, Integer> achMap) {

    }

    @Override
    public void incrementingAchievement(String key, int step) {

    }

    @Override
    public void showLeaderboard() {

    }

    @Override
    public void showAchievement() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void requestPurchase(String productString) {

    }

    @Override
    public void requestPurchaseRestore() {

    }

    @Override
    public void showBannerAds() {

    }

    @Override
    public void hideBannerAds() {

    }

    @Override
    public void showInterstitial() {

    }

    @Override
    public boolean isBannerVisible() {
        return false;
    }

    @Override
    public void trackScreen(String screenName) {

    }

    @Override
    public void trackEvent(String screenName, String category, String action, String label) {

    }

    @Override
    public void shareScore(long score) {

    }

    @Override
    public void rateMe() {

    }

    @Override
    public boolean isInternetAvailable() {
        return false;
    }
}
