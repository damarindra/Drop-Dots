package com.cgranule.dropdots.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Damar on 08/08/2015.
 */
public class Constants
{
    public static final String GAME_NAME = "Drop Dots";

    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;
    public static final float Y_BOTTOM_POSITION = SCREEN_HEIGHT * 0.05f;

    public static final String SKIN_PATH = "game.json";
    public static final String TEXTURE_ATLAS_PATH = "game.atlas";

    public static final float TIME_SPAWN_DOTS = 3.5f;
    public static final float MINI_DOT_SPEED = 4;

    public static final Color BACKGROUND_COLOR = new Color(1,1,1,1);

    public static final Color[] COLOR_DOTS = new Color[]{
            new Color(26/255f, 181/255f, 163/255f, 255/255f),        //Blue
            new Color(120/255f, 194/255f, 107/255f, 255/255f),         //Green
            new Color(255/255f, 124/255f, 6/255f, 255/255f),        //Orange
            new Color(139/255f, 107/255f, 194/255f, 255/255f),        //Purple
            new Color(255/255f, 81/255f, 96/255f, 255/255f)         //Pink
    };

    public static Color getBlueColor()
    {
        return COLOR_DOTS[0];
    }
    public static Color getGreenColor()
    {
        return COLOR_DOTS[1];
    }
    public static Color getOrangeColor()
    {
        return COLOR_DOTS[2];
    }
    public static Color getPurpleColor()
    {
        return COLOR_DOTS[3];
    }
    public static Color getPinkColor()
    {
        return COLOR_DOTS[4];
    }
    public static Color getGreyColor()
    {
        return new Color(200/255f,200/255f,200/255f,255/255f);
    }

    public static final String SOUND_ZAP = "sounds/zap1.ogg";
    public static final String SOUND_BUTTON = "sounds/pop.ogg";


    public static final String SCREENSHOT_PATH = Gdx.files.getExternalStoragePath() + "Pictures/Screenshots/share.jpeg";
    public static final String LINK_STORE = "https://play.google.com/store/apps/details?id=";

    //Google Play Game Services
    public static final String LEADERBOARD_ID = "CgkIqJH2vuUeEAIQAA";                               //ID Leaderboard
    public static final String ACHIEVEMENT_FIRST_DOT = "CgkIqJH2vuUeEAIQAw";                        //ID Acc for first play - Unlock
    public static final String ACHIEVEMENT_10_DOT = "CgkIqJH2vuUeEAIQBA";                           //ID acc for scoring 10 - Unlock
    public static final String ACHIEVEMENT_30_DOT = "CgkIqJH2vuUeEAIQBQ";                           //ID acc for scoring 30 - Unlock
    public static final String ACHIEVEMENT_50_DOT = "CgkIqJH2vuUeEAIQBg";                           //ID acc for scoring 50 - Unlock
    public static final String ACHIEVEMENT_100_DOT = "CgkIqJH2vuUeEAIQBw";                          //ID acc for scoring 100 - Unlock
    public static final String ACHIEVEMENT_ARE_YOU_KIDDING_ME = "CgkIqJH2vuUeEAIQCA";               //ID acc for scoring 250 - Unlock
    public static final String ACHIEVEMENT_DROPPER = "CgkIqJH2vuUeEAIQCQ";                          //ID acc for play Drop Dots 25 Times - Increment
    public static final String ACHIEVEMENT_DOTS_ADDICT = "CgkIqJH2vuUeEAIQCg";                      //ID acc for play Drop Dots 50 Times - Increment
    public static final String ACHIEVEMENT_DOTS_CAMPER = "CgkIqJH2vuUeEAIQDQ";                      //ID acc for play Drop Dots 100 Times - Increment
    public static final String ACHIEVEMENT_MASTER_DOTS_CATCHER = "CgkIqJH2vuUeEAIQCw";              //ID acc for play Drop Dots 250 Times - Increment
    public static final String ACHIEVEMENT_5_COMBOS = "CgkIqJH2vuUeEAIQDA";                         //ID acc for can catch 50 Dots at one time - Unlock
    public static final String ACHIEVEMENT_ROOKIE_CATCHER = "CgkIqJH2vuUeEAIQDg";                   //ID acc for total catch dot 100 - Increment
    public static final String ACHIEVEMENT_MASTER_CATCHER = "CgkIqJH2vuUeEAIQDw";                   //ID acc for total catch dot 250 - Increment

    //Admob
    public static final String ADMOB_BANNER_ANDROID = "ca-app-pub-7709531834691151/8107170224";
    public static final String ADMOB_INTERSTITIAL_ANDROID = "ca-app-pub-7709531834691151/7032233026";
    public static final int INTERSTITIAL_SHOW_FREQUENCY = 4;                                        //Show interstitial ads per INTERSTITIAL_SHOW_FREQUENCY Game

    //IAP
    public static final String GOOGLE_KEY = "";
    public static final String IAP_NO_ADS = "no_ads";

    //Analytics
    public static final String GOOGLE_ANALYTICS_KEY = "UA-62173969-4";

    //Toggle IAP
    public static final boolean USING_IAP = true;
}
