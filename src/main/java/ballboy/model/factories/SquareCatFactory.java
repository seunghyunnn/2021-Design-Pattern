package ballboy.model.factories;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.behaviour.OrbitingEntityBehaviourStrategy;
import ballboy.model.entities.collision.SquareCatCollisionStrategy;
import ballboy.model.entities.utilities.*;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

/**
 *  Concrete entity factory for square cat entities.
 */
public class SquareCatFactory implements EntityFactory {
    private DynamicEntity hero;

    public SquareCatFactory(DynamicEntity hero){
        this.hero = hero;
    }
    @Override
    public Entity createEntity(Level level, JSONObject heroConfig) {
        try{
            //$$ JSONObject should be hero's
            double startX = ((Number) heroConfig.get("startX")).doubleValue() -100;
            double startY = ((Number) heroConfig.get("startY")).doubleValue() -100;

            String imageName = "square_cat.png";
            String size = (String) heroConfig.get("size");

            double height;
            if(size.equals("small")){
                height = 10.0;
            }else if (size.equals("medium")){
                height = 25.0;
            }else if (size.equals("large")){
                height = 50.0;
            }else{
                throw new ConfigurationParseException(String.format("Invalid hero size %s", size));
            }

            Image image = new Image(imageName);
            // preserve image ratio
            double width = height * image.getWidth() / image.getHeight();

            Vector2D startingPosition = new Vector2D(startX, startY);

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .setHorizontalVelocity(0.0)
                    .build();

            AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                    startingPosition,
                    height,
                    width
            );

            return new DynamicEntityImpl(
                    kinematicState,
                    volume,
                    Entity.Layer.FOREGROUND,
                    new Image(imageName),
                    "O",
                    new SquareCatCollisionStrategy(level),
                    new OrbitingEntityBehaviourStrategy(level,this.hero )
            );


        }catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid square cat entity configuration | %s | %s", heroConfig, e));
        }
    }
}
