package model;

/**
 * Interface for the observer pattern. The ModelObserver interface is implemented by the
 * View class to allow the Model to notify the View when the model has changed.
 */
public interface ModelObserver {
    void update(String message, Model m);
}