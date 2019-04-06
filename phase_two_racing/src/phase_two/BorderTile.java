package phase_two;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.ImageIcon;

public class BorderTile extends Tile {
	public BorderTile(Point initialPosition, int type) throws IOException {
		super(initialPosition);
		
		switch(type) {
			case 0:
				super.loadImage("border_tile.png");
				break;
			case 6:
				super.loadImage("border_tile_corner_1.png");
				break;
			case 7:
				super.loadImage("border_tile_corner_2.png");
				break;
			case 8:
				super.loadImage("border_tile_corner_3.png");
				break;
			case 9:
				super.loadImage("border_tile_corner_4.png");
				break;
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getXPos(), getYPos(), m_scaledSize, m_scaledSize);
	}

}
