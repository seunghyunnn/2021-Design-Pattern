package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.Prototype;
import ballboy.model.entities.DynamicEntity;

/**
 * Behaviour of a given dynamic entity. This is to be delegated to after an entity is updated.
 */
public interface BehaviourStrategy extends Prototype {
    void behave(
            DynamicEntity entity,
            double frameDurationMilli);

    /**
     * Set up the current level to the behaviours as some behaviours require
     * information related to level to calculate.
     * @param level the current level
     */
    void setLevel(Level level);

    /**
     * Set up the current hero to the behaviours so that it could use the hero's coordinates to
     * calculate entity's behaviour.
     * This is only necessary to the OrbitingEntityBehaviourStrategy.
     * @param hero the current Ballboy object in level
     */
    void setHero(DynamicEntity hero);

}