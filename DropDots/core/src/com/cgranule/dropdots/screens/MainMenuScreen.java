package com.cgranule.dropdots.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.manager.SoundManager;
import com.cgranule.dropdots.helper.AssetLoader;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.utils.MultiViewportHandling;
import com.cgranule.dropdots.utils.SaveGameData;
import com.cgranule.dropdots.actor.DotParticle;
import com.cgranule.dropdots.helper.Assets;

/**
 * Created by Damar on 08/08/2015.
 */
public class MainMenuScreen implements Screen
{
    public static final String TAG = MainMenuScreen.class.getName();

    private Game game;

    private Stage stage;

    private Stack stack;

    private float timeToCreateParticle =0;

    private Array<DotParticle> particleDots = new Array<DotParticle>();
    private Group groupDotParticle = new Group();

    private Button btnPlay, btnCredits, btnLeaderboard, btnAchievement, btnSound;

    public MainMenuScreen(Game game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(MultiViewportHandling.FIXED_WIDTH, MultiViewportHandling.FIXED_HEIGHT, setupCamera()));//MultiViewportHandling.getViewportKeepWidthInVector2().x, Constants.SCREEN_HEIGHT, setupCamera()));
        stack = new Stack();
        stack.setFillParent(true);
        stack.setVisible(false);
        stack.setColor(1, 1, 1, 0);

        initializeButton();

        stack.addActor(groupDotParticle);
        stack.add(setupUIButtonMainemenu());
        stack.add(setupUITextMainMenu());
        stage.addActor(stack);

        stack.setVisible(true);
        stack.addAction(sequence(alpha(0), fadeIn(1.5f)));

        AssetLoader.gameServices.hideBannerAds();
        AssetLoader.gameServices.trackScreen("Main Menu Screen");
    }
    private Camera setupCamera()
    {
        Camera camera = new OrthographicCamera(MultiViewportHandling.FIXED_WIDTH, MultiViewportHandling.FIXED_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        return camera;
    }

    private Table setupUITextMainMenu()
    {
        Table table = new Table();
        table.center().top();
        table.padTop(70);

        table.add(AssetLoader.labelTitle);
        table.row();

        table.add(AssetLoader.lineTittle).padTop(20);
        table.row();

        AssetLoader.imgHighscore.setColor(Constants.getGreyColor());
        table.add(AssetLoader.imgHighscore).padTop(40);
        table.row();

        AssetLoader.labelHighscore.setText(Integer.toString(SaveGameData.GetScore()));
        table.add(AssetLoader.labelHighscore);

        return table;
    }

    private Table setupUIButtonMainemenu()
    {
        Table table = new Table();
        //table.debug();
        table.center().bottom();

        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //go to gameplay
                SoundManager.playButton();
                goToGameScreen();

                AssetLoader.gameServices.unlockingAchivement(Constants.ACHIEVEMENT_FIRST_DOT);
                //Incrementing our achievements Play Times
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DROPPER, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DOTS_ADDICT, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_DOTS_CAMPER, 1);
                AssetLoader.gameServices.incrementingAchievement(Constants.ACHIEVEMENT_MASTER_DOTS_CATCHER, 1);
            }
        });
        table.add(btnPlay).colspan(3).padBottom(80);         //add buttonplay to table, merge 3 column and padding bottom 30
        table.row();                                        //Change line

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
                if (GameManager.getInstance().isSoundActive)
                    btnSound.setStyle(AssetLoader.styleSoundOn);
                else
                    btnSound.setStyle(AssetLoader.styleSoundOff);
            }
        });
        table.add(btnSound).expandX();

        table.padBottom(20);                                //padding bottom 20
        return table;
    }

    private void goToGameScreen()
    {
        stack.addAction(sequence(fadeOut(.5f), run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new GameScreen(game));
            }
        })));

    }

    private void createParticle()
    {
        DotParticle particleDot = new DotParticle(Constants.COLOR_DOTS[MathUtils.random(0, Constants.COLOR_DOTS.length-1)]);
        particleDot.speedFactor = MathUtils.random(0.5f, 3f);
        particleDot.setPosition(MathUtils.random(0, MultiViewportHandling.FIXED_WIDTH), MultiViewportHandling.FIXED_HEIGHT + particleDot.getHeight());
        System.out.println(particleDot.getX());
        particleDots.add(particleDot);
        groupDotParticle.addActor(particleDot);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render(float delta) {

        //Clear Screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);

        //Update the stage
        stage.draw();
        stage.act(delta);

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
        if(AssetLoader.gameServices.isBannerVisible())
            AssetLoader.gameServices.hideBannerAds();

        if(btnSound.getStyle().equals(AssetLoader.styleSoundOn) && !GameManager.getInstance().isSoundActive)
            btnSound.setStyle(AssetLoader.styleSoundOff);
        else if(btnSound.getStyle().equals(AssetLoader.styleSoundOff) && GameManager.getInstance().isSoundActive)
            btnSound.setStyle(AssetLoader.styleSoundOn);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }

    private void initializeButton(){
        btnPlay = new Button(Assets.getAssetSkin(), "play");
        btnLeaderboard = new Button(Assets.getAssetSkin(), "leaderboard");
        btnAchievement = new Button(Assets.getAssetSkin(), "achievement");
        btnSound = new Button(Assets.getAssetSkin(), "sound");
    }
}
