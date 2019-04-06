package phase_three.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import phase_three.ResourceLoader;
import phase_three.control.GameController;

/**
 * Player entity, allows for interaction between user and game
 * 
 * @author LBARNES
 */
public class Player extends JLabel {

	private static final long serialVersionUID = 1L;
	private Car m_playerCar;
	private boolean m_passedCheckpoint = false;
	private int m_currentLap = 1;
	private boolean m_finishedRace;
	private String m_username;
	private int m_playerID;
	private float m_angle;

	/**
	 * Returns this player's car's x coordinate
	 * @return the x coordinate of the player's car
	 * @see Car
	 */
	public float getXPos() {
		return m_playerCar.getXPos();
	}
	
	public float getYPos() {
		return m_playerCar.getYPos();
	}

	/**
	 * Returns this player's car's x coordinate
	 * @return the y coordinate of the player's car
	 * @see Car
	 */

	/**
	 * Sets the player ID to be associated with this player 
	 * @param playerID the integer to set this player's id with
	 */
	public void setPlayerID(int playerID) {
		this.m_playerID = playerID;
	}

	/**
	 * Returns the player ID associated with this player
	 * @return the player ID associated with this player
	 */
	public int getPlayerID() {
		return m_playerID;
	}

	/**
	 * Sets the car associated with this player, all logic is contained in the car
	 * object
	 * @param car the car to set this player's car with
	 */
	public void setPlayerCar(Car car) {
		m_playerCar = car;
		this.setLocation(new Point(m_playerCar.getXPos(), m_playerCar.getYPos()));
		this.setIcon(m_playerCar.getImage());
	}

	/**
	 * Returns the car where all logic is stored for this player
	 * @return the car used for all logic for this player
	 * @see Car
	 */
	public Car getPlayerCar() {
		return m_playerCar;
	}

	/**
	 * Returns the username associated with this player
	 * @return the username associated with this player
	 */
	public String getUsername() {
		return m_username;
	}

	/**
	 * Returns the angle at which this player is facing in radians
	 * @return the angle at which this player is facing in radians
	 */
	public float getAngle() {
		return m_angle;
	}

	/**
	 * Returns the skin index of this player
	 * @return the skin index associated with this player
	 */

	/**
	 * Constructs a new player with a specified username and id
	 * @param username the username to be associated with this player
	 * @param playerID the id to be associated with this player
	 */
	public Player(String username, int playerID) {
		m_username = username;
		setPlayerID(playerID);
		this.setSize(60, 60);
		this.setVisible(true);
	}

	@Override
	public String toString() {
		return m_username;
	}

	/**
	 * Increment the lap counter for this player
	 */
	public void lap() {
		m_passedCheckpoint = false;
		m_currentLap++;
	}

	/**
	 * Sets the boolean associated with whether the player has passed the checkpoint
	 * to true
	 */
	public void passedCheckpoint() {
		m_passedCheckpoint = true;
	}

	/**
	 * Repositions the player on the screen, usually called after performing
	 * collision detection
	 */
	public void updateLocation() {
		m_playerCar.reposition();
		
		m_angle = (float) m_playerCar.getAngleRadians();
		this.setLocation(m_playerCar.getLocation());
		repaint();
	}

	/**
	 * Sets the players location
	 * @param location the point to reposition the player at
	 * @param angle    the angle that the player will face in radians
	 */
	public void setPlayerLocation(Point location, float angle) {
		this.setLocation(location);
		m_angle = angle;
	}

	/**
	 * Sets the player's icon depending on the integer supplied
	 * @param skinIndex the skin index to use for setting the icon
	 */
	public void setSkin(int skinIndex) {
		switch (skinIndex) {
		case 0:
			this.setIcon(new ImageIcon(ResourceLoader.loadImage("kart_1.png")));
			break;
		case 1:
			this.setIcon(new ImageIcon(ResourceLoader.loadImage("kart_2.png")));
			break;
		case 2:
			this.setIcon(new ImageIcon(ResourceLoader.loadImage("kart_3.png")));
			break;
		}
	}

	/**
	 * Signals to the player car that deceleration should now take place
	 * @see Car
	 */
	public void beginDeceleration() {
		m_playerCar.beginDeceleration();
	}

	/**
	 * Returns the player's car's new updated bounds, used in collision detection
	 * @return the new updated bounds of the player's car's next position
	 */
	public Ellipse2D getUpdatedBounds() {
		return m_playerCar.getUpdatedBounds();
	}

	/**
	 * Paints the player's icon to the screen at the required transformation
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform tx = new AffineTransform();
		
		tx.rotate(m_angle, 30, 30);
		
		g2d.transform(tx);
		
		this.getIcon().paintIcon(this, g2d, 5, 5);
	}

	/**
	 * Returns whether the player has passed the checkpoint
	 * 	 * @return the boolean denoting whether the player has passed the checkpoint
	 */
	public boolean hasPassedCheckpoint() {
		return m_passedCheckpoint;
	}

	/**
	 * Returns the player's current lap number
	 * @return the current lap of the player
	 */
	public int getCurrentLap() {
		return m_currentLap;
	}

	/**
	 * Sets the current lap number of the player
	 * @param currentLap the lap number to assign
	 */
	public void setCurrentLap(int currentLap) {
		this.m_currentLap = currentLap;
	}

	/**
	 * Sets the player's finished state to true
	 */
	public void setFinished() {
		m_finishedRace = true;
	}

	/**
	 * Returns the player's finished state
	 * @return the player's finished state
	 */
	public boolean isFinished() {
		return m_finishedRace;
	}
}