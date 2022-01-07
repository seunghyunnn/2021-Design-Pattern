package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.Prototype;


/**
 * Collision strategy injected into all concrete dynamic entities.
 * It should contain the non-physical entity-specific behaviour for collisions.
 */
public interface CollisionStrategy extends Prototype {

    void collideWith(
            Entity currentEntity,
            Entity hitEntity);

    /**
     * Set the level that will be used by the collision strategy.
     * Some collision strategy use the information from the level.
     * @param level The level instance that is now running
     */
    void setLevel(Level level);
}
