package controller;

/**
 * UserActionListener interface is used to define the methods that the controller will use to communicate with the view.
 * The view will implement this interface and the controller will call these methods to notify the view of user actions.
 */
public interface UserActionListener {
    void onTileSelected(char tile);

    void onBoardCellClicked(int row, int col);

    void onSubmitButtonClicked();

    void onSkipTurnClicked();
}
