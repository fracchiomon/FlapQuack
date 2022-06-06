package flapquack.ui;

import flapquack.interactions.KeyboardMouseListeners;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private KeyboardMouseListeners kbd;
    private static final int WIDTH = Frame.getWIDTH(), HEIGHT = Frame.getHEIGHT();


    public Panel(String font) {
        super(new BorderLayout());


    }
}
