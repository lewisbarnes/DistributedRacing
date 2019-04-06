package phase_three.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import phase_three.ResourceLoader;

/**
 * 
 * @author LBARNES
 *
 */
public class TrackTile extends Tile {

	private int m_variant;
	private static BufferedImage TRACK_TILE;
	private static BufferedImage TRACK_TILE_1;
	private static BufferedImage TRACK_TILE_2;
	private static BufferedImage TRACK_TILE_3;
	private static BufferedImage TRACK_TILE_4;

	public TrackTile(Point initialPosition, char variant) {
		super(initialPosition);
		m_variant = variant;

	}
	 public void loadImageNow() {

		switch (m_variant) {
		case '0':
			super.loadImage(TRACK_TILE);
			break;
		case '1':
			super.loadImage(TRACK_TILE_1);
			break;
		case '2':
			super.loadImage(TRACK_TILE_2);
			break;
		case '3':
			super.loadImage(TRACK_TILE_3);
			break;
		case '4':
			super.loadImage(TRACK_TILE_4);
			break;
		default:
			super.loadImage(TRACK_TILE);
			break;
		}
	}
	 
	 public static void InitialiseImages() {
		 TRACK_TILE = ResourceLoader.loadImage("track_tile.png");
		 TRACK_TILE_1 = ResourceLoader.loadImage("track_tile_corner_1.png");
		 TRACK_TILE_2 = ResourceLoader.loadImage("track_tile_corner_2.png");
		 TRACK_TILE_3 = ResourceLoader.loadImage("track_tile_corner_3.png");
		 TRACK_TILE_4 = ResourceLoader.loadImage("track_tile_corner_4.png");
	 }

}
