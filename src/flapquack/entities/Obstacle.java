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

public class Obstacle extends Rectangle2D implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int MAP_BORDER_DX = GameFrame.WIDTH, MAP_BORDER_DOWN = GameFrame.HEIGHT;
    //private final Color testColor = Color.GREEN.darker();
    private int x, y, width, height;
    /*private final Image sprite;
    private final Image spriteUpsideDown;*/
    private static BufferedImage spriteBuffered = null;
    private static BufferedImage spriteUpsideDownBuffered = null;
    private final String spritePath = "/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack/Assets/Sprites/pipe.png";
    private final String spriteUpsideDownPath = "/Users/fracchiomon/Documents/STM-TOR-VERGHY/Java/FlapQuack/Assets/Sprites/pipe_upsidedown.png";
    private final boolean reverse;

    //------------------------------------------------------------//

    //Costruttore unico con parametri passati e Boolean che controlla se disegnare/creare un oggetto normale o
    //"a testa in giù"
    public Obstacle(int x, int y, int width, int height, boolean reverse) {
        super();
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setFrame(x, y, width, height);
        this.reverse = reverse;
        /*sprite = new ImageIcon("Assets/Sprites/pipe.png").getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
        spriteUpsideDown = new ImageIcon("Assets/Sprites/pipe_upsidedown.png").getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);*/
        try {
            spriteBuffered = ImageIO.read(new File(spritePath));
            spriteUpsideDownBuffered = ImageIO.read(new File(spriteUpsideDownPath));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //override del metodo toString
    @Override
    public String toString() {
        String delim = "\t";
        String prop = "X: " + getX() + " Y: " + getY() + " WIDTH: " + getWidth() + " HEIGHT: " + getHeight();
        return "Tubo: " + delim + prop + delim;
    }

    public void draw(Graphics g) {
        /*g.setColor(testColor);
        g.fillRect(this.x, this.y, this.width, this.height);*/
        try {
            if (isReverse()) {
                //g.drawImage(spriteUpsideDown, this.x, this.y - 8, null);
                g.drawImage(spriteUpsideDownBuffered, this.x, this.y - 8, this.width, this.height, null);

            } else {
                //g.drawImage(sprite, this.x, this.y + 8, null);
                g.drawImage(spriteBuffered, this.x, this.y + 8,this.width, this.height, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodi GET e SET
    public boolean isReverse() {
        return reverse;
    }
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
