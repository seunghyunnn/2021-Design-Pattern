package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.Prototype;

/**
 * A passive collision strategy that does nothing.
 */
public class PassiveCollisionStrategy implements CollisionStrategy, Prototype {

    @Override
    public void collideWith(
            Entity currentEntity,
            Entity hitEntity) {
        return;
    }

    public Prototype clone(){
        return new PassiveCollisionStrategy();
    }
    public void setLevel(Level level){
        return;
    }
}
