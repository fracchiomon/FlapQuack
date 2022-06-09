package flapquack.ui;

import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import java.lang.Thread;

public class GamePanel extends JPanel implements Runnable, Serializable, MouseListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
    private static int FPS = 25;
    private static long targetTime = 1000 / FPS;
    private String playerName;
    private Random rng;
    private Thread thread;
    private Render render;
    //private Player player;
    //private Obstacle obstacle;
    //private ArrayList<Obstacle> obstacleList;
    private Rectangle rectPlayer;
    private ArrayList<Rectangle> rectObstacles;
    private boolean gameOver, gameStarted;
    private int score;
    private double dy;
    private int ticks;
    private boolean running;

    public GamePanel(String playerName) {
        super();
        this.playerName = playerName;
        thread = new Thread(this);
        render = new Render();
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        init();
        setFocusable(true);
        setVisible(true);
    }

    @Override
    public void run() {
        long startTime, elapsedTime, waitTime;
        double speed = 2;


        while (isRunning()) {
            ticks++;
            startTime = System.currentTimeMillis();

            for (int i = 0; i < rectObstacles.size(); i++) {
                Rectangle newObst = rectObstacles.get(i);
                newObst.x -= speed;
            }

            if (dy < 15)
                dy = -5;

            for (int i = 0; i < rectObstacles.size(); i++) {
                Rectangle newObst = rectObstacles.get(i);
                if (newObst.x + newObst.width < 0) {
                    rectObstacles.remove(newObst);

                    if (newObst.y == 0) {
                        addNewObstacle(false);
                    }
                }
            }

            if (falling){
                rectPlayer.y -= dy;
            }

            for (Rectangle o : rectObstacles) {
                //player passa attraverso?
                if (o.y == 0 && rectPlayer.x + rectPlayer.width / 2 > o.x + o.width / 2 - 10 &&
                        rectPlayer.x + rectPlayer.width / 2 < o.x + o.width / 2 + 10) {
                    score = score + 1;
                    System.out.println("punteggio: " + score);
                }

                //collisioni
                if (o.intersects(rectPlayer)) {
                    setGameOver(true);
                    if (rectPlayer.x <= o.x) {
                        rectPlayer.x = o.x - rectPlayer.width;
                    } else {
                        if (o.y != 0) {
                            rectPlayer.y = o.y - rectPlayer.height;
                        } else if (rectPlayer.y < o.height) {
                            rectPlayer.y = o.height;
                        }
                    }
                }

            }
            //collisione con terra
            if (rectPlayer.y > HEIGHT - 120 || rectPlayer.y < 0) {
                setGameOver(true);
                rectPlayer.y = HEIGHT - 120 - 20;
                jumping = false;
                falling = false;
            }

            //collisione col cielo
            if (rectPlayer.y + dy >= HEIGHT - 120) {
                rectPlayer.y = HEIGHT - 120 - rectPlayer.height;
            }
            //ridisegno lo schermo
            repaint();

            elapsedTime = System.nanoTime() - startTime;
            waitTime = targetTime - elapsedTime / 1000000;

            if (waitTime <= 0) {
                waitTime = 5;
            }

            try {
                Thread.sleep(waitTime); //sleep for x milliseconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void init() {
        grabFocus();
        rectPlayer = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 30, 30);
        rectObstacles = new ArrayList<Rectangle>();
        rng = new Random(System.nanoTime());
        dy = 0;
        ticks = 0;
        setRunning(true);
        setGameOver(false);
        jumping = false;
        falling = false;

        addMouseListener(this);
        addKeyListener(this);

        //pos iniziale del player

        addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);

        thread.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);

        //ground
        g.setColor(Color.ORANGE.darker());
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        //grass
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        //bird icon
        g.setColor(Color.RED);
        g.fillRect(rectPlayer.x, rectPlayer.y, rectPlayer.width, rectPlayer.height);

        for (Rectangle o : rectObstacles) {
            paintObstacle(g, o);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 60));

        if (!isGameStarted()) {
            g.drawString("Clicca o Premi 'Spazio' per Giocare", 150, HEIGHT / 2 - 50);
        }
        if (isGameOver()) {
            g.drawString("Game Over :<", 100, HEIGHT / 2);
        }
        if (!isGameOver() && isGameStarted()) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }


    public void addNewObstacle(boolean startGame) {
        int spacing = 300;
        int width = 100;
        int height = rng.nextInt(50, 300);

        if (startGame) {
            rectObstacles.add(new Rectangle(
                    WIDTH + width + rectObstacles.size() * 300,
                    HEIGHT - height - 120,
                    width,
                    height));

            rectObstacles.add(new Rectangle(
                    WIDTH + width + (rectObstacles.size() - 1) * 300,
                    0,
                    width,
                    HEIGHT - height - spacing));
        } else {
            rectObstacles.add(new Rectangle(rectObstacles.get(rectObstacles.size() - 1).x + 600,
                    HEIGHT - height - 120,
                    width,
                    height));
            rectObstacles.add(new Rectangle(rectObstacles.get(rectObstacles.size() - 1).x,
                    0,
                    width,
                    HEIGHT - height - spacing));
        }
    }

    public void paintObstacle(Graphics g, Rectangle obst) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(obst.x, obst.y, obst.width, obst.height);
    }

    double fallSpeed =0.15;
    double maxFallSpeed =4.0;
    double jumpStart =-5.5;
    boolean jumping, falling;
    double stopJumpSpeed = 0.3;

    public void jump() {

        //se è gameOver, saltare fa iniziare una nuova partita
        if (isGameOver()) {
            rectPlayer = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 30, 30);
            rectObstacles.clear();
            dy = 0;
            score = 0;
            addNewObstacle(true);
            addNewObstacle(true);
            addNewObstacle(true);
            addNewObstacle(true);
            jumping = true;
            setGameOver(false);
        }

        if (!isGameStarted()) {
            jumping = true;
            setGameStarted(true);
        }
        else if (!isGameOver()) {
            jumping = true;
            //esegue il salto
            if (jumping) {
                //System.out.println("Sto salendo: " + tileMap.getType(currRow, currCol));

                dy = jumpStart;
                if (rectPlayer.y < 0) {
                    dy = 0;
                    rectPlayer.y = 0;
                }
                //falling = true;
                //GameState.yOffset = dy;
                //if (y + dy <= 0) y = 1;
            }
            jumping = false;
            falling = true;
            // falling
            if (falling) {
                System.out.println("Sto cadendo 1");

                if (dy > 0) {
                    System.out.println("Sto cadendo 3");

                    dy += fallSpeed;
                    jumping = false;
                    if (rectPlayer.y > HEIGHT) {
                        dy = 0;
                        rectPlayer.y = HEIGHT;
                    }
                    //GameState.yOffset = dy;
                }

                if (dy < 0 && !jumping) {
                    System.out.println("Sto cadendo 4");

                    dy += stopJumpSpeed;
                    if (rectPlayer.y > HEIGHT) {
                        dy = 0;
                        rectPlayer.y = HEIGHT;
                    }
                    //GameState.yOffset = dy;
                }

                if (dy > maxFallSpeed) {
                    //System.out.println("Sto cadendo 5");

                    dy = maxFallSpeed;
                    //GameState.yOffset = dy;
                }
                System.out.println("Sto cadendo 6");
            }

        }
        //System.out.println("Sto salendo: " + tileMap.getType(currRow, currCol));


    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Random getRng() {
        return rng;
    }

    public void setRng(Random rng) {
        this.rng = rng;
    }

    public Rectangle getRectPlayer() {
        return rectPlayer;
    }

    public void setRectPlayer(Rectangle rectPlayer) {
        this.rectPlayer = rectPlayer;
    }

    public ArrayList<Rectangle> getRectObstacles() {
        return rectObstacles;
    }

    public void setRectObstacles(ArrayList<Rectangle> rectObstacles) {
        this.rectObstacles = rectObstacles;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean start) {
        this.running = true;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("premuto tasto: " + e.getKeyCode() + e.getKeyChar());
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)) {
            jumping = true;
            falling = false;
            jump();
        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE || e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            int option = JOptionPane.showConfirmDialog(this, "Vuoi uscire dal gioco?");

            if (option == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("rilasciato tasto: " + e.getKeyCode()+ e.getKeyChar());
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)) {
            //setJumping(false);
            jumping = false;
            falling = true;
            System.out.println("Valore dy: "+ dy);
            System.out.println("Lo prendo");
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jumping = true;
        //falling = false;
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //jump();
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


}


class Render extends JPanel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameFrame.getGamePanel().paintComponent(g);

    }
}