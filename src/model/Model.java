package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Model {
    private char[][] board;
    private List<Player> players;
    private int currentPlayerIndex;
    private TileBag tileBag;
    private List<ModelObserver> observers;
    private Set<String> dictionary;
    private int boardSize;
    private Map<Position, Character> currentTurnPlacements;
    private boolean isFirstTurn;


    public Model(int boardSize) {
        this.boardSize = boardSize;
        this.board = new char[boardSize][boardSize];
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.tileBag = new TileBag();
        this.observers = new ArrayList<>();
        this.dictionary = loadDictionary("src/model/words_alpha.txt");
        this.currentTurnPlacements = new HashMap<>();
        this.isFirstTurn = true;
    }

    // Load dictionary from words_alpha.txt
    private Set<String> loadDictionary(String fileName) {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error loading dictionary: " + e.getMessage());
        }
        return words;
    }

    // Add a player and assign initial tiles
    public void addPlayer(Player player) {
        player.replenishTiles(tileBag);
        players.add(player);
    }

    // Place a tile on the board
    public boolean placeTile(char tile, int row, int col) {
        if (!isValidPosition(row, col)) {
            return false; // Invalid position
        }
        if (board[row][col] != '\0') {
            return false; // Cell already occupied
        }
        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.hasTile(tile)) {
            return false; // Player doesn't have this tile
        }
        board[row][col] = tile;
        currentTurnPlacements.put(new Position(row, col), tile);
        notifyObservers();
        return true;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    // Submit the turn: validate all new words and update the board
    public boolean submitWord() {
        List<String> newWords = getAllNewWords();
        if (newWords.isEmpty()) {
            return false; // No tiles placed
        }

        // Validate all new words
        for (String word : newWords) {
            if (!validateWord(word)) {
                return false; // At least one word is invalid
            }
        }

        // All words are valid, calculate total score
        int totalScore = calculateTotalScore(newWords);
        getCurrentPlayer().addScore(totalScore);

        // After validation, replenish player's tiles
        getCurrentPlayer().replenishTiles(tileBag);

        // Clear current turn placements
        clearPlacements();

        // Check if this was the first turn and validate center coverage
        if (isFirstTurn) {
            if (!coversCenter()) {
                // Invalid first move; revert placements
                revertPlacements();
                getCurrentPlayer().deductScore(totalScore); // Assuming method exists
                notifyObservers();
                return false;
            }
            isFirstTurn = false; // First turn completed
        }

        notifyObservers();
        return true;
    }

    // Get all new words formed by the current turn's placements
    private List<String> getAllNewWords() {
        List<String> newWords = new ArrayList<>();
        Set<String> uniqueWords = new HashSet<>(); // To avoid duplicate words

        for (Position pos : currentTurnPlacements.keySet()) {
            // Check horizontal word
            String horizontalWord = getWordAtPosition(pos.row, pos.col, true);
            if (horizontalWord.length() > 1 && !uniqueWords.contains(horizontalWord)) {
                newWords.add(horizontalWord);
                uniqueWords.add(horizontalWord);
            }

            // Check vertical word
            String verticalWord = getWordAtPosition(pos.row, pos.col, false);
            if (verticalWord.length() > 1 && !uniqueWords.contains(verticalWord)) {
                newWords.add(verticalWord);
                uniqueWords.add(verticalWord);
            }
        }

        return newWords;
    }

    // Helper method to get a word at a given position in a given direction
    private String getWordAtPosition(int row, int col, boolean isHorizontal) {
        StringBuilder word = new StringBuilder();
        int deltaRow = isHorizontal ? 0 : -1;
        int deltaCol = isHorizontal ? -1 : 0;

        // Move to the start of the word
        int currentRow = row;
        int currentCol = col;
        while (isValidPosition(currentRow + deltaRow, currentCol + deltaCol) &&
                board[currentRow + deltaRow][currentCol + deltaCol] != '\0') {
            currentRow += deltaRow;
            currentCol += deltaCol;
        }

        // Move forward and build the word
        deltaRow = isHorizontal ? 0 : 1;
        deltaCol = isHorizontal ? 1 : 0;
        while (isValidPosition(currentRow, currentCol) &&
                board[currentRow][currentCol] != '\0') {
            word.append(board[currentRow][currentCol]);
            currentRow += deltaRow;
            currentCol += deltaCol;
        }

        return word.toString();
    }

    // Validate if the word exists in the dictionary
    public boolean validateWord(String word) {
        return dictionary.contains(word.toLowerCase());
    }

    // Calculate total score for all new words
    private int calculateTotalScore(List<String> words) {
        int total = 0;
        for (String word : words) {
            total += calculateWordScore(word);
        }
        return total;
    }

    // Calculate score for a single word
    private int calculateWordScore(String word) {
        int score = 0;
        // Implement premium squares logic here (to be done in Milestone 3)
        // For now, sum the tile scores
        for (char c : word.toCharArray()) {
            score += getTileScore(c);
        }
        return score;
    }

    // Get the tile score based on Scrabble letter values
    private int getTileScore(char tile) {
        switch (Character.toUpperCase(tile)) {
            case 'A': case 'E': case 'I': case 'O': case 'N': case 'R':
            case 'T': case 'L': case 'S': case 'U':
                return 1;
            case 'D': case 'G':
                return 2;
            case 'B': case 'C': case 'M': case 'P':
                return 3;
            case 'F': case 'H': case 'V': case 'W': case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J': case 'X':
                return 8;
            case 'Q': case 'Z':
                return 10;
            default:
                return 0;
        }
    }
    public boolean isCenterCovered() {
        int center = boardSize / 2;
        return board[center][center] != '\0'; // Check if the center tile is occupied
    }


    // Apply placements to the main board (already done during placement)
    private void applyPlacements() {
        // Since we placed tiles directly on the main board in placeTile(), no action needed
    }

    // Clear current turn placements (if any)
    private void clearPlacements() {
        currentTurnPlacements.clear();
    }

    // Revert placements if invalid (used for first turn center coverage)
    private void revertPlacements() {
        for (Position pos : currentTurnPlacements.keySet()) {
            board[pos.row][pos.col] = '\0';
        }
        clearPlacements();
    }

    // Check if the first word covers the center
    private boolean coversCenter() {
        int center = boardSize / 2;
        return board[center][center] != '\0';
    }

    // Get the current game board state (including temporary placements)
    public char[][] getBoardState() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Move to the next player's turn
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyObservers();
    }

    // Check if the game is over
    public boolean isGameOver() {
        // Example condition: when the tile bag is empty and a player has no tiles left
        return tileBag.isEmpty() && players.stream().anyMatch(player -> player.getTiles().isEmpty());
    }

    // Get list of players
    public List<Player> getPlayers() {
        return players;
    }

    // Observer pattern methods
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.onModelChanged();
        }
    }

    // Additional methods for restoring tiles after invalid submission
    public void restorePlayerTiles() {
        Player currentPlayer = getCurrentPlayer();
        for (Position pos : currentTurnPlacements.keySet()) {
            char tile = board[pos.row][pos.col];
            currentPlayer.addTile(tile);
            board[pos.row][pos.col] = '\0'; // Remove the tile from the board
        }
        clearPlacements();
        notifyObservers();
    }

    // Utility method to check if a position is within the board
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    // Getter for board size
    public int getBoardSize() {
        return boardSize;
    }
}
