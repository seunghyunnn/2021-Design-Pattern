package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.Prototype;

/**
 * A collision strategy for square cat.
 */
public class SquareCatCollisionStrategy implements CollisionStrategy, Prototype {
    private Level level;
    public SquareCatCollisionStrategy(Level level){
        this.level = level;
    }
    @Override
    public void collideWith(
            Entity currentEntity,
            Entity hitEntity) {
        return;
    }
    public Prototype clone(){
        return new SquareCatCollisionStrategy(this.level);
    }
    public void setLevel(Level level){
        this.level = level;
    }

}
