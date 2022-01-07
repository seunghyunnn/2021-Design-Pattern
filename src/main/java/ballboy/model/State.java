package ballboy.model;

/**
 * The state of the game engine that will be stored in Memento when the user pressed 's'
 * and retrieved when the user pressed 'r' .
 */
public class State{
    Level level;
    int[] scores;

    public State(Level level, int[] scores){
        this.level = level;
        this.scores = scores;
    }

    /**
     * Returns the level that this state is holding
     *
     * @return level that this state is holding
     */
    public Level getLevel(){
        return level;
    }

    /**
     * Returns the level that this total score is holding
     *
     * @return total scores of each color that this state is holding
     */
    public int[] getTotalScores(){
        return scores;
    }

}
