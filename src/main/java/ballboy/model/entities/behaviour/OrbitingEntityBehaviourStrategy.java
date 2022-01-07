package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.Prototype;
import ballboy.model.entities.DynamicEntity;

/**
 * Behaviour of the given dynamic entity that orbits another dynamic entity in rectangle shape
 */
public class OrbitingEntityBehaviourStrategy implements BehaviourStrategy, Prototype{
    private Level level;
    private int count = 0; // counts increases until it reaches 480
    private DynamicEntity hero; //needs hero as this entity orbits around the hero
    private double speed = 1.67; //speed of the orbiting entity
    private double seconds = 120; //the 0.017 * seconds becomes the time this orbiting entity
                                //moves from one end of the square to the another end

    public OrbitingEntityBehaviourStrategy(Level level, DynamicEntity hero){
        this.level = level;
        this.hero = hero;
    }

    public void behave(
            DynamicEntity entity,
            double frameDurationMilli){
        count ++;

        /*
         Calculate the position of orbiting entity relative to the entity that is
         orbitted by this entity.
         */
        if(count < seconds){ // top side of the rectangle
            entity.setPosition(
                    entity.getPosition().setX(this.hero.getPosition().getX()-100+speed*count)
                            .setY(this.hero.getPosition().getY()-100));
                    entity.getVelocity().setX(speed);
                    entity.getVelocity().setY(0);
        } else if (count<seconds * 2){ // right side of the rectangle
            entity.setPosition(
                    entity.getPosition().setX(this.hero.getPosition().getX()+100)
                            .setY(this.hero.getPosition().getY()-100+speed*(count%seconds)));
            entity.getVelocity().setX(0);
            entity.getVelocity().setY(speed);
        }else if (count<seconds * 3){ //down side of the rectangle
            entity.setPosition(
                    entity.getPosition().setX(this.hero.getPosition().getX()+100-speed*(count%seconds))
                            .setY(this.hero.getPosition().getY()+100));
            entity.getVelocity().setX(-speed);
            entity.getVelocity().setY(0);
        }else if (count<seconds*4){ //left side of the rectangle
            entity.setPosition(
                    entity.getPosition().setX(this.hero.getPosition().getX()-100)
                            .setY(this.hero.getPosition().getY()+100-speed*(count%seconds)));
            entity.getVelocity().setX(0);
            entity.getVelocity().setY(-speed);
        }else if(count ==seconds *4 ){ //start drawing rectangle again
            count = 0;
        }
    }

    /**
     * Set the count that is needed to position this entity in square relative to
     * the orbiting entity
     * @param count The number of count that we would like to set up
     */
    public void setCount(int count){
        this.count = count;
    }
    public Prototype clone(){
        OrbitingEntityBehaviourStrategy temp =
                new OrbitingEntityBehaviourStrategy(this.level, this.hero);
        temp.setCount(this.count);
        return temp;
    }

    public void setLevel(Level level){
       this.level = level;
    }

    public void setHero(DynamicEntity hero){
        this.hero = hero;
    }
}
