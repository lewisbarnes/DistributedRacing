package phase_three.control;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import phase_three.ResourceLoader;

public class SoundController {
	private static SoundController m_instance;
	private Clip m_backgroundMusic;
	private Clip m_startSound;
	private Clip m_crashSound;
	private Clip m_finishSound;
	public boolean isMusicPlaying() { return m_backgroundMusic.isRunning(); }
	private SoundController() {
		
	}
	
	public static SoundController getInstance() {
		if(m_instance == null) {
			m_instance = new SoundController();
		}
		return m_instance;
	}
	
	public void playBackgroundMusic() {
		if(m_backgroundMusic == null) {
			m_backgroundMusic = ResourceLoader.loadAudio("background_music.wav");
		}
		
		if(!m_backgroundMusic.isRunning()) {
			// Get gain control for audio clip, allows volume adjustment
			FloatControl gain = (FloatControl) m_backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
			gain.setValue(-10.0f);
			// Loop background music continuously
			//m_backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stopBackgroundMusic() {
		if(m_backgroundMusic != null && m_backgroundMusic.isRunning()) {
			m_backgroundMusic.stop();
		}
	}
	
	public void playStartSound() {
		if(m_startSound == null) {
			m_startSound = ResourceLoader.loadAudio("start_tone.wav");
		}
		if(!m_startSound.isRunning()) {
			m_startSound.start();
		}
	}
	
	public void playCrashSound() {
		if(m_crashSound == null) {
			m_crashSound = ResourceLoader.loadAudio("crash_sound.wav");
		}
		if(!m_crashSound.isRunning()) {
			FloatControl gain = (FloatControl) m_crashSound.getControl(FloatControl.Type.MASTER_GAIN);
			m_crashSound.start();
		} 
		
	}
	
	public void playFinishSound() {
		if(m_finishSound == null) {
			m_finishSound = ResourceLoader.loadAudio("finish_sound.wav");
		}
		if(!m_finishSound.isRunning()) {
			FloatControl gain = (FloatControl) m_finishSound.getControl(FloatControl.Type.MASTER_GAIN);
			gain.setValue(-30.0f);
			m_finishSound.start();
		}
	}
}
