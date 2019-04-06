package phase_three.network.messages;

import phase_three.entity.Player;

public class PlayerLobbyReadyMessage extends PlayerReadyMessage {
	
	private static final long serialVersionUID = 1L;
	
	private Player m_player;
	
	public PlayerLobbyReadyMessage() {
		
		this.m_messageType = MessageType.PLAYERLOBBYREADY;
	}
	
	public void setPlayer(Player player) {
		
		m_player = player;
	}
	
	public Player getPlayer() {
		
		return m_player;
	}

}
