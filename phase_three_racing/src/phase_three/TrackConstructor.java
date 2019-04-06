package phase_three;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import phase_three.entity.BorderTile;
import phase_three.entity.CheckpointTile;
import phase_three.entity.LineTile;
import phase_three.entity.Tile;
import phase_three.entity.TrackTile;
import phase_three.entity.Car.StartingDirection;

public class TrackConstructor {
	private ArrayList<Tile[]> m_tiles = new ArrayList<Tile[]>();
	private BufferedImage m_generatedImage;
	private String m_trackFileName;

	public TrackConstructor() {

	}
	
	public Track getTrack(String fileName) {
		Track tempTrack = new Track(fileName);
		
		BufferedReader reader = ResourceLoader.loadTrack(fileName);
		
		TileFactory tileFactory = new TileFactory();
		
		int tileCountX = 0;
		int tileCountY = 0;
		int checkpointNumber = 1;
		int yPos = 0;
		
		String line = "";
		
		try {
			line = reader.readLine();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		while (line != null) {
			tileCountX = 0;
			// Read each character in the line
			for (int i = 0; i < line.length(); i++) {
				
				// Get tile from TileFactory at current coordinate
				Tile tempTile = tileFactory.constructTile(new Point(tileCountX * Tile.TILE_SIZE, yPos), line.charAt(i));
				
				// Add to border tiles if instance is BorderTile
				if (tempTile instanceof BorderTile) {
					tempTrack.addBorderTile((BorderTile) tempTile);
				} 
				else if(tempTile instanceof CheckpointTile){
					((CheckpointTile) tempTile).setCheckpointNumber(checkpointNumber);
					tempTrack.addCheckpointTile((CheckpointTile)tempTile);
					
					checkpointNumber++;
				}  
				else if(tempTile instanceof LineTile){
					tempTrack.addLineTile((LineTile)tempTile);
				} 
				else {
					tempTrack.addTrackTile((TrackTile)tempTile);
				}
				
				tileCountX++;
			}
			tileCountY++;
			yPos += Tile.TILE_SIZE;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tempTrack.setTileCounts(tileCountX, tileCountY);
		
		Properties startPositionProps = new Properties();
		
		try {
			startPositionProps.load(ResourceLoader.loadTrackProps(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Point[] positions = new Point[4];
		
		positions[0] = new Point(Integer.parseInt(startPositionProps.getProperty("p1x")), Integer.parseInt(startPositionProps.getProperty("p1y")));
		positions[1] = new Point(Integer.parseInt(startPositionProps.getProperty("p2x")), Integer.parseInt(startPositionProps.getProperty("p2y")));
		positions[2] = new Point(Integer.parseInt(startPositionProps.getProperty("p3x")), Integer.parseInt(startPositionProps.getProperty("p3y")));
		positions[3] = new Point(Integer.parseInt(startPositionProps.getProperty("p4x")), Integer.parseInt(startPositionProps.getProperty("p4y")));
		
		tempTrack.setStartPositions(positions);
		
		String startDirStr = startPositionProps.getProperty("startdir");
		
		StartingDirection startDir = null;
		
		switch(startDirStr) {
		case "north":
			startDir = StartingDirection.NORTH;
			break;
		case "east":
			startDir = StartingDirection.EAST;
			break;
		case "south":
			startDir = StartingDirection.SOUTH;
			break;
		case "west":
			startDir = StartingDirection.WEST;
			
		}
		
		tempTrack.setStartDirection(startDir);
		
		return tempTrack;
		
	}

	public BufferedImage getNewTrackImage(String fileName) {
		m_trackFileName = fileName;
		int columnCount = 0;
		int tileCountX = 0;
		int tileCountY = 0;

		TileFactory tf = new TileFactory();
		
		TrackTile.InitialiseImages();
		CheckpointTile.initialiseImages();
		BorderTile.InitialiseImages();
		LineTile.InitialiseImages();

		BufferedReader reader = ResourceLoader.loadTrack(m_trackFileName);

		String line = "";
		try {
			line = reader.readLine();
			columnCount = line.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read each line
		while (line != null) {
			tileCountX = 0;
			Tile[] tempTiles = new Tile[columnCount];
			// Read each character in the line
			for (int i = 0; i < columnCount; i++) {
				// Get tile from TileFactory at current coordinate
				Tile tempTile = tf.constructTile(new Point(tileCountX * Tile.TILE_SIZE, tileCountY * Tile.TILE_SIZE),
				line.charAt(i));
				tempTile.loadImageNow();
				tempTiles[i] = tempTile;
				tileCountX++;
			}
			tileCountY++;
			m_tiles.add(tempTiles);
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int xpos = 0;
		int ypos = 0;

		BufferedImage trackImage = new BufferedImage(tileCountX * Tile.TILE_SIZE, tileCountY * Tile.TILE_SIZE,
				ColorSpace.TYPE_RGB);

		for (Tile[] ta : m_tiles) {
			for (Tile t : ta) {
				BufferedImage tileImage = t.getImage();
				
				for (int y = 0; y < tileImage.getHeight(); y++) {
					for (int x = 0; x < tileImage.getWidth(); x++) {
						trackImage.setRGB(xpos + x, ypos + y, tileImage.getRGB(x, y));
					}
				}
				xpos += Tile.TILE_SIZE;
			}
			xpos = 0;
			ypos += Tile.TILE_SIZE;
		}
		m_generatedImage = trackImage;
		return m_generatedImage;
	}
}