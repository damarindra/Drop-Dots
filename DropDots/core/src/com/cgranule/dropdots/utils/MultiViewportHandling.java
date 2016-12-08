package com.cgranule.dropdots.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.math.BigInteger;

/**
 * Created by Damar on 8/13/2015.
 */
public class MultiViewportHandling {
    public static int FIXED_WIDTH;
    public static int FIXED_HEIGHT;
    public static FitViewport fitViewport;

    public static void setupViewport(int width, int height)
    {
        fitViewport = getFixedViewport(width, height);
    }

    public static FitViewport getFixedViewport(int width, int height)
    {
        FIXED_WIDTH = width;
        if(Gdx.graphics.getWidth() == width)
        {
            FIXED_HEIGHT = Gdx.graphics.getHeight();
            return new FitViewport(width, Gdx.graphics.getHeight());
        }
        FIXED_HEIGHT = (int)(((float)width/(float)Gdx.graphics.getWidth())* Gdx.graphics.getHeight());
        return new FitViewport(width, ((float)width/(float)Gdx.graphics.getWidth())* Gdx.graphics.getHeight());
    }
}
