package clrobots.impl;

import environnement.Boite;
import environnement.Cellule;

public class MessageRequest {

		Boite box;
		String senderId;
		String destId;
		Cellule senderPosition;
		
		public MessageRequest(Boite box, String senderId, String destId, Cellule senderPosition) {
			this.box = box;
			this.senderId = senderId;
			this.destId = destId;
			this.senderPosition = senderPosition;
		}

		public Boite getBox() {
			return box;
		}

		public String getSenderId() {
			return senderId;
		}

		public String getDestId() {
			return destId;
		}

		public Cellule getSenderPosition() {
			return senderPosition;
		}
}
