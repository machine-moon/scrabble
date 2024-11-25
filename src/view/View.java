package view;


import model.Model;
import model.ModelObserver;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The View class represents the GUI of the Scrabble game.
 * It is responsible for displaying the game board, tile rack, and status messages to the user.
 * It also handles user input and forwards it to the controller.
 */
public class View extends JFrame implements ModelObserver {
    private JPanel boardPanel;
    private JPanel tileRackPanel;
    private JLabel statusLabel;
    private List<JButton> playerTiles;


    private JButton selectedTileButton; //del this?
    private JButton submitButton;
    private JButton skipTurnButton;


    // stuff the update() method will update.
    private int boardSize; // init update method
    private int center;

    private Player currentPlayer;
    private char[][] board;


    public View(int boardSize) {
        this.boardSize = boardSize;
        this.center = boardSize / 2;
        // GUI setup ONLY. Setup ups are in order of declaration.

        // Setup JFrame
        setTitle("Scrabble Game");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Board panel
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));
        add(boardPanel, BorderLayout.CENTER);

        // Initialize board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton cellButton = initializeEachBoardCell(row, col);
                boardPanel.add(cellButton);
            }
        }


        add(boardPanel, BorderLayout.CENTER);

        // Tile rack panel
        tileRackPanel = new JPanel(new FlowLayout());
        add(tileRackPanel, BorderLayout.SOUTH);

        // Status label
        statusLabel = new JLabel("Welcome to Scrabble!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Tile buttons
        playerTiles = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            JButton tileButton = new JButton();
            tileButton.setFont(new Font("Arial", Font.PLAIN, 20));
            tileButton.setFocusable(false);
            tileRackPanel.add(tileButton);
            playerTiles.add(tileButton);
        }

        // Selected tile button
        selectedTileButton = null;

        // Submit button
        submitButton = new JButton("Submit");

        // Skip turn button
        skipTurnButton = new JButton("Skip Turn");

        add(submitButton, BorderLayout.EAST);
        add(skipTurnButton, BorderLayout.WEST);

        // The game isn't actually ready till the controller says so, via the update() method
        setVisible(false);
    }

    private JButton initializeEachBoardCell(int row, int col) {
        JButton cellButton = new JButton();
        cellButton.setFont(new Font("Arial", Font.PLAIN, 12));
        cellButton.setFocusable(false);
        cellButton.setOpaque(true);

        // Triple Word Score (3×WS) - Red
        if ((row == 0 && (col == 0 || col == 7 || col == 14)) ||
                (row == 7 && (col == 0 || col == 14)) ||

                (row == 14 && (col == 0 || col == 7 || col == 14))) {
            cellButton.setBackground(Color.RED);
        }
        // Double Word Score (2×WS) - Pink
        else if ((row == col && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13)) ||
                ((row + col == 14) && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13))) {
            cellButton.setBackground(Color.PINK);
        }
        // Triple Letter Score (3×LS) - Blue
        else if (((row == 1 || row == 13) && (col == 5 || col == 9)) ||
                ((row == 5 || row == 9) && (col == 1 || col == 13)) ||
                (row == 5 && col == 5) || (row == 5 && col == 9) ||
                (row == 9 && col == 5) || (row == 9 && col == 9)) {
            cellButton.setBackground(Color.BLUE);
        }
        // Double Letter Score (2×LS) - Cyan
        else if (((row == 0 || row == 14) && (col == 3 || col == 11)) ||
                ((row == 2 || row == 12) && (col == 6 || col == 8)) ||
                ((row == 3 || row == 11) && (col == 0 || col == 14)) ||
                ((row == 6 || row == 8) && (col == 2 || col == 12)) ||
                ((row == 6 || row == 8) && (col == 6 || col == 8))) {
            cellButton.setBackground(Color.CYAN);
        }
        // Center Tile - Orange
        else if (row == 7 && col == 7) { cellButton.setBackground(Color.ORANGE); }
        else { cellButton.setBackground(Color.LIGHT_GRAY); }

        return cellButton;
    }

    public List<JButton> getPlayerTiles() {
        return playerTiles;
    }

    public JButton getBoardCellButton(int row, int col) {
        return (JButton) boardPanel.getComponent(row * boardSize + col);
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getSkipTurnButton() {
        return skipTurnButton;
    }

    /**
     * Updates the game board with the given board state.
     *
     * @param board the board state to update
     */
    public void updateBoard(char[][] board) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton cell = getBoardCellButton(row, col);
                char c = board[row][col];
                cell.setText(c == '\0' ? "" : String.valueOf(c));
                // Reapply center tile styling to maintain its special appearance

                if ((row == 0 && (col == 0 || col == 7 || col == 14)) ||
                        (row == 7 && (col == 0 || col == 14)) ||
                        (row == 14 && (col == 0 || col == 7 || col == 14))) {
                    cell.setBackground(Color.RED); // Triple Word Score
                } else if ((row == col && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13)) ||
                        ((row + col == 14) && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13))) {
                    cell.setBackground(Color.PINK); // Double Word Score
                } else if (((row == 1 || row == 13) && (col == 5 || col == 9)) ||
                        ((row == 5 || row == 9) && (col == 1 || col == 13)) ||
                        (row == 5 && col == 5) || (row == 5 && col == 9) ||
                        (row == 9 && col == 5) || (row == 9 && col == 9)) {
                    cell.setBackground(Color.BLUE); // Triple Letter Score
                } else if (((row == 0 || row == 14) && (col == 3 || col == 11)) ||
                        ((row == 2 || row == 12) && (col == 6 || col == 8)) ||
                        ((row == 3 || row == 11) && (col == 0 || col == 14)) ||
                        ((row == 6 || row == 8) && (col == 2 || col == 12)) ||
                        ((row == 6 || row == 8) && (col == 6 || col == 8))) {
                    cell.setBackground(Color.CYAN); // Double Letter Score
                } else if (row == 7 && col == 7) {
                    cell.setBackground(Color.ORANGE); // Center Tile
                } else {
                    cell.setBackground(Color.LIGHT_GRAY); // Regular Tile
                }

            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }


    /**
     * Updates the tile rack with the given tiles.
     *
     * @param tiles the tiles to update
     */
    private void loadPlayerTiles(List<Character> tiles) {
        for (int i = 0; i < playerTiles.size(); i++) {
            if (i < tiles.size() && tiles.get(i) != '\0') {
                playerTiles.get(i).setText(tiles.get(i).toString());
                playerTiles.get(i).setVisible(true);
            } else {
                playerTiles.get(i).setText("");
                playerTiles.get(i).setVisible(false);
            }
        }
        tileRackPanel.revalidate();
        tileRackPanel.repaint();
    }


    /**
     * Updates the status label with the given player.
     *
     * @param p the player to update
     */
    public void updateStatus(Player p) {
        String status = "Current player: " + p.getName() + " | Score: " + p.getScore();
        statusLabel.setText(status);
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }


    @Override
    public void update(String message, Model m) {
        // note: keep all the handle methods are under the update method.
        switch (message) {
            case "initialize":
                handleInitialize(m);
                break;
            case "board":
                handleBoardUpdate(m);
                break;
            case "centerNotCovered":
                handleCenterNotCovered(m);
                break;
            case "invalidWord":
                handleInvalidWord(m);
                break;
            case "noWordFound":
                handleNoWordFound(m);
                break;
            case "wordSubmitted":
                handleWordSubmitted(m);
                break;
            case "noAdjacentTiles":
                handleAdjacentWord(m);
                break;
            case "tilePlaced":
                handleBoardUpdate(m);
                break;
            case "updatePlayerTiles":
            case "resetTiles":
                loadPlayerTiles(m.getCurrentPlayer().getTiles());
                break;
            case "nextTurn":
                handleNextTurn(m);
                break;
            case "gameOver":
                handleGameOver();
                break;
            default:
                break;
        }
    }


    private void handleInitialize(Model m) {
        this.boardSize = m.getBoardSize();
        this.center = boardSize / 2;
        this.currentPlayer = m.getCurrentPlayer();
        loadPlayerTiles(m.getCurrentPlayer().getTiles());
        updateStatus(m.getCurrentPlayer());
        setVisible(true);
    }

    private void handleBoardUpdate(Model m) {
        updateBoard(m.getBoardState());
    }

    private void handleCenterNotCovered(Model m) {
        showMessage("First word must be placed covering the center square.");
        updateBoard(m.getBoardState());
        updateStatus(m.getCurrentPlayer());
        loadPlayerTiles(m.getCurrentPlayer().getTiles());
    }

    private void handleInvalidWord(Model m) {
        showMessage("Invalid word! Please try again.");
        updateBoard(m.getBoardState());
        updateStatus(m.getCurrentPlayer());
        loadPlayerTiles(m.getCurrentPlayer().getTiles());
    }

    private void handleNoWordFound(Model m) {
        showMessage("No new word found! Please try again.");
        updateBoard(m.getBoardState());

    }

    private void handleAdjacentWord(Model m) {
        showMessage("Word not adjacent! Please try again.");
        updateBoard(m.getBoardState());

    }

    private void handleWordSubmitted(Model m) {
        showMessage("Word accepted! Your score has been updated.");
        updateBoard(m.getBoardState());
        updateStatus(m.getCurrentPlayer());
        loadPlayerTiles(m.getCurrentPlayer().getTiles());
    }

    private void handleNextTurn(Model m) {
        updateStatus(m.getCurrentPlayer());
        loadPlayerTiles(m.getCurrentPlayer().getTiles());
    }

    private void handleGameOver() {
        showMessage("Game Over!");
        setVisible(false);
    }



}
