package model;

/**
 * Interface for listeners that want to be notified of model updates.
 */
public interface ModelListener {

    /**
     * Method to be called when the model updates.
     */
    void update();
}