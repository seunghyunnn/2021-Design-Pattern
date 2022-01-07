package ballboy.model;

/**
 * Caretaker that holds the memento
 */
public class Caretaker {
    Memento mementoWithPrevState;
    GameEngine model;

    public Caretaker(GameEngine model){
        this.model = model;
    }

    /**
     * Set the memento of the game engine
     */
    public void setMemento(){
        this.model.setMemento((Memento)this.mementoWithPrevState.clone());
    }

    /**
     * Creates the memento based on the current state of game engine
     */
    public void createMemento(){
        this.mementoWithPrevState = this.model.createMemento();
    }
}
