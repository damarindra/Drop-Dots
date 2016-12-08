package com.cgranule.dropdots.manager;

import com.cgranule.dropdots.helper.Assets;
import com.cgranule.dropdots.utils.Constants;

/**
 * Created by damar on 21/08/15.
 */
public class SoundManager {

    public static void playZap()
    {
        if(GameManager.getInstance().isSoundActive && !Assets.getAssetMusic(Constants.SOUND_ZAP).isPlaying())
            Assets.getAssetMusic(Constants.SOUND_ZAP).play();
    }
    public static void playButton()
    {
        if(GameManager.getInstance().isSoundActive)
            Assets.getAssetSound(Constants.SOUND_BUTTON).play();
    }
}
