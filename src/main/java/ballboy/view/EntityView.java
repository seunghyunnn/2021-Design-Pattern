package ballboy.view;

import ballboy.model.Entity;
import javafx.scene.Node;

/**
 * The interface that manages the view of the entities
 */
public interface EntityView {
    /**
     * Work on the entity view with the consideration of the camera view
     * that follows hero
     * @param xViewportOffset
     * @param yViewportOffset
     */
    void update(
            double xViewportOffset,
            double yViewportOffset);

    boolean matchesEntity(Entity entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
