package flapquack.ui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private String playerName;

    public GamePanel(String playerName) {
        super(new GridBagLayout());
        this.playerName = playerName;
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));

    }

}
