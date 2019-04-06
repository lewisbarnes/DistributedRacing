package phase_one;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class GameFrame extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;

	
	Timer m_gameTimer;
	GamePanel m_gamePanel;
	
	
	public GameFrame() {
		try {
			m_gamePanel = new GamePanel(null, new Point(0,0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setVisible(true);
		this.getContentPane().setPreferredSize(new Dimension(600,300));
		this.add(m_gamePanel);
		this.pack();
		this.setResizable(false);
		this.addKeyListener(this);
		this.setTitle("phase_one");
	}
	public static GameFrame g;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		g = new GameFrame();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == '-') {
			m_gamePanel.lowerFps();
			
		} else if (e.getKeyChar() == '+') {
				m_gamePanel.raiseFps();
		} else if(e.getKeyChar() == 'p') {
			m_gamePanel.pauseExecution();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
