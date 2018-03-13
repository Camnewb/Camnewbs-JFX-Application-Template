package org.whstsa.library.gui.api;

import javafx.scene.Scene;

public interface Gui {

    /**
     * Contains all content in the scene.
     * @return new Scene
     */
    Scene draw();

    /**
     * A unique ID for a scene, such as "MAIN_MENU_GUI"
     * Used to identify cached scenes
     *
     * Add 'R' as the last character in the UUID to make the scene resizablee
     * @return String UUID
     */
    String getUUID();
}
