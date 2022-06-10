package flapquack.game;

import flapquack.ui.GameFrame;

import java.io.Serial;
import java.io.Serializable;

public class FlapQuack implements Serializable {
    public static final String TITLE = "FlapQuack";
    public static final String FONT = "Helvetica";
    @Serial
    private static final long serialVersionUID = 1L;

    public FlapQuack() {
        GameFrame frame = new GameFrame();
    }

    public static void main(String[] args) {


        FlapQuack giochino = new FlapQuack();
    }
}
