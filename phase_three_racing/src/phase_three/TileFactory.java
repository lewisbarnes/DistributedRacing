package phase_three;

import java.awt.Point;

import phase_three.entity.BorderTile;
import phase_three.entity.CheckpointTile;
import phase_three.entity.LineTile;
import phase_three.entity.Tile;
import phase_three.entity.TrackTile;

public class TileFactory {
	
	private final int BORDER_TILE = '0';
	private final int TRACK_TILE = '1';
	private final int TRACK_TILE_LD = '2';
	private final int TRACK_TILE_LU = '3';
	private final int TRACK_TILE_RU = '4';
	private final int TRACK_TILE_RD = '5';
	private final int TRACK_TILE_LINE = 'A';
	private final int CHECKPOINT_TILE = 'B';
	
	private final int BORDER_TILE_LD = '6';
	private final int BORDER_TILE_LU = '7';
	private final int BORDER_TILE_RU = '8';
	private final int BORDER_TILE_RD = '9';
	
	
	public TileFactory() {
		
	}
	
	public Tile constructTile(Point p, char c) {
		switch(c) {
		case BORDER_TILE:
			return new BorderTile(p, '0');
		case BORDER_TILE_LD:
			return new BorderTile(p, '1');
		case BORDER_TILE_LU:
			return new BorderTile(p, '2');
		case BORDER_TILE_RU:
			return new BorderTile(p, '3');
		case BORDER_TILE_RD:
			return new BorderTile(p, '4');
		case TRACK_TILE:
			return new TrackTile(p, '0');
		case TRACK_TILE_LD:
			return new TrackTile(p, '1');
		case TRACK_TILE_LU:
			return new TrackTile(p, '2');
		case TRACK_TILE_RU:
			return new TrackTile(p, '3');
		case TRACK_TILE_RD:
			return new TrackTile(p, '4');
		case TRACK_TILE_LINE:
			return new LineTile(p);
		case CHECKPOINT_TILE:
			return new CheckpointTile(p);
		default:
			return null;
		}
	}
}
