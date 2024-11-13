package main;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

public class Main {

    public static void main(String[] args) {
        // Initialize the model with a standard Scrabble board size (15x15) and the word list path
        Model model = new Model(15);

        // Add players to the model
        model.addPlayer(new Player("tarek"));
        model.addPlayer(new Player("mithushan"));

        // Initialize the view with a reference to the model
        View view = new View();

        // Initialize the controller with a reference to the model
        Controller controller = new Controller(model, view);

        // Initialize the board and tile rack in the view
        view.initializeBoard(model.getBoardSize());
        view.initializeTileRack(model.getCurrentPlayer().getTiles());
        view.updateStatus("Current player: " + model.getCurrentPlayer().getName() + " | Score: " + model.getCurrentPlayer().getScore());

        // Start the game
        controller.startGame();
    }
}
