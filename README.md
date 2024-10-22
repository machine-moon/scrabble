
# Scrabble Team 11 - Project

## Authors
- Mithushan Ravichandramohan, Tarek Ibrahim, Arjun Pathak, Manit Jawa
- SYSC 3110 - Fall 2024

## Project Overview

This project is a simple implementation of the Scrabble game. It follows the Model-View-Controller (MVC) design pattern, where each component has a clear and distinct responsibility. The game allows players to participate and score points based on the standard Scrabble rules.

## Project Structure

- **Controller.java**: The controller acts as an intermediary between the model (game logic) and the view (user interface). It handles user inputs and updates the game state accordingly.
  
- **Main.java**: This is the main entry point for the program. It initializes the game components (model, view, and controller) and starts the game.

- **Model.java**: The model contains the core game logic. It manages the game state, including the board, players, and the tile bag. It ensures that the rules of Scrabble are followed.

- **ModelListener.java**: This file defines an interface or class that listens for changes in the model and triggers appropriate actions. It supports an event-driven architecture, ensuring that the view is updated whenever the model changes.

- **Player.java**: This class represents a player in the game. It handles player-specific data like the player's score, the tiles they hold, and their actions during the game.

- **View.java**: The view is responsible for rendering the game interface. It displays the current game state to the players and updates the screen based on changes in the model.

## How to Run

1. Clone the repository or extract the files from the provided archive.
2. Open the project in your preferred Java IDE.
3. Compile and run the `Main.java` file, which will initialize the game.

## Features

- **Multiple Players**: Support for multiple players to take turns.
- **Tile Management**: Each player can manage their tiles, placing them on the board and drawing new tiles from the bag.
- **Score Calculation**: The model calculates the score based on valid words formed on the Scrabble board.

## Technologies Used

- Java
- MVC Architecture

## Future Enhancements

- Add a graphical user interface (GUI) for a more interactive experience.
- Include support for dictionaries to validate words.
- Enhance game rules to handle different Scrabble variations.

## License

This project is open-source and free to use.
