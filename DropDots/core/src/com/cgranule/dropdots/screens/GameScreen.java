package com.cgranule.dropdots.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.utils.Array;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.manager.SoundManager;
import com.cgranule.dropdots.actor.DotParticle;
import com.cgranule.dropdots.helper.AssetLoader;
import com.cgranule.dropdots.helper.Assets;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.utils.MultiViewportHandling;
import com.cgranule.dropdots.utils.SaveGameData;
import com.cgranule.dropdots.utils.ScreenShotFactory;
import com.cgranule.dropdots.services.GameServices;
import com.cgranule.dropdots.stages.GameStage;

/**
 * Created by Damar on 08/08/2015.
 */
public class GameScreen implements Screen
{
    public static final String TAG = GameScreen.class.getName();

    public static Camera camera;

    private Game game;

    private GameStage gameStage;
    private Stack stackGameover;

    private Button btnRate,btnAds,btnMenu,btnShare, btnPlay, btnLeaderboard, btnAchievement, btnSound;

    private Array<DotParticle> particleDots = new Array<DotParticle>();
    private Group groupDotParticle = new Group();
    private float timeToCreateParticle = 0;

    public GameScreen(Game game)
    {
        Gdx.app.debug(TAG, " : Initializing");

        this.game = game;
        initializeButton();

        AssetLoader.labelScore.setColor(Constants.getGreyColor());
        AssetLoader.labelScore.setText("0");
        AssetLoader.imgHighscore.setColor(Constants.getGreyColor());
        AssetLoader.labelHighscore.setColor(Constants.getGreyColor());
        AssetLoader.labelHighscore.setText(Integer.toString(SaveGameData.GetScore()));

        createGameoverUIStack();        //create the stackGameover
        gameStage = new GameStage(setupCamera());
        gameStage.addAction(alpha(0));


        gameStage.addActor(createScoreCircleUI());
        gameStage.addActor(stackGameover);
        gameStage.addActor(createScoreLabelUI());
        gameStage.addActor(createHighscoreTable());

        if(AssetLoader.gameServices.isInternetAvailable() && Constants.USING_IAP){
            AssetLoader.gameServices.requestPurchaseRestore();
            AssetLoader.gameServices.trackScreen("Gameplay Screen");
        }

        gameStage.addAction(fadeIn(.5f));
    }

    /**
     * Setup Camera
     * @return camera
     */
    private Camera setupCamera()
    {
        camera = new OrthographicCamera(MultiViewportHandling.FIXED_WIDTH, MultiViewportHandling.FIXED_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        return camera;
    }

    /**
     * this stack is only used for onOver
     */
    private void createGameoverUIStack()
    {
        stackGameover = new Stack();
        stackGameover.setFillParent(true);

        stackGameover.add(groupDotParticle);
        stackGameover.add(createButtonGameover());
        stackGameover.add(createCenterTableGameover());
        stackGameover.add(createButtonNoAds());
        stackGameover.setColor(new Color(1, 1, 1, 0));         //Not Visible
        stackGameover.setVisible(false);

    }

    private Table createHighscoreTable()
    {
        Table table = new Table();
        table.setFillParent(true);
        table.center().top().padTop(80);

        AssetLoader.imgHighscore.setColor(Constants.getGreyColor());
        table.add(AssetLoader.imgHighscore).expandX();
        table.row();

        table.add(AssetLoader.labelHighscore).padTop(5).expandX();

        return table;
    }

    /**
     * Create Score Label
     * @return score label for showing the score
     */
    private Table createScoreLabelUI()
    {
        Table table = new Table();
        table.setFillParent(true);
        table.center().padBottom(125);

        table.add(AssetLoader.labelScore);

        return table;
    }

    /**
     * create Circle back at score label
     * @return table that contain background label
     */
    private Table createScoreCircleUI()
    {
        Table table = new Table();
        table.setFillParent(true);
        table.center().padBottom(125);
        AssetLoader.scoreCircle.setColor(Constants.getGreyColor());
        table.add(AssetLoader.scoreCircle);
        return table;
    }

    /**
     * create scoreBackgroundRateShare for gameover
     * @return center table gameover
     */
    private Table createCenterTableGameover()
    {
        Table table = new Table();
        table.center().padBottom(125f);
        btnRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Rate
                SoundManager.playButton();
                AssetLoader.gameServices.rateMe();
            }
        });
        table.add(btnRate).bottom().padBottom(20).padRight(15);

        AssetLoader.scoreCircleGameover.setColor(Color.BLACK);
        table.add(AssetLoader.scoreCircleGameover);

        btnShare.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //share
                SoundManager.playButton();
                ScreenShotFactory.saveScreenshot();                                                 //Save the screen shot for sharing score
                AssetLoader.gameServices.shareScore(GameManager.getInstance().score);
            }
        });
        table.add(btnShare).bottom().padBottom(20).padLeft(15);

        return table;
    }

    /**
     * create button gameover
     * @return
     */
    private Table createButtonGameover()
    {
        Table table = new Table();
        table.center().bottom();

        //button retry
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //On Retry
                SoundManager.playButton();
                stackGameover.addAction(sequence(fadeOut(.25f), run(new Runnable() {
                    @Override
                    public void run() {
                        stackGameover.setVisible(false);        //set invisible stackGame gameover
                        gameStage.restartGame();                //restart the game
                    }
                })));
                AssetLoader.labelScore.setText("0");
                AssetLoader.labelScore.addAction(color(Constants.getGreyColor(), .25f));
                AssetLoader.imgHighscore.addAction(color(Constants.getGreyColor(), .25f));
                AssetLoader.labelHighscore.addAction(color(Constants.getGreyColor(), .25f));

                //Incrementing our achievements Play Times
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DROPPER, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DOTS_ADDICT, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DOTS_CAMPER, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_MASTER_DOTS_CATCHER, 1);
            }
        });
        table.add(btnPlay).colspan(3).padBottom(10);         //add buttonplay to table, merge 3 column and padding bottom 30
        table.row();                                        //Change line

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //back to main menu
                SoundManager.playButton();
                gameStage.addAction(fadeOut(.5f));
                stackGameover.addAction(sequence(fadeOut(.5f), run(new Runnable() {
                    @Override
                    public void run() {
                        AssetLoader.labelHighscore.setColor(Constants.getGreyColor());
                        game.setScreen(new MainMenuScreen(game));
                    }
                })));
            }
        });
        table.add(btnMenu).colspan(3).padBottom(15);
        table.row();

        table.add(AssetLoader.lineBottomButton).colspan(3).padBottom(20);
        table.row();

        btnLeaderboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Open Leaderboard
                SoundManager.playButton();
                AssetLoader.gameServices.showLeaderboard();
            }
        });
        table.add(btnLeaderboard).expandX();  //add buttonLeaderboard to table

        btnAchievement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Open Achievement
                SoundManager.playButton();
                AssetLoader.gameServices.showAchievement();
            }
        });
        table.add(btnAchievement).expandX();  //add buttonAchievement to table

        btnSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Toggle Sound
                GameManager.getInstance().isSoundActive = !GameManager.getInstance().isSoundActive;
                SoundManager.playButton();
                SaveGameData.SaveSoundStatus(GameManager.getInstance().isSoundActive);
            }
        });
        table.add(btnSound).expandX();

        table.padBottom(30);                                //padding bottom 20
        return table;
    }

    private Table createButtonNoAds()
    {
        Table table = new Table();
        table.setFillParent(true);
        table.right().top().padRight(25).padTop(80);

        btnAds.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //No ads
                SoundManager.playButton();
                if (AssetLoader.gameServices.isInternetAvailable() && Constants.USING_IAP) {
                    AssetLoader.gameServices.requestPurchase(Constants.IAP_NO_ADS);
                    AssetLoader.gameServices.trackEvent("Gameover Screen", "IAP", "Request Purchase", "No Ads");
                }
            }
        });
        table.add(btnAds);

        return table;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        //Clear Screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);

        //Update the stage
        gameStage.draw();
        gameStage.act(delta);

        if(GameManager.getInstance().gameState == GameManager.GameState.onPlay) {
            //Update Score
            AssetLoader.labelScore.setText(Integer.toString(gameStage.getScore()));
            AssetLoader.labelHighscore.setText(Integer.toString(SaveGameData.GetScore()));
        }
        else if(GameManager.getInstance().gameState == GameManager.GameState.onOver && GameManager.getInstance().setupState != GameManager.SetupState.onSetup)
        {
            //Show the over screen
            if(!stackGameover.isVisible()) {
                stackGameover.setVisible(true);
                stackGameover.addAction(fadeIn(.5f));
                AssetLoader.labelScore.addAction(color(Color.BLACK, .5f));
                AssetLoader.labelHighscore.setText(Integer.toString(SaveGameData.GetScore()));
                AssetLoader.imgHighscore.addAction(color(Color.BLACK, .5f));
                AssetLoader.labelHighscore.addAction(color(Color.BLACK, .5f));
            }
        }

        if(GameManager.getInstance().noAds && btnAds.isVisible())
        {
            btnAds.setVisible(false);
        }


        if(GameManager.getInstance().noAds)
        {
            if(AssetLoader.gameServices.isBannerVisible())
                AssetLoader.gameServices.hideBannerAds();
        }
        else
        {
            if(!AssetLoader.gameServices.isBannerVisible())
                AssetLoader.gameServices.showBannerAds();
        }


        if(btnSound.getStyle().equals(AssetLoader.styleSoundOn) && !GameManager.getInstance().isSoundActive)
            btnSound.setStyle(AssetLoader.styleSoundOff);
        else if(btnSound.getStyle().equals(AssetLoader.styleSoundOff) && GameManager.getInstance().isSoundActive)
            btnSound.setStyle(AssetLoader.styleSoundOn);

        timeToCreateParticle-=delta;
        if(timeToCreateParticle<= 0)
        {
            System.out.println("Create");
            createParticle();
            timeToCreateParticle = 1.5f;
        }
        for(DotParticle miniDot : particleDots){
            if(miniDot.getY() < -miniDot.getHeight())
            {
                System.out.println("delete");
                particleDots.removeValue(miniDot, false);
                miniDot.dispose();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        gameStage.dispose();
    }

    private void initializeButton(){
        btnPlay = new Button(Assets.getAssetSkin(), "play");
        btnLeaderboard = new Button(Assets.getAssetSkin(), "leaderboard");
        btnAchievement = new Button(Assets.getAssetSkin(), "achievement");
        btnSound = new Button(Assets.getAssetSkin(), "sound");
        btnRate = new Button(Assets.getAssetSkin(), "rate");
        btnAds = new Button(Assets.getAssetSkin(), "noads");
        btnMenu = new Button(Assets.getAssetSkin(), "menu");
        btnShare = new Button(Assets.getAssetSkin(), "share");
    }
    private void createParticle()
    {
        DotParticle particleDot = new DotParticle(Constants.COLOR_DOTS[MathUtils.random(0, Constants.COLOR_DOTS.length - 1)]);
        particleDot.speedFactor = MathUtils.random(0.5f, 3f);
        particleDot.setPosition(MathUtils.random(0, MultiViewportHandling.FIXED_WIDTH), MultiViewportHandling.FIXED_HEIGHT + particleDot.getHeight());
        System.out.println(particleDot.getX());
        particleDots.add(particleDot);
        groupDotParticle.addActor(particleDot);
    }
}
