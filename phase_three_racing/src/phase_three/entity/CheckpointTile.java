package phase_three.entity;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import phase_three.ResourceLoader;

public class CheckpointTile extends Tile {
	
	private int m_checkpointNumber;
	
	private static BufferedImage CHECKPOINT_TILE;
	
	public CheckpointTile(Point initialPosition) {
		super(initialPosition);
		
	}
	public int getCheckpointNumber() {
		return m_checkpointNumber;
	}
	public void setCheckpointNumber(int checkpointNumber) {
		this.m_checkpointNumber = checkpointNumber;
	}
	
	@Override
	public Ellipse2D getBounds() {
		return new Ellipse2D.Double((int)m_position.getX(), (int)m_position.getY()-100, 50, 250);
	}
	
	public void loadImageNow() {
		super.loadImage(CHECKPOINT_TILE);
	}
	
	public static void initialiseImages() {
		CHECKPOINT_TILE = ResourceLoader.loadImage("checkpoint_tile.png");
	}
}
