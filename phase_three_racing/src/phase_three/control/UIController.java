package phase_three.control;

import phase_three.ui.GameWindow;
import phase_three.ui.LobbyWindow;
import phase_three.ui.StartupWindow;

public class UIController {

	private static UIController m_instance;

	private GameWindow m_gameWindow;
	private StartupWindow m_startupWindow;
	private LobbyWindow m_lobbyWindow;

	private UIController() {

	}

	public static UIController getInstance() {
		if (m_instance == null) {
			m_instance = new UIController();

		}
		return m_instance;
	}

	public void showGameWindow(){
		m_lobbyWindow.setVisible(false);
		m_gameWindow = new GameWindow();
		GameController.getInstance().setGameWindow(m_gameWindow);
		m_gameWindow.setVisible(true);
		
		GameController.getInstance().playerReady();
	}

	public void showStartupWindow() {

		m_startupWindow = new StartupWindow();
		m_startupWindow.setVisible(true);
	}

	public void showLobbyWindow() {

		m_startupWindow.setVisible(false);
		m_lobbyWindow.setVisible(true);
	}
	
	public void hideLobbyReadyButton() {
		m_lobbyWindow.hideReadyButton();
	}
	
	public void showLobbyReadyButton() {
		m_lobbyWindow.showReadyButton();
	}

	public void initialiseLobbyWindow() {
		m_lobbyWindow = new LobbyWindow();
	}
}