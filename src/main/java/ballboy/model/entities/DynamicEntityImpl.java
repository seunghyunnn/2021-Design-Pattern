package ballboy.model.entities;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.Prototype;
import ballboy.model.entities.behaviour.BehaviourStrategy;
import ballboy.model.entities.collision.CollisionStrategy;
import ballboy.model.entities.utilities.AxisAlignedBoundingBox;
import ballboy.model.entities.utilities.KinematicState;
import ballboy.model.entities.utilities.Vector2D;
import javafx.scene.image.Image;

/**
 * A concrete entity class that has motion
 */
public class DynamicEntityImpl extends DynamicEntity{

    private final CollisionStrategy collisionStrategy;
    private final BehaviourStrategy behaviourStrategy;
    private final AxisAlignedBoundingBox volume;
    private final Layer layer;
    private final Image image;
    private final KinematicState kinematicState;
    private final String color;

    public DynamicEntityImpl(
            KinematicState kinematicState,
            AxisAlignedBoundingBox volume,
            Layer layer,
            Image image,
            String color,
            CollisionStrategy collisionStrategy,
            BehaviourStrategy behaviourStrategy
    ) {
        this.kinematicState = kinematicState;
        this.volume = volume;
        this.layer = layer;
        this.image = image;
        this.color = color;
        this.collisionStrategy = collisionStrategy;
        this.behaviourStrategy = behaviourStrategy;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public Vector2D getPosition() {
        return kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D pos) {
        this.kinematicState.setPosition(pos);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }


    @Override
    public Vector2D getVelocity() {
        return this.kinematicState.getVelocity();
    }

    @Override
    public void setVelocity(Vector2D vel) {
        this.kinematicState.setVelocity(vel);
    }

    @Override
    public double getHorizontalAcceleration() {
        return this.kinematicState.getHorizontalAcceleration();
    }

    @Override
    public void setHorizontalAcceleration(double horizontalAcceleration) {
        this.kinematicState.setHorizontalAcceleration(horizontalAcceleration);
    }

    @Override
    public double getHeight() {
        return volume.getHeight();
    }

    @Override
    public double getWidth() {
        return volume.getWidth();
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        return volume.collidesWith(entity.getVolume());
    }

    @Override
    public AxisAlignedBoundingBox getVolume() {
        return this.volume;
    }

    @Override
    public void collideWith(Entity entity) {
        collisionStrategy.collideWith(this, entity);
    }

    @Override
    public void update(
            double milliSeconds,
            double levelGravity) {
        kinematicState.update(milliSeconds, levelGravity);
        behaviourStrategy.behave(this, milliSeconds);
        this.volume.setTopLeft(this.kinematicState.getPosition());
    }


    public String getColor(){
        return this.color;
    }

    public Prototype clone(){
        DynamicEntityImpl temp = new DynamicEntityImpl(this.kinematicState.copy(),
                this.volume.copy(),
                this.layer,
                this.image,
                this.color,
                (CollisionStrategy) this.collisionStrategy.clone(),
                (BehaviourStrategy) this.behaviourStrategy.clone());
        temp.setPosition(this.getPosition());
        temp.setHorizontalAcceleration(this.getHorizontalAcceleration());
        temp.setVelocity(this.getVelocity());
        return temp;
    }

    public void setStrategyLevel(Level level){
        this.collisionStrategy.setLevel(level);
        this.behaviourStrategy.setLevel(level);
    }

    public void setStrategyHero(DynamicEntity hero){
        this.behaviourStrategy.setHero(hero);
    }

}
