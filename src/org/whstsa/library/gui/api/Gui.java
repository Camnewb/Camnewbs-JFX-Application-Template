package org.whstsa.library.gui.api;

import javafx.scene.Scene;

public interface Gui {

    /**
     * Contains all content in the scene.
     * @return new Scene
     */
    public Scene draw();

    /**
     * A unique ID for a scene, such as "MAIN_MENU_GUI"
     * Used to identify cached scenes
     * @return String UUID
     */
    public String getUUID();
}
