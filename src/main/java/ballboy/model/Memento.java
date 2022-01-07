package ballboy.model;
/**
 * Memento that holds the state
 */
public class Memento implements Prototype {
    State state;
    public Memento(Level level, int[] totalScores){
        setState(level, totalScores);
    }

    /**
     * Returns the state that this memento is holding
     * @return the instance of state
     */
    public State getState(){
        return (State)this.state;
    }

    /**
     * Set state that this memento will hold. As State instance is consist of
     * level information and the total score, it takes in level and totalScores
     * as the parameters.
     * This memento holds the state with deep copied level and total score information
     *
     * @param level the current level information
     * @param totalScores the total score at the moment
     */
    public void setState(Level level, int[] totalScores){
        Level levelState = (Level) level.clone();
        int[] totalScoresState = new int[]{totalScores[0], totalScores[1], totalScores[2]};
        this.state = new State(levelState, totalScoresState);
    }

    public Prototype clone(){
        return new Memento(this.state.getLevel(), this.state.getTotalScores());
    }
}

