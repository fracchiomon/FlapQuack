package flapquack.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, Serializable, MouseListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
    private static final int FPS = 25;
    private static final long targetTime = 1000 / FPS;
    public final double maxFallSpeed = 2.3;
    public final double jumpStart = -1.8;
    private final Thread thread;
    public double fallSpeed = 0.04;
    public boolean jumping, falling, playing;
    double stopJumpSpeed = 0.3;
    private String playerName;
    private Random rng;
    //private Player player;
    //private Obstacle obstacle;
    //private ArrayList<Obstacle> obstacleList;
    private Rectangle uccello;
    private ArrayList<Rectangle> rectObstacles;
    private boolean gameOver, gameStarted;
    private int score;
    private double dy;
    private boolean running;
    private final int uccelloInit_X = WIDTH / 2 - 100, uccelloInit_Y = HEIGHT / 2 - 10, uccelloWidth = 30, uccelloHeight = 30;
    public GamePanel(String playerName) {
        super();
        this.playerName = playerName;
        thread = new Thread(this);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));

        init();
        setFocusable(true);
        setVisible(true);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public void run() {
        int coda = uccello.x, testa = uccello.x + uccello.width;
        boolean inMezzoAllePalle = false;
        long startTime, elapsedTime, waitTime;
        int speed = 2;


        while (isRunning()) {
            //ticks++;
            startTime = System.currentTimeMillis();

            for (Rectangle newObst : rectObstacles) {
                newObst.x -= speed;
            }

            //if (ticks % 2 == 0 && dy < 15)

            dy += 0.05;

            for (int i = 0; i < rectObstacles.size(); i++) {
                Rectangle newObst = rectObstacles.get(i);
                if (newObst.x + newObst.width < 0) {
                    rectObstacles.remove(newObst);

                    if (newObst.y == 0) {
                        addNewObstacle(false);
                    }
                }
            }

            if (jumping) {
                uccello.y += dy;
            }


            for (Rectangle rectangle : rectObstacles) {
                //player passa attraverso?
                /*if (rectangle.y == 0 &&
                        (rectPlayer.x + rectPlayer.width / 2 > rectangle.x + rectangle.width / 2 - speed
                        &&
                        rectPlayer.x + rectPlayer.width / 2 < rectangle.x + rectangle.width / 2 + speed)) {
                    System.out.println(rectangle.y == 0 + rectangle.y);
                    System.out.println();
                    System.out.println(rectPlayer.x + rectPlayer.width / 2 < rectangle.x + rectangle.width / 2 + speed);

                    score++;
                    System.out.println("punteggio: " + score);
                }*/

                //collisioni
                if (rectangle.intersects(uccello)) {
                    setGameOver(true);
                    if (uccello.x <= rectangle.x) {
                        uccello.x = rectangle.x - uccello.width;
                    } else {
                        if (rectangle.y != 0) {
                            uccello.y = rectangle.y - uccello.height;
                        } else if (uccello.y < rectangle.height) {
                            uccello.y = rectangle.height;
                        }
                    }
                }


            }

            if (!inMezzoAllePalle) {
                for (Rectangle tubo : rectObstacles) {
                    if (inMezzoAiCoglioni(tubo, uccello)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
            }

            if (inMezzoAllePalle) {
                inMezzoAllePalle = false;

                for (Rectangle rectangle : rectObstacles) {
                    if (inMezzoAiCoglioni(rectangle, uccello)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
                if (!inMezzoAllePalle)
                    score++;

            }


            //collisione con terra & cielo
            if (uccello.y >= HEIGHT - 120)
                setGameOver(true);
            if (uccello.y <= 0) {
                setGameOver(true);
                uccello.y = 0;
            }

            //collisione col cielo
            if (uccello.y + dy >= HEIGHT - 120) {
                setGameOver(true);
                uccello.y = HEIGHT - 120 - uccello.height;
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


    public boolean inMezzoAiCoglioni(Rectangle tubo, Rectangle player) {
        int coda = player.x, testa = player.x + player.width;
        int startTubo = tubo.x, endTubo = tubo.x + tubo.width;

        if (testa > startTubo && coda < endTubo) {
            return true;
        } else {
            return false;
        }
    }

    public void init() {
        grabFocus();
        uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
        rectObstacles = new ArrayList<Rectangle>();
        rng = new Random(System.nanoTime());
        dy = -3.5;
        score = 0;
        setRunning(true);
        setPlaying(false);
        setGameOver(false);

        grabFocus();

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
        g.fillRect(uccello.x, uccello.y, uccello.width, uccello.height);

        if(!isPlaying() || isGameOver()) {
            if(uccello.x < 0) {
                uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
            }
        }

        for (Rectangle o : rectObstacles) {
            paintObstacle(g, o);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 60));

        if (!isGameStarted()) {
            g.drawString("Clicca o Premi 'Spazio' per Giocare", 150, HEIGHT / 2 - 50);
        }
        if (isGameOver() && isPlaying()) {
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

    public void jump() {

        //se è gameOver, saltare fa iniziare una nuova partita
        if (isGameOver()) {
            uccello = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 30, 30);
            rectObstacles.clear();
            dy = 0;
            score = 0;
            addNewObstacle(true);
            addNewObstacle(true);
            addNewObstacle(true);
            addNewObstacle(true);
            jumping = true;
            setPlaying(true);
            setGameOver(false);
        }

        if (!isGameStarted()) {
            //jumping = true;
            {
                setGameStarted(true);
                setPlaying(true);
            }
        } else if (!isGameOver()) {
            jumping = true;
            //esegue il salto
            if (jumping) {
                //System.out.println("Sto salendo: " + tileMap.getType(currRow, currCol));

                dy = jumpStart;
                if (uccello.y < 0) {
                    dy = 0;
                    uccello.y = 0;
                }
                //falling = true;
                //GameState.yOffset = dy;
                //if (y + dy <= 0) y = 1;
            }

            // falling

            System.out.println("Sto cadendo 1");

            if (dy > 0) {
                System.out.println("Sto cadendo 3");

                dy += fallSpeed;
                //jumping = false;
                if (uccello.y > HEIGHT) {
                    dy = 0;
                    uccello.y = HEIGHT;
                }
                //GameState.yOffset = dy;
            }

            if (dy < 0 && !jumping) {
                System.out.println("Sto cadendo 4");

                dy += stopJumpSpeed;
                if (uccello.y > HEIGHT) {
                    dy = 0;
                    uccello.y = HEIGHT;
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

    public Rectangle getUccello() {
        return uccello;
    }

    public void setUccello(Rectangle uccello) {
        this.uccello = uccello;
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
            {
                if (isPlaying() && !isGameOver()) {
                    jumping = true;
                    jump();
                }
                if (!isPlaying() && !isGameOver()) {
                    setPlaying(true);
                    jumping = true;
                    jump();
                }
            }

        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE || e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
            int option = JOptionPane.showConfirmDialog(this, "Vuoi uscire dal gioco?", "Uscita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, img);

            if (option == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {

            thread.suspend();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            thread.resume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("rilasciato tasto: " + e.getKeyCode() + e.getKeyChar());
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)) {
            //setJumping(false);
            System.out.println("Valore dy: " + dy);
            System.out.println("Lo prendo");
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

