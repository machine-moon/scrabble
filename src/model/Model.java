package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Represents the game model.
 */
public class Model {
    private static Model instance;

    private char[][] board;
    private List<Player> players;
    private int currentPlayerIndex;
    private TileBag tileBag;
    private List<ModelObserver> observers;
    private Set<String> wordlist;
    private int boardSize;
    private Map<Position, Character> currentTurnPlacements;
    private boolean isFirstTurn;


    /**
     * Initializes the game model with the specified board size.
     *
     * @param boardSize the size of the board (e.g., 15 for a 15x15 board)
     */
    private Model(int boardSize) {
        this.boardSize = boardSize;
        this.board = new char[boardSize][boardSize];
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.tileBag = new TileBag();
        this.observers = new ArrayList<>();
        this.wordlist = loadWordList("src/model/words_alpha.txt");
        this.currentTurnPlacements = new HashMap<>();
        this.isFirstTurn = true;
    }

    /**
     * Gets the singleton instance of the model.
     *
     * @param boardSize the size of the board
     * @return the singleton instance of the model
     */
    public static Model getInstance(int boardSize) {
        if (instance == null) {
            instance = new Model(boardSize);
        }
        return instance;
    }

    /**
     * Resets the singleton instance of the model.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Loads a word list from a file.
     *
     * @param fileName the name of the file to load
     * @return a set of words loaded from the file
     */
    private Set<String> loadWordList(String fileName) {
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

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        player.replenishTiles(tileBag);
        players.add(player);
    }

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    public boolean placeTile(char tile, int row, int col) {

        // check if the player has the tile
        if (!(getCurrentPlayer().hasTile(tile))) {
            return false;
        }

        // check if the position is valid
        if (!isValidPosition(row, col)) {
            return false;
        }

        // check if the position is empty
        if (board[row][col] != '\0') {
            return false;
        }

        board[row][col] = tile;
        currentTurnPlacements.put(new Position(row, col), tile);
        notifyObservers("Tile placed");
        return true;
    }

    /**
     * Checks if it is the first turn of the game.
     *
     * @return true if it is the first turn, false otherwise
     */
    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    /**
     * Submits the current word placements and updates the game state.
     *
     * @return true if the word placements are valid, false otherwise
     */
    public boolean submitWord() {
        List<String> newWords = getAllNewWords();
        System.out.println(newWords);
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
        System.out.println(totalScore);
        getCurrentPlayer().addScore(totalScore);
        System.out.println(getCurrentPlayer().getScore());

        // After validation, replenish player's tiles
        getCurrentPlayer().replenishTiles(tileBag);

        // Clear current turn placements
        clearPlacements();

        // Check if this was the first turn and validate center coverage
        if (isFirstTurn) {
            if (!coversCenter()) {
                // Invalid first move; revert placements
                revertPlacements();
                getCurrentPlayer().deductScore(totalScore);
                notifyObservers("Tile must Cover Centre");
                return false;
            }
            isFirstTurn = false; // First turn completed
        }

        notifyObservers("Word Submitted");
        return true;
    }

    /**
     * Gets all new words formed by the current turn's placements.
     *
     * @return a list of new words
     */
    private List<String> getAllNewWords() {
        List<String> newWords = new ArrayList<>();
        Set<String> uniqueWords = new HashSet<>();

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

    /**
     * Gets the word at the specified position in the given direction.
     *
     * @param row          the row of the starting position
     * @param col          the column of the starting position
     * @param isHorizontal true if the word is horizontal, false if it is vertical
     * @return the word at the specified position
     */
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

    /**
     * Validates a word against the dictionary.
     *
     * @param word the word to validate
     * @return true if the word is valid, false otherwise
     */
    public boolean validateWord(String word) {
        return wordlist.contains(word.toLowerCase());
    }

    /**
     * Calculates the total score for a list of words.
     *
     * @param words the list of words to calculate the score for
     * @return the total score
     */
    private int calculateTotalScore(List<String> words) {
        int total = 0;
        for (String word : words) {
            total += calculateWordScore(word);
        }
        return total;
    }

    /**
     * Calculates the score for a word.
     *
     * @param word the word to calculate the score for
     * @return the score for the word
     */
    private int calculateWordScore(String word) {
        int score = 0;
        // Implement premium squares logic here (to be done in Milestone 3)
        // For now, sum the tile scores
        for (char c : word.toCharArray()) {
            score += getTileScore(c);
        }
        return score;
    }

    /**
     * Gets the score for a tile based on Scrabble letter values.
     *
     * @param tile the tile to get the score for
     * @return the score for the tile
     */
    private int getTileScore(char tile) {
        switch (Character.toUpperCase(tile)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'N':
            case 'R':
            case 'T':
            case 'L':
            case 'S':
            case 'U':
                return 1;
            case 'D':
            case 'G':
                return 2;
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J':
            case 'X':
                return 8;
            case 'Q':
            case 'Z':
                return 10;
            default:
                return 0;
        }
    }

    /**
     * Checks if the center of the board is covered.
     *
     * @return true if the center is covered, false otherwise
     */
    public boolean isCenterCovered() {
        int center = boardSize / 2;
        return board[center][center] != '\0'; // Check if the center tile is occupied
    }


    /**
     * Applies the current turn's placements to the main board.
     */
    private void applyPlacements() {
        // Since we placed tiles directly on the main board in placeTile(), no action needed
    }

    // Clear current turn placements (if any)
    private void clearPlacements() {
        currentTurnPlacements.clear();
    }

    // Revert placements if invalid (used for first turn center coverage)

    /**
     * Reverts the current turn's placements.
     */
    private void revertPlacements() {
        for (Position pos : currentTurnPlacements.keySet()) {
            board[pos.row][pos.col] = '\0';
        }
        clearPlacements();
    }

    /**
     * Checks if the first word covers the center of the board.
     *
     * @return true if the center is covered, false otherwise
     */
    private boolean coversCenter() {
        int center = boardSize / 2;
        return board[center][center] != '\0';
    }

    /**
     * Gets the current game board state.
     *
     * @return the current game board state
     */
    public char[][] getBoardState() {
        return board;
    }

    /**
     * Gets the current player's tiles.
     *
     * @return the current player's tiles
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Moves to the next player's turn.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyObservers("Next player's turn");
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        // Example condition: when the tile bag is empty and a player has no tiles left
        return tileBag.isEmpty() && players.stream().anyMatch(player -> player.getTiles().isEmpty());
    }

    /**
     * Gets the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Adds an observer to the model.
     *
     * @param observer the observer to add
     */
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers that the model has changed.
     */
    private void notifyObservers(String eventType) {
        for (ModelObserver observer : observers) {
            observer.update(eventType, this);
        }
    }

    /**
     * Restores the current player's tiles after an invalid submission.
     */
    public void restorePlayerTiles() {
        Player currentPlayer = getCurrentPlayer();
        for (Position pos : currentTurnPlacements.keySet()) {
            char tile = board[pos.row][pos.col];
            currentPlayer.addTile(tile);
            board[pos.row][pos.col] = '\0'; // Remove the tile from the board
        }
        clearPlacements();
        notifyObservers("Tiles Restored");
    }

    /**
     * Checks if a position is within the board.
     *
     * @param row the row of the position
     * @param col the column of the position
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    /**
     * Gets the size of the board.
     *
     * @return the size of the board
     */
    public int getBoardSize() {
        return boardSize;
    }
}