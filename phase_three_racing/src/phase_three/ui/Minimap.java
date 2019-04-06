package phase_three.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import phase_three.ResourceLoader;
import phase_three.control.GameController;
import phase_three.entity.Player;

public class Minimap extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage m_mapImage = GameController.getInstance().getTrack().getImage();
	
	public Minimap() {
		
		this.setPreferredSize(new Dimension((int)(m_mapImage.getWidth()*0.05),(int)(m_mapImage.getHeight()*0.05)));
		
		this.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform tx = new AffineTransform();
		
		tx.scale(0.05, 0.05);
		
		Player player = GameController.getInstance().getLocalPlayer();
		
		Shape playerBlip = tx.createTransformedShape(player.getBounds());
		
		g2d.setFont(new Font("Arial", Font.PLAIN, 10));
		
		g2d.drawImage(m_mapImage, tx, null);
		
		g2d.setColor(Color.RED);
		
		g2d.fill(playerBlip);
		
		g2d.drawString(player.getUsername(), (int)playerBlip.getBounds().getCenterX()+3, (int)playerBlip.getBounds().getCenterY()+3);
		
		for(Player p : GameController.getInstance().getPlayers()) {
			
			playerBlip = tx.createTransformedShape(p.getBounds());
			
			g2d.setColor(Color.BLUE);
			
			g2d.fill(playerBlip);
			
			g2d.drawString(p.getUsername(), (int)playerBlip.getBounds().getCenterX()+3, (int)playerBlip.getBounds().getCenterY()+3);
		}
	}
}
