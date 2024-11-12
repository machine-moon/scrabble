package controller;

public interface UserActionListener {
    void onTileSelected(char tile);
    void onBoardCellClicked(int row, int col);
    void onSubmitButtonClicked();
    void onSkipTurnClicked();
}