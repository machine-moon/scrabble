package main;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

public class Main {

    public static void main(String[] args) {
        // Determine the path to the word list file
        String wordListPath = args.length > 0 ? args[0] : "wordlist.txt";

        // Initialize the model with a standard Scrabble board size (15x15) and the word list path
        Model model = new Model(15, wordListPath);

        // Initialize the view with a reference to the model
        View view = new View(model);

        // Initialize the controller with a reference to the model
        Controller controller = new Controller(model);

        // Add players to the model
        model.addPlayer(new Player("tarek"));
        model.addPlayer(new Player("mithushan"));

        // Start the game loop
        controller.startGame();
    }
}
