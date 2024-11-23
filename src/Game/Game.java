package main;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        // Ask for board size
        String boardSizeInput = JOptionPane.showInputDialog("Enter board size:");
        int boardSize = Integer.parseInt(boardSizeInput);

        // Ask for number of players
        String numPlayersInput = JOptionPane.showInputDialog("Enter number of players:");
        int numPlayers = Integer.parseInt(numPlayersInput);

        // Initialize the model
        Model model = Model.getInstance(boardSize);

        // Add players
        for (int i = 1; i <= numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog("Enter name for player " + i + ":");
            model.addPlayer(new Player(playerName));
        }

        // Initialize the view
        View view = new View(boardSize);
        model.addObserver(view);
        view.update("initialize", model); // used to initialize variables only the model has.

        // Initialize the controller
        new Controller(model, view);
    }
}