package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Represents the game model
public class Model {
    private char[][] board; // Game board
    private List<Player> players; // List of players
    private int currentPlayerIndex; // Index of the current player
    private List<ModelListener> listeners; // Listeners to notify view updates
    private Set<String> validWords; // Set of valid words

    public Model(int boardSize, String wordListPath) {
        board = new char[boardSize][boardSize];
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        listeners = new ArrayList<>();
        validWords = new HashSet<>();
        loadWordList(wordListPath); // Load the word list
    }

    // Load the word list from a file
    private void loadWordList(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                validWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Verify if a word is valid using the local word list
    private boolean isValidWord(String word) {
        return validWords.contains(word.toLowerCase());
    }

    // Place a word on the board
    public String placeWord(String word, int row, int column, String direction) {
        if (!isValidWord(word)) {
            return "Invalid word.";
        }

        int rows = board.length;
        int cols = board[0].length;
        int wordLength = word.length();

        if (direction.equals("horizontal")) {
            if (column + wordLength > cols) {
                return "Word goes out of the board horizontally.";
            }
            for (int i = 0; i < wordLength; i++) {
                char currentChar = board[row][column + i];
                if (currentChar != '\0' && currentChar != word.charAt(i)) {
                    return "Word overlaps with another word.";
                }
            }
        } else if (direction.equals("vertical")) {
            if (row + wordLength > rows) {
                return "Word goes out of the board vertically.";
            }
            for (int i = 0; i < wordLength; i++) {
                char currentChar = board[row + i][column];
                if (currentChar != '\0' && currentChar != word.charAt(i)) {
                    return "Word overlaps with another word.";
                }
            }
        } else {
            return "Invalid direction.";
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
        return "Word placed successfully.";
    }

    // Calculate the score for a word based on its length
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