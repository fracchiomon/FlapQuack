package flapquack.ui;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    protected GameFrame gameFrame;

    public BasePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void dispose() {
        System.out.println("Dispose method. Subclasses should handle their things.");
    }

    public void draw(Graphics g){};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}


