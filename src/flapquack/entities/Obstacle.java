package flapquack.entities;

import flapquack.ui.GameFrame;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Obstacle extends Rectangle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int x, y, width, height;
    private final Color testColor = Color.ORANGE.darker();
    private static final int MAP_BORDER_DX = GameFrame.WIDTH, MAP_BORDER_DOWN = GameFrame.HEIGHT;

    public String toString() {
        String delim = "\t";
        String prop = "X: " + getX() + " Y: " + getY() + " WIDTH: " + getWidth() + " HEIGHT: " + getHeight();
        return this.getClass().getName() + delim + prop + delim;
    }

    public Obstacle(int x, int y, int width, int height) {
        super();
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setBounds(x, y, width, height);
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

    public void setHeight(int height) {
        this.height = height;
    }
}
