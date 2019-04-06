package phase_one;

import javax.swing.JPanel;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

public class Car extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	
	Point m_pos;
	
	private BufferedImage m_image;
	
	private ImageIcon[] m_imageStates;
	
	private final int m_scaledSize = 50;

	public int getXPos() { return m_pos.x; }
	public int getYPos() { return m_pos.y; }
	
	private int m_currentImageState;
	
	public Rectangle getBounds() { return new Rectangle(getXPos(), getYPos(), m_scaledSize, m_scaledSize); }
	
	public ImageIcon getCurrentImage() { return m_imageStates[m_currentImageState]; }
	
	public double getAngleDegrees() { return m_currentImageState * 22.5; }
	
	public double getAngleRadians() { return Math.toRadians(getAngleDegrees()); }
	

	public Car(Point initialPosition) {
		super(null);
		
		m_currentImageState = 0;
		m_imageStates = new ImageIcon[16];
		
		m_image = ResourceLoader.loadImage("car_green.png");
		initialiseImages();
		m_pos = initialPosition;
		
		this.setLocation(m_pos);
		
	}
	
//	private void initialiseImages() {
//		long startTime = System.currentTimeMillis();
//		// Loop through m_imageStates array setting values to sub-images of m_image based on X position;
//		for(int i = 0; i < m_imageStates.length; i++)
//		{
//			try {
//				m_imageStates[i] = new ImageIcon(ResourceLoader.loadImage("car_green_rotation_" + (i + 1) + ".png"));
//			}
//			catch(NullPointerException e) {
//				System.out.println(e.getMessage());
//			}
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println("Total image initialisation time: " + (endTime - startTime) + " ms");
//		
//		
//	}
	
	private void initialiseImages() {
		int tmpX = 0;
		long startTime = System.currentTimeMillis();
		// Loop through m_imageStates array setting values to sub-images of m_image based on X position;
		for(int i = 0; i < m_imageStates.length; i++)
		{
			m_imageStates[i] = new ImageIcon(m_image.getSubimage(tmpX, 0, m_scaledSize, m_scaledSize));
			tmpX += m_scaledSize;
 		}
		long endTime = System.currentTimeMillis();
		System.out.println("Total image initialisation time: " + (endTime - startTime) + " ms");
	}
	
	public void rotateLeft() {
		if(m_currentImageState == 0) {
			m_currentImageState = 15;
		}
		else {
			m_currentImageState -= 1;
		}
	}
	
	public void rotateRight() {
		if(m_currentImageState == 15) {
			m_currentImageState = 0;
		}
		else {
			m_currentImageState += 1;
		}
	}
	
	public void update() {
		rotateRight();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		getCurrentImage().paintIcon(this, g, getXPos(), getYPos());
	}
}
