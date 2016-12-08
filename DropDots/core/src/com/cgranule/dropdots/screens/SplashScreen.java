package com.cgranule.dropdots.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.utils.MultiViewportHandling;
import com.cgranule.dropdots.utils.SaveGameData;
import com.cgranule.dropdots.helper.AssetLoader;
import com.cgranule.dropdots.helper.Assets;
import com.cgranule.dropdots.services.GameServices;

/**
 * Created by Damar on 8/21/2015.
 */
public class SplashScreen implements Screen{

    private Game game;

    private Stage stage;
    private GameServices gameServices;

    private Image logoImage;
    private Image logoDropDots;

    private Skin skinUI;

    public SplashScreen(Game game, GameServices gameServices)
    {
        this.game = game;
        this.gameServices = gameServices;

        setupPreference();

        stage = new Stage(MultiViewportHandling.fitViewport);

        skinUI = new Skin(Gdx.files.internal(Constants.SKIN_PATH), new TextureAtlas(Constants.TEXTURE_ATLAS_PATH));         //Create Skin

        logoImage = new Image(skinUI, "logo_splash");                                               //Create the image
        logoDropDots = new Image(skinUI, "drop_dots");                                              //Create the image

        Table imageTable = new Table();
        imageTable.setFillParent(true);

        imageTable.center();

        imageTable.add(logoDropDots);
        imageTable.row();
        imageTable.add(logoImage).padTop(50);

        stage.addActor(imageTable);
    }

    @Override
    public void show() {

    }

    float timerToChangeScreen = 2;
    @Override
    public void render(float delta) {
//Clear Screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1);                                          //Background Color

        stage.draw();

        timerToChangeScreen -= delta;
        if(Assets.manager.update() && timerToChangeScreen <= 0)
        {
            AssetLoader.load(this.gameServices);
            game.setScreen(new MainMenuScreen(this.game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    void setupPreference()
    {
        GameManager.getInstance().isSoundActive = SaveGameData.GetSoundStatus();
    }
}
