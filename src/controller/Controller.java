package controller;

import model.Model;
import model.Player;
import model.ModelObserver;
import view.View;

import java.awt.*;
import java.util.List;

/**
 * The Controller class handles the game logic and user interactions.
 */
public class Controller {
    private Model model;
    private View view;
    private Character selectedTile;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        selectedTile = null;


        // Add action listeners to view components
        view.getSubmitButton().addActionListener(e -> onSubmitButtonClicked());
        view.getSkipTurnButton().addActionListener(e -> onSkipTurnClicked());


        // do a loop for every button in the board to add action listener
        // Component[] components = boardPanel.getComponents();
        // boardPanel is a 2D array of JButton, inside is a cell button.
        // if selectedTile is not null, then call onBoardCellClicked
        // else, show message "Please select a tile first."

        // check initializeTileRack() in view
        // for every button in tileRackPanel, add action listener
        /*
        private void handleTileButtonClick(JButton tileButton, Character tile) {
            if (selectedTileButton != null) {
                selectedTileButton.setBackground(null);
            }

            selectedTileButton = tileButton;
            tileButton.setBackground(Color.CYAN);

            if (actionListener != null) {
                actionListener.onTileSelected(tile);
            }
        }
         */

    }

    /**
     * This method is called when a tile is selected from the tile rack.
     *
     * @param tile
     */
    public void onTileSelected(char tile) {
        selectedTile = tile;
    }

    /**
     * This method is called when a tile is placed on the board.
     *
     * @param row
     * @param col
     */
    public void onBoardCellClicked(int row, int col) {
        if (selectedTile != null) {
            Player currentPlayer = model.getCurrentPlayer();
            if (currentPlayer.hasTile(selectedTile)) {
                boolean success = model.placeTile(selectedTile, row, col);
                if (success) {
                    // Remove the tile from the player's rack
                    currentPlayer.removeTile(selectedTile);
                    // Update the view
                    view.disableTileButton(selectedTile);
                    view.deselectTile();
                    selectedTile = null;
                } else {
                    view.showMessage("Invalid tile placement. Try again.");
                }
            } else {
                view.showMessage("You don't have that tile.");
                selectedTile = null;
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
        boolean success = model.submitWord();

        if (success) {
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