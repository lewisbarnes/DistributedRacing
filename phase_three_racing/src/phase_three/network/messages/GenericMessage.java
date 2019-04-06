package phase_three.network.messages;

public class GenericMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	
	public GenericMessage(MessageType type) {
		this.m_messageType = type;
	}

}
