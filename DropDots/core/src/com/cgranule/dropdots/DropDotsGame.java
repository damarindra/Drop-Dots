package com.cgranule.dropdots;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cgranule.dropdots.manager.GameManager;
import com.cgranule.dropdots.utils.Constants;
import com.cgranule.dropdots.utils.MultiViewportHandling;
import com.cgranule.dropdots.utils.SaveGameData;
import com.cgranule.dropdots.helper.Assets;
import com.cgranule.dropdots.screens.SplashScreen;
import com.cgranule.dropdots.services.GameServices;
import com.cgranule.dropdots.services.IAPPlatformResolver;

public class DropDotsGame extends Game {

    //Admob
	public GameServices gameServices;

    // ----- app stores -------------------------
    public static final int APPSTORE_UNDEFINED	= 0;
    public static final int APPSTORE_GOOGLE 	= 1;
    public static final int APPSTORE_OUYA 		= 2;
    public static final int APPSTORE_AMAZON 	= 3;
    public static final int APPSTORE_DESKTOP 	= 4;

    private int isAppStore = APPSTORE_UNDEFINED;

    //IAP PLatform Resolver
    static IAPPlatformResolver iapPlatformResolver;
    //configure product (offer) and store-parameter
    public PurchaseManagerConfig purchaseManagerConfig;
    //listener that react purchases and restore purchases
    public PurchaseObserver purchaseObserver = new PurchaseObserver() {
        @Override
        public void handleInstall() {
            Gdx.app.log("handleInstall: ", "successfully..");
        }

        @Override
        public void handleInstallError(Throwable throwable) {
            Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + throwable.getMessage());
            throw new GdxRuntimeException(throwable);
        }

        @Override
        public void handleRestore(Transaction[] transactions) {
            Gdx.app.log("gdx-pay", "PurchaseObserver: handleRestore()");
            for (int i = 0; i < transactions.length; i++) {
                if (checkTransaction(transactions[i].getIdentifier(), true) == true)
                {
                    if(transactions[i].getIdentifier().equals(Constants.IAP_NO_ADS))
                    {
                        GameManager.getInstance().noAds = true;
                        gameServices.trackEvent("Gameover Screen", "IAP", "Restore", "No Ads");
                    }
                }
            }
        }


        @Override
        public void handleRestoreError(Throwable throwable) {
            Gdx.app.log("ERROR", "PurchaseObserver: handleRestoreError!: " + throwable.getMessage());
            throw new GdxRuntimeException(throwable);
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            checkTransaction(transaction.getIdentifier(), true);
            gameServices.trackEvent("Gameover Screen", "IAP", "Purchased", "No Ads");
            Gdx.app.log("gdx-pay", "PurchaseObserver: handlePurchase()");
        }

        @Override
        public void handlePurchaseError(Throwable throwable) {
            if (throwable.getMessage().equals("There has been a Problem with your Internet connection. Please try again later")) {

                // this check is needed because user-cancel is a handlePurchaseError too)
                // getPlatformResolver().showToast("handlePurchaseError: " + e.getMessage());
            }
            throw new GdxRuntimeException(throwable);
        }

        @Override
        public void handlePurchaseCanceled() {

        }
    };

	public DropDotsGame(GameServices GameServices)
	{
		this.gameServices = GameServices;
        ///IAP
        if(Constants.USING_IAP){
            purchaseManagerConfig = new PurchaseManagerConfig();
            purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(Constants.IAP_NO_ADS));
        }
        ///END OF IAP
	}

	@Override
	public void create () {
		MultiViewportHandling.setupViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Assets.load();

		SaveGameData.Initialize();

        if(Gdx.app.getType().equals(Application.ApplicationType.Android) && Constants.USING_IAP)
            getPlatformResolver().installIAP();

		setScreen(new SplashScreen(this, gameServices));
	}

    protected boolean checkTransaction (String ID, boolean isRestore) {
        boolean returnbool = false;
        if (Constants.IAP_NO_ADS.equals(ID)) {
            if(isRestore)
                GameManager.getInstance().noAds = true;
            Gdx.app.log("gdx-pay", "No Ads : " + Boolean.toString(GameManager.getInstance().noAds));
            returnbool = true;
        }
        return returnbool;
    }

    public IAPPlatformResolver getPlatformResolver() {
        return iapPlatformResolver;
    }
    public static void setIapPlatformResolver (IAPPlatformResolver platformResolver) {
        iapPlatformResolver = platformResolver;
    }

    public int getAppStore () {
        return isAppStore;
    }
    public void setAppStore (int isAppStore) {
        this.isAppStore = isAppStore;
    }

}
