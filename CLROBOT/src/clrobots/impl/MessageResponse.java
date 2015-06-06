package clrobots.impl;

import environnement.Boite;

public class MessageResponse {

	boolean takeBox;
	String idSender;
	String idDest;
	Boite boxtoExchange;
	
	public Boite getBoxtoExchange() {
		return boxtoExchange;
	}

	public void setBoxtoExchange(Boite boxtoExchange) {
		this.boxtoExchange = boxtoExchange;
	}

	public MessageResponse(boolean takeBox, String idSender, String idDest ) {
		this.takeBox = takeBox;
		this.idSender = idSender;
		this.idDest = idDest;
		this.boxtoExchange = null;
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
