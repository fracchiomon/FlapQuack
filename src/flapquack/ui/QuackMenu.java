package flapquack.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuackMenu extends JMenuBar implements ActionListener {
    private final static String menuText[] = {"Game", "About"};
    private final static String menuItemText[] = {"Nuova Partita", "Help", "Esci"};
    private JMenu menu[];
    private JMenuItem menuItem[];
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
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (menuItem[1].equals(e.getSource())) {
            try {
                //gsm.getSTATES().push(new HelpState(gsm));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (menuItem[2].equals(e.getSource())) {
            //gsm.getSTATES().clear();

            ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
            int option = JOptionPane.showConfirmDialog(null, "Vuoi uscire dal gioco?", "Uscita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, img);

            if (option == JOptionPane.OK_OPTION) {
                System.exit(0);
            }

            System.exit(0);
        }
    }
}
