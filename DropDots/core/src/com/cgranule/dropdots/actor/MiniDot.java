package com.cgranule.dropdots.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.cgranule.dropdots.utils.Constants;

/**
 * Created by Damar on 8/12/2015.
 */
public class MiniDot extends Dot {
    private int xPos;
    public Color dotColor;

    public MiniDot(Color color, int xPos) {
        super(color);
        dotColor = color;
        this.xPos = xPos;
        createMiniDot();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(dotColor.r/color.r     //div the color, wee need to add action changing color, see at GameStage gameover.
                ,dotColor.g/color.g
                ,dotColor.b/color.b
                , color.a * parentAlpha);       //product the alpha, we need to add action fade out/ in
        batch.draw(getTextureRegion(), getX(), getY(), getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(), getScaleX() / 2f, getScaleY() / 2f, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!playingEffect)
        {
            setPosition(getX(), getY()-(Constants.MINI_DOT_SPEED));
            if(getY() <= Constants.Y_BOTTOM_POSITION)
            {
                //Check if MiniDot.DotType equals with Dot.DotType
            }
        }
        else
        {/*
            addAction(sequence(parallel(scaleTo(3,3,1), fadeOut(1)), run(new Runnable() {
                @Override
                public void run() {
                    dispose();
                }
            })));*/
        }
    }

    private boolean playingEffect = false;
    public void playEffect()
    {
        playingEffect = true;
    }

    public void createMiniDot() {
        setBounds(0, 0, getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPositionRelativeOrigin(
                (xPos * (com.cgranule.dropdots.screens.GameScreen.camera.viewportWidth / 5)) + ((com.cgranule.dropdots.screens.GameScreen.camera.viewportWidth / 5) / 2),
                Constants.SCREEN_HEIGHT + getHeight());
        setTouchable(Touchable.disabled);
    }

    public int getXPos()
    {
        return xPos;
    }
}
