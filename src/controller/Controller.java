package controller;

import java.util.InputMismatchException;
import java.util.Scanner;
import model.Model;
import model.Player;

/**
 * The Controller class handles the game logic and user interactions.
 */
public class Controller {

    private Model model;
    private Scanner scanner;

    /**
     * Constructs a Controller with the specified model.
     *
     * @param model the model to be used by the controller
     */
    public Controller(Model model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Handles the player's action of placing a word on the board.
     *
     * @param playerInput the word input by the player
     * @param row the row position to place the word
     * @param col the column position to place the word
     * @param direction the direction to place the word (horizontal/vertical)
     */
    public void handlePlayerAction(String playerInput, int row, int col, String direction) {
        String result = model.placeWord(playerInput, row, col, direction);
        if (result.equals("Word placed successfully.")) {
            int score = model.calculateScore(playerInput);
            model.addScoreToCurrentPlayer(score);
        } else {
            System.out.println(result);
        }
    }

    /**
     * Starts the game and handles the main game loop.
     */
    public void startGame() {
        while (true) {
            Player currentPlayer = model.getCurrentPlayer();
            System.out.println("It's " + currentPlayer.getName() + "'s turn.");

            System.out.println("Enter the word to place (or 'pass'):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("pass")) {
                model.nextTurn();
                continue;
            }

            System.out.println("Enter row, column, and direction (horizontal/vertical):");
            try {
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                String direction = scanner.next();
                scanner.nextLine();

                if (row < 0 || row >= model.getBoardSize() || col < 0 || col >= model.getBoardSize()) {
                    System.out.println("Invalid row or column. Please try again.");
                    continue;
                }

                handlePlayerAction(input, row, col, direction);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter valid integers for row and column.");
                scanner.nextLine();
                continue;
            }

            model.nextTurn();

            if (model.isGameOver()) {
                model.displayFinalScores();
                break;
            }
        }
    }
}
