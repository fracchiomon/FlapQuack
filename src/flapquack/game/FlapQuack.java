package flapquack.game;
import flapquack.ui.GameFrame;

public class FlapQuack {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "FlapQuack";
    public static final String FONT = "Helvetica";
    public FlapQuack() {
        GameFrame frame = new GameFrame();
    }

    public static void main(String[] args) {


        FlapQuack giochino = new FlapQuack();
    }
}
