package controller;

import model.AiPlayer;
import model.Model;
import model.Player;
import view.View;
import model.Position;

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

    /**
     * Constructs a Controller with the specified model and view.
     *
     * @param m the model
     * @param v the view
     */
    public Controller(Model m, View v) {
        model = m;
        view = v;
        selectedPlayerChar = null;
        selectedPlayerTileBtn = null;

        // enable the view to display pop up messages
        // model.toggleDisplayMessages();


        // Add action listeners to view components
        view.getSubmitButton().addActionListener(e -> onSubmitButtonClicked());
        view.getSkipTurnButton().addActionListener(e -> onSkipTurnClicked());
        view.getUndoButton().addActionListener(e -> onUndoButtonClicked());
        view.getRedoButton().addActionListener(e -> onRedoButtonClicked());

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
     * Handles the event when a player tile is selected.
     *
     * @param tileButton the selected player tile button
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
     * Handles the event when a board cell is clicked.
     *
     * @param row        the row of the board cell
     * @param col        the column of the board cell
     * @param cellButton the board cell button
     */
    public void onBoardCellClicked(int row, int col, JButton cellButton) {
        if (selectedPlayerChar != null) {
            if (model.placeTile(selectedPlayerChar, row, col)) {
                selectedPlayerChar = null;
                selectedPlayerTileBtn = null;
            }
        }
    }

    /**
     * Handles the event when the skip turn button is clicked.
     */
    public void onSkipTurnClicked() {
        model.restorePlayerTiles();
        reenablePlayerTiles();
        model.nextTurn();
        handleAITurn();
    }

    /**
     * Handles the event when the submit button is clicked.
     */
    public void onSubmitButtonClicked() {
        if (model.submitWord()) {
            if (model.isFirstTurn() && !model.isCenterCovered()) {
                model.restorePlayerTiles();
            } else {
                model.nextTurn(); // Move to the next player
                reenablePlayerTiles();
                if (model.isGameOver()) {
                    endGame();
                } else {
                    handleAITurn();
                }
            }
        }
    }

    /**
     * Handles the event when the undo button is clicked.
     */
    public void onUndoButtonClicked() {
        Player currentPlayer = model.getCurrentPlayer();
        Position lastPositionPlayed = currentPlayer.history.removeLast();
        Character lastTilePlayed = model.removeCurrentPlacementTile(lastPositionPlayed);
        currentPlayer.undoHistory.add(lastPositionPlayed);
        model.removeTileFromBoard(lastPositionPlayed.row, lastPositionPlayed.col);
        currentPlayer.addTile(lastTilePlayed);
    }

    /**
     * Handles the event when the redo button is clicked.
     */
    public void onRedoButtonClicked() {
        // function to be coded in

    }

    /**
     * Re-enables the player tiles.
     */
    private void reenablePlayerTiles() {
        for (JButton tileButton : view.getPlayerTiles()) {
            tileButton.setEnabled(true);
            tileButton.setBackground(null);
        }
    }

    /**
     * Ends the game and displays the final scores.
     */
    private void endGame() {
        List<Player> players = model.getPlayers();
        StringBuilder finalScores = new StringBuilder("Game Over! Final Scores:\n");
        for (Player player : players) {
            finalScores.append(player.getName()).append(": ").append(player.getScore()).append("\n");
        }
        view.showMessage(finalScores.toString());
    }

    /**
     * Handles the AI player's turn.
     */
    private void handleAITurn() {
        if (model.isFirstTurn()) {
            return;
        }
        // silence pop up messages.
        model.toggleDisplayMessages();

        while (model.getCurrentPlayer().isAi()) {
            AiPlayer aiPlayer = (AiPlayer) model.getCurrentPlayer();
            if (!(aiPlayer.play())) {
                view.showMessage(aiPlayer.getName() + " skipped their turn.");
            }
            model.nextTurn();
            reenablePlayerTiles();
            if (model.isGameOver()) {
                endGame();
                break;
            }
        }

        // un silence pop up messages.
        model.toggleDisplayMessages();
    }
}