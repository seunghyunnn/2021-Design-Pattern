package ballboy;

import ballboy.model.*;
import ballboy.model.factories.BallboyFactory;
import ballboy.model.factories.CloudFactory;
import ballboy.model.factories.EnemyFactory;
import ballboy.model.factories.EntityFactoryRegistry;
import ballboy.model.factories.FinishFactory;
import ballboy.model.factories.StaticEntityFactory;
import ballboy.model.levels.LevelImpl;
import ballboy.model.levels.PhysicsEngine;
import ballboy.model.levels.PhysicsEngineImpl;
import ballboy.view.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Map;

/*
 * Application root.
 * Also manages GameWindow and GameEngine and the level transition.
 *
 * Wiring of the dependency graph should be done manually in the start method.
 */
public class App extends Application implements Observer {
    PhysicsEngine engine;
    EntityFactoryRegistry entityFactoryRegistry;
    final double frameDurationMilli = 17;
    JSONObject parsedConfiguration;
    Integer levelIndex;
    JSONArray levelConfigs;
    GameEngine gameEngine;
    GameWindow window;
    boolean isGameFinished  = false;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = getParameters().getNamed();

        String s = "Java 11 sanity check";
        if (s.isBlank()) {
            throw new IllegalStateException("You must be running Java 11+. You won't ever see this exception though" +
                    " as your code will fail to compile on Java 10 and below.");
        }

        ConfigurationParser configuration = new ConfigurationParser();
        parsedConfiguration = null;
        try {
            parsedConfiguration = configuration.parseConfig("config.json");
        } catch (ConfigurationParseException e) {
            System.out.println(e);
            System.exit(-1);
        }


        engine = new PhysicsEngineImpl(frameDurationMilli);

        entityFactoryRegistry = new EntityFactoryRegistry();
        entityFactoryRegistry.registerFactory("cloud", new CloudFactory());
        entityFactoryRegistry.registerFactory("enemy", new EnemyFactory());
        entityFactoryRegistry.registerFactory("background", new StaticEntityFactory(Entity.Layer.BACKGROUND));
        entityFactoryRegistry.registerFactory("static", new StaticEntityFactory(Entity.Layer.FOREGROUND));
        entityFactoryRegistry.registerFactory("finish", new FinishFactory());
        entityFactoryRegistry.registerFactory("hero", new BallboyFactory());


        levelIndex = ((Number) parsedConfiguration.get("currentLevelIndex")).intValue();
        levelConfigs = (JSONArray) parsedConfiguration.get("levels");

        JSONObject levelConfig = (JSONObject) levelConfigs.get(levelIndex);
        Level level = new LevelImpl(levelConfig, engine, entityFactoryRegistry, frameDurationMilli,levelIndex);
        level.Attach(this);

        this.gameEngine = new GameEngineImpl(level);
        level.Attach(this.gameEngine);

        this.window = new GameWindow(gameEngine, 640, 400, frameDurationMilli);
        level.Attach(this.window);
        gameEngine.Attach(this.window);
        this.window.run();

        primaryStage.setTitle("Ballboy");
        primaryStage.setScene(window.getScene());
        primaryStage.setResizable(false);
        primaryStage.show();

        this.window.run();

    }

    /**
     * Increase the level of the Current Game.
     * If all levels are completed, then end the game.
     * @param o currently finished level's LevelImpl instance
     */
    public void update(Object o) {
        if(isGameFinished){
            return;
        }
        if(((Level)o).isFinished()){
            this.levelIndex  = ((Level)o).getLevelIndex()+1;
            if (levelIndex < levelConfigs.size()) {
                //updates the current Level
                JSONObject levelConfig = (JSONObject) levelConfigs.get(levelIndex);
                Level level = new LevelImpl(levelConfig, engine, entityFactoryRegistry, frameDurationMilli,levelIndex );
                level.Attach(this);
                level.Attach(this.gameEngine);
                level.Attach(this.window);
                gameEngine.startLevel(level);

            }
            else { //end the game
                gameEngine.finishGame();
                window.finishGame();
                isGameFinished = true;
            }
        }

    }
}
