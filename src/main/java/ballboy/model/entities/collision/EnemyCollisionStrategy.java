package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.Prototype;
import ballboy.model.levels.LevelImpl;

/**
 * Collision logic for enemies.
 */
public class EnemyCollisionStrategy implements CollisionStrategy, Prototype {
    private Level level;

    public EnemyCollisionStrategy(Level level) {
        this.level = level;
    }

    @Override
    public void collideWith(
            Entity enemy,
            Entity hitEntity) {
        if (level.isHero(hitEntity)) {
            level.resetHero();
        } else if (level.isSquareCat(hitEntity)){
            level.removeEnemy(enemy);
        }

    }

    public Prototype clone(){
        return new EnemyCollisionStrategy(this.level);
    }
    public void setLevel(Level level){
        this.level = level;
    }
}
