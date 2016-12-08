package com.cgranule.dropdots.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cgranule.dropdots.utils.Constants;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.cgranule.dropdots.services.GameServices;

/**
 * Created by damar on 21/08/15.
 */
public class AssetLoader {

    //GameService
    public static GameServices gameServices;

    //Label
    public static Label labelTitle, labelHighscore, labelScore, labelCredit;

    //LabelStyle
    public static LabelStyle styleTitle, styleGrey, styleScore;

    //ButtonStyle
    public static ButtonStyle styleSoundOn, styleSoundOff;

    //Font
    public static BitmapFont font34, font86, font42;

    //Image
    public static Image lineTittle, lineBottomButton, imgHighscore, scoreCircle, scoreCircleGameover;

    public static void load(GameServices gameServices)
    {
        AssetLoader.gameServices = gameServices;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Rounded Elegance.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font34 = generator.generateFont(parameter);

        parameter.size = 86;
        font86 = generator.generateFont(parameter);

        parameter.size = 42;
        font42 = generator.generateFont(parameter);

        styleTitle = new LabelStyle(font42, new Color(80f/255f,80f/255f,80f/255f,1));
        styleGrey = new LabelStyle(font34, new Color(Constants.getGreyColor()));
        styleScore = new LabelStyle(font86, new Color(Constants.getGreyColor()));

        labelTitle = new Label(Constants.GAME_NAME, styleTitle);
        labelHighscore = new Label("", styleGrey);
        labelScore = new Label("", styleScore);
        labelCredit = new Label("Credits", styleTitle);

        lineTittle = new Image(Assets.getAssetSkin(), "line");
        lineBottomButton = new Image(Assets.getAssetSkin(), "line");
        imgHighscore = new Image(Assets.getAssetSkin(), "highscore");
        scoreCircle = new Image(Assets.getAssetSkin(), "circle_score");
        scoreCircleGameover = new Image(Assets.getAssetSkin(), "circle_score");


        styleSoundOff = new Button(Assets.getAssetSkin(), "sound_off").getStyle();
        styleSoundOn = new Button(Assets.getAssetSkin(), "sound").getStyle();

        generator.dispose();
    }
}
