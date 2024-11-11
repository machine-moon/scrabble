// File: src/controller/Controller.java
package controller;

import model.Model;
import model.Player;
import model.ModelObserver;
import view.View;

import java.util.List;

public class Controller implements UserActionListener, ModelObserver {
    private Model model;
    private View view;
    private Character selectedTile;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.selectedTile = null;

        // Set the action listener in the view
        view.setUserActionListener(this);

        // Register as an observer of the model
        this.model.addObserver(this);
    }

    // UserActionListener methods
    @Override
    public void onTileSelected(char tile) {
        selectedTile = tile;
    }

    @Override
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

    @Override
    public void onSubmitButtonClicked() {
        submitTurn();
    }

    // Submit the turn
    public void submitTurn() {
        boolean success = model.submitWord();
        if (success) {
            view.showMessage("Word accepted! Your score has been updated.");
            if (model.isGameOver()) {
                endGame();
            } else {
                model.nextTurn();
            }
        } else if (model.isFirstTurn()) {
            view.showMessage("First Word should be placed in the center");
            model.restorePlayerTiles();
        } else {
            view.showMessage("Invalid word! Please try again.");
            model.restorePlayerTiles();
        }
    }

    // End the game
    private void endGame() {
        List<Player> players = model.getPlayers();
        StringBuilder finalScores = new StringBuilder("Game Over! Final Scores:\n");
        for (Player player : players) {
            finalScores.append(player.getName())
                    .append(": ")
                    .append(player.getScore())
                    .append("\n");
        }
        view.showMessage(finalScores.toString());
    }

    // Start the game
    public void startGame() {
        onModelChanged();
    }

    // ModelObserver method
    @Override
    public void onModelChanged() {
        view.updateBoard(model.getBoardState());
        view.updateTileRack(model.getCurrentPlayer().getTiles());
        String status = "Current player: " + model.getCurrentPlayer().getName() +
                " | Score: " + model.getCurrentPlayer().getScore();
        view.updateStatus(status);
    }
}
