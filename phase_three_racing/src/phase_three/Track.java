package phase_three;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import phase_three.entity.BorderTile;
import phase_three.entity.CheckpointTile;
import phase_three.entity.LineTile;
import phase_three.entity.Tile;
import phase_three.entity.TrackTile;
import phase_three.entity.Car.StartingDirection;

public class Track {
	
	private ArrayList<BorderTile> m_borderTiles = new ArrayList<BorderTile>();
	private ArrayList<TrackTile> m_trackTiles = new ArrayList<TrackTile>();
	private ArrayList<LineTile> m_lineTiles = new ArrayList<LineTile>();
	private ArrayList<CheckpointTile> m_checkpointTiles = new ArrayList<CheckpointTile>();
	
	private Point[] m_startPositions;
	
	private BufferedImage m_image;
	private int m_tileCountX = 0;
	
	private int m_tileCountY = 0;
	
	private StartingDirection m_startDir;
	
	public Track(String fileName) {
		m_image = ResourceLoader.loadTrackImage(fileName);
		
	}
	
	public void addBorderTile(BorderTile tile) {
		this.m_borderTiles.add(tile);
	}
	
	public void addTrackTile(TrackTile tile) {
		this.m_trackTiles.add(tile);
	}
	
	public void addLineTile(LineTile tile) {
		this.m_lineTiles.add(tile);
	}
	
	public void addCheckpointTile(CheckpointTile tile) {
		this.m_checkpointTiles.add(tile);
	}
	
	public ArrayList<BorderTile> getBorderTiles() {
		return this.m_borderTiles;
	}
	
	public ArrayList<TrackTile> getTrackTiles() {
		return this.m_trackTiles;
	}
	
	public ArrayList<LineTile> getLineTiles() {
		return this.m_lineTiles;
	}
	
	public ArrayList<CheckpointTile> getCheckpointTiles() {
		return this.m_checkpointTiles;
	}
	
	public void setTileCounts(int valueX, int valueY) {
		this.m_tileCountX = valueX;
		this.m_tileCountY = valueY;
	}
	
	public int getTileCountX() {
		return this.m_tileCountX;
	}
	
	public int getTileCountY() {
		return this.m_tileCountY;
	}
	
	public int getWidth() {
		return this.m_tileCountX * Tile.TILE_SIZE;
	}
	
	public int getHeight() {
		return this.m_tileCountY * Tile.TILE_SIZE;
	}
	
	public BufferedImage getImage() {
		return m_image;
	}
	
	public void setStartPositions(Point[] positions) {
		m_startPositions = positions;
	}
	
	public Point getStartPosition(int playerID) {
		return m_startPositions[playerID-1];
	}
	
	public void setStartDirection(StartingDirection dir) {
		m_startDir = dir;
	}
	
	public StartingDirection getStartDirection() {
		return m_startDir;
	}
}
