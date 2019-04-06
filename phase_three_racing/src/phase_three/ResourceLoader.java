package phase_three;


import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ResourceLoader {
	static ResourceLoader rl = new ResourceLoader();
	public static BufferedImage loadImage(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(rl.getClass().getClassLoader().getResourceAsStream("phase_three/res/"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("RESOURCE LOAD ERROR: " + e.getMessage());
		}
		return image;
	}
	
	public static BufferedImage loadTrackImage(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(rl.getClass().getClassLoader().getResourceAsStream("phase_three/res/tracks/" + fileName + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return image;
	}
	
	public static BufferedReader loadTrack(String fileName) {
			return new BufferedReader(
					new InputStreamReader(
							rl.getClass().getClassLoader()
							.getResourceAsStream("phase_three/res/tracks/"+fileName + ".txt")));

	}
	
	public static BufferedReader loadTrackProps(String fileName) {
		return new BufferedReader(
				new InputStreamReader(
						rl.getClass().getClassLoader()
						.getResourceAsStream("phase_three/res/tracks/"+fileName + ".properties")));
	}
	
	public static Clip loadAudio(String fileName) {
		
		BufferedInputStream is = new BufferedInputStream(rl.getClass().getClassLoader().getResourceAsStream("phase_three/res/sound/"+fileName));
		
		Clip clip = null;
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clip;
	}
}
