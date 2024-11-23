package controller;

import model.Model;
import model.Player;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The Controller class handles the game logic and user interactions.
 */
public class Controller {
    private Model model;
    private View view;
    private Character selectedPlayerChar;
    private JButton selectedPlayerTileBtn;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        selectedPlayerChar = null;
        selectedPlayerTileBtn = null;


        // Add action listeners to view components
        view.getSubmitButton().addActionListener(e -> onSubmitButtonClicked());
        view.getSkipTurnButton().addActionListener(e -> onSkipTurnClicked());


        // Loop through each cell button in the board and add action listener to each board cell button
        for (int row = 0; row < model.getBoardSize(); row++) {
            for (int col = 0; col < model.getBoardSize(); col++) {
                JButton cellButton = view.getBoardCellButton(row, col);
                final int currentRow = row;
                final int currentCol = col;
                cellButton.addActionListener(e -> onBoardCellClicked(currentRow, currentCol, (JButton) e.getSource()));
            }
        }

        // Add action listeners to player tile rack buttons
        for (JButton tileButton : view.getPlayerTiles()) {
            tileButton.addActionListener(e -> onPlayerTileSelected(tileButton));
        }


    }

    /**
     * This method is called when a tile is selected from the tile rack.
     *
     * @param tileButton the button representing the selected tile
     */
    public void onPlayerTileSelected(JButton tileButton) {
        if (selectedPlayerTileBtn != null) {
            selectedPlayerTileBtn.setBackground(null); // Deselect previous tile
        }

        selectedPlayerChar = tileButton.getText().charAt(0);
        selectedPlayerTileBtn = tileButton;
        // Highlight selected tile
        tileButton.setBackground(Color.CYAN);
    }

    /**
     * This method is called when a tile is placed on the board.
     *
     * @param row the row of the board cell
     * @param col the column of the board cell
     */
    public void onBoardCellClicked(int row, int col, JButton cellButton) {
        if (selectedPlayerChar != null) {
            if (model.placeTile(selectedPlayerChar, row, col)) {
                // against doing this.
                // Remove the tile from the player's rack
                model.getCurrentPlayer().removeTile(selectedPlayerChar);
                // --- Update the view through the model event "tile placed"
                selectedPlayerTileBtn.setEnabled(false);
                selectedPlayerTileBtn.setBackground(null);
                cellButton.setText(selectedPlayerChar.toString());
                cellButton.setEnabled(false);
                cellButton.setBackground(null);
                // ---
                selectedPlayerChar = null;
                selectedPlayerTileBtn = null;
            } else {
                view.showMessage("Invalid tile placement. Try again.");
            }
        } else {
            view.showMessage("Please select a tile first.");
        }
    }

    /**
     * This method is called when the user clicks the "Skip Turn" button.
     */
    public void onSkipTurnClicked() {
        model.restorePlayerTiles();
        model.nextTurn();
    }

    /**
     * This method is called when the user clicks the "Submit" button.
     */
    public void onSubmitButtonClicked() {

        if (model.submitWord()) {
            if (model.isFirstTurn() && !model.isCenterCovered()) {
                view.showMessage("First word must be placed covering the center square.");
                model.restorePlayerTiles();
            } else {
                view.showMessage("Word accepted! Your score has been updated.");
                model.nextTurn(); // Move to the next player
                if (model.isGameOver()) {
                    endGame();
                }
            }
        } else {
            view.showMessage("Invalid word! Please try again.");
            model.restorePlayerTiles();
        }
    }


    // should be a model method, maybe add a while(1) if model = null, trigger
    private void endGame() {
        List<Player> players = model.getPlayers();
        StringBuilder finalScores = new StringBuilder("Game Over! Final Scores:\n");
        for (Player player : players) {
            finalScores.append(player.getName()).append(": ").append(player.getScore()).append("\n");
        }
        view.showMessage(finalScores.toString());
    }


}