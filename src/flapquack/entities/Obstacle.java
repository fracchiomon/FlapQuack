package flapquack.entities;

import flapquack.ui.GameFrame;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import java.io.Serializable;

public class Obstacle extends Rectangle2D implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int x, y, width, height;
    private final Color testColor = Color.GREEN.darker();
    private static final int MAP_BORDER_DX = GameFrame.WIDTH, MAP_BORDER_DOWN = GameFrame.HEIGHT;

    public String toString() {
        String delim = "\t";
        String prop = "X: " + getX() + " Y: " + getY() + " WIDTH: " + getWidth() + " HEIGHT: " + getHeight();
        return "Tubo: " + delim + prop + delim;
    }

    public Obstacle(int x, int y, int width, int height) {
        super();
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setFrame(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(testColor);
        g.fillRect(this.x, this.y, this.width, this.height);
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

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
