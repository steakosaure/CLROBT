package clrobots.impl;

public class MessageResponse {

	boolean takeBox;
	String idSender;
	String idDest;
	
	public MessageResponse(boolean takeBox, String idSender, String idDest ) {
		this.takeBox = takeBox;
		this.idSender = idSender;
		this.idDest = idDest;
	}

	public boolean takeBox() {
		return takeBox;
	}

	public String getIdSender() {
		return idSender;
	}

	public String getIdDest() {
		return idDest;
	}
}
