package phase_three.entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

import phase_three.ResourceLoader;

/**
 * 
 * @author LBARNES
 *
 */
public abstract class Entity {

	protected int m_scaledSize;
	
	protected Point m_pos;

	protected BufferedImage m_image;

	public int getXPos() {
		return m_pos.x;
	}

	public int getYPos() {
		return m_pos.y;
	}

	protected Rectangle getBounds() {
		return new Rectangle(getXPos(), getYPos(), m_scaledSize, m_scaledSize);
	}
	
	protected Point getLocation() {
		return m_pos;
	}

	public Entity(Point initialPosition) {
		m_pos = initialPosition;
	}

	public void loadImage(String fileName) throws IOException {
		m_image = ResourceLoader.loadImage(fileName);
	}

	public abstract ImageIcon getImage();

}
