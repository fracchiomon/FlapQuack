package flapquack.ui;

import flapquack.entities.Obstacle;
import flapquack.entities.Player;
import flapquack.game.Music;
import flapquack.game.SoundFX;
import org.jetbrains.annotations.NotNull;

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

public class GamePanel extends BasePanel implements Runnable, Serializable, MouseListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
    private static final int FPS = 60;
    private static final long targetTime = 1000 / FPS;
    public static double movingSpeed = 1.5; //velocità di scorrimento dei tubazzi
    //Hashtable che vorrebbe contenere i punteggi per inserirli in un file
    protected Hashtable<Integer, String> PlayerScores;
    protected final ArrayList<Integer> scores = new ArrayList<>();
    public final Music music = GameFrame.getMusic();
    private final Thread thread;
    private final SoundFX secretFX = new SoundFX("Assets/Sounds/honk.wav");
    public boolean playing;
    private boolean gameOver, gameStarted;
    protected boolean inMezzoAiTubi = false;

    public Player uccellaccio;
    protected int pauseFrame = 0;
    private String playerName;
    private Random rng;
    public BufferedImage bgBuffered, groundBuffered, grassBuffered;
    private int score;
    private boolean running;
    private ArrayList<Obstacle> obstacles;

    public GamePanel(GameFrame gameFrame) {
        super(gameFrame);
        thread = new Thread(this);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        init();
        setFocusable(true);
        setVisible(true);

    }

    //costruttore con nome in ingresso
    public GamePanel(GameFrame gameFrame, String playerName) {
        super(gameFrame);
        this.playerName = playerName;
        thread = new Thread(this);
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        bgBuffered = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

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


    public void init() {
        grabFocus(); //ottengo il focus per attivare i KeyListener

        rng = new Random(System.nanoTime());
        obstacles = new ArrayList<>();
        PlayerScores = new Hashtable<>();
        String bg_Path = "Assets/Background/bg.png";
        try {
            bgBuffered = ImageIO.read(new File(bg_Path));
            String ground_Path = "Assets/Sprites/brick.png";
            groundBuffered = ImageIO.read(new File(ground_Path));
            String grass_Path = "Assets/Sprites/grass.png";
            grassBuffered = ImageIO.read(new File(grass_Path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setRunning(true);
        setPlaying(false);

        addMouseListener(this);
        addKeyListener(this);

        int loopEnd = 5534000;
        music.clip.setFramePosition(1591000);
        music.clip.start();
        music.clip.setLoopPoints(1590000, loopEnd);
        startGame();
        thread.start();

    }

    //inizializza i primi Ostacoli, il punteggio e il Player
    public void startGame() {
        //dy = -3.5;
        //seed del Random Generator
        rng.setSeed(System.nanoTime());
        movingSpeed = 1.5;
        score = 0;
        obstacles.clear();
        uccellaccio = new Player(playerName);
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
                uccellaccio.setDy(0);
                uccellaccio.setJumping(true);
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
                int uccelloInit_X = WIDTH / 2 - 100;
                int uccelloInit_Y = HEIGHT / 2 - 10;
                int uccelloWidth = 25;
                int uccelloHeight = 25;
                uccellaccio = new Player(playerName, uccelloInit_X, uccelloInit_Y, uccelloWidth, uccelloHeight);
                setPlaying(true);
                uccellaccio.setAlive(true);
            }
        }
        if (isPlaying() && !isGameOver()) {
            //jumping = true;
            uccellaccio.setJumping(true);
            uccellaccio.jump();
        }
        if (!isPlaying() && !isGameOver()) {
            setPlaying(true);
            uccellaccio.setAlive(true);
            //jumping = true;
            uccellaccio.setJumping(true);
            uccellaccio.jump();
        }
    }

    @Override
    public void run() {
        long startTime, elapsedTime, waitTime;

        //loop di gioco
        while (isRunning()) {

            try {
                //ticks++;
                startTime = System.currentTimeMillis();
                System.out.println(uccellaccio.toString());
                System.out.println(music.clip.getFramePosition());

                moveObstacles();
                //dy += 0.05;
                uccellaccio.setDy(uccellaccio.getDy() + 0.05);

                removeObstacles();

                if (uccellaccio.isJumping()) uccellaccio.setY((int) (uccellaccio.getY() + uccellaccio.getDy()));

                //controlla le collisioni oppure incrementa il punteggio.
                checkCollisionsOrRaiseScore();
                //ridisegno lo schermo
                repaint();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //controllo il delta dei tempi per valutare l'attesa in millisecondi del Thread
            elapsedTime = System.nanoTime() - startTime;
            waitTime = targetTime - elapsedTime / 1000000;
            if (waitTime <= 0) {
                waitTime = 5;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(waitTime); //sleep for x milliseconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertPlayerScoreNameInList(String name, int score) {
        PlayerScores.put(score, name);
        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data/HighScores.bin")));
            output.writeObject(PlayerScores);
            output.close();
        } catch (IOException f) {
            f.printStackTrace();
        }
        System.out.println(PlayerScores);
    }

    private void checkCollisionsOrRaiseScore() {
        for (Obstacle tubo : obstacles) {
            //player passa attraverso?
            //collisioni
            System.out.println(tubo.toString());
            System.out.println("Interseco? " + tubo.intersects(uccellaccio.getBounds2D()));
            if (tubo.intersects(uccellaccio)) {
                setGameOver(true);
                movingSpeed = 0;
                //Scrittura();
                if ((int) (uccellaccio.getX()) <= (int) tubo.getX()) {
                    uccellaccio.setX((int) (tubo.getX() - uccellaccio.getWidth() - uccellaccio.getWidth() / 1.2));
                } else {
                    if ((int) tubo.getY() != 0) {
                        uccellaccio.setY((int) (tubo.getY() - uccellaccio.getHeight()));
                    } else if (uccellaccio.getY() < tubo.getHeight()) {
                        uccellaccio.setY((int) tubo.getHeight());
                    }
                }
            }
        }

        if (!inMezzoAiTubi) {
            for (Obstacle tubo : obstacles) {
                if (inMezzoAiTubozziOOP(tubo, uccellaccio)) {
                    inMezzoAiTubi = true;
                    break;
                }
            }
        }

        if (inMezzoAiTubi) {
            inMezzoAiTubi = false;

            for (Obstacle tubo : obstacles) {
                if (inMezzoAiTubozziOOP(tubo, uccellaccio)) {
                    inMezzoAiTubi = true;
                    break;
                }
            }
            if (!inMezzoAiTubi) score++;

        }
        //collisioni terra/cielo OOP
        if (uccellaccio.getY() >= HEIGHT - 120 - uccellaccio.getHeight() - 40) {
            setGameOver(true);
            insertPlayerScoreNameInList(playerName, score);
            //Scrittura();
            uccellaccio.setAlive(false);
            movingSpeed = 0;
            uccellaccio.setY((int) (HEIGHT - 120 - uccellaccio.getHeight() - 40));
        }

        //collisione terra includendo lo scostamento dy - OOP
        if (uccellaccio.getY() + uccellaccio.getDy() >= HEIGHT - 120 - uccellaccio.getHeight() - 40) {
            setGameOver(true);
            insertPlayerScoreNameInList(playerName, score);

            movingSpeed = 0;
            //Scrittura();
            uccellaccio.setAlive(false);
            uccellaccio.setDy(0);
            uccellaccio.setY((int) (HEIGHT - 120 - uccellaccio.getHeight() - 40));
        }

        if (uccellaccio.getY() <= 0) {
            setGameOver(true);
            insertPlayerScoreNameInList(playerName, score);
            movingSpeed = 0;
            uccellaccio.setAlive(false);
            uccellaccio.setY(0);
        }
    }

    private void removeObstacles() {
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle tubo = obstacles.get(i);
            if (tubo.getX() + tubo.getWidth() < 0) {
                obstacles.remove(tubo);

                if (tubo.getY() == 0) {
                    addNewObstacleOOP(false);
                }
            }
        }
    }

    private void moveObstacles() {
        if(uccellaccio.isJumping() && isGameStarted()) {
            for (Obstacle tubo : obstacles) {
                tubo.setX((int) (tubo.getX() - movingSpeed));
            }
        }
    }

    public boolean inMezzoAiTubozziOOP(Obstacle tubo, Player player) {
        int coda = (int) player.getX(), testa = (int) (player.getX() + player.getWidth());
        int startTubo = (int) tubo.getX(), endTubo = (int) (tubo.getX() + tubo.getWidth());

        return testa > startTubo && coda < endTubo;
    }

    private void addNewObstacleOOP(boolean startGame) {
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


    @Override
    public void draw(@NotNull Graphics g) {
        //update dello schermo, pulizia dell'area di render
        g.clearRect(0, 0, WIDTH, HEIGHT);
        try {
            //Render del Background
            g.drawImage(bgBuffered,0,0, WIDTH, HEIGHT,null);

            //Render del 'pavimento' tramite doppio ciclo For innestato
            for (int i = 0; i < WIDTH; i += 30) {
                for (int j = HEIGHT - 120 + 20; j < HEIGHT; j += 30) {
                    g.drawImage(groundBuffered, i, j, 30, 30, null);
                }
            }

            //render dell'erba
            for (int i = 0; i < WIDTH; i += 20) {
                g.drawImage(grassBuffered, i, HEIGHT - 120, 30, 50, null);
            }

            //disegna l'oggetto Player solo se esiste per evitare eccezioni
            if (uccellaccio != null) {
                uccellaccio.draw(g);
            }

            //se non sto giocando e il player dovesse finire da solo fuori schermo lo reinizializzo
            if (!isPlaying() || isGameOver()) {
                if (uccellaccio != null) {
                    if (uccellaccio.getX() < 0) {
                        uccellaccio = new Player(playerName);
                    }
                }
            }

            //disegna i tubi
            for (Obstacle tubo : obstacles) {
                tubo.draw(g);
                if (tubo.intersects(uccellaccio)) {
                    uccellaccio = new Player(playerName);
                }
            }

            //Disegna le varie scritte
            paintStrings(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintStrings(@NotNull Graphics g) {
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
            if (!(playerName == null)) {
                g.drawString(playerName, 25, 45);
            }
            g.setFont(new Font("Helvetica", Font.BOLD, 60));
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
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
                dispose();
                System.exit(0);
            }
        }

        //debug: stop e resume del gioco
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
            System.out.println("Valore dy: " + uccellaccio.getDy());
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
        System.out.println(PlayerScores);
    }


    @Override
    public void dispose() {
        super.dispose();
        try {
            thread.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        groundBuffered = null;
        grassBuffered = null;
        bgBuffered = null;
        removeAll();
        obstacles.clear();
        uccellaccio = null;
    }
}


