package phase_three.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import phase_three.ResourceLoader;
import phase_three.control.GameController;
import phase_three.control.InputController;
import phase_three.entity.Player;

public class GameWindow extends JFrame implements KeyListener, WindowListener {

	private static final long serialVersionUID = 1L;
	
	private GamePanel m_gamePanel;
	
	private SidePanel m_sidePanel;
	
	private InputController m_inputController;

	public GameWindow() {
		
		this.m_inputController = InputController.getInstance();

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(this);
		
		this.setLocationRelativeTo(null);
		
		this.showGameScreen();
		
		// Centre window to screen
		this.setLocation(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
		
		
		this.setResizable(false);
		
		this.setVisible(true);
		
		this.setIconImage(ResourceLoader.loadImage("icon.png"));
	}

	public void showGameScreen() {

		this.m_gamePanel = new GamePanel();
		
		this.m_sidePanel = new SidePanel();
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.getContentPane().add(this.m_gamePanel, BorderLayout.WEST);
		
		this.getContentPane().add(this.m_sidePanel, BorderLayout.EAST);
		
		this.m_gamePanel.setVisible(true);

		if(GameController.getInstance().isServer()) {
			
			this.setTitle("MP Racing - Server");
			
		} else {
			
			this.setTitle("MP Racing - Client");
			
		}
		
		this.addKeyListener(this);
		
		this.setSize(new Dimension(850,650));
		
		this.pack();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		this.m_inputController.translateInput(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		this.m_inputController.translateInput(e.getKeyCode(), false);
	}

	// keyTyped event is never used, therefore left blank
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public void update() {
		
		this.m_sidePanel.update();
		
		this.m_gamePanel.updatePanel();

	}
	
	public void setPanelScreenMiddleText(String text) {
		m_gamePanel.setScreenMiddleText(text);
	}
	
	public void flashTextOnMiddleOfPanel(String text, int delay) {
		m_gamePanel.setScreenMiddleText(text);
		Timer t = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_gamePanel.setScreenMiddleText("");
			}
		});
		t.setRepeats(false);
		t.start();
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		GameController.getInstance().stopGameTimer();
		if (GameController.getInstance().isServer()) {
			GameController.getInstance().sendServerGameEnd();
			GameController.getInstance().stopServer();
		} else {
			GameController.getInstance().stopClient();
			System.exit(0);
		}
	}

	public void removePlayer(Player player) {
		// TODO Auto-generated method stub
		m_gamePanel.removePlayer(player);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
