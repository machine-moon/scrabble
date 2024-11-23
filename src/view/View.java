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
    private List<JButton> tileButtons;
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
                JButton cellButton = getCellButton(row, col);
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
        tileButtons = new ArrayList<>();

        // Selected tile button
        selectedTileButton = null;

        // Submit button
        JButton submitButton = new JButton("Submit");

        // Skip turn button
        JButton skipTurnButton = new JButton("Skip Turn");

        add(submitButton, BorderLayout.EAST);
        add(skipTurnButton, BorderLayout.WEST);

        // The game isn't actually ready till the controller says so, via the update() method
        setVisible(false);
    }

    private JButton getCellButton(int row, int col) {
        JButton cellButton = new JButton();
        cellButton.setFont(new Font("Arial", Font.PLAIN, 20));
        cellButton.setFocusable(false);

        final int currentRow = row;
        final int currentCol = col;

        // Highlight the center tile
        if (row == center && col == center) {
            cellButton.setBackground(Color.orange);
            cellButton.setOpaque(true);
        } else {
            cellButton.setBackground(Color.LIGHT_GRAY);
        }

        cellButton.addActionListener(e -> {
            if (selectedTileButton != null && actionListener != null) {
                actionListener.onBoardCellClicked(currentRow, currentCol);
            } else {
                showMessage("Please select a tile to place.");
            }
        });
        return cellButton;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getSkipTurnButton() {
        return skipTurnButton;
    }


    // add method for returning refrence to boardPanel and tileRackPanel (rename this to playerRackPanel)



    /**
     * Updates the game board with the given board state.
     *
     * @param board the board state to update
     */
    public void updateBoard(char[][] board) {
        Component[] components = boardPanel.getComponents();

        // get from field
        // int boardSize = (int) Math.sqrt(components.length);
        // int center = boardSize / 2;

        for (int i = 0; i < components.length; i++) {
            JButton cell = (JButton) components[i];
            int row = i / boardSize;
            int col = i % boardSize;
            char c = board[row][col];
            cell.setText(c == '\0' ? "" : String.valueOf(c));

            // Reapply center tile styling to maintain its special appearance
            cell.setBackground((row == center && col == center) ? Color.orange : Color.LIGHT_GRAY);

        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    /**
     * Initializes the tile rack with the given tiles.
     *
     * @param tiles the tiles to initialize the tile rack with
     */
    public void initializeTileRack(List<Character> tiles) {
        tileRackPanel.removeAll();
        tileButtons.clear();
        selectedTileButton = null;

        for (Character tile : tiles) {
            JButton tileButton = new JButton(tile.toString());
            tileButton.setFont(new Font("Arial", Font.PLAIN, 20));

            // ---- Refactor to move this to a method (whats the point of this?)
            tileButton.addActionListener(e -> {
                if (selectedTileButton != null) {
                    selectedTileButton.setBackground(null);
                }

                selectedTileButton = tileButton;
                tileButton.setBackground(Color.CYAN);

                if (actionListener != null) {
                    actionListener.onTileSelected(tile);
                }
            });
            // ----

            tileRackPanel.add(tileButton);
            tileButtons.add(tileButton);
        }

        tileRackPanel.revalidate();
        tileRackPanel.repaint();
    }

    /**
     * Updates the tile rack with the given tiles.
     *
     * @param tiles the tiles to update the tile rack with
     */
    public void updateTileRack(List<Character> tiles) {
        initializeTileRack(tiles);
    }

    /**
     * Updates the status label with the given status message.
     *
     * @param status the status message to update
     */
    public void updateStatus(String status) {
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

    /**
     * Disables the tile button with the given tile.
     *
     * @param tile the tile to disable
     */
    public void disableTileButton(char tile) {
        for (JButton btn : tileButtons) {
            if (btn.getText().equals(String.valueOf(tile))) {
                btn.setEnabled(false);
                break;
            }
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
                initializeTileRack(m.getCurrentPlayer().getTiles());
                updateStatus("Current player: " + m.getCurrentPlayer().getName() + " | Score: " + m.getCurrentPlayer().getScore());
                // -----
                setVisible(true);
                break;
            case "board":
                updateBoard(m.getBoardState());
                break;
            case "updatePlayerTiles":
                updateTileRack(m.getCurrentPlayer().getTiles());
                break;
            case "status":
                String status = "Current player: " + m.getCurrentPlayer().getName() + " | Score: " + m.getCurrentPlayer().getScore();
                updateStatus(status);
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
