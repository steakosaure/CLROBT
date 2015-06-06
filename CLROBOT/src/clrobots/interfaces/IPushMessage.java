package clrobots.interfaces;

import clrobots.impl.MessageRequest;
import clrobots.impl.MessageResponse;

public interface IPushMessage {
	public void sendRequestMessage(MessageRequest request);
	public void sendResponseMessage(MessageResponse response);
}
