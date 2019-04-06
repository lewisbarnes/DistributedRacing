package phase_two;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

import javax.swing.ImageIcon;

public class TrackTile extends Tile {

	public TrackTile(Point initialPosition, int type) throws IOException {
		super(initialPosition);
		switch(type) {
			case 1:
				super.loadImage("track_tile.png");
				break;
			case 2:
				super.loadImage("track_tile_corner_1.png");
				break;
			case 3:
				super.loadImage("track_tile_corner_2.png");
				break;
			case 4:
				super.loadImage("track_tile_corner_3.png");
				break;
			case 5:
				super.loadImage("track_tile_corner_4.png");
				break;
		}
	}

}
