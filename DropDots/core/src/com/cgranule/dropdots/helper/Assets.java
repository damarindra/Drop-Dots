package com.cgranule.dropdots.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.utils.Constants;

/**
 * Created by Damar on 8/20/2015.
 */
public class Assets {

    public static AssetManager manager;

    public static void load()
    {
        manager = new AssetManager();

        manager.load(Constants.SKIN_PATH, Skin.class);
        manager.load(Constants.SOUND_ZAP, Music.class);
        manager.load(Constants.SOUND_BUTTON, Sound.class);
    }

    public static Sound getAssetSound(String fileName) {
        return Assets.manager.get(fileName, Sound.class);
    }
    public static Music getAssetMusic(String fileName) {
        return Assets.manager.get(fileName, Music.class);
    }
    public static Skin getAssetSkin(){
        return Assets.manager.get(Constants.SKIN_PATH, Skin.class);
    }
}
