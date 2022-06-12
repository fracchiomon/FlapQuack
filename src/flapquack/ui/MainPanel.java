package flapquack.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class MainPanel extends BasePanel implements Serializable, ActionListener, KeyListener {
    private static final int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
    @Serial
    private static final long serialVersionUID = 1L;
    private StartPanel startPanel;
    private HelpPanel helpPanel;
    private GamePanel gamePanel;

    private boolean gameON, helpON, startON;
    private static JButton invisibleBtn;

    public static JButton getInvisibleBtn() {
        return invisibleBtn;
    }

    public static void setInvisibleBtn(JButton invisibleBtn) {
        MainPanel.invisibleBtn = invisibleBtn;
    }
    public static void clickInvisibleBtn() {
        MainPanel.invisibleBtn.doClick(100);
    }

    public MainPanel(GameFrame gameFrame) {
        super(gameFrame);
        this.gameFrame = gameFrame;
        init();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(360, 240));
        setMaximumSize(new Dimension(1920, 1080));
        invisibleBtn = new JButton();
        invisibleBtn.setVisible(false);
        invisibleBtn.addActionListener(this::actionPerformed);
        add(invisibleBtn);
        add(startPanel);
        validate();
        setVisible(true);

    }

    public StartPanel getStartPanel() {
        return startPanel;
    }

    public void setStartPanel(StartPanel startPanel) {
        this.startPanel = startPanel;
    }

    public HelpPanel getHelpPanel() {
        return helpPanel;
    }

    public void setHelpPanel(HelpPanel helpPanel) {
        this.helpPanel = helpPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean isGameON() {
        return gameON;
    }

    public void setGameON(boolean gameON) {
        this.gameON = gameON;
    }

    public boolean isHelpON() {
        return helpON;
    }

    public void setHelpON(boolean helpON) {
        this.helpON = helpON;
    }

    public boolean isStartON() {
        return startON;
    }

    public void setStartON(boolean startON) {
        this.startON = startON;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE || e.getExtendedKeyCode() == KeyEvent.VK_BACK_SLASH) {
            int scelta = JOptionPane.showConfirmDialog(null, "Vuoi uscire dal gioco?", "Conferma Uscita"
                    , JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (scelta == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void init() {
        startPanel = new StartPanel(gameFrame);
        setStartON(true);
        setGameON(false);
        setHelpON(false);
        //helpPanel = new HelpPanel();
        //gamePanel = new GamePanel(timer, rng, startPanel.getPlayerName());

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int choice = startPanel.getChoice();
        if (choice == 0) {
            //remove(startPanel);
            setGameON(true);
            add(gamePanel);
            validate();
        }
        else if(choice == 1) {
            setHelpON(true);
            add(helpPanel);
            validate();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isGameON()) {
            setHelpON(false);
            setStartON(false);
            //gamePanel.paintComponent(g);
            repaint();

        }
        else if (isHelpON()) {
            setGameON(false);
            setStartON(false);
            helpPanel.paintComponent(g);
            repaint();

        }
        else if(isStartON()) {
            setGameON(false);
            setHelpON(false);
            startPanel.paintComponent(g);
            repaint();

        }
    }
}
