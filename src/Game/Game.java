package Game;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public static void main(String[] args) {

        int boardSize;
        int numPlayers;

        // Ask for board size
        while (true) {
            try {
                boardSize = Integer.parseInt(JOptionPane.showInputDialog("Enter board size:"));
                // force board size is odd for centerpiece.
                if (boardSize % 2 == 0) {
                    boardSize += 1;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for board size.");
            }
        }

        // Ask for number of players
        while (true) {
            try {
                numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Enter number of players:"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for number of players.");
            }
        }



        // Initialize the model
        Model model = Model.getInstance(boardSize);

        // add players
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