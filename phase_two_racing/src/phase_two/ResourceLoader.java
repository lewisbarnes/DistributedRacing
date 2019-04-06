package phase_two;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ResourceLoader {
	static ResourceLoader rl = new ResourceLoader();
	public static BufferedImage loadImage(String fileName) {
		BufferedImage image = null;
		try {
			InputStream imageStream = rl.getClass().getClassLoader().getResourceAsStream("phase_two/res/"+fileName);
			image = ImageIO.read(imageStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	public static BufferedReader loadTrack(String fileName) {
		return new BufferedReader(
				new InputStreamReader(
						rl.getClass().getClassLoader()
						.getResourceAsStream("phase_two/res/"+fileName + ".txt")));

	}
}
