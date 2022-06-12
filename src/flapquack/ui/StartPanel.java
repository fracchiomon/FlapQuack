package flapquack.ui;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class StartPanel extends BasePanel {

    private JButton nuovaPartitaButton;
    private JPanel panel1;
    private JButton helpButton;
    private JButton esciButton;
    private JLabel title;
    private BufferedImage bg;
    private String playerName;
    private int choice;
    private GameFrame gameFrame;


    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public String getPlayerName() {
        return playerName;
    }

    public StartPanel(GameFrame gameFrame) {
        super(gameFrame);
        this.gameFrame = gameFrame;
        setChoice(-1);
        //JButton invisibleBtn = MainPanel.getInvisibleBtn();
        add(panel1);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE || e.getExtendedKeyCode() == KeyEvent.VK_BACK_SLASH) {
                    int scelta = JOptionPane.showConfirmDialog(null, "Vuoi uscire dal gioco?", "Conferma Uscita"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (scelta == JOptionPane.OK_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        //bg = new ImageIcon("Assets/Background/bg.png").getImage();
        try {
            bg = ImageIO.read(new File("Assets/Background/bg.png"));
        } catch (Exception e) {
            throw new RuntimeException();
        }
        setVisible(true);


        nuovaPartitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = JOptionPane.showInputDialog(StartPanel.this, "Inserisci il tuo nome", "Nuovo Giocatore", JOptionPane.PLAIN_MESSAGE);
                System.out.println(playerName);
                Icon icon = new ImageIcon("Assets/Sprites/player.png");
                JCheckBox unfair = new JCheckBox("Unfair Mode? (Extreme only)");
                Object[] options = {"Easy", "Normal", "Hard", "EXTREME", unfair};
                int difficulty = JOptionPane.showOptionDialog(null, "Seleziona la Difficoltà!", "Difficulty Selection",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
                switch (difficulty) {
                    case 0 -> gameFrame.ShowGamePanel(playerName, 0, false);
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

            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.ShowHelpPanel();
            }
        });
        esciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
                int scelta = JOptionPane.showConfirmDialog(null, "Vuoi uscire dal gioco?", "Conferma Uscita"
                        , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, img);
                if (scelta == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });
        panel1.addKeyListener(new KeyAdapter() {
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
        });

    }

    public void draw(Graphics g) {
        try {
            g.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setEnabled(true);
        panel1.setMaximumSize(new Dimension(1920, 1080));
        panel1.setMinimumSize(new Dimension(360, 240));
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(1280, 720));
        nuovaPartitaButton = new JButton();
        nuovaPartitaButton.setHorizontalTextPosition(0);
        nuovaPartitaButton.setText("Nuova Partita");
        nuovaPartitaButton.setMnemonic('N');
        nuovaPartitaButton.setDisplayedMnemonicIndex(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 5;
        panel1.add(nuovaPartitaButton, gbc);
        helpButton = new JButton();
        helpButton.setHorizontalTextPosition(0);
        helpButton.setText("Help");
        helpButton.setMnemonic('H');
        helpButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 5;
        panel1.add(helpButton, gbc);
        esciButton = new JButton();
        esciButton.setHideActionText(false);
        esciButton.setHorizontalTextPosition(0);
        esciButton.setText("Esci");
        esciButton.setMnemonic('E');
        esciButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 5;
        panel1.add(esciButton, gbc);
        title = new JLabel();
        Font titleFont = this.$$$getFont$$$("Domus Titling", Font.BOLD, 36, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setHorizontalAlignment(0);
        title.setHorizontalTextPosition(11);
        title.setText("FlapQuack");
        title.setVerticalAlignment(1);
        title.setVerticalTextPosition(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 100, 0);
        panel1.add(title, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


}

