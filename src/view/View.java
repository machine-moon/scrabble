package view;

import model.Model;
import model.ModelObserver;
import model.Player;
import model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class View extends JFrame implements ModelObserver {
    private Model model;
    private JPanel boardPanel;
    private JPanel playerPanel;
    private JLabel statusLabel;

    public View(Model model) {
        this.model = model;
        this.model.addObserver(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Scrabble Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(model.getBoardSize(), model.getBoardSize()));
        playerPanel = new JPanel(new GridLayout(1, 2));
        statusLabel = new JLabel("Welcome to Scrabble!");

        add(boardPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        initializeBoard();
        initializePlayerPanel();
    }

    private void initializeBoard() {
        for (int row = 0; row < model.getBoardSize(); row++) {
            for (int col = 0; col < model.getBoardSize(); col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(40, 40));
                button.setActionCommand(row + "," + col);
                button.addActionListener(new BoardButtonListener());
                boardPanel.add(button);
            }
        }
    }

    private void initializePlayerPanel() {
        List<Player> players = model.getPlayers();
        for (Player player : players) {
            JPanel playerInfoPanel = new JPanel(new BorderLayout());
            JLabel playerNameLabel = new JLabel(player.getName());
            JLabel playerScoreLabel = new JLabel("Score: " + player.getScore());
            playerInfoPanel.add(playerNameLabel, BorderLayout.NORTH);
            playerInfoPanel.add(playerScoreLabel, BorderLayout.SOUTH);
            playerPanel.add(playerInfoPanel);
        }
    }

    @Override
    public void update(String eventType, Model model) {
        if (eventType.equals("board")) {
            updateBoard();
        } else if (eventType.equals("nextTurn")) {
            updatePlayerPanel();
        } else if (eventType.equals("gameOver")) {
            statusLabel.setText("Game Over!");
        }
    }

    private void updateBoard() {
        char[][] boardState = model.getBoardState();
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JButton button = (JButton) components[i];
            int row = i / model.getBoardSize();
            int col = i % model.getBoardSize();
            char tile = boardState[row][col];
            button.setText(tile == '\0' ? "" : String.valueOf(tile));
        }
    }

    private void updatePlayerPanel() {
        List<Player> players = model.getPlayers();
        Component[] components = playerPanel.getComponents();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            JPanel playerInfoPanel = (JPanel) components[i];
            JLabel playerScoreLabel = (JLabel) playerInfoPanel.getComponent(1);
            playerScoreLabel.setText("Score: " + player.getScore());
        }
    }

    private class BoardButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] parts = e.getActionCommand().split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            // Handle board button click
        }
    }

    public static void main(String[] args) {
        Model model = Model.getInstance(15, "src/model/board_config.xml");
        View view = new View(model);
        view.setVisible(true);
    }
}