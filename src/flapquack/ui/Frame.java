package flapquack.ui;

import flapquack.game.FlapQuack;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private Panel panel;
    private static final int WIDTH = 1280, HEIGHT = 720;

    public static int getHEIGHT() {
        return HEIGHT;
    }
    public static int getWIDTH() {
        return WIDTH;
    }
    public Panel getPanel() {
        return panel;
    }

    public Frame(String title, String font) {
        super(title);
        panel = new Panel(font);
        add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(FlapQuack.getTITLE());
        setFocusable(true);

        pack();


    }
}
