package flapquack.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public abstract class BasePanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1L;
    protected GameFrame gameFrame;
    public final String iconRelativePath = "Assets/Icon/icon64.png";
    public final String iconAbsolutePath = "/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack"+iconRelativePath;
    public final ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");


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


