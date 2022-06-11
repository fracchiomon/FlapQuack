package flapquack.ui;

import flapquack.entities.Obstacle;
import flapquack.entities.Player;
import flapquack.game.Music;
import flapquack.game.SoundFX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, Serializable, MouseListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
    private static final int FPS = 25;
    private static final long targetTime = 1000 / FPS;
    public static double movingSpeed = 1.5;
    protected final ArrayList<Integer> scores = new ArrayList<Integer>();
    final Music music = GameFrame.getMusic();
    /*public final double maxFallSpeed = 2.3;
    public final double jumpStart = -1.8;*/
    private final Thread thread;
    private final int uccelloInit_X = WIDTH / 2 - 100, uccelloInit_Y = HEIGHT / 2 - 10, uccelloWidth = 25, uccelloHeight = 25;
    private final SoundFX secretFX = new SoundFX("Assets/Sounds/honk.wav");
    //public double fallSpeed = 0.04;
    public boolean playing;
    private boolean gameOver, gameStarted;
    protected Hashtable<Integer, String> PlayerScores;
    Player uccelloOOP;
    int pauseFrame = 0;
    //double stopJumpSpeed = 0.3;
    private String playerName;
    private Random rng;
    private Image bg, ground, grass;
    public BufferedImage bgBuffered, groundBuffered, grassBuffered;

    private final String bg_Path = "Assets/Background/bg.png";
    private final String ground_Path = "Assets/Sprites/brick.png";
    private final String grass_Path = "Assets/Sprites/grass.png";




    //private Player player;
    //private Obstacle obstacle;
    //private ArrayList<Obstacle> obstacleList;
    /*private Rectangle uccello;
    private ArrayList<Rectangle> rectObstacles;*/
    private int score;
    //private double dy;
    private boolean running;
    private ArrayList<Obstacle> obstacles;

    public GamePanel() {
        super();
        //this.playerName = playerName;
        thread = new Thread(this);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        init();
        setFocusable(true);
        setVisible(true);

    }

    //costruttore con nome in ingresso
    public GamePanel(String playerName) {
        super();
        this.playerName = playerName;
        thread = new Thread(this);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        //scores = new ArrayList<Integer>();
        bgBuffered = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        init();
        setFocusable(true);
        setVisible(true);
        //nuovoNome();

    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }


    public void init() {
        grabFocus();

        //uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
        //rectObstacles = new ArrayList<Rectangle>();

        rng = new Random(System.nanoTime());
        obstacles = new ArrayList<Obstacle>();
        //uccelloOOP = new Player("New Player", uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
        PlayerScores = new Hashtable<Integer, String>();
        bg = new ImageIcon(bg_Path).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        try {
            bgBuffered = ImageIO.read(new File(bg_Path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ground = new ImageIcon("Assets/Sprites/brick.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        grass = new ImageIcon("Assets/Sprites/grass.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        setRunning(true);
        setPlaying(false);
        startGame();


        addMouseListener(this);
        addKeyListener(this);

        //pos iniziale del player

        /*addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);
        addNewObstacle(true);*/


        int musStart = 0, musEnd = 382, loopStartSeconds = 35;
        int frameLength = GameFrame.getMusic().clip.getFrameLength();   // length in frames
        long duration = GameFrame.getMusic().clip.getMicrosecondLength();   // length in microseconds
        int durationSeconds = (int) (duration / 1000000);
        int loopEnd = 5534000;
        music.clip.setFramePosition(1591000);
        music.clip.start();
        music.clip.setLoopPoints(1590000, loopEnd);
        thread.start();

    }
    //inizializza i primi Ostacoli, il punteggio
    public void startGame() {
        //dy = -3.5;
        //seed del Random Generator
        rng.setSeed(System.nanoTime());
        movingSpeed = 1.5;
        score = 0;
        obstacles.clear();
        uccelloOOP = new Player(playerName);
        setPlaying(true);
        setGameOver(false);
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);
        addNewObstacleOOP(true);
    }

    //Controlla lo stato del gioco e determina se Eseguire il salto fa partire una nuova partita (se siamo in GameOver)
    private void checkNewGameOrJump() {
        //se è gameOver, saltare fa iniziare una nuova partita
        if (isGameOver()) {
            try {
                int scelta = JOptionPane.showConfirmDialog(this, "Nuovo Giocatore?", "Continua?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (scelta == JOptionPane.YES_OPTION) {
                    nuovoNome();
                }
                startGame();
                //jumping = true;
                uccelloOOP.setDy(0);
                uccelloOOP.setJumping(true);
                //uccelloOOP.setAlive(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (!isGameStarted()) {
            //jumping = true;
            {
                setGameStarted(true);
                nuovoNome();
                if (playerName == null || playerName.isBlank() || playerName.isEmpty()) {
                    playerName = "New Player";
                }
                uccelloOOP = new Player(playerName, uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
                setPlaying(true);
                uccelloOOP.setAlive(true);
            }
        }
        if (isPlaying() && !isGameOver()) {
            //jumping = true;
            uccelloOOP.setJumping(true);
            uccelloOOP.jump();
        }
        if (!isPlaying() && !isGameOver()) {
            setPlaying(true);
            uccelloOOP.setAlive(true);
            //jumping = true;
            uccelloOOP.setJumping(true);
            uccelloOOP.jump();
        }
    }

    @Override
    public void run() {
        boolean inMezzoAiTubi = false;
        long startTime, elapsedTime, waitTime;

        //loop di gioco
        while (isRunning()) {

            try {
                //ticks++;
                startTime = System.currentTimeMillis();
                System.out.println(uccelloOOP.toString());
                System.out.println(music.clip.getFramePosition());

            /*for (Rectangle newObst : rectObstacles) {
                newObst.x -= movingSpeed;
            }*/

                if(uccelloOOP.isJumping() && isGameStarted()) {
                    for (Obstacle tubo : obstacles) {
                        tubo.setX((int) (tubo.getX() - movingSpeed));
                    }
                }

                //if (ticks % 2 == 0 && dy < 15)

                //dy += 0.05;
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

            /*if (jumping) {
                uccello.y += dy;
            }*/
                if (uccelloOOP.isJumping()) uccelloOOP.setY((int) (uccelloOOP.getY() + uccelloOOP.getDy()));


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
                    System.out.println(tubo.toString());
                    System.out.println("Interseco? " + tubo.intersects(uccelloOOP.getBounds2D()));
                    if (tubo.intersects(uccelloOOP)) {
                        setGameOver(true);
                        movingSpeed = 0;
                        //Scrittura();
                        if ((int) (uccelloOOP.getX()) <= (int) tubo.getX()) {
                            uccelloOOP.setX((int) (tubo.getX() - uccelloOOP.getWidth() - uccelloOOP.getWidth() / 1.2));
                        } else {
                            if ((int) tubo.getY() != 0) {
                                uccelloOOP.setY((int) (tubo.getY() - uccelloOOP.getHeight()));
                            } else if (uccelloOOP.getY() < tubo.getHeight()) {
                                uccelloOOP.setY((int) tubo.getHeight());
                            }
                        }
                    }
                }

            /*if (!inMezzoAiTubi) {
                for (Rectangle tubo : rectObstacles) {
                    if (inMezzoAiCoglioni(tubo, uccello)) {
                        inMezzoAiTubi = true;
                        break;
                    }
                }
            }*/

                if (!inMezzoAiTubi) {
                    for (Obstacle tubo : obstacles) {
                        if (inMezzoAiTubozziOOP(tubo, uccelloOOP)) {
                            inMezzoAiTubi = true;
                            break;
                        }
                    }
                }

            /*if (inMezzoAiTubi) {
                inMezzoAiTubi = false;

                for (Rectangle rectangle : rectObstacles) {
                    if (inMezzoAiCoglioni(rectangle, uccello)) {
                        inMezzoAiTubi = true;
                        break;
                    }
                }
                if (!inMezzoAiTubi)
                    score++;

            }*/

                if (inMezzoAiTubi) {
                    inMezzoAiTubi = false;

                    for (Obstacle tubo : obstacles) {
                        if (inMezzoAiTubozziOOP(tubo, uccelloOOP)) {
                            inMezzoAiTubi = true;
                            break;
                        }
                    }
                    if (!inMezzoAiTubi) score++;

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
                if (uccelloOOP.getY() >= HEIGHT - 120 - uccelloOOP.getHeight() - 40) {
                    setGameOver(true);
                    //Scrittura();
                    uccelloOOP.setAlive(false);
                    movingSpeed = 0;
                    uccelloOOP.setY((int) (HEIGHT - 120 - uccelloOOP.getHeight() - 40));
                }
            /*//collisione terra includendo lo scostamento dy
            if (uccello.y + dy >= HEIGHT - 120) {
                setGameOver(true);
                uccello.y = HEIGHT - 120 - uccello.height;
            }*/
                //collisione terra includendo lo scostamento dy - OOP
                if (uccelloOOP.getY() + uccelloOOP.getDy() >= HEIGHT - 120 - uccelloOOP.getHeight() - 40) {
                    setGameOver(true);
                    movingSpeed = 0;
                    //Scrittura();
                    uccelloOOP.setAlive(false);
                    uccelloOOP.setDy(0);
                    uccelloOOP.setY((int) (HEIGHT - 120 - uccelloOOP.getHeight() - 40));
                }

                if (uccelloOOP.getY() <= 0) {
                    setGameOver(true);
                    movingSpeed = 0;
                    uccelloOOP.setAlive(false);
                    uccelloOOP.setY(0);
                }


                //ridisegno lo schermo
                repaint();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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

    /*public boolean inMezzoAiCoglioni(Rectangle tubo, Rectangle player) {
        int coda = player.x, testa = player.x + player.width;
        int startTubo = tubo.x, endTubo = tubo.x + tubo.width;

        return testa > startTubo && coda < endTubo;
    }*/

    public boolean inMezzoAiTubozziOOP(Obstacle tubo, Player player) {
        int coda = (int) player.getX(), testa = (int) (player.getX() + player.getWidth());
        int startTubo = (int) tubo.getX(), endTubo = (int) (tubo.getX() + tubo.getWidth());

        return testa > startTubo && coda < endTubo;
    }

   /* public void addNewObstacle(boolean startGame) {
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
    }*/

    private void addNewObstacleOOP(boolean startGame) {
        int spacing = 300;
        int spacingRand = rng.nextInt(250, 300);
        int width = 100;
        int widthRand = rng.nextInt(95, 110);
        int height = rng.nextInt(50, 300);

        if (startGame) {
            obstacles.add(new Obstacle(WIDTH + widthRand + obstacles.size() * spacingRand, HEIGHT - height - 120, widthRand, height, false));

            obstacles.add(new Obstacle(WIDTH + widthRand + (obstacles.size() - 1) * spacingRand, 0, widthRand, HEIGHT - height - spacingRand, true));
        } else {
            obstacles.add(new Obstacle((int) obstacles.get(obstacles.size() - 1).getX() + (2 * spacingRand), HEIGHT - height - 120, width, height, false));
            obstacles.add(new Obstacle((int) obstacles.get(obstacles.size() - 1).getX(), 0, widthRand, HEIGHT - height - spacingRand, true));
        }
    }
    /*public void paintObstacle(Graphics g, Rectangle obst) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(obst.x, obst.y, obst.width, obst.height);
    }*/
    /*public void jump() {

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


    }*/
    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //Background
        /*g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);*/
        try {
            //g.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(bgBuffered,0,0, WIDTH, HEIGHT,null);
            //ground
            /*g.setColor(Color.ORANGE.darker());
            g.fillRect(0, HEIGHT - 120, WIDTH, 120);
*/
            for (int i = 0; i < WIDTH; i += 30) {
                for (int j = HEIGHT - 120 + 20; j < HEIGHT; j += 30) {
                    g.drawImage(ground, i, j, 30, 30, null);
                }
            }
            //grass
            /*g.setColor(Color.GREEN);
            g.fillRect(0, HEIGHT - 120, WIDTH, 20);*/
            for (int i = 0; i < WIDTH; i += 20) {
                g.drawImage(grass, i, HEIGHT - 120, null);
            }

            if (uccelloOOP != null) {
                uccelloOOP.draw(g);
            }

        /*if(!isPlaying() || isGameOver()) {
            if(uccello.x < 0) {
                uccello = new Rectangle(uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
            }
        }*/
            if (!isPlaying() || isGameOver()) {
                if (uccelloOOP != null) {
                    if (uccelloOOP.getX() < 0) {
                        uccelloOOP = new Player(playerName);
                    }
                }
            }

        /*for (Rectangle o : rectObstacles) {
            paintObstacle(g, o);
        }*/

            //disegna i tubi
            for (Obstacle tubo : obstacles) {
                tubo.draw(g);
                if (tubo.intersects(uccelloOOP)) {
                    uccelloOOP = new Player(playerName);
                }
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
                if (!(playerName.isEmpty() || playerName.isBlank())) {
                    g.drawString(playerName, 25, 45);
                }
                g.setFont(new Font("Helvetica", Font.BOLD, 60));
                g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //bird icon
        /*g.setColor(Color.RED);
        g.fillRect(uccello.x, uccello.y, uccello.width, uccello.height);*/

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

   /* public Rectangle getUccello() {
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
    }*/

    public void setRng(Random rng) {
        this.rng = rng;
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

    //inserisci il nuovo nome per il nuovo giocatore
    public void nuovoNome() {
        //ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
        String newName = JOptionPane.showInputDialog(this, "Inserisci il nome del giocatore", "Nuova Partita", JOptionPane.PLAIN_MESSAGE);
        if (newName == null) playerName = "New Player";
        else playerName = newName;

        //piccolo easter egg
        if (playerName.contains("quack") || playerName.contains("Quack") ||
                playerName.contains("QUACK") || playerName.contains("duck") ||
                playerName.contains("papera"))
        {
            secretFX.playMusic();
        }
        secretFX.clip.setFramePosition(0);



    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("premuto tasto: " + e.getKeyCode() + e.getKeyChar());
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)) {
            {
                checkNewGameOrJump();
            }

        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE || e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            ImageIcon img = new ImageIcon("Assets/Icon/icon64.png");
            int option = JOptionPane.showConfirmDialog(this, "Vuoi uscire dal gioco?", "Uscita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, img);

            if (option == JOptionPane.OK_OPTION) {
                music.clip.stop();
                System.exit(0);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            pauseFrame = music.clip.getFramePosition();
            music.clip.stop();
            //noinspection removal
            thread.suspend();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            music.clip.setFramePosition(pauseFrame);
            music.clip.start();
            //noinspection removal
            thread.resume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("rilasciato tasto: " + e.getKeyCode() + e.getKeyChar());
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)) {
            //setJumping(false);
            System.out.println("Valore dy: " + uccelloOOP.getDy());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        checkNewGameOrJump();

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


    //protected final ArrayList<String> names = new ArrayList<String>();


    public void Scrittura()                    //funzione che aggiunge un giocatore nell'arraylist player quando la partita si interrompe
    {                                        //la ordine e infine la scrive in un file .csv
        scores.add(score);
        Collections.sort(scores);
        PlayerScores.put(score, playerName);


        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data/HighScores.bin")));
            output.writeObject(PlayerScores);
            output.close();
        } catch (IOException f) {
            f.printStackTrace();
        }

        System.out.println(PlayerScores.keys());
    }

    public void HighScores() {

    }
}


