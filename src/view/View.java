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

    private JButton selectedTileButton;
    private JButton submitButton;
    private JButton skipTurnButton;

    private int boardSize;
    private int center;

    public View(int boardSize) {
        this.boardSize = boardSize;
        this.center = boardSize / 2;

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
        add(submitButton, BorderLayout.EAST);

        // Skip turn button
        skipTurnButton = new JButton("Skip Turn");
        add(skipTurnButton, BorderLayout.WEST);

        setVisible(false);
    }

    private JButton initializeEachBoardCell(int row, int col) {
        JButton cellButton = new JButton();
        cellButton.setFont(new Font("Arial", Font.PLAIN, 20));
        cellButton.setFocusable(false);
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

    public void updateBoard(char[][] board) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton cell = getBoardCellButton(row, col);
                char c = board[row][col];
                cell.setText(c == '\0' ? "" : String.valueOf(c));
                cell.setBackground((row == center && col == center) ? Color.orange : Color.LIGHT_GRAY);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void loadPlayerTiles(List<Character> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) != '\0') {
                playerTiles.get(i).setText(tiles.get(i).toString());
                playerTiles.get(i).setVisible(true);
            } else {
                playerTiles.get(i).setText("\0");
                playerTiles.get(i).setVisible(false);
            }
        }
        tileRackPanel.revalidate();
        tileRackPanel.repaint();
    }

    public void updateStatus(Player p) {
        String status = "Current player: " + p.getName() + " | Score: " + p.getScore();
        statusLabel.setText(status);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void deselectTile() {
        if (selectedTileButton != null) {
            selectedTileButton.setBackground(null);
            selectedTileButton = null;
        }
    }

    @Override
    public void update(String message, Model m) {
        switch (message) {
            case "initialize":
                this.boardSize = m.getBoardSize();
                this.center = boardSize / 2;
                loadPlayerTiles(m.getCurrentPlayer().getTiles());
                updateStatus(m.getCurrentPlayer());
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
                setVisible(false);
                break;
            default:
                break;
        }
    }
}