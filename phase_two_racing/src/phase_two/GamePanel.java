package phase_two;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

import phase_two.Car.StartingDirection;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Car m_playerOne;
	private Car m_playerTwo;
	private ArrayList<Tile> m_tiles = new ArrayList<Tile>();
	private ArrayList<TrackTile> m_trackTiles = new ArrayList<TrackTile>();
	private ArrayList<BorderTile> m_borderTiles = new ArrayList<BorderTile>();
	private Rectangle m_bounds;
	
	public GamePanel(LayoutManager layoutManager, Point initialPosition) throws IOException {
		super(layoutManager);
		int tileCountX = 0;
		int tileCountY = 0;
		BufferedReader reader = ResourceLoader.loadTrack("track");
		String line = reader.readLine();
		int yPos = 0;
		while(line != null)
		{
			tileCountX = 0;
			for(int i = 0; i < line.length(); i++) {
				if(line.charAt(i) == '0' || line.charAt(i) == '6' || line.charAt(i) == '7' || line.charAt(i) == '8' || line.charAt(i) == '9')
				{
					BorderTile tempTile = new BorderTile(new Point(i*50, yPos), Integer.parseInt(Character.toString(line.charAt(i))));
					m_tiles.add(tempTile);
					m_borderTiles.add(tempTile);
					tileCountX++;
				} 
				else if(line.charAt(i) == '1' || line.charAt(i) == '2' || line.charAt(i) == '3' || line.charAt(i) == '4' || line.charAt(i) == '5') {
					TrackTile tempTile = new TrackTile(new Point(i*50, yPos), Integer.parseInt(Character.toString(line.charAt(i))));
					m_tiles.add(tempTile);
					m_trackTiles.add(tempTile);
					tileCountX++;
				}
				
			}
			tileCountY++;
			yPos+= 50;
			line = reader.readLine();
		}
		reader.close();
		Dimension bounds = new Dimension(tileCountX * 50, tileCountY * 50);
		m_bounds = new Rectangle(0, 0, bounds.width, bounds.height);
		m_playerOne = new Car(new Point(100,100), StartingDirection.EAST, 0);
		m_playerTwo = new Car(new Point(200,100),StartingDirection.EAST, 1);
		setPreferredSize(bounds);
		this.setVisible(true);
		this.setLocation(initialPosition);
	}
	
	public Car getPlayerOne() { return m_playerOne; }
	public Car getPlayerTwo() { return m_playerTwo; }
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		
		Optional<BorderTile> hasP1CollidedTile = m_borderTiles.stream().filter((x) -> x.getBounds()
				.intersects(m_playerOne.getUpdatedBounds().getFrame())).findFirst();
		
		Optional<BorderTile> hasP2CollidedTile = m_borderTiles.stream().filter((x) -> x.getBounds()
				.intersects(m_playerTwo.getUpdatedBounds().getFrame())).findFirst();
		
		if(m_playerTwo.getUpdatedBounds().intersects((m_playerOne.getUpdatedBounds().getBounds2D()))) {
			GameController.getInstance().setGameOver();
			m_playerTwo.stop();
			m_playerOne.stop();
			
		}
		
		if(hasP1CollidedTile.isPresent()) {
			m_playerOne.stop();
			m_playerOne.beginDeceleration();
		} else {
			m_playerOne.update();
		}
		if(hasP2CollidedTile.isPresent()) {
			m_playerTwo.stop();
			m_playerTwo.beginDeceleration();
		} else {
			m_playerTwo.update();
		}


		
		int height = g2d.getFontMetrics().getHeight();
		for(Tile t: m_tiles) {
			t.getImage().paintIcon(this, g2d, t.getXPos(), t.getYPos());
		}
		g2d.setColor(Color.black);
		g2d.drawString("Player One Speed: " + m_playerOne.getSpeed(), 10, 20);
		g2d.drawString("Player Two Speed: " + m_playerTwo.getSpeed(), 10, 40);
		g2d.drawString("P1: WASD, P2: UDLR", 10, m_bounds.height -10);

		
		m_playerOne.getImage().paintIcon(this, g2d, m_playerOne.getXPos(), m_playerOne.getYPos());
		m_playerTwo.getImage().paintIcon(this, g2d, m_playerTwo.getXPos(), m_playerTwo.getYPos());
		
		if(GameController.getInstance().isGameOver()) {
			g2d.setFont(new Font("Arial",0,18));
			int stringWidth = g2d.getFontMetrics().stringWidth("Game Over!");
			
			g2d.drawString("Game Over!", this.getWidth()/2 - stringWidth/2 , this.getHeight()/2 - 9);
		}
	}
	
	
}