package controller;

import model.AiPlayer;
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

        // enable the view to display pop up messages
        // model.toggleDisplayMessages();


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

    public void onPlayerTileSelected(JButton tileButton) {
        if (selectedPlayerTileBtn != null) {
            selectedPlayerTileBtn.setBackground(null); // Deselect previous tile
        }

        selectedPlayerChar = tileButton.getText().charAt(0);
        selectedPlayerTileBtn = tileButton;
        // Highlight selected tile
        tileButton.setBackground(Color.CYAN);
    }

    public void onBoardCellClicked(int row, int col, JButton cellButton) {
        if (selectedPlayerChar != null) {
            if (model.placeTile(selectedPlayerChar, row, col)) {
                selectedPlayerChar = null;
                selectedPlayerTileBtn = null;
            }
        }
    }

    public void onSkipTurnClicked() {
        model.restorePlayerTiles();
        model.nextTurn();
        reenablePlayerTiles();
        handleAITurn();
    }

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

    private void reenablePlayerTiles() {
        for (JButton tileButton : view.getPlayerTiles()) {
            tileButton.setEnabled(true);
            tileButton.setBackground(null);
        }
    }

    private void endGame() {
        List<Player> players = model.getPlayers();
        StringBuilder finalScores = new StringBuilder("Game Over! Final Scores:\n");
        for (Player player : players) {
            finalScores.append(player.getName()).append(": ").append(player.getScore()).append("\n");
        }
        view.showMessage(finalScores.toString());
    }


    private void handleAITurn() {


        // silence notify observers.
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
        // un silence notify observers.
        model.toggleDisplayMessages();
    }
}