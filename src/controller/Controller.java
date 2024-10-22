package controller;

import model.Model;
import model.Player;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Controller {
    private Model model;
    private Scanner scanner;

    public Controller(Model model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    public void handlePlayerAction(String playerInput, int row, int col, String direction) {
        String result = model.placeWord(playerInput, row, col, direction);
        if (result.equals("Word placed successfully.")) {
            int score = model.calculateScore(playerInput);
            model.addScoreToCurrentPlayer(score);
        } else {
            System.out.println(result);
        }
    }

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
                scanner.nextLine(); // Consume newline

                if (row < 0 || row >= model.getBoardSize() || col < 0 || col >= model.getBoardSize()) {
                    System.out.println("Invalid row or column. Please try again.");
                    continue;
                }

                handlePlayerAction(input, row, col, direction);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter valid integers for row and column.");
                scanner.nextLine(); // Clear invalid input
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