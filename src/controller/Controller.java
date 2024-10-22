package controller;

import model.Model;
import model.Player;
import view.View;

import java.util.Scanner;
import java.util.InputMismatchException;

// Controls the flow of the game
public class Controller {
    private Model model; // Reference to the model
    private View view; // Reference to the view
    private Scanner scanner; // Input scanner

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    // Handle player action and update both model and view
    public void handlePlayerAction(String playerInput, int row, int col) {
        // Communicate the action to the model
        boolean validMove = model.placeWord(playerInput, row, col); // Process action in the model

        // Check if the action was valid and update the view accordingly
        if (validMove) {
            int score = model.calculateScore(playerInput); // Calculate the score based on the word and board state
            model.addScoreToCurrentPlayer(score); // Add the calculated score
            view.showMessage("Word placed successfully. Score: " + score);
        } else {
            view.showMessage("Invalid word placement, try again.");
        }

        // Always update the view with the current board state
        view.updateBoard(model.getBoard());
    }

    // Start the game loop
    public void startGame() {
        while (true) {
            Player currentPlayer = model.getCurrentPlayer(); // Get the current player
            view.showMessage("It's " + currentPlayer.getName() + "'s turn.");

            // Get user input for placing a word or passing turn
            view.showMessage("Enter the word to place (or 'pass'):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("pass")) {
                model.nextTurn();
                continue;
            }

            // Get row and column input
            view.showMessage("Enter row and column:");
            try {
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                if (row < 0 || row >= model.getBoardSize() || col < 0 || col >= model.getBoardSize()) {
                    view.showMessage("Invalid row or column. Please try again.");
                    continue;
                }
                scanner.nextLine(); // Consume newline
                handlePlayerAction(input, row, col); // Handle the word placement action
            } catch (InputMismatchException e) {
                view.showMessage("Invalid input. Please enter valid integers for row and column.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            model.nextTurn(); // Move to the next player's turn

            if (model.isGameOver()) {
                view.showMessage("Game Over! Final Scores:");
                model.displayFinalScores(); // Show final scores
                break; // Exit the loop
            }
        }
    }
}
