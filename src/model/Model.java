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
    }

    // Place a word on the board (placeholder method)
    public boolean placeWord(String word, int row, int col) {
        // Logic for placing the word on the board would go here
        notifyView(); // Notify view of the update
        return true; // Placeholder return value
    }

    // Get the current game board
    public char[][] getBoard() {
        return board;
    }

    // Add score to the current player (placeholder method)
    public void addScoreToCurrentPlayer(int score) {
        // Logic to add score would go here
    }
}
