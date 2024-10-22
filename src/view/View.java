package view;

import model.Model;
import model.ModelListener;

// Represents the game view
public class View implements ModelListener {
    private Model model; // Reference to the model

    public View(Model model) {
        this.model = model;
        model.addListener(this); // Add this view as a listener to the model
    }

    // Display the current state of the board to the user
    public void displayBoard() {
        char[][] board = model.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? '.' : cell) + " ");
            }
            System.out.println();
        }
    }

    // Display player status
    public void displayPlayerStatus() {
        System.out.println("Current player: " + model.getCurrentPlayer().getName());
        System.out.println("Score: " + model.getCurrentPlayer().getScore());
    }

    // Display a message to the user
    public void showMessage(String message) {
        System.out.println(message);
    }

    // Update the view when notified by the model
    @Override
    public void update() {
        displayBoard(); // Update the board display
        displayPlayerStatus(); // Update player status display
    }
}