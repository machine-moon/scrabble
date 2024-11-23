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
        cellButton.setFont(new Font("Arial", Font.PLAIN, 20));
        cellButton.setFocusable(false);

        // Highlight the center tile
        cellButton.setBackground((row == center && col == center) ? Color.orange : Color.LIGHT_GRAY);
        cellButton.setOpaque(row == center && col == center);

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

                cell.setBackground((row == center && col == center) ? Color.orange : Color.LIGHT_GRAY);
                // also reapply premium tiles styling

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
    public void loadPlayerTiles(List<Character> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) != '\0') {
                playerTiles.get(i).setText(tiles.get(i).toString());
                playerTiles.get(i).setVisible(true);
            } else {
                playerTiles.get(i).setText("\0"); // don't know if this is the right move, but well preserve.
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

    /**
     * Deselects the currently selected tile.
     */
    public void deselectTile() {
        if (selectedTileButton != null) {
            selectedTileButton.setBackground(null);
            selectedTileButton = null;
        }
    }


    @Override
    public void update(String message, Model m) {
        // handle different types of updates
        switch (message) {
            case "initialize":
                // note sure to make this its own event like updateVars or maybe put it outside... or just stream from model like below.
                // load the variables from the model
                this.boardSize = m.getBoardSize();
                this.center = boardSize / 2;
                this.currentPlayer = m.getCurrentPlayer();


                // -----I REFACTOR TO MOVE IT HERE JUST FINE.
                //initializeBoard();  //refactored, get board size from view and do ALL initialization in constructor
                loadPlayerTiles(m.getCurrentPlayer().getTiles());
                updateStatus(m.getCurrentPlayer());
                // -----
                setVisible(true);
                break;
            case "board":
                updateBoard(m.getBoardState());
                break;
            case "updatePlayerTiles":
                loadPlayerTiles(m.getCurrentPlayer().getTiles());
                break;
            case "nextTurn":
                updateStatus(m.getCurrentPlayer());
                break;
            case "gameOver":
                showMessage("Game Over!");
                // tear down the view
                // maybe add a button to restart the game?
                // joptionpane to displayer winner
                // ask the model who won, m.getWinner().getName()
                // scoreboard?


                setVisible(false);
                break;

            default:
                break;
        }

    }

}
