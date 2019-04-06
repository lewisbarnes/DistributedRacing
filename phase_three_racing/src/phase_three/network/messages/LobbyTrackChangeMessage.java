package phase_three.network.messages;

public class LobbyTrackChangeMessage extends Message {


	private static final long serialVersionUID = 1L;
	
	private int m_trackNum;
	
	public LobbyTrackChangeMessage() {
		m_messageType = MessageType.TRACKINFO;
		
	}
	
	public void setTrackNum(int trackNum) {
		m_trackNum = trackNum;
	}
	
	public int getTrackNum() {
		return m_trackNum;
	}

}
