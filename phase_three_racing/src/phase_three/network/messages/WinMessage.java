package phase_three.network.messages;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import phase_three.entity.Player;

public class WinMessage extends Message {

	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Player> m_players = new ArrayList<Player>();
	
	public WinMessage() {
		
		this.m_messageType = MessageType.PLAYERWIN;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		
		this.m_players = players;
	}
	
	public Player getWinner() {
		// Winner is always first player in list;
		return this.m_players.get(0);
	}
	
	public Player getNthPlace(int n) {
		// Return the player that appears in place n;
		return this.m_players.get(n-1);
		
	}
	
	public int getSpecificPlayerPlace(int id) throws NoSuchElementException {
		
		Optional<Player> player = this.m_players.stream().filter((x) -> x.getPlayerID() == id).findFirst();
		
		if(player.isPresent()) {
			return this.m_players.indexOf(player.get())+1;
		} else {
			throw new NoSuchElementException("No such player with ID of "+ id+" could be found.");
		}
	}
	
}
