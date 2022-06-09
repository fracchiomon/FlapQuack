package flapquack.ui;

import flapquack.game.FlapQuack;
import flapquack.game.Music;

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
    public QuackMenu menu;
public static GamePanel gamePanel;

    static class QuackMenu extends JMenuBar implements ActionListener {
        private final static String menuText[] = {"Game", "About"};
        private final static String menuItemText[] = {"Nuova Partita", "Help", "Esci"};
        private JMenu menu[];
        private JMenuItem menuItem[];
        private String playerName;

        private static Music music;
        //private GameStateManager gsm;

        public QuackMenu() {
            super();
            //gsm = gameStateManager;
            menu = new JMenu[2];
            menuItem = new JMenuItem[3];


            for (int m = 0; m < menu.length; m++) {
                menu[m] = new JMenu(menuText[m]);
                add(menu[m]);
            }
            for (int m = 0; m < menuItem.length; m++) {
                menuItem[m] = new JMenuItem(menuItemText[m]);
                menuItem[m].addActionListener(this);
                menu[0].add(menuItem[m]);
            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (menuItem[0].equals(e.getSource())) {
                try {
                    //gsm.getSTATES().push(new Level1State(gsm));
                    playerName = JOptionPane.showInputDialog(this, "Inserisci nome giocatore");
                    gamePanel = new GamePanel(playerName);
                    removeAll();
                    add(gamePanel);
                    revalidate();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (menuItem[1].equals(e.getSource())) {
                try {
                    //gsm.getSTATES().push(new HelpState(gsm));
                    removeAll();
                    add(new HelpPanel());
                    revalidate();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (menuItem[2].equals(e.getSource())) {
                //gsm.getSTATES().clear();

                ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
                int option = JOptionPane.showConfirmDialog(this, "Vuoi uscire dal gioco?", "Uscita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, img);

                if (option == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }

                System.exit(0);
            }
        }
    }

    public GameFrame() {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(title);
        menu = new QuackMenu();
        setJMenuBar(menu);
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
