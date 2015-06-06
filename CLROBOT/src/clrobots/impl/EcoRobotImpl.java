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

public class EcoRobotImpl extends EcoRobotAgents<Iinteragir, IEnvInfos, IRobotKnowledge> {

	private Map<String,Runnable> robotsMap;
	private Map<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>> listRobot;
	
	public EcoRobotImpl() {
		robotsMap = new HashMap<String, Runnable>();
		listRobot = new HashMap<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>>();
	}
	
	private class RobotImpl extends AbstractRobot<Iinteragir, IEnvInfos, IRobotKnowledge> implements Runnable {

		private String id;
		private Color color;
		private Cellule coord;
		private Boite boite;
		private List<Cellule> adjacentCells; 
		private Map<Color, Point> nestsCoords;
		
		@Override
		protected void start() {
			synchronized (robotsMap) {
				eco_requires().threads().setAgentsMap(robotsMap);
			}

			this.parts().knowledge().selfKnowledge().setNestsPotisions(nestsCoords);
		}
		
		public RobotImpl(String id, Color color, Cellule initialPosition, Map<Color, Point> nestsCoord) {
			this.id = id;
			this.color = color;
			this.coord = initialPosition;
			this.nestsCoords = nestsCoord;
			this.boite = null;
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
		protected Decider<Iinteragir, IRobotKnowledge> make_decider() {
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
				adjacentCells = this.requires().context().getAdjacentCells(coord.getCoordinates());
			}

		}

		
		private class DeciderImpl extends AbstractDecider<Iinteragir, IRobotKnowledge> {

			private String id;
			
			public DeciderImpl(String id) {
				this.id = id;
			}
			
			public Cellule getNearestCellule(Point destination, List<Cellule> cells){
				System.out.println("destination ("+destination.x+","+destination.y+")");
				Cellule depart = null;
				if (!cells.isEmpty()){
					for  (Cellule cell : cells) {
						if (depart == null){
							depart = cell;
						} else {
							double dist1 = depart.getCoordinates().distance(destination);
							System.out.println("distance de depart"+ depart.getCoordinates() +" et destination"+ destination + "= "+dist1 );
							double dist2 = cell.getCoordinates().distance(destination);

							System.out.println("distance de cell"+ cell.getCoordinates() +" et destination"+ destination + "= "+dist2 );
							if (dist2 - dist1 < 0.){
								depart = cell;
							}
						}
					}

					System.out.println("currentCoords=" + coord.getCoordinates()+ "  nid="+destination+"   cellule depart="+depart.getCoordinates());
					return depart;
				} else {
					return coord;
				}	
			}
			
			public Cellule chooseBox(List<Cellule> cellList){
				Cellule choosenBox = null;
				
				for(Cellule cell : cellList) {
					if (choosenBox == null) {
						choosenBox = cell;
					}
					if (choosenBox.getBox().getCouleur() == color){
						return choosenBox;
					} else {
						choosenBox = cell;
					}
				}
				
				return choosenBox;
			}
			
			public List<Cellule> getFreeCellulesFrom(List<Cellule> cellList){
				List<Cellule> freeCells = new ArrayList<Cellule>();
				for (Cellule cell : cellList){
					if (cell.getStatus() == CellStatus.FREE){
						freeCells.add(cell);
					}
				}
				return freeCells;
			}
			
			@Override
			public void makeDecision() {
				System.out.println(id+" : Decision");
				List<Cellule> containingBoxCells = new ArrayList<Cellule>();
				
				for (Cellule cell : adjacentCells){
					if (cell.getStatus() == CellStatus.BOX){
						System.out.println("Boite a ("+cell.getCoordinates().x+","+cell.getCoordinates().y+")");
						containingBoxCells.add(cell);
					}
				}				
				

				System.out.println(containingBoxCells.size() + " --------------------------------------------------------------------------------------------");
				
				if (boite == null && containingBoxCells.isEmpty()){
					/*Si j'ai pas de boite et rien en vue alors se déplace aléatoirement */
					
					int cellIndex = new Random().nextInt(getFreeCellulesFrom(adjacentCells).size());
					System.out.println(id+" se d�place de ("+coord.getCoordinates().x+","+ coord.getCoordinates().y+") a ("
							+ adjacentCells.get(cellIndex).getCoordinates().x+","+ adjacentCells.get(cellIndex).getCoordinates().y+")");
					
					if (this.requires().action().mooveRobotWithoutBox(id, color, coord.getCoordinates(), adjacentCells.get(cellIndex).getCoordinates())) {
						coord = adjacentCells.get(cellIndex);
					}

				} else if(boite == null && !containingBoxCells.isEmpty()){
					/*Si pas de boite et boites en vue alors prend la boite de la meme couleur en priorité */
					Cellule choosenBox = chooseBox(containingBoxCells);
					System.out.println(id+" Prend boite de ("+coord.getCoordinates().x+","+ coord.getCoordinates().y+") a ("
							+ choosenBox.getCoordinates().x+","+ choosenBox.getCoordinates().y+")");

					boite = choosenBox.getBox();
					if (this.requires().action().takeBox(id, color, coord.getCoordinates(), choosenBox.getCoordinates())) {	
						coord = choosenBox;
					}
				} else if(boite != null ){
					/*Si une boite et rien en vue alors se déplace vers le nid */
					Point nestCoordinates = this.requires().knowledge().getNestCoord(boite.getCouleur());
					System.out.println(nestCoordinates);
					Cellule depart = getNearestCellule(nestCoordinates, getFreeCellulesFrom(adjacentCells));
					if (depart != null){
						if (this.requires().action().mooveRobotWithBox(id, color, boite, coord.getCoordinates(), depart.getCoordinates())) {
							coord = depart;
						}
					}
				} else if(boite != null && this.requires().knowledge().getNestCoord(boite.getCouleur()) == coord.getCoordinates()){
					/*Si une boite pas de la meme couleur et une boite en vue de la meme couleur alors deposer la boite*/
					this.requires().action().putDownBox(coord.getCoordinates());
				}			
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
			public boolean mooveRobotWithoutBox(String idRobot, Color color,
					Point oldPoint, Point newPoint) {
				System.out.println(id+" : Action moveRobotWitoutBox");
				boolean b = this.requires().interagir().mooveRobotWithoutBox(idRobot, color, oldPoint, newPoint);
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				return b;
			}
			
			@Override
			public boolean mooveRobotWithBox(String idRobot, Color color,
					Boite boite, Point oldPoint, Point newPoint) {
				boolean b = this.requires().interagir().mooveRobotWithBox(idRobot, color, boite, oldPoint, newPoint);
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				return b;
				
			}
			@Override
			public boolean takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint) {
				boolean b = this.requires().interagir().takeBox(idRobot, robotColor, oldPoint, newPoint);
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				return b;
			}
			
			@Override
			public void putDownBox(Point point) {
				this.requires().interagir().putDownBox(point);
				this.requires().finishedCycle().endOfCycleAlert(id);
			}
			
		}


		@Override
		protected Knowledge<IRobotKnowledge> make_knowledge() {
			return new KnowledgeImpl();
		}
	}


	@Override
	protected clrobots.EcoRobotAgents.Robot<Iinteragir, IEnvInfos, IRobotKnowledge> make_Robot(
			String id, Color color, Cellule position, Map<Color, Point> nests) {
		RobotImpl robot = new RobotImpl(id, color, position, nests);
		robotsMap.put(id, robot);
		listRobot.put(id, robot);
		return robot;
	}


}
