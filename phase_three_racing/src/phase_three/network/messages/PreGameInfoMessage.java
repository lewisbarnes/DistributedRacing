package phase_three.network.messages;

import java.util.ArrayList;

import phase_three.entity.Player;
import phase_three.network.messages.Message;

public class PreGameInfoMessage extends Message {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Player> m_players = new ArrayList<Player>();
	
	public PreGameInfoMessage()  {
		m_messageType = MessageType.PREGAMEINFO;
	}
	
	public void addPlayer(Player player) {
		
		m_players.add(player);
	}
	
	public ArrayList<Player> getPlayers() {
		 
		return m_players;
	}
}
