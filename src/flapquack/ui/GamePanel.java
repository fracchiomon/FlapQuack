package flapquack.ui;

import flapquack.entities.Obstacle;
import flapquack.entities.Player;

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
    private ArrayList<Obstacle> obstacles;
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
        boolean inMezzoAllePalle = false;
        long startTime, elapsedTime, waitTime;
        int movingSpeed = 2;


        while (isRunning()) {
            //ticks++;
            startTime = System.currentTimeMillis();

            /*for (Rectangle newObst : rectObstacles) {
                newObst.x -= movingSpeed;
            }*/

            for(Obstacle tubo : obstacles) {
                tubo.setX((int)(tubo.getX() - movingSpeed));
            }

            //if (ticks % 2 == 0 && dy < 15)

            dy += 0.05;
            uccelloOOP.setDy(uccelloOOP.getDy() + 0.05);

            /*for (int i = 0; i < rectObstacles.size(); i++) {
                Rectangle newObst = rectObstacles.get(i);
                if (newObst.x + newObst.width < 0) {
                    rectObstacles.remove(newObst);

                    if (newObst.y == 0) {
                        addNewObstacle(false);
                    }
                }
            }*/

            for (int i = 0; i < obstacles.size(); i++) {
                Obstacle tubo = obstacles.get(i);
                if (tubo.getX() + tubo.getWidth() < 0) {
                    obstacles.remove(tubo);

                    if (tubo.getY() == 0) {
                        addNewObstacleOOP(false);
                    }
                }
            }

            if (jumping) {
                uccello.y += dy;
            }
            if(uccelloOOP.isJumping())
                uccelloOOP.setY((int)(uccelloOOP.getY() + uccelloOOP.getDy()));


            /*for (Rectangle rectangle : rectObstacles) {
                //player passa attraverso?
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
            }*/

            for (Obstacle tubo : obstacles) {
                //player passa attraverso?
                //collisioni
                if (tubo.intersects(uccelloOOP)) {
                    setGameOver(true);
                    if (uccelloOOP.getX() <= tubo.getX()) {
                        uccelloOOP.setX((int)(tubo.getX() - uccello.width));
                    } else {
                        if (tubo.getY() != 0) {
                            uccelloOOP.setY((int) (tubo.getY() - uccello.height));
                        } else if (uccelloOOP.getY() < tubo.getHeight()) {
                            uccelloOOP.setY((int) tubo.getHeight());
                        }
                    }
                }
            }

            /*if (!inMezzoAllePalle) {
                for (Rectangle tubo : rectObstacles) {
                    if (inMezzoAiCoglioni(tubo, uccello)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
            }*/

            if (!inMezzoAllePalle) {
                for (Obstacle tubo : obstacles) {
                    if (inMezzoAiCoglioniOOP(tubo, uccelloOOP)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
            }

            /*if (inMezzoAllePalle) {
                inMezzoAllePalle = false;

                for (Rectangle rectangle : rectObstacles) {
                    if (inMezzoAiCoglioni(rectangle, uccello)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
                if (!inMezzoAllePalle)
                    score++;

            }*/

            if (inMezzoAllePalle) {
                inMezzoAllePalle = false;

                for (Obstacle tubo : obstacles) {
                    if (inMezzoAiCoglioniOOP(tubo, uccelloOOP)) {
                        inMezzoAllePalle = true;
                        break;
                    }
                }
                if (!inMezzoAllePalle)
                    score++;

            }


            /*//collisione con terra & cielo
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
*/
            //collisioni terra/cielo OOP
            if (uccelloOOP.getY() >= HEIGHT - 120) {
                setGameOver(true);
                uccelloOOP.setAlive(false);
            }
            /*//collisione terra includendo lo scostamento dy
            if (uccello.y + dy >= HEIGHT - 120) {
                setGameOver(true);
                uccello.y = HEIGHT - 120 - uccello.height;
            }*/
            //collisione terra includendo lo scostamento dy - OOP
            if (uccelloOOP.getY() + uccelloOOP.getDy() >= HEIGHT - 120) {
                setGameOver(true);
                uccelloOOP.setAlive(false);
                uccelloOOP.setY((int) (HEIGHT - 120 - uccello.height));
            }

            if (uccelloOOP.getY() <= 0) {
                setGameOver(true);
                uccelloOOP.setAlive(false);
                uccelloOOP.setY(0);
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
    public boolean inMezzoAiCoglioniOOP(Obstacle tubo, Player player) {
        int coda = (int)player.getX(), testa = (int)(player.getX() + player.getWidth());
        int startTubo = (int)tubo.getX(), endTubo = (int)(tubo.getX()+ tubo.getWidth());

        if (testa > startTubo && coda < endTubo) {
            return true;
        } else {
            return false;
        }
    }

    Player uccelloOOP;
    public void init() {
        grabFocus();
        uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
        uccelloOOP = new Player(playerName, uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
        rectObstacles = new ArrayList<Rectangle>();
        obstacles = new ArrayList<Obstacle>();
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

        /*addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);*/
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);


        thread.start();

    }

    private void addNewObstacleOOP(boolean startGame) {
        int spacing = 300;
        int width = 100;
        int height = rng.nextInt(50, 300);

        if (startGame) {
            obstacles.add(new Obstacle(WIDTH + width + obstacles.size() * spacing,
                    HEIGHT - height - 120,
                    width,
                    height));

            obstacles.add(new Obstacle(
                    WIDTH + width + (obstacles.size() - 1) * spacing,
                    0,
                    width,
                    HEIGHT - height - spacing));
        }
        else {
            obstacles.add(new Obstacle((int)obstacles.get(obstacles.size() - 1).getX() + (2*spacing),
                    HEIGHT - height - 120,
                    width,
                    height));
            obstacles.add(new Obstacle((int)obstacles.get(obstacles.size() - 1).getX(),
                    0,
                    width,
                    HEIGHT - height - spacing));
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
        /*g.setColor(Color.RED);
        g.fillRect(uccello.x, uccello.y, uccello.width, uccello.height);*/
        uccelloOOP.draw(g);

        /*if(!isPlaying() || isGameOver()) {
            if(uccello.x < 0) {
                uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
            }
        }*/
        if(!isPlaying() || isGameOver()) {
            if(uccelloOOP.getX() < 0) {
                uccelloOOP = new Player(playerName);
            }
        }

        /*for (Rectangle o : rectObstacles) {
            paintObstacle(g, o);
        }*/

        //disegna i tubi
        for(Obstacle tubo : obstacles) {
            tubo.draw(g);
        }

        //disegna le stringhe
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 60));

        if (!isGameStarted()) {
            g.drawString("Clicca o Premi 'Spazio' per Giocare", 150, HEIGHT / 2 - 50);
        }
        if (isGameOver() && isPlaying()) {
            g.drawString("Game Over :<", 100, HEIGHT / 2);
        }
        if (!isGameOver() && isGameStarted()) {
            g.setFont(new Font("Helvetica", Font.ITALIC, 30));
            g.drawString(playerName, 25, 45);
            g.setFont(new Font("Helvetica", Font.BOLD, 60));
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }


    /*public void paintObstacle(Graphics g, Rectangle obst) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(obst.x, obst.y, obst.width, obst.height);
    }*/

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
            //if (jumping) {
                //System.out.println("Sto salendo: " + tileMap.getType(currRow, currCol));

                dy = jumpStart;
                if (uccello.y < 0) {
                    dy = 0;
                    uccello.y = 0;
                }
                //falling = true;
                //GameState.yOffset = dy;
                //if (y + dy <= 0) y = 1;
            //}

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
                //se è gameOver, saltare fa iniziare una nuova partita
                if (isGameOver()) {
                    uccelloOOP = new Player(playerName);
                    obstacles.clear();
                    uccelloOOP.setDy(0);
                    score = 0;
                    addNewObstacleOOP(true);
                    addNewObstacleOOP(true);
                    addNewObstacleOOP(true);
                    addNewObstacleOOP(true);
                    jumping = true;
                    uccelloOOP.setJumping(true);
                    setPlaying(true);
                    uccelloOOP.setAlive(true);
                    setGameOver(false);
                }

                if (!isGameStarted()) {
                    //jumping = true;
                    {
                        setGameStarted(true);
                        setPlaying(true);
                        uccelloOOP.setAlive(true);
                    }
                }
                if (isPlaying() && !isGameOver()) {
                    jumping = true;
                    uccelloOOP.setJumping(true);
                    uccelloOOP.jump();
                }
                if (!isPlaying() && !isGameOver()) {
                    setPlaying(true);
                    uccelloOOP.setAlive(true);
                    jumping = true;
                    uccelloOOP.setJumping(true);
                    uccelloOOP.jump();
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
        uccelloOOP.jump();
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

