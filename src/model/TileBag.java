package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {
    private List<Character> tiles;

    public TileBag() {
        tiles = new ArrayList<>();
        initializeBag();
        Collections.shuffle(tiles); // Shuffle the tiles to randomize the draw order
    }

    // Initialize the bag with the correct number of each tile
    private void initializeBag() {
        addTiles('A', 9);
        addTiles('B', 2);
        addTiles('C', 2);
        addTiles('D', 4);
        addTiles('E', 12);
        addTiles('F', 2);
        addTiles('G', 3);
        addTiles('H', 2);
        addTiles('I', 9);
        addTiles('J', 1);
        addTiles('K', 1);
        addTiles('L', 4);
        addTiles('M', 2);
        addTiles('N', 6);
        addTiles('O', 8);
        addTiles('P', 2);
        addTiles('Q', 1);
        addTiles('R', 6);
        addTiles('S', 4);
        addTiles('T', 6);
        addTiles('U', 4);
        addTiles('V', 2);
        addTiles('W', 2);
        addTiles('X', 1);
        addTiles('Y', 2);
        addTiles('Z', 1);
    }

    // Helper method to add tiles to the bag
    private void addTiles(char letter, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(letter);
        }
    }

    // Draw a tile from the bag
    public Character drawTile() {
        if (tiles.isEmpty()) {
            return null; // No more tiles available
        }
        return tiles.remove(tiles.size() - 1); // Draw from the end of the list
    }

    // Get the number of remaining tiles in the bag
    public int remainingTiles() {
        return tiles.size();
    }

    // Check if the bag is empty
    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}
