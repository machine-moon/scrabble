package model;

import java.util.*;


public class AiPlayer extends Player {
    private final Set<String> wordlist;
    private Model model;

    public AiPlayer(String name, Set<String> wordlist, Model m) {
        super(name);
        this.wordlist = wordlist;
        this.model = m;
    }

    @Override
    public boolean isAi() {
        return true;
    }

    public boolean play() {
        List<Character> tiles = getTiles();
        // force cut off the tiles to 7
        while (tiles.size() > 7) {
            tiles.removeLast();
        }

        System.out.println("Tiles: " + tiles);
        Set<String> possibleWords = generateAllCombinations(tiles);
        Set<String> validWords = filterValidWords(possibleWords);

        System.out.println("Valid words: " + validWords);

        // Calculate scores for each valid word
        List<String> sortedWords = new ArrayList<>(validWords);
        sortedWords.sort((word1, word2) -> Integer.compare(model.calculateWordScore(word2), model.calculateWordScore(word1)));

        // Try to place each word on the board

        for (String word : sortedWords) {
            if (tryPlaceWord(model, word)) {
                if (model.submitWord())
                    return true;
            }
        }




        System.out.println(getName() + " could not find a valid move.");
        return false;
    }

    private Set<String> generateAllCombinations(List<Character> tiles) {
        Set<String> combinations = new HashSet<>();
        for (int i = 1; i <= tiles.size(); i++) {
            generateCombinations(tiles, "", i, combinations);
        }
        return combinations;
    }

    private void generateCombinations(List<Character> tiles, String prefix, int length, Set<String> combinations) {
        if (length == 0) {
            combinations.add(prefix);
            return;
        }
        for (int i = 0; i < tiles.size(); i++) {
            List<Character> remaining = new ArrayList<>(tiles);
            remaining.remove(i);
            generateCombinations(remaining, prefix + tiles.get(i), length - 1, combinations);
        }
    }

    private Set<String> filterValidWords(Set<String> possibleWords) {
        Set<String> validWords = new HashSet<>();
        for (String word : possibleWords) {
            if (wordlist.contains(word.toLowerCase())) {
                validWords.add(word);
            }
        }
        return validWords;
    }


    private boolean tryPlaceWord(Model model, String word) {
        int boardSize = model.getBoardSize();
        List<int[]> positions = new ArrayList<>();

        // Generate all possible positions on the board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                positions.add(new int[]{row, col});
            }
        }

        // Shuffle positions to place words randomly
        Collections.shuffle(positions);

        // Try to place the word at each position
        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            if (canPlaceWord(model, word, row, col, true)) {
                placeWord(model, word, row, col, true);
                return true;
            }
            if (canPlaceWord(model, word, row, col, false)) {
                placeWord(model, word, row, col, false);
                return true;
            }
        }
        return false;
    }

    private boolean canPlaceWord(Model model, String word, int row, int col, boolean isHorizontal) {
        char[][] boardState = model.getBoardState();
        boolean hasAdjacent = false;

        for (int i = 0; i < word.length(); i++) {
            int currentRow = isHorizontal ? row : row + i;
            int currentCol = isHorizontal ? col + i : col;
            if (currentRow >= boardState.length || currentCol >= boardState[0].length || boardState[currentRow][currentCol] != '\0') {
                return false;
            }
            // Check for adjacent tiles
            if (model.isValidPosition(currentRow - 1, currentCol) && boardState[currentRow - 1][currentCol] != '\0' ||
                    model.isValidPosition(currentRow + 1, currentCol) && boardState[currentRow + 1][currentCol] != '\0' ||
                    model.isValidPosition(currentRow, currentCol - 1) && boardState[currentRow][currentCol - 1] != '\0' ||
                    model.isValidPosition(currentRow, currentCol + 1) && boardState[currentRow][currentCol + 1] != '\0') {
                hasAdjacent = true;
            }
        }
        return hasAdjacent;
    }

    private void placeWord(Model model, String word, int row, int col, boolean isHorizontal) {
        for (int i = 0; i < word.length(); i++) {
            int currentRow = isHorizontal ? row : row + i;
            int currentCol = isHorizontal ? col + i : col;
            model.placeTile(word.charAt(i), currentRow, currentCol);
        }
    }
}