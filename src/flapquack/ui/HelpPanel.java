package flapquack.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HelpPanel extends JPanel implements KeyListener {

    public JOptionPane optionPane;

    public HelpPanel() {
        super(new BorderLayout());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
            optionPane = new JOptionPane();
            optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
            optionPane.setMessage("Vuoi uscire dal gioco?");
            optionPane.setSize(new Dimension(360, 240));
            optionPane.requestFocus();
            optionPane.setVisible(true);
            add(optionPane);
            revalidate();
            if (optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
