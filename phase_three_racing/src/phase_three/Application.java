package phase_three;


import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import phase_three.control.UIController;

public class Application {
	
	public static void main(String[] args) {
		
//		TrackConstructor tc = new TrackConstructor();
//		try {
//			ImageIO.write(tc.getNewTrackImage("track_3"), "png", new File("track_3.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		UIController uic = UIController.getInstance();
		uic.showStartupWindow();
	}
}
