package de.kintel.ki.gui;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.internal.MvvmfxApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractJavaFxApplicationSupport extends Application implements MvvmfxApplication {

    private static String[] savedArgs;
    static ConfigurableApplicationContext applicationContext;

    private Stage primaryStage;
    private static boolean running = false;


    @Override
    public void init() throws Exception {
        super.init();

        //applicationContext = SpringApplication.run(getClass(), savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

        // let the user init stuff
        initMvvmfx();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void launchApp(Class<? extends AbstractJavaFxApplicationSupport> appClass, String[] args) {
        AbstractJavaFxApplicationSupport.savedArgs = args;
        if(!running) {
            running = true;
            Application.launch(appClass, args);
        }
    }


    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        MvvmFX.setCustomDependencyInjector(applicationContext.getAutowireCapableBeanFactory()::getBean);

        startMvvmfx(primaryStage);
    }

}