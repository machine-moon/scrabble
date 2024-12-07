package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CelebrationPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private String message;
    private int x, y;
    private Timer timer;

    public CelebrationPanel(String message) {
        this.message = message;
        this.x = 0;
        this.y = 100;
        this.timer = new Timer(30, this);
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Serif", Font.BOLD, 36));
        g.setColor(Color.RED);
        g.drawString(message, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x += 5;
        if (x > getWidth()) {
            x = -getFontMetrics(getFont()).stringWidth(message);
        }
        repaint();
    }
}