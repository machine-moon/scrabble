package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        Model.resetInstance(); // Reset the singleton instance before each test
        model = Model.getInstance(15);
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        model.addPlayer(player1);
        model.addPlayer(player2);
    }

    @Test
    public void getInstance() {
        Model instance1 = Model.getInstance(15);
        Model instance2 = Model.getInstance(15);
        assertSame(instance1, instance2);
    }

    @Test
    public void addPlayer() {
        Player player3 = new Player("Player3");
        model.addPlayer(player3);
        assertTrue(model.getPlayers().contains(player3));
    }

    @Test
    public void placeTile() {
        player1.addTile('A');
        assertTrue(model.placeTile('A', 7, 7));
        assertFalse(model.placeTile('A', 7, 7)); // Cell already occupied
        assertFalse(model.placeTile('B', -1, 7)); // Out of bounds
    }

    @Test
    public void isFirstTurn() {
        assertTrue(model.isFirstTurn());
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        model.submitWord();
        assertFalse(model.isFirstTurn());
    }

    @Test
    public void submitWord() {
        player1.addTile('C');
        player1.addTile('A');
        player1.addTile('T');
        model.placeTile('C', 7, 7);
        model.placeTile('A', 7, 8);
        model.placeTile('T', 7, 9);
        assertTrue(model.submitWord());
        assertEquals(5, player1.getScore()); // C=3, A=1, T=1
    }

    @Test
    public void validateWord() {
        assertTrue(model.validateWord("CAT"));
        assertFalse(model.validateWord("XYZ"));
    }

    @Test
    public void isCenterCovered() {
        assertFalse(model.isCenterCovered());
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        assertTrue(model.isCenterCovered());
    }

    @Test
    public void getBoardState() {
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        char[][] boardState = model.getBoardState();
        assertEquals('A', boardState[7][7]);
    }

    @Test
    public void getCurrentPlayer() {
        assertEquals(player1, model.getCurrentPlayer());
        model.nextTurn();
        assertEquals(player2, model.getCurrentPlayer());
    }

    @Test
    public void nextTurn() {
        assertEquals(player1, model.getCurrentPlayer());
        model.nextTurn();
        assertEquals(player2, model.getCurrentPlayer());
    }

    @Test
    public void isGameOver() {
        assertFalse(model.isGameOver());
        // Simulate game over condition
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        model.submitWord();
        // Add logic to set game over condition
        assertTrue(model.isGameOver());
    }

    @Test
    public void getPlayers() {
        assertTrue(model.getPlayers().contains(player1));
        assertTrue(model.getPlayers().contains(player2));
    }

    @Test
    public void restorePlayerTiles() {
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        model.restorePlayerTiles();
        assertTrue(player1.getTiles().contains('A'));
    }

    @Test
    public void getBoardSize() {
        assertEquals(15, model.getBoardSize());
    }

    @Test
    public void testValidTilePlacement() {
        player1.addTile('A');
        assertTrue(model.placeTile('A', 7, 7));
    }

    @Test
    public void testInvalidTilePlacementOutOfBounds() {
        player1.addTile('A');
        assertFalse(model.placeTile('A', -1, 7));
        assertFalse(model.placeTile('A', 7, 15));
    }

    @Test
    public void testInvalidTilePlacementOccupiedCell() {
        player1.addTile('A');
        model.placeTile('A', 7, 7);
        player1.addTile('B');
        assertFalse(model.placeTile('B', 7, 7));
    }

    @Test
    public void testInvalidTilePlacementWithoutTile() {
        assertFalse(model.placeTile('A', 7, 7));
    }

    @Test
    public void testSingleWordScoring() {
        player1.addTile('C');
        player1.addTile('A');
        player1.addTile('T');
        model.placeTile('C', 7, 7);
        model.placeTile('A', 7, 8);
        model.placeTile('T', 7, 9);
        assertTrue(model.submitWord());
        assertEquals(5, player1.getScore()); // C=3, A=1, T=1
    }

    @Test
    public void testMultipleWordsScoring() {
        player1.addTile('C');
        player1.addTile('A');
        player1.addTile('T');
        model.placeTile('C', 7, 7);
        model.placeTile('A', 7, 8);
        model.placeTile('T', 7, 9);
        model.submitWord();

        player1.addTile('S');
        model.placeTile('S', 7, 10);
        assertTrue(model.submitWord());
        assertEquals(6, player1.getScore()); // CAT=5, S=1
    }

    @Test
    public void testInvalidWordScoring() {
        player1.addTile('X');
        player1.addTile('Y');
        player1.addTile('Z');
        model.placeTile('X', 7, 7);
        model.placeTile('Y', 7, 8);
        model.placeTile('Z', 7, 9);
        assertFalse(model.submitWord());
        assertEquals(0, player1.getScore());
    }
}