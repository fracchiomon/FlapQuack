package flapquack.interactions;

import java.awt.event.*;
import java.util.Hashtable;

/*
* Sto cercando di usare una classe a parte per gestire le interazioni da tastiera per tenere tutto il progetto un po'
*   più ordinato, ma è ardua.
* Ad ogni modo contiene un po' di dati su Tasti usati per il movimento (alcuni anche superflui) e tasti azione
* Il movimento del personaggio è gestibile tramite tasti WASD e/o tasti Freccia (anche quelli dell'eventuale tastierino
*   numerico.
* I tasti azione (per effettuare l'HONK) sono legati ai tasti 'E', 'Enter', e al 'Mouse_Left_Button' o 'Button1'
*
* Ho tentato di implementare una HashTable per unire i valori dei KeyCodes alle chiavi Stringa, ma sembra tutto
*   abbastanza superfluo al momento, perlomeno ai miei occhi, e quantomeno macchinoso per l'uso dalle altre classi.
* */

public abstract class KeyboardMouseListeners implements KeyListener, MouseListener, MouseMotionListener {

    protected static final int[] MOVE_KEYS_CODES = {KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D,
                                        KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                                     KeyEvent.VK_KP_UP,KeyEvent.VK_KP_DOWN,KeyEvent.VK_KP_LEFT,KeyEvent.VK_KP_RIGHT};
    protected static final String[] MOVE_KEYS_NAME = {
            "UP", "LEFT", "DOWN", "RIGHT", "ARROW_UP", "ARROW_DOWN", "ARROW_LEFT", "ARROW_RIGHT",
            "KEYPAD_UP", "KEYPAD_DOWN", "KEYPAD_LEFT", "KEYPAD_RIGHT"
    };
    protected static int[] ACTION_KEYS_CODES = {KeyEvent.VK_E, KeyEvent.VK_ENTER, MouseEvent.BUTTON1};
    protected static final String[] ACTION_KEYS_NAME = {"E_KEY", "ENTER_KEY", "MOUSE_LEFT_CLICK"};
    private static Hashtable<String, Integer> KEYS_HASHTABLE;

    public static Hashtable<String, Integer> getKeysHashtable() {
        return KEYS_HASHTABLE;
    }

    public boolean click = false;

    public KeyboardMouseListeners() {
        KEYS_HASHTABLE = new Hashtable<String, Integer>();
        for(int i = 0; i < MOVE_KEYS_CODES.length; i++) {
            KEYS_HASHTABLE.put(MOVE_KEYS_NAME[i], MOVE_KEYS_CODES[i]);
        }
        for(int j = 0; j < ACTION_KEYS_CODES.length; j++) {
            KEYS_HASHTABLE.put(ACTION_KEYS_NAME[j],ACTION_KEYS_CODES[j]);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
