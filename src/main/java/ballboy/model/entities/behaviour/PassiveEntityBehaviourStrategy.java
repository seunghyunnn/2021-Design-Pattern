package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.Prototype;
import ballboy.model.entities.DynamicEntity;

/**
 * Passive behaviour logic for dynamic entities.
 * This will do nothing.
 */
public class PassiveEntityBehaviourStrategy implements BehaviourStrategy, Prototype {

    @Override
    public void behave(
            DynamicEntity entity,
            double frameDurationMilli) {
        return;
    }

    public Prototype clone(){
        //System.out.println("PassiveEntityBehaviourStrategy");
        return new PassiveEntityBehaviourStrategy();
    }
    public void setLevel(Level level){
        return;
    }
    public void setHero(DynamicEntity hero){
        return;
    }
}
