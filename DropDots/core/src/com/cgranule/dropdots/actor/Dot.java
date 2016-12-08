package com.cgranule.dropdots.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cgranule.dropdots.utils.Constants;

/**
 * Created by Damar on 08/08/2015.
 */
public abstract class Dot extends Actor
{
    public com.cgranule.dropdots.enums.DotType dotType;

    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;

    public Dot(Color dotColor)
    {
        textureAtlas = new TextureAtlas(Gdx.files.internal(Constants.TEXTURE_ATLAS_PATH));
        textureRegion = textureAtlas.findRegion("dot");

        if(dotColor.equals(Constants.getBlueColor()))
        {
            dotType = com.cgranule.dropdots.enums.DotType.BLUE;
        }
        else if(dotColor.equals(Constants.getGreenColor()))
        {
            dotType = com.cgranule.dropdots.enums.DotType.GREEN;
        }
        else if(dotColor.equals(Constants.getOrangeColor()))
        {
            dotType = com.cgranule.dropdots.enums.DotType.ORANGE;
        }
        else if(dotColor.equals(Constants.getPurpleColor()))
        {
            dotType = com.cgranule.dropdots.enums.DotType.PURPLE;
        }
        else if(dotColor.equals(Constants.getPinkColor()))
        {
            dotType = com.cgranule.dropdots.enums.DotType.PINK;
        }
        else
            Gdx.app.error("Dot ", "Name Of Dot is fail");
    }

    public void dispose()
    {
        textureAtlas.dispose();
        remove();
    }

    public TextureRegion getTextureRegion(){ return textureRegion; }

    public void setPositionRelativeOrigin(float x, float y)
    {
        float tempX = x - getOriginX();
        float tempY = y - getOriginY();
        setPosition(tempX,tempY);
    }
    public void setXPositionRelativeOrigin(float x)
    {
        float tempX = x -getOriginX();
        setX(tempX);
    }
}
