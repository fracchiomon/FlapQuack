package flapquack.game;

//Imports
import flapquack.ui.Frame;


public class FlapQuack {

    private static final long serialVersionUID = 1L;
    private static final String TITLE = "FlapQuack";   //Nome del Gioco
    private static final String FONT = "Helvetica";     //Font con cui visualizzo il nome
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public static String getTITLE() {
        return TITLE;
    }
    public static String getFONT() {
        return FONT;
    }


    public FlapQuack() {
        Frame frame = new Frame(TITLE, FONT);
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}