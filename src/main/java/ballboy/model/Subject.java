package ballboy.model;

/**
 * The interface for observer design pattern's subject
 */
public interface Subject {
    /**
     * Attach the observer to the current subject
     *
     * @param o The observer that will be attached to this instance
     */
    void Attach(Observer o);

    /**
     * Detach the observer from the current subject
     *
     * @param o The observer that will be detached from this instance
     */
    void Detach(Observer o);

    /**
     * Notifies the observers
     */
    void Notify();
}
