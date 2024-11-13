  // File: src/test/java/model/ModelTest.java
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private Model model;
    private Player player;

    @BeforeEach
    public void setUp() {
        model = new Model(15);
        player = new Player("TestPlayer");
        model.addPlayer(player);
    }

    // Placement Tests

    /**
     * Test valid tile placement on the board.
     */
    @Test
    public void testValidTilePlacement() {
        player.addTile('A');
        assertTrue(model.placeTile('A', 7, 7));
    }

    /**
     * Test invalid tile placement out of bounds.
     */
    @Test
    public void testInvalidTilePlacementOutOfBounds() {
        player.addTile('A');
        assertFalse(model.placeTile('A', -1, 7));
        assertFalse(model.placeTile('A', 7, 15));
    }

    /**
     * Test invalid tile placement on an occupied cell.
     */
    @Test
    public void testInvalidTilePlacementOccupiedCell() {
        player.addTile('A');
        model.placeTile('A', 7, 7);
        player.addTile('B');
        assertFalse(model.placeTile('B', 7, 7));
    }

    /**
     * Test invalid tile placement without having the tile.
     */
    @Test
    public void testInvalidTilePlacementWithoutTile() {
        assertFalse(model.placeTile('A', 7, 7));
    }

    // Scoring Tests

    /**
     * Test scoring for a single word placement.
     */
    @Test
    public void testSingleWordScoring() {
        player.addTile('C');
        player.addTile('A');
        player.addTile('T');
        model.placeTile('C', 7, 7);
        model.placeTile('A', 7, 8);
        model.placeTile('T', 7, 9);
        assertTrue(model.submitWord());
        assertEquals(5, player.getScore()); // C=3, A=1, T=1
    }

    /**
     * Test scoring for multiple words placement.
     */
    @Test
    public void testMultipleWordsScoring() {
        player.addTile('C');
        player.addTile('A');
        player.addTile('T');
        model.placeTile('C', 7, 7);
        model.placeTile('A', 7, 8);
        model.placeTile('T', 7, 9);
        model.submitWord();

        player.addTile('S');
        model.placeTile('S', 7, 10);
        assertTrue(model.submitWord());
        assertEquals(6, player.getScore()); // CAT=5, S=1
    }

    /**
     * Test invalid word scoring.
     */
    @Test
    public void testInvalidWordScoring() {
        player.addTile('X');
        player.addTile('Y');
        player.addTile('Z');
        model.placeTile('X', 7, 7);
        model.placeTile('Y', 7, 8);
        model.placeTile('Z', 7, 9);
        assertFalse(model.submitWord());
        assertEquals(0, player.getScore());
    }
}