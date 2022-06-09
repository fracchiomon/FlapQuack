package flapquack.ui;

import flapquack.game.FlapQuack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable;

public class GameFrame extends JFrame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final String title = FlapQuack.TITLE, font = FlapQuack.FONT;
    public static final int WIDTH = 1280, HEIGHT = 720;

    public static int switcher;
public static GamePanel gamePanel;

    public GameFrame() {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(title);
        MainPanel mainPanel = new MainPanel();
        GamePanel gamePanel = new GamePanel("Fracchio");
        add(gamePanel);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }


    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}
