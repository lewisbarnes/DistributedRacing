package phase_three.network.messages;

import java.util.ArrayList;

import phase_three.entity.Player;

public class PlayerConnectedMessage extends Message {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Player> m_players;
	
	public PlayerConnectedMessage() {
		m_messageType = MessageType.PLAYERCONNECTED;
		m_players = new ArrayList<Player>();
	}
	
	// Add a player to the list of players
	public void addPlayer(Player player) {
		m_players.add(player);
	}
	
	public ArrayList<Player> getPlayers() {
		return m_players;
	}
}
