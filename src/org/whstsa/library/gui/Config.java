package org.whstsa.library.gui;

import javafx.scene.control.Alert;
import org.apache.commons.lang3.SystemUtils;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.factories.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that provides a simple interface with a properties file
 */
public class Config {

    private Properties properties;
    private File configFile;
    private boolean notified;

    public Config(File configFile) {
        this.properties = new Properties();
        this.configFile = configFile;
        this.notified = false;
        try {
            properties.load(new FileReader(this.configFile));
        } catch (IOException ex) {
            initializeProperties();
            AppMain.LOGGER.debug("Couldn't load config.");
            ex.printStackTrace();
        }

    }

    /**
     * This method will write initial properties to the config file
     * If the config fails to load, this method will be called
     */
    public void initializeProperties() {
        this.setProperty("anything", "Whatever you want");
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    public void setProperty(String key, String property) {
        this.properties.setProperty(key, property);
        AppMain.LOGGER.debug("Property " + property + " was applied to " + key);
    }

    /**
     * Saves all changes since the last save to the properties file
     */
    public void save() {
        try {
            this.properties.store(new FileOutputStream(this.configFile), null);
            if (notified) {
                notified = false;
            }
        } catch (IOException ex) {
            if (!notified) {
                DialogUtils.createDialog("Couldn't Save Config.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                notified = true;
            }
            ex.printStackTrace();
        }
    }

    /**
     * Determines the best place for the file; dependant on operating system
     * @return
     */
    public static String determineOptimalFileLocation() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return System.getProperty("user.home") + "/AppData/Local/AppMain/config.properties";
        } else if (SystemUtils.IS_OS_MAC) {
            return System.getProperty("user.home") + "/Library/Application\\ Support/AppMain/config.properties";
        } else {
            return System.getProperty("user.home") + "/AppMain/config.properties";
        }
    }

}
