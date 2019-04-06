package phase_three.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = -1L;
	
	protected MessageType m_messageType;

	public Message() {
		m_messageType = null;
	}

	public MessageType getMessageType() {
		
		return m_messageType;
	}
}
