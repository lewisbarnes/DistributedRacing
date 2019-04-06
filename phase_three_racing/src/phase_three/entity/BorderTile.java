package phase_three.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import phase_three.ResourceLoader;

/**
 * Tile used as area on track not used to drive on
 * 
 * @author LBARNES
 */
public class BorderTile extends Tile {
	private int m_variant;
	private static BufferedImage BORDER_TILE;
	private static BufferedImage BORDER_TILE_1;
	private static BufferedImage BORDER_TILE_2;
	private static BufferedImage BORDER_TILE_3;
	private static BufferedImage BORDER_TILE_4;
	
	public BorderTile(Point initialPosition, char variant) {
		super(initialPosition);
		m_variant = variant;

	}
	
	public void loadImageNow() {
		switch (m_variant) {
		case '0':
			super.loadImage(BORDER_TILE);
			break;
		case '1':
			super.loadImage(BORDER_TILE_1);
			break;
		case '2':
			super.loadImage(BORDER_TILE_2);
			break;
		case '3':
			super.loadImage(BORDER_TILE_3);
			break;
		case '4':
			super.loadImage(BORDER_TILE_4);
			break;
		}
	}
	
	public static void InitialiseImages() {
		BORDER_TILE = ResourceLoader.loadImage("border_tile.png");
		BORDER_TILE_1 = ResourceLoader.loadImage("border_tile_corner_1.png");
		BORDER_TILE_2 = ResourceLoader.loadImage("border_tile_corner_2.png");
		BORDER_TILE_3 = ResourceLoader.loadImage("border_tile_corner_3.png");
		BORDER_TILE_4 = ResourceLoader.loadImage("border_tile_corner_4.png");
	}

}
