package main;

import controller.Controller;
import model.Model;
import model.Player;
import view.View;

public class Main {
    public static void main(String[] args) {
        Model model = new Model(15); // Standard Scrabble board size is 15x15
        View view = new View(model); // Create the view with a reference to the model
        Controller controller = new Controller(model); //Create the controller

        // Add players
        model.addPlayer(new Player("Alice"));
        model.addPlayer(new Player("Bob"));

        controller.startGame(); // Start the game loop
    }
}