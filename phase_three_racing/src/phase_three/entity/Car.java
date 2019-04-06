package phase_three.entity;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;

import phase_three.ResourceLoader;
import phase_three.control.GameController;

/**
 * Entity providing player movement, imagery and interaction with game
 * 
 * @author LBARNES
 *
 */
public class Car extends Entity {

	private ImageIcon m_imageIcon;

	private float m_speed;

	private final int MAX_SPEED = 25;
	private final int ROTATION_STEP = 10;
	private final float ACCELERATION_FACTOR = 1.05f;
	private final int INITIAL_SPEED_STEP = 2;
	private final int DECELERATION_STEP = 3;

	private float m_angleDegrees;

	@Override
	public ImageIcon getImage() {
		return m_imageIcon;
	}

	public double getAngleRadians() {
		return Math.toRadians(m_angleDegrees);
	}
	
	public float getAngleDegrees() {
		return m_angleDegrees;
	}

	public float getSpeed() {
		return m_speed;
	}

	private boolean m_isDecelerating = false;
	private boolean m_isAccelerating = false;

	public Car(Point initialPosition, StartingDirection starting, int skinType) {
		super(initialPosition);
		m_angleDegrees = GameController.getInstance().getDegreesFromStartDirection(starting);
		switch (skinType) {
		case 0:
			m_imageIcon = new ImageIcon(ResourceLoader.loadImage("kart_1.png"));
			break;
		case 1:
			m_imageIcon = new ImageIcon(ResourceLoader.loadImage("kart_2.png"));
			break;
		case 2:
			m_imageIcon = new ImageIcon(ResourceLoader.loadImage("kart_3.png"));
			break;
		}
		m_scaledSize = 50;
		m_speed = 0;

	}

	public void rotateLeft() {
		if (m_angleDegrees <= 0) {
			m_angleDegrees = 360 - ROTATION_STEP;
		} else {
			m_angleDegrees -= ROTATION_STEP;
		}
	}

	public void rotateRight() {
		if (m_angleDegrees == 360) {
			m_angleDegrees = ROTATION_STEP;
		} else {
			m_angleDegrees += ROTATION_STEP;
		}
	}
	
	public void accelerate() {
		
		// If car is stopped set, initial speed
		if (m_speed == 0) {
			m_speed = INITIAL_SPEED_STEP;
			return;
		}
		// If car is not travelling at maximum speed, multiply speed by acceleration factor
		if (m_speed < MAX_SPEED) {
			m_speed *= ACCELERATION_FACTOR;
		}
		
		// If car is travelling faster than max speed, set speed to max speed
		if(m_speed > MAX_SPEED) {
			m_speed = MAX_SPEED;
		}
	}

	
	public void decelerate() {
		// If speed is less than or equal to max speed decrease speed by deceleration step
		if(m_speed <= MAX_SPEED) {
			m_speed -= DECELERATION_STEP;
		}
		// If speed is smaller than or equal to 0, set speed to 0 and stop car decelerating
		if(m_speed <= 0)
		{
			m_speed = 0;
			m_isDecelerating =  false;
			return;
		}

	}
	public void stop() {
		m_isAccelerating = false;
		m_isDecelerating = false;
		m_speed = 0;
	}

	public void beginDeceleration() {
		m_isAccelerating = false;
		m_isDecelerating = true;
	}

	public void beginAcceleration() {
		m_isDecelerating = false;
		m_isAccelerating = true;
	}

	public void collide() {
		m_speed = m_speed * -1;
		
		m_pos.x += Math.sin(getAngleRadians()) * m_speed;
		m_pos.y += Math.cos(getAngleRadians()) * (m_speed * -1);
	}

	public Ellipse2D getUpdatedBounds() {
		
		float tempX = (float) (m_pos.x + Math.sin(getAngleRadians()) * m_speed);
		float tempY = (float) (m_pos.y + Math.cos(getAngleRadians()) * (m_speed * -1));
		
		return new Ellipse2D.Float(tempX+5, tempY+5, 40, 40);
	}

	public void reposition() {

		
		m_pos.x += Math.sin(getAngleRadians()) * m_speed;
		m_pos.y += Math.cos(getAngleRadians()) * (m_speed * -1);
		
		if (m_speed < 0) {
			m_speed = 0;
		}
		
		if (m_isDecelerating) {
			decelerate();
		}
		if (m_isAccelerating) {
			accelerate();
		}
	}

	public enum StartingDirection {
		NORTH, WEST, EAST, SOUTH
	}

}
