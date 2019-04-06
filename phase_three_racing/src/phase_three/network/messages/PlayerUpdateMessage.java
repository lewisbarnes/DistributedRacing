package phase_three.network.messages;

import java.awt.Point;

public class PlayerUpdateMessage extends Message {
	private static final long serialVersionUID = 1L;

	private int m_playerID;
	private String m_username;
	private int m_skinIndex;
	private Point m_position;
	private float m_angle;
	private int m_currentLap;

	public PlayerUpdateMessage() {
		this.m_messageType = MessageType.PLAYERUPDATE;
	}

	public void setPlayerID(int id) {
		this.m_playerID = id;
	}

	public int getPlayerID() {
		return this.m_playerID;
	}

	public void setUsername(String m_username) {
		this.m_username = m_username;
	}

	public String getUsername() {
		return this.m_username;
	}

	public void setSkinIndex(int index) {
		this.m_skinIndex = index;
	}

	public int getSkinIndex() {
		return this.m_skinIndex;
	}

	public void setPosition(Point position) {
		this.m_position = position;
	}

	public Point getPosition() {
		return this.m_position;
	}

	public void setAngle(float angle) {
		this.m_angle = angle;
	}

	public float getAngle() {
		return this.m_angle;
	}

	public void setCurrentLap(int lap) {
		this.m_currentLap = lap;
	}

	public int getCurrentLap() {
		return this.m_currentLap;
	}

}
