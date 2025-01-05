Scrabble Game - Team 11 Project

## Authors
- Mithushan Ravichandramohan, Tarek Ibrahim, Arjun Pathak, Manit Jawa
- SYSC 3110 - Fall 2024

## Overview of the Project
This project is a Java version of the Scrabble game, which has been designed using the Model-View-Controller, or MVC architecture. The player is allowed to interactively play Scrabble, considering the standard rules concerning word creation, tile placing, and score calculation.

## Setup

The following are detailed steps that can be followed to run the game Scrabble.

### Prerequisites 
1.	Ensure `words_alpha.txt` is in the same directory as model.
2.	The system must be running Java. Java can be installed for your specific device from here: https://www.oracle.com/ca-en/java/technologies/downloads/

### Downlaoding Scrabble from GitHub and Running the game
1. Clone the repository or extract the archive to your desired location.
2. Open the project in your preferred Java IDE, or navigate to the project directory in your command line.
3. Run `Main.java` to start the game (e.g., in the command line, execute `java Main`).
4. The game Scrabble is now running and the game can now be played by players. 

## Usage - Playing the game

### Rules of Scrabble
The rules of the game can be found here: https://en.wikipedia.org/wiki/Scrabble

### Gameplay
1. Click on the letter you want to place on the board and then click the tile on the board where it should be placed (The game must be started with the word placed in the center, marked in orange)
2. Hit submit to submit the word or skip to skip your turn
3. The score of the current player will be displayed on top.
4. Keep playing until a winner is found 
5. View Images folder for screenshots

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


## Features

- **Turn-based Multiplayer**: This allows multiple players to play in turns by placing tiles and forming words.
- **Word Validator**: A check of every word played, against a predetermined word list, to assure only Scrabble legal words are accepted.
- **Scoring Calculations**: The calculation and tracking of the current score of players placing valid words on the board.
- **Dynamic Board Update**: The board and player scores would dynamically update as the players make moves.

## Technologies Used

- **Java**: Primary programming for the game logic and GUI.
- **Swing**: For designing the GUI.
MVC Architecture: Logics of game, user interface, and handling the interactions further segregate to keep the code clean and maintainable.

## Critical Changes/Improvements

Added Gui, Why? becuase its required.
added a singleton method aproach, only 1 instance of the model may exist.
changed archtecture from 

model <-> controller
|	   /
view  /

to 

model <-> controller <-> view

why? A team memeber didnt sync with master and decided theyll change everything and this is the architecture they decided. its fine it works.
							


## Bugs

we have bugs due to one team memebr doing their own thing and messing with the master branch, we are aware of the bugs and will fix on next milestone.

Player's tiles do not update after they placed a word correctly on the board i.e. the corresponding letters are not removed from the Player's tiles
Once an incorrect word is detected, Player gets more letters in their tile, instead of the tile remaining unchanged.
The first letter can be placed anywhere on the board violating the rules of the game that the first letter should be placed in the center.


## License

This open-source project comes under the Open Source License, allowing free usage and modification without any restriction.

   
