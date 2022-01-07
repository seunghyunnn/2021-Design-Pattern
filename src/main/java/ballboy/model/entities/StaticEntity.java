package ballboy.model.entities;

import ballboy.model.Entity;
import ballboy.model.Prototype;
import ballboy.model.levels.LevelImpl;

/**
 * A static entity instance depended on by Levels.
 */
public abstract class StaticEntity implements Entity{
    /**
     * To make the staticEntity class abstract, this method from Prototype interface
     * should be an abstract method
     * @return The deep copy of this entity
     */
    public abstract Prototype clone();
}
