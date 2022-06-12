package flapquack.entities;

import flapquack.ui.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class Player extends Rectangle2D implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //confini dello schermo
    private static final int MAP_BORDER_DX = GameFrame.WIDTH;
    private static final int MAP_BORDER_DOWN = GameFrame.HEIGHT;

    //costanti di default per un'inizializzazione base del Player
    private final int X_DEFAULT = MAP_BORDER_DX / 2 - 100, Y_DEFAULT = MAP_BORDER_DOWN / 2 - 10;
    private final int WIDTH_DEFAULT = 30, HEIGHT_DEFAULT = 30;
    private final double maxFallSpeed = 2.3;
    private final double jumpStart = -1.8;
    private final double fallSpeed = 0.04;
    private final double stopJumpSpeed = 0.3;

    //Location del file Immagine
    //private final Image sprite;
    private static BufferedImage spriteBuffered;
    private final String spritePath = "Assets/Sprites/player.png";

    //spostamento verticale
    private double dy;
    //coordinate e dimensioni
    private int x, y, width, height;
    private boolean alive, jumping;
    private String playerName;

    //costruttore senza parametri coordinate/dimensioni -> inizializza con costanti default
    public Player(String playerName) {
        super();
        setPlayerName(playerName);
        setDy(-3.5);
        setX(X_DEFAULT);
        setY(Y_DEFAULT);
        setWidth(WIDTH_DEFAULT);
        setHeight(HEIGHT_DEFAULT);
        setFrame(this.x, this.y, this.width + this.width / 1.2, this.height + 50);
        //sprite = new ImageIcon(spritePath).getImage().getScaledInstance(this.width + 40, this.height + 40, Image.SCALE_SMOOTH);
        try {
            spriteBuffered = ImageIO.read(new File(spritePath));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        setJumping(false);
        setAlive(true);

    }

    //costruttore con parametri coordinate/dimensioni
    public Player(String playerName, int x, int y, int width, int height) {
        super();
        setPlayerName(playerName);
        setDy(-3.5);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setJumping(false);
        setFrame(this.x, this.y, this.width - this.width / 1.2, this.height + 70);

        //sprite = new ImageIcon(spritePath).getImage().getScaledInstance(this.width + 40, this.height + 40, Image.SCALE_SMOOTH);
        try {
            spriteBuffered = ImageIO.read(new File(spritePath));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        setAlive(true);
    }

    //metodo toString custom
    @Override
    public String toString() {
        String nome = "Player: " + getPlayerName();
        String prop = "X: " + getX() + " Y: " + getY() + " WIDTH: " + getWidth() + " HEIGHT: " + getHeight();
        String vivo;
        if (isAlive()) vivo = "Sono vivo";
        else vivo = "Sono morto";
        String delim = "\t";
        return nome + delim + prop + delim + vivo + delim;
    }

    //metodo chiamato dalla paintComponent per disegnare l'oggetto Player
    public void draw(Graphics g) {
        try {
            g.drawImage(spriteBuffered, this.x, this.y, this.width + 40, this.height + 40, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*g.setColor(Color.RED);
        g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());*/
    }

    //metodo per il salto
    public void jump() {
        //se è "morto" reinizializza le variabili saltando -> inizia una nuova partita
        if (!isAlive()) {
            dy = 0;
            setJumping(true);
            setAlive(true);
        }
        setJumping(true);

        //fase iniziale del salto -> dy assume valore negativo per effettuare la salita
        dy = jumpStart;
        //check collisione con il soffitto
        if (this.y < 0) {
            dy = 0;
            this.y = 0;
        }
        //caduta
        if (getDy() > 0) {
            this.dy += fallSpeed;
            if (this.y > MAP_BORDER_DOWN) {
                this.dy = 0;
                this.y = MAP_BORDER_DOWN;
            }
        }

        //"rallentamento della risalita"
        if (this.dy < 0 && !jumping) {
            dy += stopJumpSpeed;
            if (this.y > MAP_BORDER_DOWN) {
                this.dy = 0;
                this.y = MAP_BORDER_DOWN;
            }
        }

        //velocità terminale di caduta
        if (this.dy > maxFallSpeed) {
            this.dy = maxFallSpeed;
        }
    }

    //metodi GET e SET
    @Override
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    //metodi ereditati da Rectangle2D che non sono stati implementati
    @Override
    public void setRect(double x, double y, double w, double h) {

    }

    @Override
    public int outcode(double x, double y) {
        return 0;
    }

    @Override
    public Rectangle2D createIntersection(Rectangle2D r) {
        return null;
    }

    @Override
    public Rectangle2D createUnion(Rectangle2D r) {
        return null;
    }
}
