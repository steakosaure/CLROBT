package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import robot.impl.AbstractAgir;
import robot.impl.AbstractDecider;
import robot.impl.AbstractPercevoir;
import robot.impl.AbstractRobot;
import robot.impl.KnowledgeImpl;
import clrobots.Agir;
import clrobots.Decider;
import clrobots.EcoRobotAgents;
import clrobots.Knowledge;
import clrobots.Percevoir;
import clrobots.interfaces.ICreateRobot;
import clrobots.interfaces.IRobotKnowledge;
import clrobots.interfaces.Iinteragir;
import environnement.Boite;
import environnement.CellStatus;
import environnement.Cellule;
import environnement.interfaces.IEnvInfos;

public class EcoRobotImpl extends EcoRobotAgents<Iinteragir, IEnvInfos, IRobotKnowledge> implements ICreateRobot {

	private Map<String,Runnable> robotsMap;
	private Map<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>> listRobot;
	
	public EcoRobotImpl() {
		robotsMap = new HashMap<String, Runnable>();
		listRobot = new HashMap<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>>();
	}

	@Override
	protected Robot<Iinteragir, IEnvInfos, IRobotKnowledge> make_Robot(String id, Color color) {
		RobotImpl robot = new RobotImpl(id, color);
		robotsMap.put(id, robot);
		listRobot.put(id, robot);
		return robot;
	}
	

	@Override
	protected ICreateRobot make_createRobot() {
		return this;
	}

	@Override
	public void createNewRobot(Color color) {
		make_Robot("Robot" + listRobot.size(), color);
	}
	
	private class RobotImpl extends AbstractRobot<Iinteragir, IEnvInfos, IRobotKnowledge> implements Runnable {

		private String id;
		private Color color;
		private Cellule coord;
		private Boite boite;
		private List<Cellule> adjacentCells;
		
		@Override
		protected void start() {
			synchronized (robotsMap) {
				eco_requires().threads().setAgentsMap(robotsMap);
			}
		}
		
		public RobotImpl(String id, Color color, Cellule initialPosition, Map<Color, Point> nestsCoord) {
			this.id = id;
			this.color = color;
			this.coord = initialPosition;
		}
		
		public RobotImpl(String id, Color color) {
			this.id = id;
			this.color = color;
		}
		
		@Override
		public void run() {
			this.parts().percevoir().perception().doIt();
		}

		@Override
		protected Percevoir<IEnvInfos, IRobotKnowledge> make_percevoir() {
			// TODO Auto-generated method stub
			return new PercevoirImpl(id);
		}

		@Override
		protected Decider<Iinteragir> make_decider() {
			// TODO Auto-generated method stub
			return new DeciderImpl(id);
		}

		@Override
		protected Agir<Iinteragir, IRobotKnowledge> make_agir() {
			// TODO Auto-generated method stub
			return new AgirImpl(id);
		}
		 
		private class PercevoirImpl extends AbstractPercevoir<IEnvInfos,IRobotKnowledge> {

			private String id;
			
			public PercevoirImpl(String id) {
				this.id = id;
			}

			@Override
			public void makePerception() {
				System.out.println(id+" : Perception");
				
			}

		}

		
		private class DeciderImpl extends AbstractDecider<Iinteragir> {

			private String id;
			
			public DeciderImpl(String id) {
				this.id = id;
			}
			
			@Override
			public void makeDecision() {
				System.out.println(id+" : Decision");
				boolean box = false;
				List<Cellule> containingBoxCells = new ArrayList<Cellule>();
				
				for (Cellule cell : adjacentCells){
					if (cell.getStatus() == CellStatus.BOX){
						containingBoxCells.add(cell);
					}
				}
				
				/*Si j'ai pas de boite et rien en vue alors se déplace aléatoirement */
				if (boite == null && containingBoxCells.isEmpty() && coord.getStatus() != CellStatus.BOX){
					int cellIndex = new Random().nextInt(adjacentCells.size());
					this.requires().action().mooveRobotWithoutBox(id, color, coord.getCoordinates(), adjacentCells.get(cellIndex).getCoordinates());
				} else if(boite == null){
				/*Si pas de boite et boites en vue alors se déplace en direction de la boite de la meme couleur en priorité */
					
				}
				/*Si une boite et rien en vue alors se déplace vers le nid */
				/*Si une boite pas de la meme couleur et une boite en vue de la meme couleur alors deposer la boite*/
				
				//this.requires().action().mooveRobotWithoutBox("", color, new Point(1,1), new Point(1,2));
				
			}
			

		}

		
		private class AgirImpl extends AbstractAgir<Iinteragir, IRobotKnowledge> implements Iinteragir{

			String id;
			public AgirImpl(String id){
				this.id = id;
			}
			@Override
			protected Iinteragir make_action() {
				// TODO Auto-generated method stub
				return this;
			}
			@Override
			public void mooveRobotWithoutBox(String idRobot, Color color,
					Point oldPoint, Point newPoint) {
				System.out.println(id+" : Action moveRobotWitoutBox");
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
			}
			@Override
			public void mooveRobotWithBox(String idRobot, Color color,
					Boite boite, Point oldPoint, Point newPoint) {
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				
			}
			@Override
			public void takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint) {
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
			}
			@Override
			public void putDownBox(Point point) {
				this.requires().finishedCycle().endOfCycleAlert(id);
			}
			
		}


		@Override
		protected Knowledge<IRobotKnowledge> make_knowledge() {
			return new KnowledgeImpl();
		}
	}
		


}
