package phase_three.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import phase_three.Track;
import phase_three.control.CollisionController;
import phase_three.control.GameController;
import phase_three.control.SoundController;
import phase_three.entity.Player;
import phase_three.entity.Tile;

/**
 * 
 * @author LBARNES
 *
 */
public class GamePanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	private Player m_player;

	private GameController m_gameController = GameController.getInstance();

	private BufferedImage m_image;

	private Track m_track;
	
	private int m_camXMin = 0;
	
	private int m_camYMin = 0;
	
	private int m_camXMax;
	
	private int m_camYMax;
	
	private int m_viewportSizeX = 850;
	
	private int m_viewportSizeY = 650;
	
	private String m_screenMiddleText = "";

	public GamePanel() {
		
		super(null);
		
		initialiseTrack();
		m_player = GameController.getInstance().getLocalPlayer();
		
		// Add the player to the panel
		add(m_player);
		
		// Add observer to gameController for player list
		m_gameController.addObserver(this);
		
		// Change size of viewport to ensure no empty space around edges
		if (m_viewportSizeX > m_track.getWidth()) {
			
			m_viewportSizeX = m_track.getWidth();
		}

		if (m_viewportSizeY > m_track.getHeight()) {
			
			m_viewportSizeY = m_track.getHeight();
		}

		// Calculate max camera positions based on tiles and viewport size
		m_camXMax = (m_track.getTileCountX() * Tile.TILE_SIZE - m_viewportSizeX);
		
		m_camYMax = (m_track.getTileCountY() * Tile.TILE_SIZE - m_viewportSizeY);
		
		// Set preferred size of panel to viewport size
		setPreferredSize(new Dimension(m_viewportSizeX, m_viewportSizeY));
		
		setMaximumSize(new Dimension(m_viewportSizeX, m_viewportSizeY));
		setVisible(true);
		
	}

	public void initialiseTrack() {
		m_track = GameController.getInstance().getTrack();
		m_image = m_track.getImage();
		CollisionController.getInstance().setTrack(m_track);
	}
	
	public void setScreenMiddleText(String text) {
		m_screenMiddleText = text;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		

		
		AffineTransform affineTransform = new AffineTransform();
		
		g2d.setTransform(affineTransform);

		
		double camX = GameController.getInstance().getLocalPlayer().getXPos() - 
				GameController.getInstance().getLocalPlayer().getBounds().getWidth() 
				- m_viewportSizeX / 2;

		
		double camY = GameController.getInstance().getLocalPlayer().getYPos() - 
				GameController.getInstance().getLocalPlayer().getBounds().getHeight() 
				- m_viewportSizeY / 2;

		/**
		 * Next section of code prevents track leaving game window
		 */

		if (camX < m_camXMin) {
			
			camX = m_camXMin;
		}


		if (camY < m_camYMin) {
			
			camY = m_camYMin;
		}

		if (camX > m_camXMax) {
			
			camX = m_camXMax;
		}

		if (camY > m_camYMax) {
			
			camY = m_camYMax;
		}

		affineTransform.translate(-camX, -camY);
		
		g2d.setColor(Color.WHITE);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, 
				RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		
		g2d.drawImage(m_image, affineTransform, null);
		
		//g2d.transform(affineTransform);
		g2d.translate(-camX, -camY);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial",0,18));
		
		int stringWidth = g2d.getFontMetrics().stringWidth(m_screenMiddleText);
		g2d.drawString(m_screenMiddleText, (int)(m_viewportSizeX/2 - stringWidth/2 + camX),(int)(m_viewportSizeY/2 + camY));
		float playerSpeed = GameController.getInstance().getLocalPlayer().getPlayerCar().getSpeed();
		DecimalFormat speedFormat = new DecimalFormat("0.00");
		g2d.drawString("Speed: "+ speedFormat.format(playerSpeed), (int)(50+camX), (int)(50+camY));
		
		// Fixes bug with rendering player on translated Graphics2D where player would render as top corner if in exact x y position on track,
		// e.g. track coords - 100, 200. would be rendered on panel wherever local player is in relative coords
		for(Player p : GameController.getInstance().getPlayers()) {
			if(p.getBounds().x > GameController.getInstance().getLocalPlayer().getBounds().x + 500 
					|| p.getBounds().x < GameController.getInstance().getLocalPlayer().getBounds().x - 500) {
				p.setVisible(false);
			} else if(p.getBounds().y > GameController.getInstance().getLocalPlayer().getBounds().y + 500 
					|| p.getBounds().y < GameController.getInstance().getLocalPlayer().getBounds().y - 500) {
				p.setVisible(false);
			} else {
				p.setVisible(true);
			}
		}
	}
	

	public void updatePanel() {
		
		repaint();
		
		boolean hasCollidedTrack = false;
		
		boolean hasCollidedPlayer = false;
		
		boolean hasCollidedCheckpoint = false;
		
		boolean hasCollidedLine = false;

		// Check if player has collided with track
		hasCollidedTrack = CollisionController.getInstance()
				.checkForCollisionWithTrack(GameController.getInstance().getLocalPlayer());

		// Check if player has collided with another player
		hasCollidedPlayer = CollisionController.getInstance()
				.checkForCollisionWithRemotePlayer(GameController.getInstance().getLocalPlayer());

		// Check if player has collided with checkpoint
		hasCollidedCheckpoint = CollisionController.getInstance()
				.checkForCollisionWithCheckpoint(GameController.getInstance().getLocalPlayer());

		// Check if player has collided with the line
		hasCollidedLine = CollisionController.getInstance()
				.checkForCollisionWithLine(GameController.getInstance().getLocalPlayer());

		// Do something if player collided with a checkpoint
		if (hasCollidedCheckpoint) {
			
			if (!GameController.getInstance().getLocalPlayer().hasPassedCheckpoint()) {
				
				GameController.getInstance().getLocalPlayer().passedCheckpoint();
			}
		}

		// If player has collided with line, call player lap and play sound
		if (hasCollidedLine) {
			
			if (GameController.getInstance().getLocalPlayer().hasPassedCheckpoint()) {
				
				GameController.getInstance().getLocalPlayer().lap();
			}
		}

		// Stop player if had collision
		if (hasCollidedTrack | hasCollidedPlayer) {
			
			GameController.getInstance().getLocalPlayer().getPlayerCar().stop();
			
			SoundController.getInstance().playCrashSound();
			
		} else {
			
			GameController.getInstance().getLocalPlayer().updateLocation();
		}
	}
	
	public void removePlayer(Player p) {
		this.remove(p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable source, Object obj) {
		
		ArrayList<Player> players = (ArrayList<Player>) obj;
		
		for (Player p : players) {
			
			add(p);
		}
		
		updatePanel();
	}

}