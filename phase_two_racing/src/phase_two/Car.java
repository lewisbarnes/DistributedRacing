package phase_two;


import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Car extends Entity {
	
	
	private ImageIcon[] m_imageStates;

	private int m_speed;
	
	private final int m_maxSpeed = 50;
	
	private int m_currentImageState;
	
	@Override
	public ImageIcon getImage() { return m_imageStates[m_currentImageState]; }
	
	public double getAngleDegrees() { return m_currentImageState * 22.5; }
	
	public double getAngleRadians() { return Math.toRadians(getAngleDegrees()); }
	
	public int getSpeed() { return m_speed; }
	
	private boolean m_isDecelerating = false;
	private boolean m_isAccelerating = false;
	

	public Car(Point initialPosition, StartingDirection direction, int skinType) throws IOException {
		super(initialPosition);
		switch (direction) {
		case NORTH:
			m_currentImageState = 0;
			break;
		case WEST:
			m_currentImageState = 12;
			break;
		case EAST:
			m_currentImageState = 4;
			break;
		case SOUTH:
			m_currentImageState = 8;
			break;
		}
		switch(skinType) {
			case 0:
				loadImage("car_green.png");
				break;
			case 1:
				loadImage("car_pink.png");
				break;
		}
		m_scaledSize = 50;
		m_imageStates = new ImageIcon[16];
		m_speed = 0;
		initialiseImages();
		
		
	}
	
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
	
	public void accelerate()
	{
			m_speed += 10;
	}
	public void beginAcceleration() {
		m_isDecelerating = false;
		m_isAccelerating = true;
	}
	public void beginDeceleration() {
		m_isAccelerating = false;
		m_isDecelerating = true;
	}
	
	public void decelerate()
	{
		if(m_speed <= 0)
		{
			m_isDecelerating =  false;
			return;
		}
		if(m_speed < m_maxSpeed) {
			m_speed -= 5;
		}
	}
	

	public void stop() {
		m_speed = 0;
		m_isDecelerating = false;
		m_isAccelerating = false;
	}
	
	public void update() {
		if(m_isDecelerating) {
			decelerate();
		}
		else if(m_isAccelerating)
		{
			accelerate();
		}
		updateLocation();
		if (m_speed < 0) {
			m_speed = 0;
		}
	}
	
	public void updateLocation() {
		m_pos.x += Math.sin(getAngleRadians()) * m_speed;
		m_pos.y += Math.cos(getAngleRadians()) * (m_speed * -1);
	}
	
	public Ellipse2D getUpdatedBounds() {
		float tempX = (float) (m_pos.x + Math.sin(getAngleRadians()) * m_speed);
		float tempY = (float) (m_pos.y + Math.cos(getAngleRadians()) * (m_speed * -1));
		return new Ellipse2D.Float(tempX + 7, tempY + 7, 35, 35);
	}
	
	public enum StartingDirection {
		NORTH, WEST, EAST, SOUTH
	}

}

