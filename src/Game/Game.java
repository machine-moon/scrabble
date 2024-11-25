package Game;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

import javax.swing.*;


public class Game {

    public static void main(String[] args) {

        // Ask for board size
        int boardSize;
        while (true) {
            try {
                //boardSize = Integer.parseInt(JOptionPane.showInputDialog("Enter board size:"));
                boardSize = 15;
                // force board size is odd for centerpiece.
                if (boardSize % 2 == 0) {
                    boardSize += 1;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for board size.");
            }
        }

        // ask for number of AI players
        int numAiPlayers;
        while (true) {
            try {
                numAiPlayers = Integer.parseInt(JOptionPane.showInputDialog("Enter number of AI players:"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for number of AI players.");
            }
        }


        // Ask for number of players
        int numPlayers;
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

        // add AI players
        model.addAiPlayers(numAiPlayers);


        // Initialize the view
        View view = new View(boardSize);
        model.addObserver(view);
        view.update("initialize", model); // used to initialize variables only the model has.

        // Initialize the controller
        new Controller(model, view);
    }
}