package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import clrobots.Forward;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.IPullMessage;
import clrobots.interfaces.IPushMessage;
import clrobots.interfaces.Iinteragir;
import environnement.Boite;
import environnement.Cellule;
import environnement.interfaces.IEnvInfos;

public class ForwardILauncherRobotmpl extends Forward<CycleAlert, IEnvInfos, Iinteragir, IPushMessage, IPullMessage> implements IPushMessage{
	
	private Map<String, AgentImpl> agentMap = new HashMap<String, ForwardILauncherRobotmpl.AgentImpl>();


	private class AgentImpl extends Agent<CycleAlert, IEnvInfos, Iinteragir, IPushMessage, IPullMessage> implements CycleAlert, IEnvInfos, Iinteragir, IPushMessage, IPullMessage{

		String id;
		Queue<MessageRequest> requestMessageBox; 
		Queue<MessageResponse> responseMessageBox; 
		
		public AgentImpl(String id) {
			this.id = id;
			requestMessageBox = new PriorityQueue<MessageRequest>();
			responseMessageBox = new PriorityQueue<MessageResponse>();
		}
		
		@Override
		public void endOfCycleAlert(String id) {
			eco_requires().i().endOfCycleAlert(id);
		}

		@Override
		public boolean mooveRobotWithoutBox(String idRobot, Color color,
				Point oldPoint, Point newPoint) {
			return eco_requires().k().mooveRobotWithoutBox(idRobot, color, oldPoint, newPoint);
		}

		@Override
		public boolean mooveRobotWithBox(String idRobot, Color color, Boite boite,
				Point oldPoint, Point newPoint) {
			return eco_requires().k().mooveRobotWithBox(idRobot, color, boite, oldPoint, newPoint);	
		}

		@Override
		public boolean takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint) {
			return eco_requires().k().takeBox(idRobot, robotColor, oldPoint, newPoint);
			
		}

		@Override
		public void putDownBox(Point point, Color color) {
			eco_requires().k().putDownBox(point, color);
			
		}

		@Override
		public List<Cellule> getAdjacentCells(Point robotCoord) {
			return eco_requires().j().getAdjacentCells(robotCoord);
		}
		
		@Override
		protected CycleAlert make_a() {
			return this;
		}

		@Override
		protected IEnvInfos make_b() {
			return this;
		}

		@Override
		protected Iinteragir make_c() {
			return this;
		}

		@Override
		public void doNothing() {			
		}

		@Override
		public void suicide(Point cell) {
			eco_requires().k().suicide(cell);
		}

		@Override
		public void exchange(String idRobot, Color robotColor,
				Cellule nouvelleBoite, Boite ancienneBoite, Cellule celluleRobot) {
			eco_requires().k().exchange(idRobot, robotColor, nouvelleBoite, ancienneBoite, celluleRobot);
			
		}

		@Override
		protected IPushMessage make_push() {
			return this;
		}

		@Override
		protected IPullMessage make_pull() {
			return this;
		}

		@Override
		public MessageRequest pullNextRequestMessage() {
			MessageRequest msg = null;
			synchronized (agentMap) {
				msg = this.requestMessageBox.poll();
			}
			return msg;
		}

		@Override
		public MessageResponse pullNextResponseMessage() {
			MessageResponse msg = null;
			synchronized (agentMap) {
				msg = this.responseMessageBox.poll();
			}
			return msg;
		}

		@Override
		public void sendRequestMessage(MessageRequest request) {
			eco_provides().push().sendRequestMessage(request);
		}

		@Override
		public void sendResponseMessage(MessageResponse response) {
			eco_provides().push().sendResponseMessage(response);
		}
		
		public void postRequestMessage(MessageRequest request) {
			
			synchronized (requestMessageBox) {

				this.requestMessageBox.add(request);
			}
			
		}

		public void postResponseMessage(MessageResponse response) {
			synchronized (responseMessageBox) {

				this.responseMessageBox.add(response);
			}
		}

		@Override
		public void gotBox(String idRobot, Color robotColor, Point position,
				Color boxColor) {
			eco_requires().k().gotBox(idRobot, robotColor, position, boxColor);
			
		}

		@Override
		public void lostBox(String idRobot, Color robotColor, Point position) {
			eco_requires().k().lostBox(idRobot, robotColor, position);
		}
		
	}

	@Override
	protected IPushMessage make_push() {
		return this;
	}

	@Override
	protected clrobots.Forward.Agent<CycleAlert, IEnvInfos, Iinteragir, IPushMessage, IPullMessage> make_Agent(
			String id) {
		AgentImpl agentFw = new AgentImpl(id);
		agentMap.put(id,agentFw);
		return agentFw;
	}

	@Override
	public void sendRequestMessage(MessageRequest request) {
		agentMap.get(request.getDestId()).postRequestMessage(request);
	}

	@Override
	public void sendResponseMessage(MessageResponse response) {
		agentMap.get(response.getIdDest()).postResponseMessage(response);
	}

}
