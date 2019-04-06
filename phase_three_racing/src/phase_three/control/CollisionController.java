package phase_three.control;

import phase_three.Track;
import phase_three.entity.Player;

public class CollisionController {
	private static CollisionController m_instance;
	private Track m_track;

	private CollisionController() {

	}

	public static CollisionController getInstance() {
		if (m_instance == null) {
			m_instance = new CollisionController();
		}
		return m_instance;
	}

	public boolean checkForCollisionWithRemotePlayer(Player player) {
		return GameController.getInstance().getPlayers().stream().filter(
				(x) -> player.getUpdatedBounds().intersects(x.getBounds().getBounds2D()))
				.findFirst().isPresent();

	}

	public boolean checkForCollisionWithTrack(Player player) {
		return m_track.getBorderTiles().stream().filter(
				(x) -> player.getUpdatedBounds().intersects(x.getBounds().getBounds2D()))
				.findFirst().isPresent();
	}

	public boolean checkForCollisionWithCheckpoint(Player player) {
		return m_track.getCheckpointTiles().stream().filter(
				(x) -> player.getUpdatedBounds().intersects(x.getBounds().getBounds2D()))
				.findFirst().isPresent();

	}

	public boolean checkForCollisionWithLine(Player player) {
		return m_track.getLineTiles().stream().filter(
				(x) -> player.getUpdatedBounds().intersects(x.getBounds().getBounds2D()))
				.findFirst().isPresent();

	}

	public void setTrack(Track track) {
		m_track = track;
	}
}
