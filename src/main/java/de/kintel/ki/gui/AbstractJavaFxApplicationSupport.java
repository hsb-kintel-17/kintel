/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.gui;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.internal.MvvmfxApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractJavaFxApplicationSupport extends Application implements MvvmfxApplication {

    private static String[] savedArgs;
    private static ConfigurableApplicationContext applicationContext;
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        super.init();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        // let the user init stuff
        initMvvmfx();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        stopMvvmfx();
        applicationContext.close();
    }

    protected static void launchApp(Class<? extends AbstractJavaFxApplicationSupport> appClass, ConfigurableApplicationContext ctx, String[] args) {
        applicationContext = ctx;
        savedArgs = args;
        Application.launch(appClass, args);
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        MvvmFX.setCustomDependencyInjector(applicationContext::getBean);
        startMvvmfx(primaryStage);
    }

}