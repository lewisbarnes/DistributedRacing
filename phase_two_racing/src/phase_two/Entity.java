package phase_two;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class Entity {
	
	protected int m_scaledSize;
	Point m_pos;
	
	protected BufferedImage m_image;

	public int getXPos() { return m_pos.x; }
	public int getYPos() { return m_pos.y; }
	
	protected Rectangle getBounds() { return new Rectangle(getXPos(), getYPos(), m_scaledSize, m_scaledSize); }
	


	public Entity(Point initialPosition) {
			m_pos = initialPosition;
			
	}
	
	public void loadImage(String fileName) throws IOException {
		m_image = ResourceLoader.loadImage(fileName);
	}
	
	public abstract ImageIcon getImage();

}
