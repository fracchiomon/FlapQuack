package flapquack.game;
//Imports relativi al progetto

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

//in realtà è la classe Music sotto diverso nome. lol.
public class SoundFX {
    public File musicPath;
    public Clip clip;

    public SoundFX(String MusicLocation) {
        try {
            musicPath = new File(MusicLocation);
            File musicPath = new File(MusicLocation);

            if (musicPath.exists()) {
                AudioInputStream audioinput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioinput);
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
    }

    public void playMusic() {

        if (musicPath.exists()) {
            clip.start();
            //clip.loop(Clip.LOOP_CONTINUOUSLY);
            //clip.loop(Clip.LOOP_CONTINUOUSLY);

				/*JOptionPane.showMessageDialog(null, "ferma la canzone con ok");
				long clipTimePosition = clip.getMicrosecondPosition();
				clip.stop();

				JOptionPane.showMessageDialog(null,"premi per riprendere");
				clip.setMicrosecondPosition(clipTimePosition);
				clip.start();

				JOptionPane.showMessageDialog(null,"STOP");*/

        } else {
            System.out.println("Traccia non trovata!!");
        }


    }


    public void stopMusic() {
        if (musicPath.exists()) {
            clip.stop();
        } else {
            System.out.println("Traccia non trovata!!");
        }
    }

}