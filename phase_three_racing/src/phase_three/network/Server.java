package phase_three.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Timer;

import phase_three.control.GameController;
import phase_three.control.UIController;
import phase_three.entity.Player;
import phase_three.network.messages.GenericMessage;
import phase_three.network.messages.LobbyTrackChangeMessage;
import phase_three.network.messages.Message;
import phase_three.network.messages.MessageType;
import phase_three.network.messages.PlayerFinishedMessage;
import phase_three.network.messages.PlayerInfoMessage;
import phase_three.network.messages.WinMessage;

/**
 * 
 * @author LBARNES
 *
 */
public class Server {
	private final int MAX_CLIENTS_CONNECTED = 4;
	public static final int SERVER_PORT = 62896;

	private ServerSocket m_serverSocket;
	private ArrayList<Session> m_clientSessions;
	private ExecutorService m_clientThreads;
	private Thread m_socketAccepterThread;
	private boolean isAcceptingClients = true;
	
	private ArrayList<Player> m_finishOrder = new ArrayList<Player>();

	public ArrayList<Session> getSessions() {
		return m_clientSessions;
	}

	public Server() {

		// Open a new ServerSocket on designated server port
	}

	private final Runnable SocketAccepter = () -> {
		try {
			m_serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("SERVER: SOCKET STARTED ON " + m_serverSocket.getLocalSocketAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_clientSessions = new ArrayList<Session>();
		
		this.initialiseClientThreadPool();

		// Accept clients until max clients reached or not accepting clients
		while (!m_serverSocket.isClosed()) {
			try {
				if(isAcceptingClients) {
					Socket sock = m_serverSocket.accept();
					if(m_clientSessions.size() < MAX_CLIENTS_CONNECTED) {
						System.out.println("SERVER: NEW CLIENT CONNECTION ON " + sock.getInetAddress() + ":" + sock.getPort());
	
						Session session = new Session(m_clientSessions.size() + 1, sock, this);
						m_clientThreads.submit(session);
						m_clientSessions.add(session);
					} else {
						isAcceptingClients = false;
						sock.close();
					}
				}
			} catch (Exception e) {

			}
			if (allPlayersReady()) {
				attemptGameStart();
			}
		}

	};

	public void initialiseClientThreadPool() {
		m_clientThreads = Executors.newCachedThreadPool();
	}

	public void startServer() {
		m_socketAccepterThread = new Thread(SocketAccepter);
		m_socketAccepterThread.start();
	}

	public void newConnection(Message message, Session source) {
		synchronized(m_clientSessions) {
			for (Session session : m_clientSessions) {
				session.sendMessage(message);
			}
		}

	}
	

	public boolean allPlayersReady() {
		if(isAcceptingClients) {
			for (int i = 0; i < m_clientSessions.size(); i++) {
				if (!m_clientSessions.get(i).isReady()) {
					return false;
				}
			}
			System.out.println("SERVER: ALL PLAYERS READY IN GAME");
			isAcceptingClients = false;
			return true;
		}

		return false;
	}
	
	public boolean allOtherLobbyReady() {
		for (int i = 1; i < m_clientSessions.size(); i++) {
			if (!m_clientSessions.get(i).isLobbyReady()) {
				return false;
			}
		}
		System.out.println("SERVER: ALL OTHER PLAYERS READY IN LOBBY");
		return true;
	}
	
	public boolean allLobbyReady() {
			for (int i = 0; i < m_clientSessions.size(); i++) {
				if (!m_clientSessions.get(i).isLobbyReady()) {
					return false;
				}
			}
			System.out.println("SERVER: ALL PLAYERS READY IN LOBBY");
			return true;

	}

	public void handleGameUpdateMessage(Message message, Session source, CountDownLatch latch) {
		synchronized (m_clientSessions) {
			for (Session s : m_clientSessions) {
				if (source != s) {
					s.sendMessage(message);
				}
			}
		}
		latch.countDown();
	}
	
	public void removeSession(Session s) {
		m_clientSessions.remove(s);
	}
	
	public void sendPlayerFinishNotification(Session source) {
		
		PlayerFinishedMessage message = new PlayerFinishedMessage();
		
		message.setPlayerID(source.getPlayerID());
		if(m_finishOrder.stream().filter((x) -> x.getPlayerID() == source.getPlayerID()).findFirst().isPresent()) {
			return;
		}
		
		m_finishOrder.add(source.getPlayer());
		
		for(Session s : m_clientSessions) {
			s.sendMessage(message);
		}
		
		attemptSendWinNotification();

	}
	
	private boolean allPlayersFinished() {
		synchronized(m_clientSessions) {
			for(Session s : m_clientSessions) {
				if(!s.getPlayer().isFinished())
				{
					return false;
				}
			}
		}
		
		System.out.println("SERVER: ALL PLAYERS FINISHED");
		return true;
	}
	
	public void attemptSendWinNotification() {
		if(!allPlayersFinished()) {
			return;
		}
	
		WinMessage message = new WinMessage();
		message.setPlayers(m_finishOrder);
		
		synchronized(m_clientSessions) {
			for(Session s : m_clientSessions) {
				s.sendMessage(message);
			}
		}
		GenericMessage closeMessage = new GenericMessage(MessageType.GAMEEND);
		Timer t = new Timer(6000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(m_clientSessions) {
					for(Session s : m_clientSessions) {
						s.sendMessage(closeMessage);
					}
				}
			}
		});
		t.setRepeats(false);
		t.start();
	}

	public void sendPlayerInfo(Session source) {
		
		for (Session s1 : m_clientSessions) {
			
			for (Session s2 : m_clientSessions) {
				if (s1.getPlayerID() != s2.getPlayerID()) {
					
					PlayerInfoMessage message = new PlayerInfoMessage();
					
					message.setPlayerID(s2.getPlayerID());
					message.setUsername(s2.getUsername());
					
					s1.sendMessage(message);
				}
			}
		}
	}
	public void attemptSendGameLoadSignal() {
		if(allOtherLobbyReady()) {
			
			UIController.getInstance().showLobbyReadyButton();
		}
		
		if(!allLobbyReady()) {
			
			return;
		}

		sendTrackChangeMessage();
		GenericMessage update = new GenericMessage(MessageType.LOADGAME);
		synchronized(m_clientSessions) {
			
			for(Session s : m_clientSessions) {
				
				s.sendMessage(update);
			}
		}


	}
	
	public void sendTrackChangeMessage() {
		LobbyTrackChangeMessage trackMessage = new LobbyTrackChangeMessage();
		trackMessage.setTrackNum(GameController.getInstance().getTrackIndex());
		synchronized(m_clientSessions) {
			
			for(Session s : m_clientSessions) {
				
				s.sendMessage(trackMessage);
			}
		}
	}

	public void attemptGameStart() {
		
		if (allPlayersReady()) {
			
			System.out.println("SERVER: PREPARING TO SEND GAMESTART");
			
			Timer gameStartTimer = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
						synchronized (m_clientSessions) {
							
							for (Session s : m_clientSessions) {
								
								s.sendMessage(new GenericMessage(MessageType.GAMESTART));
								
								System.out.println("SERVER: GAMESTART SENT TO CLIENTS");
							}
						}
				}
			});
			
			gameStartTimer.setRepeats(false);
			
			gameStartTimer.start();
		}
	}
	
	public void sendGameEnd() {
		synchronized (m_clientSessions) {
			for (Session s : m_clientSessions) {
				s.sendMessage(new GenericMessage(MessageType.GAMEEND));
			}
		}
	}
	
	public void stopServer() {
		CountDownLatch latch = new CountDownLatch(m_clientSessions.size());
		synchronized(m_clientSessions) {
			for(Session s : m_clientSessions) {
				s.stopSocket(latch);
			}
		}
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		m_clientThreads.shutdownNow();
		try {
			m_serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
		}
	}
}
