package model;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Finds the best move for the AI player.
     *
     * @param model the game model
     * @return the best move as a list of positions and characters
     */
    public List<Move> findBestMove(Model model) {
        List<Move> bestMove = new ArrayList<>();
        int bestScore = 0;

        // Generate all possible moves
        List<List<Move>> allMoves = generateAllMoves(model);

        // Evaluate each move and find the highest scoring one
        for (List<Move> move : allMoves) {
            int score = model.calculateMoveScore(move);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Generates all possible moves for the AI player.
     *
     * @param model the game model
     * @return a list of all possible moves
     */
    private List<List<Move>> generateAllMoves(Model model) {
        List<List<Move>> allMoves = new ArrayList<>();
        // Make a copy of the tiles list to avoid ConcurrentModificationException
        List<Character> tilesCopy = new ArrayList<>(getTiles());
        for (char tile : tilesCopy) {
            for (int row = 0; row < model.getBoardSize(); row++) {
                for (int col = 0; col < model.getBoardSize(); col++) {
                    if (model.placeTile(tile, row, col)) {
                        List<Move> move = new ArrayList<>();
                        move.add(new Move(new Position(row, col), tile));
                        allMoves.add(move);
                        model.restorePlayerTiles(); // Revert the move
                    }
                }
            }
        }
        return allMoves;
    }

    /**
     * Executes the best move for the AI player.
     *
     * @param model the game model
     */
    public void playBestMove(Model model) {
        List<Move> bestMove = findBestMove(model);
        for (Move move : bestMove) {
            model.placeTile(move.getTile(), move.getPosition().row, move.getPosition().col);
        }
        model.submitWord();
    }
}