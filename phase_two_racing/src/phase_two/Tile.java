package phase_two;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Tile extends Entity {
	
	

	public Tile(Point initialPosition) {
		super(initialPosition);
		m_scaledSize = 50;
	}

	@Override
	public ImageIcon getImage() {
		return new ImageIcon(m_image);
	}
	
	public void loadImage(String imageName) throws IOException {
		super.loadImage(imageName);
	}
}
