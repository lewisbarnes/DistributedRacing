package phase_three.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import phase_three.control.GameController;
import phase_three.entity.Player;
import phase_three.network.messages.InitialPlayerInfoMessage;
import phase_three.network.messages.Message;
import phase_three.network.messages.MessageType;
import phase_three.network.messages.PlayerConnectedMessage;
import phase_three.network.messages.PlayerDisconnectMessage;
import phase_three.network.messages.PlayerInfoMessage;
import phase_three.network.messages.PlayerLobbyReadyMessage;
import phase_three.network.messages.PlayerReadyMessage;
import phase_three.network.messages.PlayerUpdateMessage;

public class Session implements Runnable {
	private int m_playerID;
	private Socket m_socket;
	private ObjectOutputStream m_outToClient;
	private ObjectInputStream m_inFromClient;
	private Server m_server;
	private Player m_player;
	private boolean m_readyState = false;
	private boolean m_lobbyReadyState = false;

	public Session(int playerID, Socket socket, Server server) {
		
		m_playerID = playerID;
		
		m_socket = socket;
		
		m_server = server;
		
		try {
			
			m_outToClient = new ObjectOutputStream(m_socket.getOutputStream());
			
		} catch (IOException e) {
			
			System.err.println("SESSION: Failed to obtain object output stream for "
					+ m_socket.getRemoteSocketAddress());
		}
		try {
			
			m_inFromClient = new ObjectInputStream(m_socket.getInputStream());
			
		} catch (IOException e) {
			
			System.err.println("SESSION: Failed to obtain object input stream for "
					+ m_socket.getRemoteSocketAddress());
		}
	}

	public Socket getSocket() {
		return m_socket;
	}

	public int getPlayerID() {
		return m_playerID;
	}

	public void sendMessage(Message message) {
		try {
			m_outToClient.writeObject(message);
			m_outToClient.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNewConnectionInfo() {
		PlayerConnectedMessage lm = new PlayerConnectedMessage();
		for(Session s : m_server.getSessions())
		{
			lm.addPlayer(s.getPlayer());
		}
		m_server.newConnection(lm, this);
	}

	public Player getPlayer() {
		return m_player;
	}

	@Override
	public void run() {
		System.out.println("SESSION: NEW SESSION AT " + m_socket.getInetAddress());
		Message response;

		while(!m_socket.isClosed()) {

			try {
				
				String username;
				response =  (Message) m_inFromClient.readObject();
					

				
				if(response.getMessageType() == MessageType.PLAYERINFO)
				{
					PlayerInfoMessage message = (PlayerInfoMessage) response;
					
					// If username not provided by player set default username to "Player" + their id
					if(message.getUsername().length() < 1) {
						
						username = "Player " + m_playerID;
						
						m_player = new Player(username, m_playerID);
						
						System.out.println("SESSION: USERNAME NOT PROVIDED, SETTING TO \"" + username + "\"");
					}
					else {
						username = message.getUsername();
						m_player = new Player(username, m_playerID);

					}
					Thread.currentThread().setName("Session Thread: Player "+ m_playerID);
						InitialPlayerInfoMessage initMessage = new InitialPlayerInfoMessage();
						
						initMessage.setPlayerID(m_playerID);
						
						initMessage.setUsername(username);
						
						m_outToClient.writeObject(initMessage);
						
						m_outToClient.flush();
						
						System.out.println("SESSION: INITIAL INFO MESSAGE SENT TO CLIENT");
						
						sendNewConnectionInfo();
						
					} else if(response.getMessageType() == MessageType.PLAYERUPDATE) {
					
					PlayerUpdateMessage message = (PlayerUpdateMessage) response;
					
					m_player.setLocation(message.getPosition());
					
					m_player.setCurrentLap(message.getCurrentLap());
					
					if(message.getCurrentLap() > GameController.getInstance().getTotalLapCount()) {
						
						m_player.setFinished();
						
						m_server.sendPlayerFinishNotification(this);
					}
					CountDownLatch latch = new CountDownLatch(1);
					if(!m_socket.isClosed()) {
						try {
						m_server.handleGameUpdateMessage(message, this, latch);
						latch.await();
						}
						catch(Exception e) {
							
						}
						
					}
				} else if(response.getMessageType() == MessageType.PLAYERLOBBYREADY) {
					
					PlayerLobbyReadyMessage message = (PlayerLobbyReadyMessage) response;
					
					m_lobbyReadyState = message.isReady();
					m_player.setSkin(message.getSkinIndex());
					CountDownLatch latch = new CountDownLatch(1);
					m_server.handleGameUpdateMessage(message, this, latch);
					latch.await();
					if(m_lobbyReadyState) {
						
						m_server.attemptSendGameLoadSignal();
					}
				} else if(response.getMessageType() == MessageType.PLAYERREADY) {
					
					PlayerReadyMessage message = (PlayerReadyMessage)  response;
					
					m_readyState = message.isReady();
					
					if(m_readyState) {
						
						m_server.attemptGameStart();
					}
				}
			} catch(IOException e) {
				clientDisconnected();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void clientDisconnected() {
		System.out.println("SESSION: PLAYER \"" + m_player.getUsername() + "\" DISCONNECTED");
		try {

			m_inFromClient.close();
			m_outToClient.close();
			
			
			m_server.removeSession(this);
			if(m_server.getSessions().size() > 0) {
				CountDownLatch latch = new CountDownLatch(1);
				m_server.handleGameUpdateMessage(new PlayerDisconnectMessage(m_playerID), this, latch);
				latch.await();
				m_server.sendGameEnd();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getUsername() {
		
		return m_player.getUsername();
	}

	public boolean isReady() {
		
		return m_readyState;
	}
	
	public boolean isLobbyReady() {
		
		return m_lobbyReadyState;
	}
	
	public void stopSocket(CountDownLatch latch) {
		try {
			m_socket.close();
			System.out.println("SESSION: SOCKET CLOSED");
			latch.countDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
