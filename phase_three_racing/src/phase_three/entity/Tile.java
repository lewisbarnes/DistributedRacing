package phase_three.entity;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import phase_three.ResourceLoader;

/**
 * Base entity used as a surface for cars to drive on
 * 
 * @author LBARNES
 */
public abstract class Tile {


	public static final int TILE_SIZE = 50;
	
	private String m_imageName;
	
	private BufferedImage m_image;
	protected Point m_position;
	public Ellipse2D getBounds() {
		return new Ellipse2D.Double((int)m_position.getX() + 5, (int)m_position.getY() + 5, 40, 40);
	}

	public Tile(Point initialPosition) {
		this.m_position = initialPosition;
	}

	public void loadImage(BufferedImage image) {
		m_image = image;
	}
	
	public BufferedImage getImage() {
		return m_image;
	}
	
	public void loadImageNow() {
	}
}
