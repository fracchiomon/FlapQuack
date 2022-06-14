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

    public QuackMenu menu;
    private BasePanel currentPanel;

    public static GamePanel gamePanel;
    public static HelpPanel helpPanel;
    public static StartPanel startPanel;

    class QuackMenu extends JMenuBar implements ActionListener {
        private final static String menuText[] = {"Game"};
        private final static String menuItemText[] = {"Nuova Partita","Menu Principale", "Help", "Esci"};

        private JMenu menu[];
        private JMenuItem menuItem[];
        private JMenuItem aboutMenuItem;
        private String playerName;
        private GameFrame gameFrame;
        //private GameStateManager gsm;

        public QuackMenu(GameFrame gameFrame) {
            super();
            this.gameFrame = gameFrame;
            //gsm = gameStateManager;
            menu = new JMenu[1];
            menuItem = new JMenuItem[4];


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
                    playerName = JOptionPane.showInputDialog(this, "Inserisci nome giocatore");
                    Icon icon = new ImageIcon("Assets/Icon/icon64.png");
                    JCheckBox unfair = new JCheckBox("Unfair Mode? (Extreme only)");
                    Object[] options = {"Easy", "Normal", "Hard", "EXTREME", unfair};
                    int difficulty = JOptionPane.showOptionDialog(null,"Seleziona la Difficoltà!","Difficulty Selection",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
                    switch (difficulty) {
                        case 0 -> gameFrame.ShowGamePanel(playerName, 0,false);
                        case 1 -> gameFrame.ShowGamePanel(playerName, 1, false);
                        case 2 -> gameFrame.ShowGamePanel(playerName, 2, false);
                        case 3 -> {
                            if (unfair.isSelected()) {
                                gameFrame.ShowGamePanel(playerName, 3, true);
                            }
                            gameFrame.ShowGamePanel(playerName, 3, false);
                        }
                        default -> gameFrame.ShowGamePanel(playerName, 1, false);
                    }

                    revalidate();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if(menuItem[1].equals(e.getSource())) {

                try {
                    gameFrame.ShowStartPanel();
                    revalidate();
                } catch (Exception ex) {
                    throw new RuntimeException();
                }

            } else if (menuItem[2].equals(e.getSource())) {
                try {
                    //gsm.getSTATES().push(new HelpState(gsm));
                    gameFrame.ShowHelpPanel();
                    gameFrame.transferFocus();
                    revalidate();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (menuItem[3].equals(e.getSource())) {
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

    private static Music song1;

    public static Music getSong1() {
        return song1;
    }
    public static void setSong1(Music musica) {
        song1 = musica;
    }

    private void SetCurrentPanel(BasePanel basePanel)
    {
        if (this.currentPanel != null)
        {
            this.currentPanel.dispose();
            remove(this.currentPanel);
        }

        this.currentPanel = basePanel;
        add(this.currentPanel);
        validate();
    }

    public void ShowGamePanel(String playerName, int difficulty, boolean unfair)
    {
        if ( playerName == null )
        {
            SetCurrentPanel(new GamePanel(this, difficulty, unfair));
        }
        else
        {
            SetCurrentPanel(new GamePanel(this, playerName, difficulty, unfair));
        }
    }

    public void ShowHelpPanel() {
        SetCurrentPanel(new HelpPanel(this));
    }


    public void ShowStartPanel() {
        SetCurrentPanel(new StartPanel(this));
    }


    public GameFrame() {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(title);
        menu = new QuackMenu(this);
        song1 = new Music("/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack/Assets/Music/monty_on_the_run.wav");
        setIconImage(new ImageIcon("/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack/Assets/Icon/icon64.png").getImage());
        setJMenuBar(menu);
        ShowStartPanel();

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }


    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}
