package phase_one;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ResourceLoader {

	static ResourceLoader rl = new ResourceLoader();
	
	public static BufferedImage loadImage(String imageName) {
			BufferedImage image = null;
			try
			{
				InputStream imageStream = rl.getClass().getClassLoader().getResourceAsStream("phase_one/res/"+imageName);
				image = ImageIO.read(imageStream);
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
			return image;
	}
}
