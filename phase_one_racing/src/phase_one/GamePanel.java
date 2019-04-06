package phase_one;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Car m_player;
	Timer m_gameTimer;
	int m_fps = 30;
	boolean m_paused;
	public GamePanel(LayoutManager layoutManager, Point initialPosition) throws IOException {
		super(layoutManager);
		m_player = new Car(new Point(600/2 - 25, 300/2 - 25));
		this.setPreferredSize(new Dimension(600,300));
		this.add(m_player);
		this.setVisible(true);
		m_gameTimer = new Timer((int)1000/m_fps, this);
		m_gameTimer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		this.setBackground(Color.GRAY);
		if(!m_paused) {
			m_player.update();
		}
		
		m_player.getCurrentImage().paintIcon(this, g2d, 600/2-25, 300/2-25);
		g2d.setColor(Color.WHITE);
		g2d.drawString("CAR ANGLE: " + m_player.getAngleDegrees(), 20, 290);
		g2d.drawString("FPS: " + m_fps, 20, 275);
		g2d.drawString("+ & - to change FPS", 20, 20);
		g2d.drawString("P to pause", 20, 35);
		
	}
	
	public Car getCar() {
		return m_player;
	}
	
	public void lowerFps() {
		if(m_fps > 1) {
			m_fps--;
		}
		if(m_paused) {
			repaint();
		}
		
		m_gameTimer.setDelay(1000/m_fps);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		
	}

	public void raiseFps() {
		// TODO Auto-generated method stub
		if(m_fps < 60) {
			m_fps++;
		}
		if(m_paused) {
			repaint();
		}
		m_gameTimer.setDelay(1000/m_fps);
	}
	
	public void pauseExecution() {
		if(!m_paused) {
			m_paused = true;
			m_gameTimer.stop();
			return;
		}
		m_gameTimer.start();
		m_paused = false;
		
	}
	
	
}