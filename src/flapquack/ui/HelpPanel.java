package flapquack.ui;

import flapquack.game.FlapQuack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HelpPanel extends BasePanel implements KeyListener {

    private static final String[] text = {
            "Benvenuto!",
            "Premi 'Spazio' o 'Pulsante Sinistro Mouse' per saltare.",
            "Puoi anche usare la Freccia SU",
            "Premi 'BackSpace' o 'Esc' per tornare indietro."
    };

    public HelpPanel(GameFrame frame) {
        super(frame);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        setFocusable(true);
        grabFocus();

        //addMouseListener(this);
        addKeyListener(this);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
            ImageIcon img = new ImageIcon("/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack/Assets/Icon/icon64.png");
            int scelta = JOptionPane.showConfirmDialog(this, "Vuoi uscire dal gioco?", "Conferma Uscita"
                    , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, img);
            if (scelta == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        int xText = 10, yText = 100;
        int yTextOffset = 40;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,GameFrame.WIDTH, GameFrame.HEIGHT);

        for (String s : text) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(FlapQuack.FONT, Font.PLAIN, 28));
            g.drawString(s, xText, yText);
            yText += yTextOffset;
        }
    }
}
