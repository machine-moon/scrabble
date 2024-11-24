package model;

public class Move {
    private final Position position;
    private final char tile;

    public Move(Position position, char tile) {
        this.position = position;
        this.tile = tile;
    }

    public Position getPosition() {
        return position;
    }

    public char getTile() {
        return tile;
    }
}