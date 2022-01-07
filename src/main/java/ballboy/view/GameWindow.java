package ballboy.view;

import ballboy.model.Entity;
import ballboy.model.GameEngine;
import ballboy.model.GameEngineImpl;
import ballboy.model.Observer;
import ballboy.model.levels.LevelImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that manages view of the game
 */
public class GameWindow implements Observer {
    private static final double VIEWPORT_MARGIN_X = 100;
    private static final double VIEWPORT_MARGIN_Y = 50;
    private final int width;
    private final int height;
    private final double frameDurationMilli;
    private final Scene scene;
    private final Pane pane;
    private final GameEngine model;
    private final List<EntityView> entityViews;
    private final BackgroundDrawer backgroundDrawer;
    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private Text levelinfo;
    private Text currentScore;
    private Text totalScore;
    private boolean lastAdd= true;
    private boolean gameFinished = false;
    public GameWindow(
            GameEngine model,
            int width,
            int height,
            double frameDurationMilli) {
        this.model = model;
        this.width = width;
        this.height = height;
        this.frameDurationMilli = frameDurationMilli;
        pane = new Pane();
        levelinfo = new Text(480, 40, "Level : "+ String.valueOf(model.getLevelIndex()+1));
        levelinfo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        pane.getChildren().add(levelinfo);

        //showing the currentLevel Score
        currentScore = new Text();
        currentScore.setX(465);
        currentScore.setY(120);
        currentScore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        pane.getChildren().add(currentScore);

        //showing the total Score
        totalScore = new Text();
        totalScore.setX(465);
        totalScore.setY(80);
        totalScore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        pane.getChildren().add(totalScore);
        scene = new Scene(pane, width, height);

        entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        backgroundDrawer = new BlockedBackground();
        backgroundDrawer.draw(model, pane);


    }

    public Scene getScene() {
        return scene;
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(frameDurationMilli),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        model.tick();

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView : entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        double viewportLeftBar = xViewportOffset + VIEWPORT_MARGIN_X;
        double viewportRightBar = viewportLeftBar + (width - 2 * VIEWPORT_MARGIN_X);

        if (heroXPos < viewportLeftBar) {
            xViewportOffset -= heroXPos - viewportLeftBar;
        } else if (heroXPos + model.getCurrentLevel().getHeroWidth() > viewportRightBar) {
            xViewportOffset += heroXPos + model.getCurrentLevel().getHeroWidth() - viewportRightBar;
        }

        heroXPos -= xViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN_X) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN_X - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        }

        double levelRight = model.getCurrentLevel().getLevelWidth();
        double screenRight = xViewportOffset + width - model.getCurrentLevel().getHeroWidth();
        if (screenRight > levelRight) {
            xViewportOffset = levelRight - width + model.getCurrentLevel().getHeroWidth();
        }
        double levelTop = 0.0;
        double levelBottom = model.getCurrentLevel().getLevelHeight();
        double heroYPos = model.getCurrentLevel().getHeroY();
        double heroHeight = model.getCurrentLevel().getHeroHeight();
        double viewportTop = yViewportOffset + VIEWPORT_MARGIN_Y;
        double viewportBottom = yViewportOffset + height - 2 * VIEWPORT_MARGIN_Y;

        if (heroYPos + heroHeight > viewportBottom) {
            // if below, shift down
            yViewportOffset += heroYPos + heroHeight - viewportBottom;
        } else if (heroYPos < viewportTop) {
            // if above, shift up
            yViewportOffset -= viewportTop - heroYPos;
        }

        double screenBottom = yViewportOffset + height;
        double screenTop = yViewportOffset;
        // shift back in the instance when we're near the boundary
        if (screenBottom > levelBottom) {
            yViewportOffset -= screenBottom - levelBottom;
        } else if (screenTop < 0.0) {
            yViewportOffset -= screenTop;
        }


//        double viewportBottomBar = yViewportOffset + height - VIEWPORT_MARGIN_Y;
//        double viewportTopBar = yViewportOffset + VIEWPORT_MARGIN_Y;
//
//        if (heroYPos + model.getCurrentLevel().getHeroHeight() > viewportBottomBar) {
//            yViewportOffset += (heroYPos + model.getCurrentLevel().getHeroHeight()) - viewportBottomBar;
//        } else if (heroYPos < viewportTopBar) {
//            yViewportOffset -= viewportTopBar - heroYPos;
//        }
//
//        heroYPos -= yViewportOffset;
//
//        if (heroYPos > VIEWPORT_MARGIN_Y) {
//            if (yViewportOffset >= 0) { // avoid going further than bottom of the screen
//                yViewportOffset -= heroYPos - VIEWPORT_MARGIN_Y;
//                if (yViewportOffset < 0) {
//                    yViewportOffset = 0;
//                }
//            }
//        }


        backgroundDrawer.update(xViewportOffset, yViewportOffset);


        for (Entity entity : entities) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

    public void finishGame(){
        gameFinished = true;
        //change the label with the level information
        levelinfo.setText("WINNER!");

        //change the label with the total score information
        int[] scores = model.getTotalScore();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("R : "+String.valueOf(scores[0])+" ");
        sb3.append("G : "+String.valueOf(scores[1])+" ");
        sb3.append("B : "+String.valueOf(scores[2])+" ");
        this.totalScore.setText(sb3.toString());

        //change the label with the current score with the next Level's information
        this.currentScore.setText("R : 0 G : 0 B : 0 ");

    }


    public void update(Object o){
        int[] scores;
        if(gameFinished){
            return;
        }
        if(o instanceof LevelImpl){
            if(((LevelImpl)o).isFinished()){
            }else{
                scores = ((LevelImpl) o).getCurrentScore();
                StringBuilder sb = new StringBuilder();
                if(((LevelImpl) o).getShowScore().substring(0,1).equals("R")){
                    sb.append("R : "+String.valueOf(scores[0])+" ");
                }
                if(((LevelImpl) o).getShowScore().substring(1,2).equals("G")){
                    sb.append("G : "+String.valueOf(scores[1])+" ");
                }
                if(((LevelImpl) o).getShowScore().substring(2,3).equals("B")){
                    sb.append("B : "+String.valueOf(scores[2])+" ");
                }
                this.currentScore.setText(sb.toString());
            }
        }
        else if(o instanceof GameEngineImpl ){ //Finished the Level
            if(!(this.levelinfo.getText().equals("WINNER"))){ //If it is not the last level
                //change the label with the level information
                levelinfo.setText("Level : "+ String.valueOf(model.getLevelIndex()+1));

                //change the label with the total score information
                scores = ((GameEngineImpl) o).getTotalScore();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("R : "+String.valueOf(scores[0])+" ");
                sb2.append("G : "+String.valueOf(scores[1])+" ");
                sb2.append("B : "+String.valueOf(scores[2])+" ");
                this.totalScore.setText(sb2.toString());

                //change the label with the current score with the next Level's information
                scores = ((GameEngineImpl) o).getCurrentLevel().getCurrentScore();
                sb2 = new StringBuilder();
                if(((GameEngine) o).getCurrentLevel().getShowScore().substring(0,1).equals("R")){
                    sb2.append("R : "+String.valueOf(scores[0])+" ");
                }
                if(((GameEngine) o).getCurrentLevel().getShowScore().substring(1,2).equals("G")){
                    sb2.append("G : "+String.valueOf(scores[1])+" ");
                }
                if(((GameEngine) o).getCurrentLevel().getShowScore().substring(2,3).equals("B")){
                    sb2.append("B : "+String.valueOf(scores[2])+" ");
                }
                this.currentScore.setText(sb2.toString());
            }
        }
    }

}
