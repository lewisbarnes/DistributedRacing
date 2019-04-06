package phase_two;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

public class GameFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Car m_playerOne;
	private Car m_playerTwo;
	
	private JLabel m_SpeedLabel;
	private GamePanel m_track;
	
	public GameFrame() throws IOException {
		m_track = new GamePanel(null, new Point(0,0));
		m_playerOne = m_track.getPlayerOne();
		m_playerTwo = m_track.getPlayerTwo();
		m_SpeedLabel = new JLabel();
		this.getContentPane().add(m_track);
		this.setTitle("Phase Two");
		this.setResizable(false);
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Timer t = new Timer((int)1000/60, new GameUpdateListener());
		
		this.addKeyListener(new GameKeyListener());

		t.start();
	}
	
	
	public class GameUpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			m_track.repaint();
		}
		
	}
	
	public class GameKeyListener implements KeyListener {
		boolean isWDown = false;
		boolean isADown = false;
		boolean isSDown = false;
		boolean isDDown = false;
		
		boolean isUpDown = false;
		boolean isLeftDown = false;
		boolean isDownDown = false;
		boolean isRightDown = false;
		
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					isWDown = true;
					break;
				case KeyEvent.VK_A:
					isADown = true;
					break;
				case KeyEvent.VK_S:
					isSDown = true;
					break;
				case KeyEvent.VK_D:
					isDDown = true;
					break;
				case KeyEvent.VK_UP:
					isUpDown = true;
					break;
				case KeyEvent.VK_LEFT:
					isLeftDown = true;
					break;
				case KeyEvent.VK_DOWN:
					isDownDown = true;
					break;
				case KeyEvent.VK_RIGHT:
					isRightDown = true;
					break;
				default:
					break;
			}
			
			if(!GameController.getInstance().isGameOver()) {
				translateInput();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					isWDown = false;
					break;
				case KeyEvent.VK_A:
					isADown = false;
					break;
				case KeyEvent.VK_S:
					isSDown = false;
					break;
				case KeyEvent.VK_D:
					isDDown = false;
					break;
				case KeyEvent.VK_UP:
					isUpDown = false;
					break;
				case KeyEvent.VK_LEFT:
					isLeftDown = false;
					break;
				case KeyEvent.VK_DOWN:
					isDownDown = false;
					break;
				case KeyEvent.VK_RIGHT:
					isRightDown = false;
					break;
				default:
					break;
			}
			
			if(!GameController.getInstance().isGameOver()) {
				translateInput();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
//			// TODO Auto-generated method stub
//			switch(e.getKeyCode()) {
//				case KeyEvent.VK_W:
//					isWDown = true;
//					break;
//				case KeyEvent.VK_A:
//					isADown = true;
//					break;
//				case KeyEvent.VK_S:
//					isSDown = true;
//					break;
//				case KeyEvent.VK_D:
//					isDDown = true;
//					break;
//				default:
//					break;
//			}
//			
//			translateInput();
		}
		
		private void translateInput() {
			if(isWDown) {
				m_playerOne.accelerate();
			}
			else {
				m_playerOne.beginDeceleration();
			}
			if(isADown) {
				m_playerOne.rotateLeft();
			}
			
			if(isSDown) {
				m_playerOne.decelerate();
			}
			
			if(isDDown) {
				m_playerOne.rotateRight();
			}
			
			
			if(isUpDown) {
				m_playerTwo.accelerate();
			}
			else {
				m_playerTwo.beginDeceleration();
			}
			
			if(isLeftDown) {
				m_playerTwo.rotateLeft();
			}
			
			if(isDownDown) {
				m_playerTwo.decelerate();
			}
			
			if(isRightDown) {
				m_playerTwo.rotateRight();
			}
		}
		
	}

}
