// File: src/model/Player.java
package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Character> tiles;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
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

    // Deduct points from the player's score (used when reverting placements)
    public void deductScore(int points) {
        this.score -= points;
    }

    // Get the player's current tiles
    public List<Character> getTiles() {
        return tiles;
    }

    // Check if the player has a specific tile
    public boolean hasTile(char tile) {
        return tiles.contains(tile);
    }

    // Add a tile to the player's rack
    public void addTile(char tile) {
        tiles.add(tile);
    }

    // Remove a tile from the player's rack
    public boolean removeTile(char tile) {
        return tiles.remove(Character.valueOf(tile));
    }

    // Replenish the player's tiles to a maximum of 7 from the TileBag
    public void replenishTiles(TileBag tileBag) {
        while (tiles.size() < 7 && !tileBag.isEmpty()) {
            Character tile = tileBag.drawTile();
            if (tile != null) {
                tiles.add(tile);
            }
        }
    }
}
