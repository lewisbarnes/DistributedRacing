package phase_three.network.messages;

public class InitialPlayerInfoMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int m_playerID;
	private String m_username;
	
	public InitialPlayerInfoMessage() {
		
		this.m_messageType = MessageType.INITIALINFO;
	}
	
	public void setPlayerID(int id) {
		this.m_playerID = id;
	}
	
	public int getPlayerID() {
		return this.m_playerID;
	}
	
	public void setUsername(String username) {
		this.m_username = username;
	}
	
	public String getUsername() {
		return this.m_username;
	}

}
