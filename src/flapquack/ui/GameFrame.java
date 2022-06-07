package flapquack.ui;

import flapquack.game.FlapQuack;

import javax.swing.*;

public class GameFrame extends JFrame {
    public static final String title = FlapQuack.TITLE, font = FlapQuack.FONT;
    public static final int WIDTH = 1280, HEIGHT = 720;

    private GamePanel gamePanel;

    public GameFrame() {
        super(title);


    }
}
