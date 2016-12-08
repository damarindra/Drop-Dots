package com.cgranule.dropdots.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cgranule.dropdots.manager.SoundManager;
import com.cgranule.dropdots.helper.AssetLoader;
import com.cgranule.dropdots.helper.Assets;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.utils.MultiViewportHandling;
import com.cgranule.dropdots.utils.SaveGameData;
import com.cgranule.dropdots.actor.BigDot;
import com.cgranule.dropdots.actor.MiniDot;

import java.util.HashMap;

/**
 * Created by Damar on 08/08/2015.
 */
public class GameStage extends Stage {
    private static final String TAG = GameStage.class.getName();
    int indexDots;
    private BigDot[] bigDots;
    private float dotXOriginPosPickedTemporary = 99f;               //set to 99, because we use this var to condition with 99, see bellow
    private Array<MiniDot> miniDots = new Array<MiniDot>();
    private float timeToSpawnMiniDotInSeconds = Constants.TIME_SPAWN_DOTS;
    private int counterMiniDotSpawn = 0;
    private float timerToSpawnMiniDot = 0;
    private boolean isTouched = false;
    private Vector3 touchPosition;
    private int score = 0;
    //interstitial show counter
    private int interstitialShowCounter = 0;

    private boolean chanceToUnlock5Combos = false;                                                  //set true if mini dot spawn 5
    private int ach5CombosCounter = 0;                                                              //Counter for combos, iterate if chanceToUnlock5Combos is true and miniDot catched

    private HashMap<String, Integer> achievementCatcherMap = new HashMap<String, Integer>();                        //for temp store Catch Dots, use this because we didn't want to submit our progress Iteration to Achievement

    //Sound Manager
    private Assets assets;

    public GameStage(Camera camera) {
        super(new FitViewport(MultiViewportHandling.FIXED_WIDTH, MultiViewportHandling.FIXED_HEIGHT, camera));
        Gdx.app.debug(TAG, " : Initializing");

        setupDots();

        assets = new Assets();
    }

    /**
     * This funtion for swaping array type data
     *
     * @param a   is for array
     * @param i   is for index 1
     * @param j   is for index 2
     * @param <T> type of array
     */
    public static final <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * Create 5 bigDots at bottom for player swapping the dot
     */
    private void setupDots() {
        Gdx.app.debug(TAG, " : Setup Dots");

        GameManager.getInstance().setupState = GameManager.SetupState.onSetup;          //Set setupState to onSetup;

        bigDots = new BigDot[]{new BigDot(Constants.getBlueColor())
                , new BigDot(Constants.getGreenColor())
                , new BigDot(Constants.getOrangeColor())
                , new BigDot(Constants.getPurpleColor())
                , new BigDot(Constants.getPinkColor())};
        int i = 0;
        for (BigDot dot : bigDots) {
            dot.setOrigin(dot.getWidth() / 2, dot.getHeight() / 2);
            dot.setPositionRelativeOrigin((i * (getCamera().viewportWidth / 5) + (getCamera().viewportWidth / 5) / 2) //set X position at
                    , (-(Constants.Y_BOTTOM_POSITION * 2)));                     //set Y position at Y_BOTTOM_POSITION (negatif -> set to the bottom until not visible. because we will add some action(animation)
            dot.setxPosOrigin(dot.getX());
            dot.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);     //get the touch position
                    getCamera().unproject(touchPosition);                                   //unproject it, to find the touchPosition with camera projection
                    if (touchPosition.x < getCamera().viewportWidth / 5)
                        indexDots = 0;
                    else if (touchPosition.x < 2 * (getCamera().viewportWidth / 5))
                        indexDots = 1;
                    else if (touchPosition.x < 3 * (getCamera().viewportWidth / 5))
                        indexDots = 2;
                    else if (touchPosition.x < 4 * (getCamera().viewportWidth / 5))
                        indexDots = 3;
                    else if (touchPosition.x < 5 * (getCamera().viewportWidth / 5))
                        indexDots = 4;
                    //dotChoosenTemporary = new Dot(bigDots[indexDots]);         //Just Temporary Dot!
                    dotXOriginPosPickedTemporary = bigDots[indexDots].getxPosOrigin();
                    isTouched = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    isTouched = false;
                }
            });
            addActor(bigDots[i]);
            i += 1;
        }
        timeToSpawnMiniDotInSeconds = Constants.TIME_SPAWN_DOTS;
        timerToSpawnMiniDot = 0;
        bounceUpGroupDots(bigDots);
    }

    private void bounceUpGroupDots(BigDot[] bigDot) {
        float t = 1.5f;
        for (BigDot dot : bigDot) {
            final BigDot finalDot = dot;
            dot.addAction(sequence(moveTo(dot.getX(), Constants.Y_BOTTOM_POSITION, t, Interpolation.bounceOut), run(new Runnable() {
                //If animation ended, set state to onPlay
                @Override
                public void run() {
                    if (GameManager.getInstance().gameState != GameManager.GameState.onPlay) {
                        if (finalDot.dotColor == bigDots[bigDots.length - 1].dotColor)         //check if final dot is equal with last array member of bigdots
                        {
                            GameManager.getInstance().gameState = GameManager.GameState.onPlay;            //Set to onPlay
                            GameManager.getInstance().setupState = GameManager.SetupState.onFinishSetup;    //if animation end, setup set to complete
                        }
                    }
                }
            })));
            t += .2f;
        }
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /**
         * Check condition if onplay or onover?
         */
        if (GameManager.getInstance().gameState == GameManager.GameState.onPlay || GameManager.getInstance().gameState == GameManager.GameState.onOver) {

            //If still onPlay, you can control and produce the miniDot!
            if (GameManager.getInstance().gameState == GameManager.GameState.onPlay) {
                /**
                 * This is for logic Dots Controller (Bottom)
                 */
                //Logic
                if (isTouched) {         //Check if its touched?
                    touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);             //get the touch position
                    getCamera().unproject(touchPosition);                                           //unproject the touch position to camera
                    bigDots[indexDots].setXPositionRelativeOrigin(touchPosition.x);                 //set the X position of dot clicked
                } else {
                    if (!isTouched && (dotXOriginPosPickedTemporary != 99f))                        //if !isTouched(touch up) and posOriginX not 99
                    {
                        float xDotPosReleaseTemp = bigDots[indexDots].getX() + bigDots[indexDots].getWidth() / 2;          //var for check position between 5 bigDots
                        if (xDotPosReleaseTemp < getCamera().viewportWidth / 5)                     //first rows at bigDots position
                        {
                            if (bigDots[0].dotType == bigDots[indexDots].dotType)
                                bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                            else {
                                bigDots[indexDots].addAction(moveTo(bigDots[0].getxPosOrigin(), bigDots[0].getY(), 0.2f));
                                bigDots[0].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                                //swap the xOriginPosition
                                bigDots[indexDots].setxPosOrigin(bigDots[0].getxPosOrigin());
                                bigDots[0].setxPosOrigin(dotXOriginPosPickedTemporary);
                                swap(bigDots, indexDots, 0);
                            }
                        } else if (xDotPosReleaseTemp < (getCamera().viewportWidth / 5) * 2) {
                            if (bigDots[1].dotType == bigDots[indexDots].dotType)
                                bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                            else {
                                bigDots[indexDots].addAction(moveTo(bigDots[1].getxPosOrigin(), bigDots[1].getY(), 0.2f));
                                bigDots[1].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                                bigDots[indexDots].setxPosOrigin(bigDots[1].getxPosOrigin());
                                bigDots[1].setxPosOrigin(dotXOriginPosPickedTemporary);
                                swap(bigDots, indexDots, 1);

                            }
                        } else if (xDotPosReleaseTemp < (getCamera().viewportWidth / 5) * 3) {
                            if (bigDots[2].dotType == bigDots[indexDots].dotType)
                                bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                            else {
                                bigDots[indexDots].addAction(moveTo(bigDots[2].getxPosOrigin(), bigDots[2].getY(), 0.2f));
                                bigDots[2].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                                bigDots[indexDots].setxPosOrigin(bigDots[2].getxPosOrigin());
                                bigDots[2].setxPosOrigin(dotXOriginPosPickedTemporary);
                                swap(bigDots, indexDots, 2);

                            }
                        } else if (xDotPosReleaseTemp < (getCamera().viewportWidth / 5) * 4) {
                            if (bigDots[3].dotType == bigDots[indexDots].dotType)
                                bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                            else {
                                bigDots[indexDots].addAction(moveTo(bigDots[3].getxPosOrigin(), bigDots[3].getY(), 0.2f));
                                bigDots[3].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                                bigDots[indexDots].setxPosOrigin(bigDots[3].getxPosOrigin());
                                bigDots[3].setxPosOrigin(dotXOriginPosPickedTemporary);
                                swap(bigDots, indexDots, 3);

                            }
                        } else if (xDotPosReleaseTemp < ((getCamera().viewportWidth / 5) * 5) + 30) {
                            if (bigDots[4].dotType == bigDots[indexDots].dotType)
                                bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                            else {
                                bigDots[indexDots].addAction(moveTo(bigDots[4].getxPosOrigin(), bigDots[4].getY(), 0.2f));
                                bigDots[4].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                                bigDots[indexDots].setxPosOrigin(bigDots[4].getxPosOrigin());
                                bigDots[4].setxPosOrigin(dotXOriginPosPickedTemporary);
                                swap(bigDots, indexDots, 4);

                            }
                        } else {
                            Gdx.app.error("Touch UP : ", "Error, position touch up is more than screen width");
                            bigDots[indexDots].addAction(moveTo(bigDots[indexDots].getxPosOrigin(), bigDots[indexDots].getY(), 0.2f));
                        }

                        dotXOriginPosPickedTemporary = 99f;                                         //set the wrong number, to handle touchUp condition, swaping condition
                    }
                }
                /**
                 * End Condition Controller
                 */

                /**
                 * Logic of miniDots
                 */
                timerToSpawnMiniDot += delta;
                if (timerToSpawnMiniDot >= timeToSpawnMiniDotInSeconds)                              //check if timer has reach Time to spawn
                {
                    //Random condition how much mini dot will spawn
                    int randomManyDot;
                    if(counterMiniDotSpawn < 3){
                        randomManyDot = 1;
                    }
                    else if(counterMiniDotSpawn < 7)
                    {
                        randomManyDot = MathUtils.random(1, 3);
                    }
                    else if(counterMiniDotSpawn < 13){
                        randomManyDot = MathUtils.random(1, 5);
                    }
                    else if( counterMiniDotSpawn < 20)
                        randomManyDot = MathUtils.random(2, 5);
                    else
                        randomManyDot = MathUtils.random(3, 5);
                    if(randomManyDot == 5)
                        chanceToUnlock5Combos = true;
                    spawnMiniDot(randomManyDot);

                    counterMiniDotSpawn += 1;                                                       //iterate the counter dot spawn
                    if (counterMiniDotSpawn % 5 == 0 && counterMiniDotSpawn < 40)                    //check if counter dot spawn is reach multiple with 8
                        timeToSpawnMiniDotInSeconds *= .8f;                                         // then set the time to Spawn product with .8f

                    timerToSpawnMiniDot = 0;                                                        //set it to 0
                }
            }

            //Logic if dot reach to BigDots
            for (MiniDot dot : miniDots) {
                if (dot.getY() <= (Constants.Y_BOTTOM_POSITION + dot.getHeight() / 4) && dot.getY() > Constants.Y_BOTTOM_POSITION - (dot.getHeight() * 2)) {
                    if (dot.dotType == bigDots[dot.getXPos()].dotType)                              //Check if miniDot same with big dot
                    {
                        score += 1;
                        if(chanceToUnlock5Combos)                                                   //check if 5 combos true for unlocking the 5Combos Achievements
                        {
                            ach5CombosCounter += 1;                                                 //Iterate the counter
                            if(ach5CombosCounter == 5)                                              //check if combos counter reach to 5
                                AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_5_COMBOS);   //Unlock Achievement Combos
                        }
                        SoundManager.playZap();

                        achievementCatcherMap.put(Constants.ACHIEVEMENT_ROOKIE_CATCHER, score);     //put the key and the value
                        achievementCatcherMap.put(Constants.ACHIEVEMENT_MASTER_CATCHER, score);     //put the key and the value
                        //If game has been over, dont forget to send this map to our Achievements
                    }
                    else        //Game Over
                    {
                        dot.dotColor = Constants.getGreyColor();
                        for (final BigDot bigDot : bigDots) {
                            bigDot.addAction(sequence(parallel(scaleTo(2, 2, .4f), fadeOut(.4f)), run(new Runnable() {
                                @Override
                                public void run() {
                                    bigDot.dispose();
                                }
                            })));
                        }
                        GameManager.getInstance().gameState = GameManager.GameState.onOver;         //Change State to onOver

                        GameManager.getInstance().score = score;                                    //set score to GameManager.getInstance().score. purpose to access score from other screen
                        SaveGameData.SaveScore(score);                                              //Save to local Disk
                        AssetLoader.gameServices.submitScore(score);                                            //Submit to Leaderboard
                        checkIfScoreUnlockingAchievement();                                         //Check if score can Unlock Achievement
                        AssetLoader.gameServices.incrementingAchievement(achievementCatcherMap);                //send our progress catcher to achievements
                        AssetLoader.gameServices.trackScreen("Gameover Screen");

                        counterMiniDotSpawn = 0;                                                    //reset it to 0, we need to add the speed spawn in the new game

                        if(!GameManager.getInstance().noAds)
                        {
                            interstitialShowCounter += 1;       //iterate the counter
                            if (interstitialShowCounter >= Constants.INTERSTITIAL_SHOW_FREQUENCY)       //check if counter has reach the frequency
                            {
                                AssetLoader.gameServices.showInterstitial();                                   //Show Interstitial Ads
                                interstitialShowCounter = 0;                                            //Reset counter Interstitial to zero
                            }
                        }
                    }
                    dot.playEffect();
                    final MiniDot d = dot;
                    dot.setScale(1.5f, 1.5f);
                    dot.addAction(sequence(parallel(scaleTo(4, 4, .4f), fadeOut(.4f)), run(new Runnable() {     //change the dot to effect
                        @Override
                        public void run() {
                            d.dispose();
                        }
                    })));
                    miniDots.removeValue(d, false);
                }

                //If Game Over, delete all mini dot
                if (GameManager.getInstance().gameState == GameManager.GameState.onOver) {
                    dot.dotColor = Constants.getGreyColor();
                    dot.playEffect();
                    final MiniDot d = dot;
                    dot.setScale(1.5f, 1.5f);
                    dot.addAction(sequence(parallel(scaleTo(4, 4, .4f), fadeOut(.4f)), run(new Runnable() {     //change the dot to effect
                        @Override
                        public void run() {
                            d.dispose();
                        }
                    })));
                    miniDots.removeValue(d, false);
                }
            }
        }
    }

    void spawnMiniDot(int manyDot) {
        Array<Color> dotColor = new Array<Color>();
        dotColor.addAll(Constants.COLOR_DOTS);
        Array<Integer> dotXPos = new Array<Integer>(5);
        dotXPos.add(0);
        dotXPos.add(1);
        dotXPos.add(2);
        dotXPos.add(3);
        dotXPos.add(4);
        for (int i = 0; i < manyDot; i++) {
            Color randomColor = dotColor.get(MathUtils.random(0, dotColor.size - 1));                  //Random the miniDot Color
            int randomXPos = dotXPos.get(MathUtils.random(0, dotXPos.size - 1));                       //Random X Position
            MiniDot miniDot = new MiniDot(randomColor, randomXPos);
            dotColor.removeValue(randomColor, false);
            dotXPos.removeValue(randomXPos, false);

            miniDots.add(miniDot);
            addActor(miniDot);                                        //add the minidot to stage
        }
    }

    public void restartGame() {
        score = 0;
        setupDots();
    }

    public int getScore() {
        return score;
    }

    void checkIfScoreUnlockingAchievement()
    {
        if(score >= 10)
            AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_10_DOT);
        if(score >= 30)
            AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_30_DOT);
        if(score >= 50)
            AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_50_DOT);
        if(score >= 100)
            AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_100_DOT);
        if(score >= 250)
            AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_ARE_YOU_KIDDING_ME);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
