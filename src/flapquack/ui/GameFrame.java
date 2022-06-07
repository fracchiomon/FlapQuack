package flapquack.ui;

import flapquack.game.FlapQuack;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public static final String title = FlapQuack.TITLE, font = FlapQuack.FONT;
    public static final int WIDTH = 1280, HEIGHT = 720;
    private StartPanel startPanel;

    public GameFrame() {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(title);
        add(new StartPanel());
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }
}
