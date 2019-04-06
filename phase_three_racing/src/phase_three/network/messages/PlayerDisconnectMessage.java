package phase_three.network.messages;

public class PlayerDisconnectMessage extends Message {
		private static final long serialVersionUID = 1L;
		
		private int m_playerId;
		
		public PlayerDisconnectMessage(int id) {
			m_messageType = MessageType.PLAYERDISCONNECTED;
			m_playerId = id;
		}
		
		public int getPlayerId() {
			return m_playerId;
		}
}
