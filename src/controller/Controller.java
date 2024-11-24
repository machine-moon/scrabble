package controller;

import model.AIPlayer;
import model.Model;
import model.Player;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

        view.getSubmitButton().addActionListener(e -> onSubmitButtonClicked());
        view.getSkipTurnButton().addActionListener(e -> onSkipTurnClicked());

        for (int row = 0; row < model.getBoardSize(); row++) {
            for (int col = 0; col < model.getBoardSize(); col++) {
                JButton cellButton = view.getBoardCellButton(row, col);
                final int currentRow = row;
                final int currentCol = col;
                cellButton.addActionListener(e -> onBoardCellClicked(currentRow, currentCol, (JButton) e.getSource()));
            }
        }

        for (JButton tileButton : view.getPlayerTiles()) {
            tileButton.addActionListener(e -> onPlayerTileSelected(tileButton));
        }

        checkAndPlayAIMove();
    }

    public void onPlayerTileSelected(JButton tileButton) {
        if (selectedPlayerTileBtn != null) {
            selectedPlayerTileBtn.setBackground(null);
        }

        selectedPlayerChar = tileButton.getText().charAt(0);
        selectedPlayerTileBtn = tileButton;
        tileButton.setBackground(Color.CYAN);
    }

    public void onBoardCellClicked(int row, int col, JButton cellButton) {
        if (selectedPlayerChar != null) {
            if (model.placeTile(selectedPlayerChar, row, col)) {
                model.getCurrentPlayer().removeTile(selectedPlayerChar);
                selectedPlayerTileBtn.setEnabled(false);
                selectedPlayerTileBtn.setBackground(null);
                cellButton.setText(selectedPlayerChar.toString());
                cellButton.setEnabled(false);
                cellButton.setBackground(null);
                selectedPlayerChar = null;
                selectedPlayerTileBtn = null;
            } else {
                view.showMessage("Invalid tile placement. Try again.");
            }
        } else {
            view.showMessage("Please select a tile first.");
        }
    }

    public void onSkipTurnClicked() {
        model.restorePlayerTiles();
        model.nextTurn();
        checkAndPlayAIMove();
    }

    public void onSubmitButtonClicked() {
        if (model.submitWord()) {
            if (model.isFirstTurn() && !model.isCenterCovered()) {
                view.showMessage("First word must be placed covering the center square.");
                model.restorePlayerTiles();
            } else {
                view.showMessage("Word accepted! Your score has been updated.");
                model.nextTurn();
                if (model.isGameOver()) {
                    endGame();
                } else {
                    checkAndPlayAIMove();
                }
            }
        } else {
            view.showMessage("Invalid word! Please try again.");
            model.restorePlayerTiles();
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

    private void checkAndPlayAIMove() {
        Player currentPlayer = model.getCurrentPlayer();
        if (currentPlayer instanceof AIPlayer) {
            ((AIPlayer) currentPlayer).playBestMove(model);
            model.nextTurn();
            if (model.isGameOver()) {
                endGame();
            } else {
                checkAndPlayAIMove();
            }
        }
    }
}