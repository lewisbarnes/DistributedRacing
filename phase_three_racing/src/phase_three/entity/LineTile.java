package phase_three.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import phase_three.ResourceLoader;

/**
 * 
 * @author LBARNES
 *
 */
public class LineTile extends Tile {

	private static BufferedImage LINE_TILE;
	
	public LineTile(Point initialPosition) {
		super(initialPosition);
		
	}
	
	public void loadImageNow() {
		super.loadImage(LINE_TILE);
	}
	
	public static void InitialiseImages() {
		LINE_TILE = ResourceLoader.loadImage("track_tile_line.png");
	}
}
