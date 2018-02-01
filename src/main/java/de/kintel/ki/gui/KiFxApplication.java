/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.gui;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KiFxApplication extends AbstractJavaFxApplicationSupport {

    private static CountDownLatch latch;

    /**
     * Override this method with your application startup logic.
     * <p/>
     * This method is a wrapper method for javafx's {@link Application#start(Stage)}.
     *
     * @param primaryStage
     */
    @Override
    public void startMvvmfx(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Kintel");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);

        Scene scene = new Scene(FluentViewLoader.fxmlView(MainView.class).load().getView());
        scene.setFill(Color.TRANSPARENT);
        scene.getRoot().getStylesheets().add("de/kintel/ki/gui/MainView.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        latch.countDown();
    }

    public static void launchGUI(ConfigurableApplicationContext ctx, CountDownLatch latch, String[] args) {
        KiFxApplication.latch = latch;
        launchApp(KiFxApplication.class, ctx, args);
    }
}
