package ballboy.model;

import java.util.ArrayList;

/**
 * Implementation of the GameEngine interface.
 * This provides a common interface for the entire game.
 */
public class GameEngineImpl implements GameEngine, Observer, Subject {
    private Level level;
    private int totalRScore = 0;
    private int totalGScore = 0;
    private int totalBScore = 0;
    boolean gameFinished = false;
    private ArrayList<Observer> observers;

    public GameEngineImpl(Level level) {
        this.level = level;
        observers = new ArrayList<Observer>();
    }

    public void finishGame(){
        int[] scores = level.getCurrentScore();
        totalRScore += scores[0];
        totalGScore += scores[1];
        totalBScore += scores[2];
        this.gameFinished = true;
    }

    public Level getCurrentLevel() {
        return this.level;
    }

    public void startLevel(Level level) {
        this.level = level;
        return;
    }

    public boolean boostHeight() {
        return level.boostHeight();
    }

    public boolean dropHeight() {
        return level.dropHeight();
    }

    public boolean moveLeft() {
        return level.moveLeft();
    }

    public boolean moveRight() {
        return level.moveRight();
    }

    public void tick() {
        level.update();
    }

    public int getLevelIndex(){return this.level.getLevelIndex();}

    public void update(Object o) {
        if(gameFinished){
            return;
        }
        if(((Level)o).isFinished()){
            int[] scores = ((Level)o).getCurrentScore();
            totalRScore += scores[0];
            totalGScore += scores[1];
            totalBScore += scores[2];
            Notify();
        }
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

    public int[] getTotalScore(){
        return new int[]{totalRScore, totalGScore,totalBScore};
    }

    public void setMemento(Memento m){
        this.level = m.getState().getLevel();
        this.totalRScore = m.getState().getTotalScores()[0];
        this.totalGScore = m.getState().getTotalScores()[1];
        this.totalBScore = m.getState().getTotalScores()[2];
        Notify();
    }

    public Memento createMemento(){
        Memento m = new Memento(this.level, new int[]{totalRScore, totalGScore, totalBScore} );
        return m;
    }
}