package phase_three.network.messages;

public class PlayerFinishedMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private int m_playerID;
	public PlayerFinishedMessage() {
		
		this.m_messageType = MessageType.PLAYERFINISHED;
	}
	
	public void setPlayerID(int id) {
		this.m_playerID = id;
	}
	
	public int getPlayerID() {
		return this.m_playerID;
	}

}
