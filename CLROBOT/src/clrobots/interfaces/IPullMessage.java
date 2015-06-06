package clrobots.interfaces;

import clrobots.impl.MessageRequest;
import clrobots.impl.MessageResponse;

public interface IPullMessage {

	public MessageRequest pullNextRequestMessage();

	public MessageResponse pullNextResponseMessage();
}
