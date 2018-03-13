package org.whstsa.library;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.whstsa.library.api.BackgroundWorker;
import org.whstsa.library.gui.Config;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.api.GuiMain;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.util.CommandWatcher;
import org.whstsa.library.util.Logger;

import java.io.File;
import java.io.IOException;

/**
 * The main class and runner for the entire application
 * Refactor the name of this class a shortened name of your application. Ex: "LibraryDB"
 * Created by eric on 11/19/17.
 */
public class AppMain extends Application {

    private static final boolean TESTING = false;
    public static final Logger LOGGER = new Logger();
    private Stage stage;
    private InterfaceManager interfaceManager;
    private Config config;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void start(Stage stage) {
        BackgroundWorker.getBackgroundWorker().start();
        new CommandWatcher(System.in, System.out).run();
        this.stage = stage;
        stage.setTitle("Application");
        try {
            stage.getIcons().add(new Image(this.getClass().getResource("").toString()));//Set the direcctory of your icon here
        } catch (NullPointerException ex) {
            Logger.DEFAULT_LOGGER.debug("Not setting stage icon in dev mode.");
        }
        File configFile = new File(Config.determineOptimalFileLocation());
        if (configFile.exists()) {
            LOGGER.debug("Found config at " + configFile.getAbsolutePath());
            this.config = new Config(configFile);
        } else {
            try {
                LOGGER.debug("Couldn't find config. Creating new one.");
                LOGGER.debug(configFile.getPath());
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                this.config = new Config(configFile);
                this.config.initializeProperties();

                this.config.save();
            } catch (IOException ex) {
                ex.printStackTrace();
                DialogUtils.createDialog("There was an error", "Couldn't create file.\n" + ex.getMessage(), null, Alert.AlertType.ERROR).showAndWait();
            }
        }
        this.stage.setResizable(true);
        this.interfaceManager = new InterfaceManager(this);
        if (TESTING) {
            //Do some stuff
        }
        this.interfaceManager.display(new GuiMain(this));
    }

    public Stage getStage() {
        return this.stage;
    }

    public InterfaceManager getInterfaceManager() {
        return this.interfaceManager;
    }

    public Config getConfig() {
        return this.config;
    }

}