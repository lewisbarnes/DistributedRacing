package phase_three.network.messages;

public class PlayerInfoMessage extends Message{

	private static final long serialVersionUID = 1L;
	
	private int m_playerID;
	private int m_skinIndex;
	private String m_username;
	
	public PlayerInfoMessage() {
		this.m_messageType = MessageType.PLAYERINFO;
	}
	
	// Set the player ID of the message
	public void setPlayerID(int id) {
		this.m_playerID = id;
		
	}
	
	// Return the player ID of the message
	public int getPlayerID() {
		return this.m_playerID;
	}
	
	// Set the skinIndex of the message
	public void setSkinIndex(int index) {
		this.m_skinIndex = index;
	}
	
	// Return the skin index of the message
	public int getSkinIndex() {
		return this.m_skinIndex;
	}

	public void setUsername(String username) {
		this.m_username = username;
	}
	
	public String getUsername() {
		return this.m_username;
	}
}
