package com.cgranule.dropdots.android;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.cgranule.dropdots.DropDotsGame;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.services.GameServices;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import java.io.File;
import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication implements GameServices {
    DropDotsGame game;

    //Google Play Game Service
    private GameHelper gameHelper;

    //Admob
    private AdView adViewBanner;
    private InterstitialAd interstitialAd;

    //Analytics
    private Tracker tracker;
    /**
     * Gets the default {@link Tracker} for this {@link android.app.Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker(Constants.GOOGLE_ANALYTICS_KEY);
        }
        return tracker;
    }

    //Share
    private Intent shareIntent;

    @Override
    public void rateMe() {
        Gdx.net.openURI(Constants.LINK_STORE + getPackageName());
    }

    @Override
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        game = new DropDotsGame(this);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        /////Hiding NAVBAR
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }
        ////END of Hiding NAVBAR

        ///Google Play Game Service
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {

            }

            @Override
            public void onSignInSucceeded() {

            }
        };
        gameHelper.setup(gameHelperListener);
        ///End of Google Play Game Service

        ///IAP
        DropDotsGame.setIapPlatformResolver(new GooglePlayResolver(game, this));
        ///END IAP

        //Analytics
        tracker = getDefaultTracker();
        tracker.enableAdvertisingIdCollection(true);

        ///Share
        setupShare();

        ///setup Interstitial
        setupInterstitialAds();

        ///Setup Layout for game and Admob Banner
        setupAdsBanner();
        RelativeLayout layout = new RelativeLayout(this);       //we want to create the layout view is stacking. (admob and libgdx game)
        View gameView = initializeForView(game, config);

        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout.addView(adViewBanner, params);
        ///END of Layout Admob And Game
        setContentView(layout);
	}



    @Override
    protected void onResume() {
        if(adViewBanner != null)
            adViewBanner.resume();
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adViewBanner != null)
            adViewBanner.destroy();
        game.getPlatformResolver().dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Function for Hiding Navbar
     */
    @TargetApi(19)
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * Function for Hide Navbar after showing navbar. (Swipe down visible Top UI)
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    @Override
    public void signIn() {
        try
        {
            runOnUiThread(new Runnable()
            {
                //@Override
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                // @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage()
                    + ".");
        }
    }

    private int REQUEST_CODE_UNUSED = 9002;
    @Override
    public void submitScore(long score) {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), Constants.LEADERBOARD_ID, score);
        } else {
// Maybe sign in here then redirect to submitting score?
        }
    }

    @Override
    public void unlockingAchivement(String keyId) {
        if(isSignedIn())
        {
            Games.Achievements.unlock(gameHelper.getApiClient(), keyId);
        }
    }

    @Override
    public void incrementingAchievement(HashMap<String, Integer> achMap) {
        if(isSignedIn())
        {
            for(String key : achMap.keySet())
            {
                Games.Achievements.increment(gameHelper.getApiClient(), key, achMap.get(key));
            }
        }
    }

    @Override
    public void incrementingAchievement(String key, int step)
    {
        if(isSignedIn())
        {
            Games.Achievements.increment(gameHelper.getApiClient(), key, step);
        }
    }

    @Override
    public void showLeaderboard() {
        if (isSignedIn() == true)
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), Constants.LEADERBOARD_ID), REQUEST_CODE_UNUSED);
        else
        {
// Maybe sign in here then redirect to showing scores?
            signIn();
        }
    }

    @Override
    public void showAchievement() {
        if(isSignedIn() == true)
        {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_CODE_UNUSED);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void requestPurchase(String productString) {
        game.getPlatformResolver().requestPurchase(productString);
        Gdx.app.log("gdx-pay : ", "PurchaseSystem.purchase");
    }

    @Override
    public void requestPurchaseRestore() {
        game.getPlatformResolver().requestPurchaseRestore();
    }

    @Override
    public void showBannerAds() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adViewBanner.setVisibility(View.VISIBLE);
                AdRequest.Builder builder = new AdRequest.Builder().tagForChildDirectedTreatment(true);
                AdRequest ad = builder.build();
                adViewBanner.loadAd(ad);
            }
        });
    }
    @Override
    public void hideBannerAds() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adViewBanner.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd.isLoaded())
                    interstitialAd.show();
            }
        });
    }

    @Override
    public boolean isBannerVisible() {
        return adViewBanner.isShown();
    }

    @Override
    public void trackScreen(String screenName) {
        Gdx.app.log("Analytics", "Track Screen");
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.setScreenName(null);
    }

    @Override
    public void trackEvent(String screenName, String category, String action, String label) {
        Gdx.app.log("Analytics", "Track Screen and Event");
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder()
        .setCategory(category)
        .setAction(action)
        .setLabel(label)
        .build());
        tracker.setScreenName(null);
    }

    @Override
    public void shareScore(long score) {
        String file = Constants.SCREENSHOT_PATH;
        Uri uriToImage = Uri.fromFile(new File(file));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I got " + score + "!! Can you beat it?" + Constants.LINK_STORE + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Score"));
    }

    void setupAdsBanner()
    {
        adViewBanner = new AdView(this);
        adViewBanner.setVisibility(View.INVISIBLE);
        adViewBanner.setBackgroundColor(0xffffffff);
        adViewBanner.setAdUnitId(Constants.ADMOB_BANNER_ANDROID);
        adViewBanner.setAdSize(AdSize.SMART_BANNER);
    }
    void setupInterstitialAds()
    {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(Constants.ADMOB_INTERSTITIAL_ANDROID);
        AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
                interstitialAd.loadAd(adRequest);
            }
        });
    }
    void setupShare()
    {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
    }
}
