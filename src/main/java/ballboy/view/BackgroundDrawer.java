package ballboy.view;

import ballboy.model.GameEngine;
import javafx.scene.layout.Pane;
/**
 * Interface for background of the game
 */
public interface BackgroundDrawer {

    /**
     * Draws the background of the game
     * @param model The game engine this game is using
     * @param pane The pane that this method will like to draw the background on
     */
    void draw(
            GameEngine model,
            Pane pane);

    /**
     * The camera view for the background
     * @param xViewportOffset
     * @param yViewportOffset
     */
    void update(
            double xViewportOffset,
            double yViewportOffset);
}
