package phase_three.network.messages;

public class PlayerWinMessage extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int m_playerID;
	public PlayerWinMessage() {
		super();
		m_messageType = MessageType.PLAYERWIN;
	}
	public void setPlayerID(int id) {
		m_playerID = id;
	}
	
	public int getPlayerID() {
		return m_playerID;
	}

}
