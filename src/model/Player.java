package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {

    private String name;
    private int score;
    public List<String> playerTiles;

    /**
     * Constructs a Player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.playerTiles = new ArrayList<>();
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's score.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds points to the player's score.
     *
     * @param points the points to add
     */
    public void addScore(int points) {
        this.score += points;
    }

    public boolean addTiles(List<String> tileList) {
        int tilesCount = 0;
        if (playerTiles.size() < 7){
            tilesCount = playerTiles.size();
            for (String c: tileList) {
                if (playerTiles.size() < 7) {
                    playerTiles.add(c);
                    tilesCount++;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
}
