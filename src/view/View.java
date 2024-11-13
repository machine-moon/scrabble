package view;

import controller.UserActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {
    private UserActionListener actionListener;
    private JPanel boardPanel;
    private JPanel tileRackPanel;
    private JLabel statusLabel;
    private List<JButton> tileButtons;
    private JButton selectedTileButton;

    public View() {
        this.tileButtons = new ArrayList<>();

        // Setup JFrame
        setTitle("Scrabble Game");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status label
        statusLabel = new JLabel("Welcome to Scrabble!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Board panel
        boardPanel = new JPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Tile rack panel
        tileRackPanel = new JPanel(new FlowLayout());
        add(tileRackPanel, BorderLayout.SOUTH);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (actionListener != null) {
                actionListener.onSubmitButtonClicked();
            }
        });
        add(submitButton, BorderLayout.EAST);

        setVisible(true);
    }

    // Set the action listener
    public void setUserActionListener(UserActionListener listener) {
        this.actionListener = listener;
    }

    // Initialize the board
    public void initializeBoard(int boardSize) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));

        // Calculate center position
        int center = (boardSize) / 2; // For 15x15, center = 8

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton cellButton = new JButton();
                cellButton.setFont(new Font("Arial", Font.PLAIN, 20));
                cellButton.setFocusable(false);
                //cellButton.setOpaque(true);
                //cellButton.setBorderPainted(false); // Remove button border

                final int currentRow = row;
                final int currentCol = col;

                // Highlight the center tile
                if (row == center && col == center) {
                    cellButton.setBackground(Color.orange);
                    cellButton.setOpaque(true);
                    //cellButton.setBorderPainted(false);
                } else {
                    cellButton.setBackground(Color.LIGHT_GRAY); // Default color
                }


                cellButton.addActionListener(e -> {
                    if (selectedTileButton != null && actionListener != null) {
                        actionListener.onBoardCellClicked(currentRow, currentCol);
                    } else {
                        showMessage("Please select a tile to place.");
                    }
                });

                boardPanel.add(cellButton);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Update the board
    public void updateBoard(char[][] board) {
        Component[] components = boardPanel.getComponents();
        int boardSize = (int) Math.sqrt(components.length);
        int center = boardSize / 2;

        for (int i = 0; i < components.length; i++) {
            JButton cell = (JButton) components[i];
            int row = i / boardSize;
            int col = i % boardSize;
            char c = board[row][col];
            cell.setText(c == '\0' ? "" : String.valueOf(c));

            // Reapply center tile styling to maintain its special appearance
            if (row == center && col == center) {
                cell.setBackground(Color.orange);
                //cell.setOpaque(true);
                //cell.setBorderPainted(false);
            } else {
                cell.setBackground(Color.LIGHT_GRAY); // Reset to default color
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Initialize the tile rack
    public void initializeTileRack(List<Character> tiles) {
        tileRackPanel.removeAll();
        tileButtons.clear();
        selectedTileButton = null;

        for (Character tile : tiles) {
            JButton tileButton = new JButton(tile.toString());
            tileButton.setFont(new Font("Arial", Font.PLAIN, 20));

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

            tileRackPanel.add(tileButton);
            tileButtons.add(tileButton);
        }

        tileRackPanel.revalidate();
        tileRackPanel.repaint();
    }

    // Update the tile rack
    public void updateTileRack(List<Character> tiles) {
        initializeTileRack(tiles);
    }

    // Update the status label
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

    // Deselect the tile
    public void deselectTile() {
        if (selectedTileButton != null) {
            selectedTileButton.setBackground(null);
            selectedTileButton = null;
        }
    }

    // Disable a tile button
    public void disableTileButton(char tile) {
        for (JButton btn : tileButtons) {
            if (btn.getText().equals(String.valueOf(tile))) {
                btn.setEnabled(false);
                break;
            }
        }
    }
}