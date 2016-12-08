package com.cgranule.dropdots.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.cgranule.dropdots.utils.Constants;

/**
 * Created by Damar on 8/12/2015.
 */
public class BigDot extends Dot {

    private float xPosOrigin;
    public Color dotColor;


    public BigDot(Color color) {
        super(color);
        dotColor = color;
        setBounds(0, 0, Constants.SCREEN_WIDTH / 5, getTextureRegion().getRegionHeight() * 1.5f);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(dotColor.r * color.r
                , dotColor.g * color.g
                , dotColor.b * color.b
                , color.a * parentAlpha);       //product the alpha, we need to add action fade out/ in
        //batch.draw(getTextureRegion(), getX() + getWidth() / 2 - getTextureRegion().getRegionWidth() / 2, getY() + getHeight()/2 - getTextureRegion().getRegionHeight()/2, getWidth() / 2, getHeight() / 2,
        //        getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight(),getScaleX(), getScaleY(), 0);
        batch.draw(getTextureRegion(),
                //First, getX() -> for fix position, then plus (width product scaleX -> purpose for if you change the scale,
                // width not change it, only scale is change, therefore you must manualy set the position of dot still to center
                // since you change the scale) div 2 to get the center. then minus with size of texture region product with scale to handle the size and div 2 to get center position
                getX() + ((getWidth()*getScaleX())/2) - (getTextureRegion().getRegionWidth()*getScaleX())/2,
                //same as above, but relative in Y (height)
                getY() + ((getHeight()*getScaleY())/2) - (getTextureRegion().getRegionHeight()*getScaleY())/2,
                getWidth()/2, getHeight()/2,
                getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }


    public float getxPosOrigin()
    {
        return xPosOrigin;
    }
    public void setxPosOrigin(float x)
    {
        this.xPosOrigin = x;
    }
}
