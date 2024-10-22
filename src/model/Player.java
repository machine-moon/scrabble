package model;

// Represents a player in the game
public class Player {
    private String name; // Player's name
    private int score; // Player's score

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // Get the player's name
    public String getName() {
        return name;
    }

    // Get the player's score
    public int getScore() {
        return score;
    }

    // Add points to the player's score
    public void addScore(int points) {
        this.score += points;
    }
}
