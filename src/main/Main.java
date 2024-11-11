// File: src/main/Main.java
package main;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

public class Main {
    public static void main(String[] args) {
        // Initialize the model with a 15x15 board
        Model model = new Model(15);

        // Add players to the game
        model.addPlayer(new Player("Alice"));
        model.addPlayer(new Player("Bob"));

        // Initialize the view
        View view = new View();

        // Initialize the controller
        Controller controller = new Controller(model, view);

        // Initialize the board and tile rack in the view
        view.initializeBoard(model.getBoardSize());
        view.initializeTileRack(model.getCurrentPlayer().getTiles());
        view.updateStatus("Current player: " + model.getCurrentPlayer().getName() + " | Score: " + model.getCurrentPlayer().getScore());

        // Start the game
        controller.startGame();
    }
}
