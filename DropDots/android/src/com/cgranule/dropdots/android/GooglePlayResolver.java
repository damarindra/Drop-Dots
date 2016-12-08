package com.cgranule.dropdots.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.cgranule.dropdots.DropDotsGame;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.services.IAPPlatformResolver;

/**
 * Created by Damar on 8/20/2015.
 */
public class GooglePlayResolver extends IAPPlatformResolver {

    AndroidApplication androidApplication;
    DropDotsGame game;

    public GooglePlayResolver(DropDotsGame game, AndroidApplication androidApplication) {
        super(game);
        this.game = game;
        this.androidApplication = androidApplication;

        PurchaseManagerConfig purchaseManagerConfig = game.purchaseManagerConfig;
        purchaseManagerConfig.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, Constants.GOOGLE_KEY);
        initializeIAP(null, game.purchaseObserver, game.purchaseManagerConfig);
    }
}
