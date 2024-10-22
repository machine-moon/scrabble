package model;

import java.util.ArrayList;
import java.util.List;

// Represents the game model
public class Model {
    private char[][] board; // Game board
    private List<Player> players; // List of players
    private int currentPlayerIndex; // Index of the current player
    private List<ModelListener> listeners; // Listeners to notify view updates

    public Model(int boardSize) {
        board = new char[boardSize][boardSize];
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        listeners = new ArrayList<>();
    }

    // Add a listener to notify the view
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    // Notify all listeners about the model changes
    private void notifyView() {
        for (ModelListener listener : listeners) {
            listener.update();
        }
    }

    // Add a player to the game
    public void addPlayer(Player player) {
        players.add(player);
    }

    // Get the current player
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Move to the next player's turn
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyView();
    }

    // Get the current game board
    public char[][] getBoard() {
        return board;
    }

    // Get the board size
    public int getBoardSize() {
        return board.length;
    }

    // Place a word on the board
    public boolean placeWord(String word, int row, int column, String direction) {
        int rows = board.length;
        int cols = board[0].length;
        int wordLength = word.length();

        if (direction.equals("horizontal")) {
            if (column + wordLength > cols) {
                return false;
            }
            for (int i = 0; i < wordLength; i++) {
                char currentChar = board[row][column + i];
                if (currentChar != '\0' && currentChar != word.charAt(i)) {
                    return false;
                }
            }
        } else if (direction.equals("vertical")) {
            if (row + wordLength > rows) {
                return false;
            }
            for (int i = 0; i < wordLength; i++) {
                char currentChar = board[row + i][column];
                if (currentChar != '\0' && currentChar != word.charAt(i)) {
                    return false;
                }
            }
        } else {
            return false; // Invalid direction
        }

        // Place the word on the board
        if (direction.equals("horizontal")) {
            for (int i = 0; i < wordLength; i++) {
                board[row][column + i] = word.charAt(i);
            }
        } else if (direction.equals("vertical")) {
            for (int i = 0; i < wordLength; i++) {
                board[row + i][column] = word.charAt(i);
            }
        }

        notifyView(); // Notify view of the update
        return true;
    }

    // Calculate the score for a word (placeholder method)
    public int calculateScore(String word) {
        return word.length(); // Simple scoring: 1 point per letter
    }

    // Add score to the current player
    public void addScoreToCurrentPlayer(int score) {
        getCurrentPlayer().addScore(score);
        notifyView();
    }

    // Check if the game is over (placeholder method)
    public boolean isGameOver() {
        // Placeholder logic: game ends when a player reaches 100 points
        for (Player player : players) {
            if (player.getScore() >= 100) {
                return true;
            }
        }
        return false;
    }

    // Display final scores
    public void displayFinalScores() {
        System.out.println("Game Over! Final Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }
}