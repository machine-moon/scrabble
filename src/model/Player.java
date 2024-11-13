package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {

    private String name;
    private int score;
    public List<Character> tiles;

    /**
     * Constructs a Player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
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


    /**
     * Deducts points from the player's score.
     *
     * @param points the points to deduct
     */
    public void deductScore(int points) {
        this.score -= points;
    }

    /**
     * Gets the player's current tiles.
     *
     * @return the player's current tiles
     */
    public List<Character> getTiles() {
        return tiles;
    }

    /**
     * Checks if the player has a specific tile.
     *
     * @param tile the tile to check
     * @return true if the player has the tile, false otherwise
     */
    public boolean hasTile(char tile) {
        return tiles.contains(tile);
    }

    /**
     * Add a tile to the player's rack
     *
     * @param tile
     */
    public void addTile(char tile) {
        tiles.add(tile);
    }

    /**
     * Remove a tile from the player's rack
     *
     * @param tile
     */
    public void removeTile(char tile) {
        tiles.remove(tile);
    }

    /**
     * Replenishes the player's tiles to a maximum of 7 from the TileBag.
     *
     * @param tileBag the TileBag to draw tiles from
     */
    public void replenishTiles(TileBag tileBag) {
        while (tiles.size() < 7 && !tileBag.isEmpty()) {
            Character tile = tileBag.drawTile();
            if (tile != null) {
                tiles.add(tile);
            }
        }
    }
}
