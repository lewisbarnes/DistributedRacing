package phase_three.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import phase_three.control.GameController;
import phase_three.control.UIController;

public class StartupWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton m_serverButton;
	private JButton m_clientButton;
	private JTextField m_ipTextBox;
	private JTextField m_usernameTextBox;

	private UIController m_UIController;

	public StartupWindow() {
		m_UIController = UIController.getInstance();
		this.setLocationRelativeTo(null);
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		m_ipTextBox = new JTextField();
		m_usernameTextBox = new JTextField();
		m_ipTextBox.setBackground(Color.WHITE);

		m_serverButton = new JButton("Server");
		m_clientButton = new JButton("Client");
		m_serverButton.setBackground(Color.WHITE);
		m_clientButton.setBackground(Color.WHITE);

		this.setTitle("MP Racing - Startup");
		m_serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameController.getInstance().setLocalUserName(m_usernameTextBox.getText());
				GameController.getInstance().setServer();
				GameController.getInstance().startServer();
				m_UIController.initialiseLobbyWindow();
				GameController.getInstance().startClient("127.0.0.1");
				m_UIController.showLobbyWindow();
			}
		});

		m_clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_UIController.initialiseLobbyWindow();
				GameController.getInstance().setLocalUserName(m_usernameTextBox.getText());
				GameController.getInstance().startClient(m_ipTextBox.getText());
				m_UIController.showLobbyWindow();
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.CENTER;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(new JLabel("Username"), constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(m_usernameTextBox, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.CENTER;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(new JLabel("Server IP"), constraints);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		m_ipTextBox.setSize(150, m_ipTextBox.getHeight());
		panel.add(m_ipTextBox, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 2;
		panel.add(m_serverButton, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		panel.add(m_clientButton, constraints);
		panel.setMinimumSize(new Dimension(500, 45));
		panel.setPreferredSize(panel.getMinimumSize());
		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocation(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2);
		this.setResizable(false);
	}
}
