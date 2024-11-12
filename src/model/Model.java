package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Random;


/**
 * Represents the game model.
 */
public class Model {

    private char[][] board;                 // Game board
    private List<Player> players;           // List of players
    private int currentPlayerIndex;         // Index of the current player
    private List<ModelListener> listeners;  // Listeners to notify view updates
    private Set<String> validWords;         // Set of valid words
    private Map<String, Integer> tiles;

    /**
     * Constructs a Model with the specified board size and word list path.
     *
     * @param boardSize the size of the game board
     * @param wordListPath the path to the word list file
     */
    public Model(int boardSize, String wordListPath) {
        board = new char[boardSize][boardSize];
        players = new ArrayList<>();
        tiles = new HashMap<>();
        Bag();
        currentPlayerIndex = 0;
        listeners = new ArrayList<>();
        validWords = new HashSet<>();
        loadWordList(wordListPath); // Load the word list
    }

    private void Bag() {
        tiles.put("A", 9);
        tiles.put("B", 2);
        tiles.put("C", 2);
        tiles.put("D", 4);
        tiles.put("E", 12);
        tiles.put("F", 2);
        tiles.put("G", 3);
        tiles.put("H", 2);
        tiles.put("I", 9);
        tiles.put("J", 1);
        tiles.put("K", 1);
        tiles.put("L", 4);
        tiles.put("M", 2);
        tiles.put("N", 6);
        tiles.put("O", 8);
        tiles.put("P", 2);
        tiles.put("Q", 1);
        tiles.put("R", 6);
        tiles.put("S", 4);
        tiles.put("T", 6);
        tiles.put("U", 4);
        tiles.put("V", 2);
        tiles.put("W", 2);
        tiles.put("X", 1);
        tiles.put("Y", 2);
        tiles.put("Z", 1);
        tiles.put("Blank", 2);
    }

    /**
     * Loads the word list from a file.
     *
     * @param filePath the path to the word list file
     */
    private void loadWordList(String filePath) {
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                validWords.add(line.trim().toLowerCase()); // Add words to the set (lowercase)
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        }
    }

    /**
     * Adds a listener to notify the view.
     *
     * @param listener the listener to add
     */
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners about the model changes.
     */
    private void notifyView() {
        for (ModelListener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Moves to the next player's turn.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyView();
    }

    /**
     * Gets the current game board.
     *
     * @return a copy of the board
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Gets the board size.
     *
     * @return the board size
     */
    public int getBoardSize() {
        return board.length;
    }

    /**
     * Verifies if a word is valid using the local word list.
     *
     * @param word the word to verify
     * @return true if the word is valid, false otherwise
     */
    private boolean isValidWord(String word) {
        return validWords.contains(word.toLowerCase());
    }

    /**
     * Places a word on the board.
     *
     * @param word the word to place
     * @param row the starting row
     * @param column the starting column
     * @param direction the direction ("horizontal" or "vertical")
     * @return a message indicating the result of the operation
     */
    public String placeWord(String word, int row, int column, String direction) {
        if (!isValidWord(word)) {
            return "Invalid word.";
        }

        int rows = board.length;
        int cols = board[0].length;
        int wordLength = word.length();

        // handle out of bounds and overlapping cases
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

    /**
     * Calculates the score for a word based on its length.
     *
     * @param word the word to score
     * @return the score of the word
     */
    public int calculateScore(String word) {
        return word.length(); // Simple scoring: 1 point per letter
    }

    /**
     * Adds score to the current player.
     *
     * @param score the score to add
     */
    public void addScoreToCurrentPlayer(int score) {
        getCurrentPlayer().addScore(score);
        notifyView();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        // Placeholder logic: game ends when a player reaches 100 points
        for (Player player : players) {
            if (player.getScore() >= 100) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays the final scores.
     */
    public void displayFinalScores() {
        System.out.println("Game Over! Final Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

    /**
     * Returns a random tile from the bag of tiles
     * @return
     */
    public String getTile(){
        Random random = new Random();
        int randomInt = random.nextInt(27);
        int counter = 0;
        for (String key: tiles.keySet()) {
            if (counter == randomInt){
                tiles.put(key, tiles.get(key) - 1);
                return key;
            }
        }
        return "";
    }
}
