package ballboy.model;

import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The base interface for a Ballboy level.
 */
public interface Level extends Subject, Prototype{

    /**
     * Return a List of the currently existing Entities.
     *
     * @return The list of current entities for this level
     */
    List<Entity> getEntities();

    /**
     * The height of the level
     *
     * @return The height (should be in the same format as Entity sizes)
     */
    double getLevelHeight();

    /**
     * The width of the level
     *
     * @return The width (should be in the same format as Entity sizes)
     */
    double getLevelWidth();

    /**
     * @return double The height of the hero.
     */
    double getHeroHeight();

    /**
     * @return double The width of the hero.
     */
    double getHeroWidth();

    /**
     * @return double The vertical position of the floor.
     */
    double getFloorHeight();

    /**
     * @return Color The current configured color of the floor.
     */
    Color getFloorColor();

    /**
     * @return double The current level gravity.
     */
    double getGravity();

    /**
     * Instruct the level to progress forward in time by one increment.
     */
    void update();

    /**
     * The current x position of the hero. This is useful for views so they can follow the hero.
     *
     * @return The hero x position (should be in the same format as Entity sizes)
     */
    double getHeroX();

    /**
     * The current y position of the hero. This is useful for views so they can follow the hero.
     *
     * @return The hero y position (should be in the same format as Entity sizes)
     */
    double getHeroY();

    /**
     * Increase the height the bouncing hero can reach. This could be the vertical acceleration of the hero, unless
     * the current level has special behaviour.
     *
     * @return true if successful
     */
    boolean boostHeight();

    /**
     * Reduce the height the bouncing hero can reach. This could be the vertical acceleration of the hero, unless the
     * current level has special behaviour.
     *
     * @return true if successful
     */
    boolean dropHeight();

    /**
     * Move the hero left or accelerate the hero left, depending on the current level's desired behaviour
     *
     * @return true if successful
     */
    boolean moveLeft();

    /**
     * Move the hero right or accelerate the hero right, depending on the current level's desired behaviour
     *
     * @return true if successful
     */
    boolean moveRight();

    /**
     * @param entity The entity to be checked.
     * @return boolean True if the provided entity is the current hero.
     */
    boolean isHero(Entity entity);

    /**
     * Determine whether the entered entity is square cat
     * @param entity The entity to be checked.
     * @return boolean True if the provided entity is the current square cat.
     */
    boolean isSquareCat(Entity entity);
    /**
     * @param entity The entity to be checked.
     * @return boolean True if the provided entity is the finish of this level.
     */
    boolean isFinish(Entity entity);

    /**
     * Currently, this will just reset the hero to its starting position.
     */
    void resetHero();

    /**
     * Finishes the level.
     */
    void finish();

    /**
     * Returns whether the current Level is ended
     * @return boolean True if the level is finished
     */
    boolean isFinished();

    /**
     * Attach the observer.
     * @param o The instance of the class that implemented observer.
     */
    void Attach(Observer o);

    /**
     * Detach the observer.
     * @param o The instance of the class that implemented observer.
     */
    void Detach(Observer o);

    /**
     * Notifies the observer when there is change in the Level's state.
     */
    void Notify();

    /**
     * Removes the enemy from the current Level. i.e. used when the enemy is killed
     * @param entity the enemy that will be eliminated from the entity list
     */
    void removeEnemy(Entity entity);

    /**
     * Retrieve the current score from the current Level
     * @return the integer array. They contains R score, G score, B score in order.
     */
    int[] getCurrentScore();

    /**
     * Retrieve which score should be shown in the screen for the current Level
     * @return Each digit of String represents R(ed), G(reen), and B(lue).
     * If we don'w want to show the score, write - instead.
     * For example, "RGB" means showing all scores while "R-B" means showing scores for only red and blue.
     */
    String getShowScore();

    public Prototype clone();

    /**
     * Returns the level index of this level which starts from 0
     * @return level index
     */
    int getLevelIndex();

}
