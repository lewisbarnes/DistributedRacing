package phase_three.control;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import phase_three.Track;
import phase_three.TrackConstructor;
import phase_three.entity.Car;
import phase_three.entity.Player;
import phase_three.entity.Car.StartingDirection;
import phase_three.network.Client;
import phase_three.network.Server;
import phase_three.network.messages.PlayerUpdateMessage;
import phase_three.ui.GameWindow;

public class GameController extends Observable implements ActionListener {

	private Player m_localPlayer;
	private ArrayList<Player> m_remotePlayers = new ArrayList<Player>();
	private GameWindow m_gameWindow;
	private static GameController m_instance;
	private int m_localSkinIndex;
	private int m_localClientID;
	private boolean m_gameInProgress = false;
	private boolean m_isServer = false;
	private Client m_gameClient;
	private Server m_gameServer;
	private Thread m_clientThread;
	private String m_localPlayerUsername;
	private Timer m_gameTimer;
	private Track m_track;
	private int m_trackIndex;
	
	public int getTrackIndex() {
		return m_trackIndex;
	}

	private int m_lapCount = 1;
	
	private final int GAME_TARGET_FPS = 60;

	public ArrayList<Player> getPlayers() {
		return m_remotePlayers;
	}

	public int getPlayerID() {
		return m_gameClient.getPlayerID();
	}

	public Player getLocalPlayer() {
		return m_localPlayer;
	}

	private GameController() {
	}

	public static GameController getInstance() {
		if (m_instance == null) {
			
			m_instance = new GameController();
		}
		return m_instance;
	}
	
	public void setTrack(int num) {
		m_trackIndex = num;
		String trackFileName = "";
		switch(num) {
		case 0:
			trackFileName = "track_1";
			break;
		case 1:
			trackFileName = "track_2";
			break;
		case 2:
			trackFileName = "track_3";
			break;
		}
		TrackConstructor tc = new TrackConstructor();
		tc.getTrack(trackFileName);
		m_track = tc.getTrack(trackFileName);
	}
	
	public Track getTrack() {
		return m_track;
	}

	public void setLocalUserName(String username) {
		m_localPlayerUsername = username;

	}

	public String getLocalUserName() {
		return m_localPlayerUsername;
	}

	public void setGameWindow(GameWindow gameWindow) {

		m_gameWindow = gameWindow;
	}

	public void setServer() {
		m_isServer = true;
	}

	public boolean isServer() {
		return m_isServer;
	}

	public void attemptRemoveThisPlayer() {
		Optional<Player> thisPlayer = m_remotePlayers.stream().filter((x) -> x.getPlayerID() == m_localClientID)
				.findFirst();
		if (thisPlayer.isPresent()) {
			m_remotePlayers.remove(thisPlayer.get());
		}
	}

	public void setLocalPlayerFinished() {
		if (!m_localPlayer.isFinished()) {
			m_localPlayer.setFinished();
			m_gameWindow.flashTextOnMiddleOfPanel("You finished!", 1000);
			SoundController.getInstance().playFinishSound();
		}
	}

	public void startGame() {
		this.setChanged();

		this.notifyObservers(m_remotePlayers);
		m_gameWindow.repaint();
		m_gameInProgress = true;
		SoundController.getInstance().playStartSound();
		
		m_gameTimer = new Timer(1000 / GAME_TARGET_FPS, this);
		m_gameTimer.start();
	}

	public void endGame() {

		m_gameWindow.setVisible(false);
	}

	public void startServer() {

		m_gameServer = new Server();

		m_gameServer.startServer();
	}

	public void startClient(String hostIP) {
		m_gameClient = new Client(hostIP);

		m_clientThread = new Thread(m_gameClient);

		m_clientThread.start();
	}

	public void stopServer() {

		m_gameServer.stopServer();
	}

	public void stopClient() {

		m_gameClient.disconnect();
	}
	
	public void stopGameTimer() {
		m_gameTimer.stop();
	}

	public boolean gameInProgress() {

		return m_gameInProgress;
	}

	public void playerReady() {

		SoundController.getInstance().playBackgroundMusic();
		this.notifyObservers(m_remotePlayers);
		m_gameClient.sendReady();
	}

	public void lobbyReady(int skinIndex) {
		attemptRemoveThisPlayer();
		m_gameClient.sendLobbyReady(skinIndex);
	}

	public void initialiseLocalPlayer(int skinIndex) {

		m_localSkinIndex = skinIndex;
		Car tempCar = new Car(m_track.getStartPosition(m_localClientID), m_track.getStartDirection(), m_localSkinIndex);
		m_localPlayer = new Player(m_localPlayerUsername, m_localClientID);
		m_localPlayer.setPlayerCar(tempCar);
		m_localPlayer.setSkin(m_localSkinIndex);
		m_localPlayer.setPlayerLocation(m_track.getStartPosition(m_localClientID), (float) Math.toRadians(getDegreesFromStartDirection(m_track.getStartDirection())));
	}

	public void playerConnected(ArrayList<Player> players) {

		m_remotePlayers = players;

		// Hide the ready button if more than one player is in the lobby, stops game from being started too early
		if (m_remotePlayers.size() > 1) {

			if (this.isServer()) {

				UIController.getInstance().hideLobbyReadyButton();
			}
		}
		this.setChanged();

		this.notifyObservers(m_remotePlayers);
	}

	public boolean allRemotePlayersInitialised() {

		for (Player p : m_remotePlayers) {

			if (p.getLocation() == new Point(0, 0)) {

				return false;
			}
		}
		return true;
	}

	public int getTotalLapCount() {

		return m_lapCount;
	}

	public void setTotalLapCount(int count) {

		m_lapCount = count;
	}

	public void initialiseRemotePlayer(int playerID) {

		for (Player p : m_remotePlayers) {

			if (p.getPlayerID() == playerID) {
				
				p.setPlayerLocation(m_track.getStartPosition(playerID), (float) Math.toRadians(getDegreesFromStartDirection(m_track.getStartDirection())));
				System.out.println("GAMECONTROLLER: REMOTE PLAYER INITIALISED WITH ID "+ p.getPlayerID());
			}
		}
		
		if (!allRemotePlayersInitialised()) {
			return;
		}

		this.setChanged();

		this.notifyObservers(m_remotePlayers);
	}

	public void setRemotePlayerFinished(int playerID) {

		Optional<Player> player = m_remotePlayers.stream().filter((x) -> x.getPlayerID() == playerID).findFirst();

		if (player.isPresent()) {

			player.get().setFinished();
			m_gameWindow.flashTextOnMiddleOfPanel(player.get().getUsername() + " finished!", 1000);
		}
	}
	
	public void setClientID(int id) {

		m_localClientID = id;
	}

	public void updatePlayerLocation(int id, Point point, float angle, int skin) {

		for (Player p : m_remotePlayers) {

			if (p.getPlayerID() == id) {

				p.setPlayerLocation(point, angle);
				p.setSkin(skin);
			}
		}
	}
	
	public void updatePlayerSkin(int playerID, int skinIndex) {
		Optional<Player> player = m_remotePlayers.stream().filter((x) -> x.getPlayerID() == playerID).findFirst();
		if(player.isPresent()) {
			player.get().setSkin(skinIndex);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (!m_localPlayer.isFinished()) {

			PlayerUpdateMessage message = new PlayerUpdateMessage();

			message.setPosition(m_localPlayer.getLocation());

			message.setAngle(m_localPlayer.getAngle());
			
			message.setPlayerID(m_localClientID);
			
			message.setSkinIndex(m_localSkinIndex);

			message.setCurrentLap(m_localPlayer.getCurrentLap());
			
			m_gameClient.sendUpdate(message);

		}
		m_gameWindow.update();
	}
	
	public int getDegreesFromStartDirection(StartingDirection dir) {
		switch (dir) {
			case NORTH:
				return 0;
			case WEST:
				return 270;
			case EAST:
				return 90;
			case SOUTH:
				return 180;
			default:
				return 0;
		}
	}

	public void playerDisconnected(int playerId) {
		Optional<Player> player = m_remotePlayers.stream().filter((x) -> x.getPlayerID() == playerId).findFirst();
		if(player.isPresent()) {
			m_remotePlayers.remove(player.get());
			m_gameWindow.removePlayer(player.get());
			m_gameWindow.flashTextOnMiddleOfPanel(player.get().getUsername() + " disconnected!", 500);
		}
	}
	
	public void playerWin(int playerId) {
		Optional<Player> player = m_remotePlayers.stream().filter((x) -> x.getPlayerID() == playerId).findFirst();
		if(playerId == m_localClientID) {
			m_gameWindow.flashTextOnMiddleOfPanel("You won!", 5000);
		} else if(player.isPresent()) {
			m_gameWindow.flashTextOnMiddleOfPanel(player.get().getUsername() + " won!", 5000);
		}
	}
	
	public void gameClosed() {
		m_gameWindow.flashTextOnMiddleOfPanel("Game Closed, Exiting in 3s", 3000);
		
		Timer t = new Timer(3000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		t.setRepeats(false);
		t.start();
	}
	
	public void sendServerGameEnd() {
		m_gameServer.sendGameEnd();
	}
}