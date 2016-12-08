package com.cgranule.dropdots.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.PurchaseSystem;
import com.cgranule.dropdots.DropDotsGame;

/**
 * Created by Damar on 8/20/2015.
 */
public abstract class IAPPlatformResolver {
    public DropDotsGame game;
    PurchaseManager mgr;
    PurchaseObserver purchaseObserver;
    PurchaseManagerConfig config;

    public IAPPlatformResolver (DropDotsGame game) {
        this.game = game;
    }

    public void initializeIAP (PurchaseManager mgr, PurchaseObserver purchaseObserver, PurchaseManagerConfig config) {
        this.mgr = mgr;
        this.purchaseObserver = purchaseObserver;
        this.config = config;
    }

    public void installIAP() {
        // set and install the manager manually
        if (mgr != null) {
            PurchaseSystem.setManager(mgr);
            mgr.install(purchaseObserver, config, false);    // dont call PurchaseSystem.install() because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.install() manually");
        }
        else {
            Gdx.app.log("gdx-pay", "initializeIAP(): purchaseManager == null => call PurchaseSystem.hasManager()");
            if (PurchaseSystem.hasManager()) { // install and get the manager automatically via reflection
                this.mgr = PurchaseSystem.getManager();
                Gdx.app.log("gdx-pay", "calls PurchaseSystem.install() via reflection");
                PurchaseSystem.install(purchaseObserver, config); // install the observer
                Gdx.app.log("gdx-pay", "manager succesfully installed");
            }
        }
    }

    public void requestPurchase (String productString) {
        if (mgr != null) {
            mgr.purchase(productString);	// dont call PurchaseSystem... because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.purchase()");
        } else {
            Gdx.app.log("gdx-pay", "ERROR: requestPurchase(): purchaseManager == null");
        }
    }

    public void requestPurchaseRestore () {
        if (mgr != null) {
            mgr.purchaseRestore();	// dont call PurchaseSystem.purchaseRestore(); because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.purchaseRestore()");
        } else {
            Gdx.app.log("gdx-pay", "ERROR: requestPurchaseRestore(): purchaseManager == null");
        }
    }

    public PurchaseManager getPurchaseManager () {
        return mgr;
    }

    public void dispose () {
        if (mgr != null) {
            Gdx.app.log("gdx-pay", "calls purchasemanager.dispose()");
            mgr.dispose();		// dont call PurchaseSystem... because it may bind openIAB!
            mgr = null;
        }
    }
}
