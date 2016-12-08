package com.cgranule.dropdots.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Damar on 8/15/2015.
 */
public class DotParticle extends Dot {
    private Color dotColor;
    public float speedFactor;
    public DotParticle(Color dotColor) {
        super(dotColor);
        setBounds(0,0,getTextureRegion().getRegionWidth(),getTextureRegion().getRegionHeight());
        setOrigin(getWidth()/2, getHeight()/2);
        this.dotColor = dotColor;
        speedFactor = MathUtils.random(0.5f, 1.5f);
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
                getWidth(), getHeight(), getScaleX() / 4f, getScaleY() / 4f, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setPosition(getX(), getY()-(speedFactor));
    }
}
