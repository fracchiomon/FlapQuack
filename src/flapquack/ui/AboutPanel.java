package flapquack.ui;

import flapquack.game.FlapQuack;

import java.awt.*;

public class AboutPanel extends BasePanel{

    private String[] text;
    public AboutPanel(GameFrame gameFrame) {
        super(gameFrame);
        costruisciTesto();
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        setFocusable(true);
        grabFocus();
        setVisible(true);
    }

    private void costruisciTesto() {
        text = new String[]{"FlapQuack è un clone di FlappyBird creato con la libreria Swing di Java.", "Created by Francesco Maria Poerio",
                "Progetto realizzato per il corso di Programmazione in Java per la Grafica", "Università degli Studi di Roma \"Tor Vergata\"",
                "Si ringrazia il Prof. Jacopo Zuliani e i colleghi Valerio Collacchi e Nicolas Palacio", "per l'essenziale aiuto fornito alla realizzazione.",
                "Un grazie generale a tutte le fonti web che hanno fornito ispirazione per la creazione."};
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
            g.setFont(new Font(FlapQuack.FONT, Font.PLAIN, 24));
            g.drawString(s, xText, yText);
            yText += yTextOffset;
        }
    }
}
