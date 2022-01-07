package ballboy.model;

/**
 * The base interface for interacting with the Ballboy model
 */
public interface GameEngine extends  Observer, Subject{
    /**
     * Return the currently loaded level
     *
     * @return The current level
     */
    Level getCurrentLevel();

    /**
     * Start the level
     */
    void startLevel(Level level);

    /**
     * Increases the bounce height of the current hero.
     *
     * @return boolean True if the bounce height of the hero was successfully boosted.
     */
    boolean boostHeight();

    /**
     * Reduces the bounce height of the current hero.
     *
     * @return boolean True if the bounce height of the hero was successfully dropped.
     */
    boolean dropHeight();

    /**
     * Applies a left movement to the current hero.
     *
     * @return True if the hero was successfully moved left.
     */
    boolean moveLeft();

    /**
     * Applies a right movement to the current hero.
     *
     * @return True if the hero was successfully moved right.
     */
    boolean moveRight();

    /**
     * Instruct the model to progress forward in time by one increment.
     */
    void tick();
    void update(Object o) ;

    /**
     * Retrieve the total score until the previous level
     * @return array of int that contains totalScore for Red, totalScore for Green,
     * totalScore for Blue in this order
     */
    int[] getTotalScore();

    /**
     * Set up the status of the game engine by using the memento given as parameter
     * @param m Memento that is holding the status information that this game engine should be configured on
     */
    void setMemento(Memento m);

    /**
     * Creates memento based on the current state of the game engine
     * @return memento that contains the status of current game engine
     */
    Memento createMemento();

    /**
     * Returns the current level's index
     * @return the integer indicating the current level's index starting from 0
     */
    int getLevelIndex();

    /**
     * The actionthat this game engine should do when the game is over
     */
    void finishGame();

}
