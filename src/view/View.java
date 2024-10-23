package view;

import model.Model;
import model.ModelListener;

/**
 * Represents the view in the MVC pattern. Listens to model updates and displays
 * the game state to the user.
 */
public class View implements ModelListener {

    private Model model; // Reference to the model

    /**
     * Constructs a View with the specified model.
     *
     * @param model the model to be used by the view
     */
    public View(Model model) {
        this.model = model;
        model.addListener(this); // Add this view as a listener to the model
    }

    /**
     * Displays the current state of the board to the user.
     */
    public void displayBoard() {
        char[][] board = model.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? '.' : cell) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Displays the current player's status.
     */
    public void displayPlayerStatus() {
        System.out.println("Current player: " + model.getCurrentPlayer().getName());
        System.out.println("Score: " + model.getCurrentPlayer().getScore());
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Updates the view when notified by the model.
     */
    @Override
    public void update() {
        displayBoard();
        displayPlayerStatus();
    }
}
