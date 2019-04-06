package phase_three.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import phase_three.control.GameController;
import phase_three.control.SoundController;
import phase_three.control.UIController;
import phase_three.network.messages.InitialPlayerInfoMessage;
import phase_three.network.messages.LobbyTrackChangeMessage;
import phase_three.network.messages.Message;
import phase_three.network.messages.MessageType;
import phase_three.network.messages.PlayerConnectedMessage;
import phase_three.network.messages.PlayerDisconnectMessage;
import phase_three.network.messages.PlayerFinishedMessage;
import phase_three.network.messages.PlayerInfoMessage;
import phase_three.network.messages.PlayerLobbyReadyMessage;
import phase_three.network.messages.PlayerReadyMessage;
import phase_three.network.messages.PlayerUpdateMessage;
import phase_three.network.messages.WinMessage;

public class Client implements Runnable {
	private InetAddress m_hostIP;

	private final int SERVER_PORT = Server.SERVER_PORT;

	private Socket m_clientSocket;

	private ObjectOutputStream m_outToServer;
	private ObjectInputStream m_inFromServer;

	private int m_clientID;

	private boolean m_hasClientID = false;

	public int getPlayerID() {
		return m_clientID;
	}
	
	int errorCount = 0;

	public Client(String hostIP) {
		// Set host IP to that provided by user
		try {
			m_hostIP = InetAddress.getByName(hostIP);
		} catch (UnknownHostException e) {
			System.err.println("CLIENT ERROR: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		
		// Try to open new socket on specified server port
		try {

			m_clientSocket = new Socket(m_hostIP, SERVER_PORT);
			System.out.println("CLIENT: CONNECTED TO " + m_clientSocket.getRemoteSocketAddress().toString());
		} catch (IOException e1) {

			System.err.println("CLIENT: FAILED TO CONNECT TO SERVER AT " + m_hostIP + ":" + SERVER_PORT);
		}
		// Try to get object output stream
		if (!m_clientSocket.isClosed()) {
			try {

				m_outToServer = new ObjectOutputStream(m_clientSocket.getOutputStream());
			} catch (IOException | NullPointerException e) {
				System.err.println("CLIENT: FAILED TO OBTAIN OUTPUT STREAM TO SERVER");
			}

			// Try to get object input stream
			try {
				m_inFromServer = new ObjectInputStream(m_clientSocket.getInputStream());
			} catch (IOException | NullPointerException e) {
				System.err.println("CLIENT: FAILED TO OBTAIN INPUT STREAM TO SERVER");
			}
		}

		while (!m_clientSocket.isClosed()) {
			Message response = null;
				if (!m_hasClientID) {
					PlayerInfoMessage playerInfo = new PlayerInfoMessage();
					playerInfo.setUsername(GameController.getInstance().getLocalUserName());

					try {
						m_outToServer.writeObject(playerInfo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					response = (Message) m_inFromServer.readObject();
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
				

				if (response.getMessageType() == MessageType.INITIALINFO) {
					
					InitialPlayerInfoMessage message = (InitialPlayerInfoMessage) response;
					
					if (!m_hasClientID) {
						
						m_clientID = message.getPlayerID();
						
						GameController.getInstance().setLocalUserName(message.getUsername());
						
						System.out.println("CLIENT: OBTAINED PLAYER ID " + m_clientID);
						
						GameController.getInstance().setClientID(m_clientID);
						
						m_hasClientID = true;
					}
				} else if (response.getMessageType() == MessageType.PLAYERCONNECTED) {
					
					PlayerConnectedMessage message  = (PlayerConnectedMessage) response;
					
					GameController.getInstance().playerConnected(message.getPlayers());
					
				} else if (response.getMessageType() == MessageType.GAMESTART) {
					
					// Start game if GAMESTART received from server
					System.out.println("CLIENT: RECEIVED GAMESTART FROM SERVER");
					
					GameController.getInstance().startGame();
					
				} else if (response.getMessageType() == MessageType.PLAYERUPDATE) {
					
					PlayerUpdateMessage message = (PlayerUpdateMessage) response;
					
					GameController.getInstance().updatePlayerLocation(message.getPlayerID(), message.getPosition(), message.getAngle(), message.getSkinIndex());
						
				}  else if (response.getMessageType() == MessageType.PLAYERINFO) {
					
					PlayerInfoMessage message = (PlayerInfoMessage) response;
					
					GameController.getInstance().initialiseRemotePlayer(message.getPlayerID());
					
				} else if (response.getMessageType() == MessageType.PLAYERFINISHED) {
					PlayerFinishedMessage message = (PlayerFinishedMessage)  response;
					if(m_clientID == message.getPlayerID()) {
						
						GameController.getInstance().setLocalPlayerFinished();

					} else {
						
						GameController.getInstance().setRemotePlayerFinished(message.getPlayerID());
					}
				} else if(response.getMessageType() == MessageType.LOADGAME) {
					
					UIController.getInstance().showGameWindow();
					
				} else if(response.getMessageType() == MessageType.PLAYERWIN) {
					
					WinMessage message = (WinMessage) response;
					
					GameController.getInstance().playerWin(message.getWinner().getPlayerID());
					
				}  else if(response.getMessageType() == MessageType.PLAYERLOBBYREADY) {
					
					PlayerLobbyReadyMessage message = (PlayerLobbyReadyMessage) response;
					
					GameController.getInstance().updatePlayerSkin(message.getPlayerID(), message.getSkinIndex());
				} else if(response.getMessageType() == MessageType.TRACKINFO) {
					System.out.println("CLIENT: TRACK INFO RECEIVED");
					LobbyTrackChangeMessage message = (LobbyTrackChangeMessage) response;
					GameController.getInstance().setTrack(message.getTrackNum());
				} else if(response.getMessageType() == MessageType.PLAYERDISCONNECTED) {
					System.out.println("CLIENT: DISCONNECTION MESSAGE RECEIVED");
					PlayerDisconnectMessage message = (PlayerDisconnectMessage) response;
					GameController.getInstance().playerDisconnected(message.getPlayerId());
				} else if(response.getMessageType() == MessageType.GAMEEND) {
					GameController.getInstance().gameClosed();
				}
			}
		}

	public void sendUpdate(Message message) {
		if(errorCount >= 3) {
			return;
		}
		try {
			m_outToServer.writeObject(message);
			m_outToServer.flush();
			errorCount = 0;
		} catch (IOException e) {
			e.printStackTrace();
			errorCount++;
		}
	}

	public void sendReady() {
		// Create new message with type PLAYERINFO
		PlayerReadyMessage message = new PlayerReadyMessage();

		// Set variables of message
		message.setPlayerID(m_clientID);
		message.setReady();

		// Attempt to send message to server
		try {
			m_outToServer.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLobbyReady(int skinIndex) {
		
		PlayerLobbyReadyMessage message = new PlayerLobbyReadyMessage();

		// Set variables of message
		message.setPlayerID(m_clientID);
		message.setSkinIndex(skinIndex);
		message.setReady();

		// Attempt to send message to server
		try {
			m_outToServer.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			m_clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
