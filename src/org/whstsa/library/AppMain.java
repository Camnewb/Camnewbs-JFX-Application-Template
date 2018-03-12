package org.whstsa.library;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.whstsa.library.api.BackgroundWorker;
import org.whstsa.library.db.IOFileDelegate;
import org.whstsa.library.gui.Config;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.api.GuiMain;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.scenes.IOFileSelection;
import org.whstsa.library.util.CommandWatcher;
import org.whstsa.library.util.Logger;
import org.whstsa.library.util.Readline;

import java.io.File;
import java.io.IOException;

/**
 * Created by eric on 11/19/17.
 */
public class AppMain extends Application {

    public static final Readline READER = new Readline(System.in, System.out);
    public static final boolean TESTING = false;
    public static final Logger LOGGER = new Logger();
    private static IOFileDelegate FILE_DELEGATE;
    private Stage stage;
    private InterfaceManager interfaceManager;
    private IOFileSelection jsonFileBrowser;
    private File jsonRawFile;
    private Config config;
    private String jsonPath;

    public static IOFileDelegate getFileDelegate() {
        return FILE_DELEGATE;
    }

    public void start(Stage stage) {
        final String javaCWD = new File(".").getPath().replace("file:/", "");

        BackgroundWorker.getBackgroundWorker().start();
        new CommandWatcher(System.in, System.out).run();
        this.stage = stage;
        stage.setTitle("Library Manager 1.0");
        try {
            stage.getIcons().add(new Image(this.getClass().getResource("/").toString()));//Set the direcctory of your icon here
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

    public File getJsonRawFile() {
        return this.jsonRawFile;
    }

    public Config getConfig() {
        return this.config;
    }

    private void setDirectory(String path) {
        this.jsonPath = path;
    }

}