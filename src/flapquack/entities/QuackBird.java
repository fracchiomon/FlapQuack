package flapquack.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class QuackBird {

    //Variabili
    private double x, y, w, h;
    private double dy;
    private int score;
    private boolean empty;
    private Rectangle2D rect;

    //GET & SET METHODS
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getW() {
        return w;
    }
    public void setW(double w) {
        this.w = w;
    }
    public double getH() {
        return h;
    }
    public void setH(double h) {
        this.h = h;
    }
    public double getDy() {
        return dy;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public boolean isEmpty() {return empty;}
    public void setEmpty(boolean empty) {this.empty = empty;}
    public Rectangle2D getRect() {return rect;}

    public QuackBird(double x, double y, double w, double h) {
        rect = new Rectangle2D.Double(x,y,w,h);
        rect.setRect(x,y,w,h);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x,(int)y,(int)w,(int)h);
    }

}
