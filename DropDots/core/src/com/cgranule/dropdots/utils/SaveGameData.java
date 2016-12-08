package com.cgranule.dropdots.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Damar on 8/17/2015.
 */
public class SaveGameData {

    public static Preferences preferences;

    public static void Initialize()
    {
        preferences = Gdx.app.getPreferences("SavedGame");
    }

    public static void SaveScore(int score)
    {
        if(preferences.getInteger("Best Score") < score)
        {
            preferences.putInteger("Best Score", score);
            preferences.flush();
        }
    }
    public static int GetScore()
    {
        return preferences.getInteger("Best Score");
    }

    public static void SaveSoundStatus(boolean bool)
    {
        preferences.putBoolean("Sound", bool);
        preferences.flush();
    }

    public static boolean GetSoundStatus()
    {
        return preferences.getBoolean("Sound");
    }
}
