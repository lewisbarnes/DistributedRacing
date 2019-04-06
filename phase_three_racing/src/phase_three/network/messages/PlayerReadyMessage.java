package phase_three.network.messages;

public class PlayerReadyMessage extends PlayerInfoMessage {


	private static final long serialVersionUID = 1L;
	
	private boolean m_ready;
	
	public PlayerReadyMessage() {
		this.m_messageType = MessageType.PLAYERREADY;
	}
	
	public void setReady() {
		this.m_ready = true;
	}
	
	public boolean isReady() {
		return this.m_ready;
	}

}
