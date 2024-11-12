Scrabble Game - Team 11 Project

## Authors
- Mithushan Ravichandramohan, Tarek Ibrahim, Arjun Pathak, Manit Jawa
- SYSC 3110 - Fall 2024

## Overview of the Project
This project is a Java version of the Scrabble game, which has been designed using the Model-View-Controller, or MVC architecture. The player is allowed to interactively play Scrabble, considering the standard rules concerning word creation, tile placing, and score calculation.

## Project Structure

* **Controller.java**: In this program, the Controller acts like an intermediary between the `Model`, the game's logic, and the `View`, the user interface. It processes the moves the player makes and updates changes in the state of the game.
- **Main.java**: entry point; instantiates and wires the game components of Model, View, and Controller and launches the game.
- **Model.java**: responsible for the business logic of the game; it maintains the game board, players, and tile bag, implementing all the rules of Scrabble.
- **ModelObserver.java**: defines an interface for listeners of `Model` changes; allows the `View` to update its state whenever the `Model` changes.
- **Player.java**: represents a player; he maintains his current score, his tile rack, and the moves that he has made.
- **Position.java**: Helper class for position handling on the board game, used in tile placement.
- **TileBag.java**: The bag holding all the tiles that are being drawn by the players; in charge of drawing new tiles and keeping track of how many tiles are left in the bag.
- **UserActionListener.java**: Abstract Interface to the Controller for user action handling, then inform `Model`.
-**View.java**: GUI displaying the actual status of the board, points of players, and messages. It refreshes automatically on every change of the Model.

## Running the Application

1. Clone the repository or unpack the archive.
2. Open the project in a Java IDE or compile it with the command-line.
3. Run Main.java to start the application.

## Features

- **Turn-based Multiplayer**: This allows multiple players to play in turns by placing tiles and forming words.
- **Word Validator**: A check of every word played, against a predetermined word list, to assure only Scrabble legal words are accepted.
- **Scoring Calculations**: The calculation and tracking of the current score of players placing valid words on the board.
- **Dynamic Board Update**: The board and player scores would dynamically update as the players make moves.

## Technologies Used

- **Java**: Primary programming for the game logic and GUI.
- **Swing**: For designing the GUI.
MVC Architecture: Logics of game, user interface, and handling the interactions further segregate to keep the code clean and maintainable.

## Future Improvements

Advanced GUI for an overall interesting user experience
Advanced game rules like premium squares and word multipliers
Multi-language dictionary support
Better error handling and addition of tooltips

## License

This open-source project comes under the Open Source License, allowing free usage and modification without any restriction.
