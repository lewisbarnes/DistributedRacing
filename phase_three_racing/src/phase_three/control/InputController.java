package phase_three.control;

import java.awt.event.KeyEvent;

import phase_three.entity.Car;

public class InputController {

	private static InputController m_instance;
	private Car m_playerCar;
	boolean[] m_inputs;
	private InputController() {
		
	}

	public static InputController getInstance() {
		if (m_instance == null) {
			m_instance = new InputController();
		}
		
		return m_instance;
	}

	public void translateInput(int keyCode, boolean dir) {
		
		if(m_playerCar == null) {
			m_inputs = new boolean[4];
			m_playerCar = GameController.getInstance().getLocalPlayer().getPlayerCar();
		}
		if(GameController.getInstance().getLocalPlayer().isFinished()) {
			m_playerCar.stop();
			return;
		}
		switch (keyCode) {
		case KeyEvent.VK_W:
			m_inputs[0] = dir;
			break;
		case KeyEvent.VK_A:
			m_inputs[1] = dir;
			break;
		case KeyEvent.VK_S:
			m_inputs[2] = dir;
			break;
		case KeyEvent.VK_D:
			m_inputs[3] = dir;
			break;
		default:
			break;
		}

		if (m_inputs[0]) {
			m_playerCar.beginAcceleration();
		} else {
			m_playerCar.beginDeceleration();
		}

		if (m_inputs[1]) {
			m_playerCar.rotateLeft();
		}

		if (m_inputs[2]) {
			m_playerCar.stop();
		}

		if (m_inputs[3]) {
			m_playerCar.rotateRight();
		}
	}
}
