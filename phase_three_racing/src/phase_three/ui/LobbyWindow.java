package phase_three.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import phase_three.ResourceLoader;
import phase_three.control.GameController;
import phase_three.control.UIController;
import phase_three.entity.Player;
public class LobbyWindow extends JFrame implements ActionListener, Observer {
	private static final long serialVersionUID = 2400604532787713308L;
	
	private JButton m_skinRightButton;
	private JButton m_skinLeftButton;
	private JButton m_startGameButton;
	private JButton m_lapCountLeftButton;
	private JButton m_lapCountRightButton;
	private JButton m_trackLeftButton;
	private JButton m_trackRightButton;
	private JLabel m_lapCountLabel;
	private JLabel m_skinViewLabel;
	private JLabel m_trackViewLabel;
	
	private JList<Player> m_playerList;
	
	private int m_skinIndex;
	
	
	private DefaultListModel<Player> m_playerListModel;
	private ImageIcon[] m_skinIcons;
	private ImageIcon[] m_trackIcons;
	private int m_trackNum = 0;
	private int m_lapCountValue = 1;
	private boolean m_usernameSet = false;
	private static final int MAX_LAPS = 20;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LobbyWindow() {
		
		GameController.getInstance().addObserver(this);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setTitle("MP Racing - Lobby");
		this.setLocationRelativeTo(null);
		
		ResourceLoader.loadImage("icon.png");
		
		this.setIconImage(ResourceLoader.loadImage("icon.png"));
		
		m_skinIndex = 0;
		
		m_skinIcons = new ImageIcon[3];
		m_trackIcons = new ImageIcon[3];
		
		m_skinIcons[0] = new ImageIcon(ResourceLoader.loadImage("kart_1.png"));
		m_skinIcons[1] = new ImageIcon(ResourceLoader.loadImage("kart_2.png"));
		m_skinIcons[2] = new ImageIcon(ResourceLoader.loadImage("kart_3.png"));
		
		BufferedImage track1Image = ResourceLoader.loadTrackImage("track_1");
		BufferedImage track2Image = ResourceLoader.loadTrackImage("track_2");
		BufferedImage track3Image = ResourceLoader.loadTrackImage("track_3");
		
		BufferedImage tmp = new BufferedImage((int)Math.floor(track1Image.getWidth()*0.1),
				(int)Math.floor(track1Image.getHeight()*0.1), BufferedImage.TYPE_INT_ARGB);
		
		
		
		AffineTransform at = new AffineTransform();
		
		at.scale(0.1, 0.1);
		
		Graphics2D g2d = tmp.createGraphics();
		
		g2d.transform(at);
		g2d.drawImage(track1Image, null, 0, 0);
		track1Image = tmp;
		
		tmp = new BufferedImage((int)Math.floor(track2Image.getWidth()*0.1),
				(int)Math.floor(track2Image.getHeight()*0.1), BufferedImage.TYPE_INT_ARGB);
		
		g2d = tmp.createGraphics();
		
		g2d.transform(at);
		g2d.drawImage(track2Image, null, 0, 0);
		track2Image = tmp;
		
		tmp = new BufferedImage((int)Math.floor(track3Image.getWidth()*0.1),
				(int)Math.floor(track3Image.getHeight()*0.1), BufferedImage.TYPE_INT_ARGB);
		
		g2d = tmp.createGraphics();
		
		g2d.transform(at);
		g2d.drawImage(track3Image, null, 0, 0);
		track3Image = tmp;
		
		m_trackIcons[0] = new ImageIcon(track1Image);
		m_trackIcons[1] = new ImageIcon(track2Image);
		m_trackIcons[2] = new ImageIcon(track3Image);
		
		m_skinViewLabel = new JLabel(m_skinIcons[m_skinIndex]);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 4;
		this.getContentPane().add(new JLabel("Choose a track"), constraints);
		
		constraints.gridx = 0; 
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		m_trackLeftButton = new JButton("<");
		m_trackLeftButton.setBackground(Color.WHITE);
		m_trackLeftButton.addActionListener(this);
		this.getContentPane().add(m_trackLeftButton, constraints);
		
		constraints.gridx = 2; 
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		m_trackRightButton = new JButton(">");
		m_trackRightButton.setBackground(Color.WHITE);
		m_trackRightButton.addActionListener(this);
		this.getContentPane().add(m_trackRightButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		m_trackViewLabel = new JLabel();
		m_trackViewLabel.setIcon(m_trackIcons[m_trackNum]);
		this.getContentPane().add(m_trackViewLabel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 4;
		this.getContentPane().add(new JLabel("Choose your car"), constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		m_skinLeftButton = new JButton("<");
		m_skinLeftButton.setBackground(Color.WHITE);
		m_skinLeftButton.addActionListener(this);
		this.getContentPane().add(m_skinLeftButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 4;
		this.getContentPane().add(m_skinViewLabel, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		m_skinRightButton = new JButton(">");
		m_skinRightButton.setBackground(Color.WHITE);
		m_skinRightButton.addActionListener(this);
		this.getContentPane().add(m_skinRightButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 8;
		this.getContentPane().add(new JLabel("Number of Laps"), constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 9;
		m_lapCountLeftButton = new JButton("-");
		m_lapCountLeftButton.setBackground(Color.WHITE);
		m_lapCountLeftButton.addActionListener(this);
		this.getContentPane().add(m_lapCountLeftButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 9;
		m_lapCountLabel = new JLabel(""+m_lapCountValue);
		m_lapCountLabel.setBackground(Color.WHITE);
		this.getContentPane().add(m_lapCountLabel, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 9;
		m_lapCountRightButton = new JButton("+");
		m_lapCountRightButton.setBackground(Color.WHITE);
		m_lapCountRightButton.addActionListener(this);
		this.getContentPane().add(m_lapCountRightButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 10;
		m_startGameButton = new JButton("Ready");
		m_startGameButton.setBackground(Color.WHITE);
		m_startGameButton.addActionListener(this);
		this.getContentPane().add(m_startGameButton, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridheight = 5;
		constraints.gridwidth = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		m_playerListModel = new DefaultListModel();
		m_playerList = new JList(m_playerListModel);
		
		if(!GameController.getInstance().isServer()) {
			m_lapCountLeftButton.setEnabled(true);
			m_lapCountRightButton.setEnabled(false);
			m_trackRightButton.setEnabled(false);
			m_trackLeftButton.setEnabled(false);
		}
		
		m_playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getContentPane().add(m_playerList, constraints);
		
		m_playerList.setPreferredSize(new Dimension(m_playerList.getWidth(), 80));
		this.pack();
		
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight() + 50));
		this.setResizable(false);
		this.setLocation(this.getX()-this.getWidth()/2, this.getY()-this.getHeight()/2);
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		
		if(source == m_skinLeftButton) {
			if(m_skinIndex == 0) {
				m_skinIndex = 2;
			}
			else {
				m_skinIndex--;
			}
			m_skinViewLabel.setIcon(m_skinIcons[m_skinIndex]);
		}
		if(source == m_skinRightButton) {
			if(m_skinIndex == 2) {
				m_skinIndex = 0;
			}
			else {
				m_skinIndex++;
			}
			m_skinViewLabel.setIcon(m_skinIcons[m_skinIndex]);
		}
		if(source == m_lapCountLeftButton) {
			if(m_lapCountValue > 1) {
				m_lapCountRightButton.setEnabled(true);
				m_lapCountLeftButton.setEnabled(true);
				m_lapCountValue -= 1;
				m_lapCountLabel.setText(""+m_lapCountValue);
			} else {
				m_lapCountLeftButton.setEnabled(false);
			}
		}
		if(source == m_lapCountRightButton) {
			if(m_lapCountValue < MAX_LAPS) {
				m_lapCountRightButton.setEnabled(true);
				m_lapCountLeftButton.setEnabled(true);
				m_lapCountValue += 1;
				m_lapCountLabel.setText(""+m_lapCountValue);
			} else {
				m_lapCountRightButton.setEnabled(false);
			}
		}
		if(source == m_startGameButton) {
			GameController.getInstance().setTrack(m_trackNum);
			GameController.getInstance().initialiseLocalPlayer(m_skinIndex);
			GameController.getInstance().setTotalLapCount(m_lapCountValue);
			GameController.getInstance().lobbyReady(m_skinIndex);
			m_startGameButton.setEnabled(false);
		}
		if(source == m_trackLeftButton) {
			if(m_trackNum > 0) {
				m_trackNum--;
				m_trackViewLabel.setIcon(m_trackIcons[m_trackNum]);
			}
		}
		if(source == m_trackRightButton) {
			if(m_trackNum < m_trackIcons.length -1) {
				m_trackNum++;
				m_trackViewLabel.setIcon(m_trackIcons[m_trackNum]);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object obj) {
		ArrayList<Player> players = (ArrayList<Player>) obj;
		if(!m_usernameSet) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 3;
			this.getContentPane().add(new JLabel("Your username is " + GameController.getInstance().getLocalUserName()),constraints);
			this.pack();
			this.setSize(new Dimension(this.getWidth() + 50, this.getHeight() + 50));
			m_usernameSet = true;
		}
		if(players != null) {
			for(Player p : players) {
				if(!m_playerListModel.contains(p)) {
					m_playerListModel.addElement(p);
				}
			}
		}
	}
	
	public void hideReadyButton() {
		if(GameController.getInstance().isServer()) {
			m_startGameButton.setEnabled(false);
		}
	}
	
	public void showReadyButton() {
		if(GameController.getInstance().isServer()) {
			m_startGameButton.setEnabled(true);
		}
	}
}