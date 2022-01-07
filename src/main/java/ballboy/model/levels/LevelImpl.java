package ballboy.model.levels;

import ballboy.ConfigurationParseException;
import ballboy.model.*;
import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.StaticEntity;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.model.factories.EntityFactory;
import ballboy.model.factories.SquareCatFactory;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Level logic, with abstract factor methods.
 */
public class LevelImpl implements Level, Subject, Prototype {

    private List<Entity> entities = new ArrayList<>();
    private final PhysicsEngine engine;
    private final EntityFactory entityFactory;
    private ControllableDynamicEntity<DynamicEntity> hero;
    private Entity finish;
    private double levelHeight;
    private double levelWidth;
    private double levelGravity;
    private double floorHeight;
    private Color floorColor;

    private double frameDurationMilli;
    private ArrayList<Observer> observers;
    private boolean isFinished = false;
    private String showScore; //"RGB" for showing all the score, "R-B" for showing score of red and blue only
//$$
    private DynamicEntity squareCat;
    private int currentRScore = 0;
    private int currentGScore = 0;
    private int currentBScore = 0;
    private int levelIndex;
    /**
     * A callback queue for post-update jobs. This is specifically useful for scheduling jobs mid-update
     * that require the level to be in a valid state.
     */
    private final Queue<Runnable> afterUpdateJobQueue = new ArrayDeque<>();

    public LevelImpl(
            JSONObject levelConfiguration,
            PhysicsEngine engine,
            EntityFactory entityFactory,
            double frameDurationMilli,
            int levelIndex) {
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.frameDurationMilli = frameDurationMilli;
        this.observers = new ArrayList<Observer>();
        this.levelIndex = levelIndex;
        initLevel(levelConfiguration);
    }

    /**
     * Another constructor for LevelImpl. It will be used when cloning the LevelImpl without
     * configuration file
     *
     */
    public LevelImpl(
            PhysicsEngine engine,
            EntityFactory entityFactory,
            double frameDurationMilli,
            int levelIndex
    ){
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.frameDurationMilli = frameDurationMilli;
        this.observers = new ArrayList<Observer>();
        this.levelIndex = levelIndex;
    }

    /**
     * Instantiates a level from the level configuration.
     *
     * @param levelConfiguration The configuration for the level.
     */
    private void initLevel(JSONObject levelConfiguration) {
        this.levelWidth = ((Number) levelConfiguration.get("levelWidth")).doubleValue();
        this.levelHeight = ((Number) levelConfiguration.get("levelHeight")).doubleValue();
        this.levelGravity = ((Number) levelConfiguration.get("levelGravity")).doubleValue();

        JSONObject floorJson = (JSONObject) levelConfiguration.get("floor");
        this.floorHeight = ((Number) floorJson.get("height")).doubleValue();
        String floorColorWeb = (String) floorJson.get("color");
        this.floorColor = Color.web(floorColorWeb);

        this.showScore = (String) levelConfiguration.get("scoreOn");
        JSONArray generalEntities = (JSONArray) levelConfiguration.get("genericEntities");
        for (Object o : generalEntities) {
            this.entities.add(entityFactory.createEntity(this, (JSONObject) o));
        }

        JSONObject heroConfig = (JSONObject) levelConfiguration.get("hero");
        double maxVelX = ((Number) levelConfiguration.get("maxHeroVelocityX")).doubleValue();

        Object hero = entityFactory.createEntity(this, heroConfig);
        if (!(hero instanceof DynamicEntity)) {
            throw new ConfigurationParseException("hero must be a dynamic entity");
        }
        DynamicEntity dynamicHero = (DynamicEntity) hero;
        Vector2D heroStartingPosition = dynamicHero.getPosition();
        this.hero = new ControllableDynamicEntity<>(dynamicHero, heroStartingPosition, maxVelX, floorHeight,
                levelGravity);
        this.entities.add(this.hero);
        SquareCatFactory scf = new SquareCatFactory(dynamicHero);

        this.squareCat= (DynamicEntity) scf.createEntity(this, heroConfig);
        this.entities.add(squareCat);
        //this.entities.add(scf.createEntity(this, heroConfig));

        JSONObject finishConfig = (JSONObject) levelConfiguration.get("finish");
        this.finish = entityFactory.createEntity(this, finishConfig);
        this.entities.add(finish);

    }

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    private List<DynamicEntity> getDynamicEntities() {
        return entities.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return entities.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public double getLevelHeight() {
        return this.levelHeight;
    }

    @Override
    public double getLevelWidth() {
        return this.levelWidth;
    }

    @Override
    public double getHeroHeight() {
        return hero.getHeight();
    }

    @Override
    public double getHeroWidth() {
        return hero.getWidth();
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public Color getFloorColor() {
        return floorColor;
    }

    @Override
    public double getGravity() {
        return levelGravity;
    }

    @Override
    public void update() {
        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        dynamicEntities.stream().forEach(e -> {
            e.update(frameDurationMilli, levelGravity);
        });

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB)) {
                    dynamicEntityA.collideWith(dynamicEntityB);
                    dynamicEntityB.collideWith(dynamicEntityA);
                    if (!isHero(dynamicEntityA) && !isHero(dynamicEntityB)) {
                        engine.resolveCollision(dynamicEntityA, dynamicEntityB);
                    }
                }
            }

            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(staticEntity);
                    engine.resolveCollision(dynamicEntityA, staticEntity, this);
                }
            }
        }
        //$$
      dynamicEntities.stream().forEach(
               e->engine.enforceWorldLimits(e, this)
        );

//       for(DynamicEntity e : dynamicEntities) {
//           if (e.equals(this.squareCat)) {
//               System.out.println("!!");
//               continue;
//           } else {
//               engine.enforceWorldLimits(e, this);
//           }
//       }

        afterUpdateJobQueue.forEach(j -> j.run());
        afterUpdateJobQueue.clear();

    }

    @Override
    public double getHeroX() {
        return hero.getPosition().getX();
    }

    @Override
    public double getHeroY() {
        return hero.getPosition().getY();
    }

    @Override
    public boolean boostHeight() {
        return hero.boostHeight();
    }

    @Override
    public boolean dropHeight() {
        return hero.dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return hero.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return hero.moveRight();
    }

    @Override
    public boolean isHero(Entity entity) {
        return entity == hero;
    }

    public boolean isSquareCat(Entity entity){return entity == squareCat;}

    @Override
    public boolean isFinish(Entity entity) {
        //isFinished = this.finish == entity;
        return this.finish == entity;
    }

    @Override
    public void resetHero() {
        afterUpdateJobQueue.add(() -> this.hero.reset());
    }

    @Override
    public void finish() {
        this.isFinished = true;
        this.Notify();
    }

    public boolean isFinished(){
        return this.isFinished;
    }

    public void Attach(Observer o){
        observers.add(o);
        Notify();
    }

    public void Detach(Observer o){
        observers.remove(o);
    }

    public void Notify() {
        for(int i =0; i<observers.size();i++){
            observers.get(i).update(this);
        }
    }
    public int[] getCurrentScore(){
        return new int[]{currentRScore, currentGScore, currentBScore};
    }

    public void removeEnemy(Entity entity){
        String color = ((DynamicEntity)entity).getColor();
        if(color.equals("R")){
            currentRScore++;
            Notify();
        }else if (color.equals("G")){
            currentGScore++;
            Notify();
        }else if(color.equals("B")){
            currentBScore++;
            Notify();
        }
        entities.remove(entity);
    }

    public String getShowScore(){
        return this.showScore;
    }
    public int getLevelIndex(){ return this.levelIndex;}

    /**
     * Set the entity list of this level
     * @param entities ArrayList that contains every entity for this level
     */
    private void setEntities(ArrayList<Entity> entities){
        this.entities = entities;
    }

    /**
     * Set the Ballboy entity of this level
     * @param hero Ballboy entity for this level
     */
    private void setHero(ControllableDynamicEntity<DynamicEntity> hero){
        this.hero = hero;
    }

    /**
     * Set the finish object entity of this level
     * @param finish Finish object entity for this level
     */
    private void setFinish(Entity finish){
        this.finish = finish;
    }

    /**
     * Set the level height of this level
     * @param levelHeight level height
     */
    private void setLevelHeight(double levelHeight){
        this.levelHeight= levelHeight;
    }

    /**
     * Set the level width of this level
     * @param levelWidth level width
     */
    private void setLevelWidth(double levelWidth){
        this.levelWidth = levelWidth;
    }

    /**
     * Set the gravity of this level
     * @param levelGravity gravity of this level
     */
    private void setLevelGravity(double levelGravity){
        this.levelGravity = levelGravity;
    }

    /**
     * Set the floor height of this level
     * @param floorHeight floor height of this level
     */
    private void setFloorHeight(double floorHeight){
        this.floorHeight=floorHeight;
    }

    /**
     * Set the floor color of this level
     * @param floorColor color of this level
     */
    private void setFloorColor(Color floorColor){
        this.floorColor = floorColor;
    }

    /**
     * Set the frame duration of this level
     * @param frameDurationMilli frame duration of this level in milliseconds
     */
    private void setFrameDurationMilli(double frameDurationMilli){
        this.frameDurationMilli = frameDurationMilli;
    }

    /**
     * Set the observer of this level
     * @param observers list of the observer for this level
     */
    private void setObservers(ArrayList<Observer> observers){
        this.observers = observers;
    }

    /**
     * Set which scores to be shown in this level
     * @param showScore string that indicates which color score should be shown on this level
     */
    private void setShowScore(String showScore){
        this.showScore = showScore;
    }

    /**
     * Set square cat of this level
     * @param squareCat dynamic entity instance of square cat of this level
     */
    private void setSquareCat(DynamicEntity squareCat){
        this.squareCat = squareCat;
    }

    /**
     * Set the current score of this level at the moment
     * @param scores integer array that contains R-score, G-score, B-score in order
     */
    private void setCurrentScore(int[] scores){
        this.currentRScore = scores[0];
        this.currentGScore = scores[1];
        this.currentBScore = scores[2];
    }

    public Prototype clone(){
        //create new level which will be the copy of this level
        LevelImpl temp = new LevelImpl(
                this.engine,
                this.entityFactory,
        this.frameDurationMilli,
                this.levelIndex
        );

        //clone the entities except for hero, square cat, finish object
        ArrayList<Entity> tempEntities = new ArrayList<Entity>();
        for(Entity e : this.entities){
            if(!isHero(e)  &&!isSquareCat(e)&& !isFinish(e)){
                tempEntities.add((Entity)e.clone());
            }
        }

        //create hero for the copied level
        ControllableDynamicEntity<DynamicEntity> tHero =
                (ControllableDynamicEntity<DynamicEntity>) this.hero.clone();
        temp.setHero(tHero);
        tempEntities.add(tHero);

        //create finish object for copied level
        Entity tFinish = (Entity)this.finish.clone();
        temp.setFinish(tFinish);
        tempEntities.add(tFinish);

        //create square cat object for the copied level
        DynamicEntity tSquareCat = (DynamicEntity) this.squareCat.clone();
        temp.setSquareCat(tSquareCat);
        tSquareCat.setStrategyHero(tHero);
        tempEntities.add(tSquareCat);

        //set level used in the strategy of the cloned entities into the copied new level
        for(Entity e: tempEntities){
            e.setStrategyLevel(temp);
        }
        temp.setEntities((ArrayList<Entity>)tempEntities);
        temp.setLevelHeight(this.levelHeight);
        temp.setLevelWidth(this.levelWidth);
        temp.setLevelGravity(this.levelGravity);
        temp.setFloorHeight(this.floorHeight);
        temp.setFloorColor(this.floorColor);
        temp.setFrameDurationMilli(this.frameDurationMilli);
        temp.setObservers(this.observers);
        temp.setShowScore(this.showScore);
        temp.setCurrentScore(this.getCurrentScore());

        return temp;
    }
}
