package phase_two;

public class GameController {
	
	private static GameController m_instance;
	
	private boolean m_isGameOver = false;
	
	public static GameController getInstance() {
		if(m_instance == null) {
			m_instance = new GameController();
		}
		
		return m_instance;
	}
	private GameController () {
		
	}
	
	public boolean isGameOver() {
		return m_isGameOver;
	}
	
	public void setGameOver() {
		m_isGameOver = true;
	}
}
