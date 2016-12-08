package com.cgranule.dropdots.manager;

/**
 * Created by Damar on 8/13/2015.
 */
public class GameManager {
    public static GameManager mInstance = new GameManager();
    public static GameManager getInstance(){
        return mInstance;
    }

    public enum GameState{
        onMenu, onPlay, onPause, onOver
    }
    public GameState gameState = GameState.onMenu;

    public enum SetupState{
        onSetup, onFinishSetup
    }
    public SetupState setupState = SetupState.onSetup;
    public int score = 0;

    public boolean noAds = false;
    public boolean isSoundActive = true;

}
