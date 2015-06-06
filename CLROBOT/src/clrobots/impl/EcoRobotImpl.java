package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

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

	private ConcurrentHashMap<String,Runnable> robotsMap;
	private ConcurrentHashMap<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>> listRobot;
	private int MAXNJ = 40;

	public EcoRobotImpl() {
		robotsMap = new ConcurrentHashMap<String, Runnable>();
		listRobot = new ConcurrentHashMap<String, Robot<Iinteragir, IEnvInfos, IRobotKnowledge>>();
	}

	private class RobotImpl extends AbstractRobot<Iinteragir, IEnvInfos, IRobotKnowledge> implements Runnable {

		private String id;
		private Color robotColor;
		private Cellule coord;
		private Cellule previousCell;
		private Boite boite;
		private List<Cellule> adjacentCells; 
		private Map<Color, Point> nestsCoords;
		private int energyLevel;

		@Override
		protected void start() {
			synchronized (robotsMap) {
				eco_requires().threads().setAgentsMap(robotsMap);
			}

			this.parts().knowledge().selfKnowledge().setNestsPotisions(nestsCoords);
		}

		public RobotImpl(String id, Color color, Cellule initialPosition, Map<Color, Point> nestsCoord) {
			this.id = id;
			this.robotColor = color;
			this.coord = initialPosition;
			this.previousCell = initialPosition;
			this.nestsCoords = nestsCoord;
			this.boite = null;
			this.energyLevel = MAXNJ;
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
				System.out.println(id+" : Perception  " + "ENERGIE :"+energyLevel);
				adjacentCells = this.requires().context().getAdjacentCells(coord.getCoordinates());
			}

		}


		private class DeciderImpl extends AbstractDecider<Iinteragir, IRobotKnowledge> {

			private String id;

			public DeciderImpl(String id) {
				this.id = id;
			}

			public Cellule getNearestCellule(Point destination, List<Cellule> cells){
				Cellule depart = null;
				if (!cells.isEmpty()){
					for  (Cellule cell : cells) {
						if (depart == null){
							depart = cell;
						} else {
							double dist1 = depart.getCoordinates().distance(destination);
							double dist2 = cell.getCoordinates().distance(destination);

							if (dist2 - dist1 < 0){
								depart = cell;
							}
						}
					}

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
					if (choosenBox.getBox().getCouleur() == robotColor){
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


				// SI PAS DE BOITE et PAS DE BOITES A PRENDRE, SE DEPLACER
				if (boite == null && containingBoxCells.isEmpty()){

					List<Cellule> freeCells = getFreeCellulesFrom(adjacentCells);

					// SI BLOQUE
					if(freeCells.size() > 0) {

						if(freeCells.size() != 1 && freeCells.contains(previousCell)) {
							freeCells.remove(freeCells.indexOf(previousCell));
						}

						int cellIndex = new Random().nextInt(freeCells.size());
						System.out.println(id+" se déplace de ("+coord.getCoordinates().x+","+ coord.getCoordinates().y+") a ("
								+ adjacentCells.get(cellIndex).getCoordinates().x+","+ adjacentCells.get(cellIndex).getCoordinates().y+")");

						if (this.requires().action().mooveRobotWithoutBox(id, robotColor, coord.getCoordinates(), adjacentCells.get(cellIndex).getCoordinates())) {
							previousCell = coord;
							coord = adjacentCells.get(cellIndex);
						}
					}
					else {
						this.requires().action().doNothing();
					}


					// SI PAS DE BOITE ET AU MOINS UNE BOITE DANS UNE CELLULE ADJACENTE
				} else if(boite == null && !containingBoxCells.isEmpty()){
					/*Si pas de boite et boites en vue alors prend la boite de la meme couleur en prioritÃ© */
					Cellule choosenBox = chooseBox(containingBoxCells);
					System.out.println(id+" Prend boite de ("+coord.getCoordinates().x+","+ coord.getCoordinates().y+") a ("
							+ choosenBox.getCoordinates().x+","+ choosenBox.getCoordinates().y+")");

					boite = choosenBox.getBox();
					if (this.requires().action().takeBox(id, robotColor, coord.getCoordinates(), choosenBox.getCoordinates())) {	
						coord = choosenBox;
					}
					//SI J'AI UNE BOITE
				} else if(boite != null ){

					boolean adjNest = false;
					Cellule cellNest = null;
					System.out.println("NID de couleur "+boite.getCouleur()+" "+this.requires().knowledge().getNestCoord(boite.getCouleur()));

					for(Cellule adjCell: adjacentCells){
						if(adjCell.getCoordinates().x == this.requires().knowledge().getNestCoord(boite.getCouleur()).x 
								&& adjCell.getCoordinates().y == this.requires().knowledge().getNestCoord(boite.getCouleur()).y) {


							adjNest = true;
							cellNest = adjCell;
						}
					}

					//SI JE SUIS A COTE DU NID JE DEPOSE LA BOITE
					if(adjNest){/*Si une boite pas de la meme couleur et une boite en vue de la meme couleur alors deposer la boite*/
						System.out.println(id + " pose boite ");

						this.requires().action().putDownBox(cellNest.getCoordinates(), boite.getCouleur());
						boite = null;

						//SINON JE ME DEPLACE VERS LE NID
					} else {
						boolean exchange = false;
						if(boite.getCouleur() != robotColor && !containingBoxCells.isEmpty()) {
							Cellule choosenBox = chooseBox(containingBoxCells);

							if (choosenBox!= null && choosenBox.getBox().getCouleur() == robotColor) {
								exchange = true;


								System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA "+choosenBox.getBox().getCouleur());
								Boite nouvelleBoite = new Boite(choosenBox.getBox());
								this.requires().action().exchange(id, robotColor, choosenBox, boite, coord);
								boite = nouvelleBoite;

								System.out.println("BBBBBBBBBBBBBBB "+boite.getCouleur());
								
							}
						}

						if(!exchange) {
							Point nestCoordinates = this.requires().knowledge().getNestCoord(boite.getCouleur());
							System.out.println(nestCoordinates);
							int nbFreeCells =  getFreeCellulesFrom(adjacentCells).size();

							//SI JE SUIS BLOQUE J'ATTEND
							if(nbFreeCells == 0) {

								System.out.println(id+" porte une boite mais peu pas bouger ");
								this.requires().action().doNothing();
								//SINON J'AVANCE
							} else {
								Cellule depart = getNearestCellule(nestCoordinates, getFreeCellulesFrom(adjacentCells));
								if (depart != null){
									System.out.println(id+" essai de bouger ");

									if (this.requires().action().mooveRobotWithBox(id, robotColor, boite, coord.getCoordinates(), depart.getCoordinates())) {
										coord = depart;
									}

								}
							}
						}
					}

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
				return this;
			}
			@Override
			public boolean mooveRobotWithoutBox(String idRobot, Color color,
					Point oldPoint, Point newPoint) {
				System.out.println(id+" : Action moveRobotWitoutBox");
				boolean b = false;
				energyLevel--;

				if(energyLevel <= 0) {
					suicide(oldPoint);
				} else {
					b = this.requires().interagir().mooveRobotWithoutBox(idRobot, color, oldPoint, newPoint);
				}

				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				return b;
			}

			@Override
			public boolean mooveRobotWithBox(String idRobot, Color color,
					Boite boite, Point oldPoint, Point newPoint) {
				boolean b = false;
				energyLevel--;

				if(energyLevel == 0) {
					suicide(oldPoint);
				} else {
					b = this.requires().interagir().mooveRobotWithBox(idRobot, color, boite, oldPoint, newPoint);
				}

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
			public void putDownBox(Point point, Color color) {
				this.requires().interagir().putDownBox(point, color);

				int treat = 0;

				if(color == robotColor) {
					treat = (int) MAXNJ *2 / 3;
				} else {
					treat = (int) MAXNJ / 3;
				}
				energyLevel += treat; 

				if(energyLevel > MAXNJ) energyLevel = MAXNJ;

				this.requires().finishedCycle().endOfCycleAlert(id);
			}

			@Override
			public void doNothing() {
				this.requires().finishedCycle().endOfCycleAlert(id);
			}
			@Override
			public void suicide(Point cell) {
				synchronized (robotsMap) {
					Runnable robot = robotsMap.get(id);

					ConcurrentHashMap<String, Runnable> nouvelleMap = new ConcurrentHashMap<String, Runnable>();
					for(String idRobot: robotsMap.keySet()) {
						if (!id.equals(idRobot)) {
							nouvelleMap.put(idRobot, robotsMap.get(idRobot));
						}
					}
					robotsMap = nouvelleMap;
					eco_requires().threads().setAgentsMap(robotsMap);
				}

				this.requires().interagir().suicide(cell);
			}
			@Override
			public void exchange(String idRobot, Color robotColor,
					Cellule nouvelleBoite, Boite ancienneBoite,
					Cellule celluleRobot) {
				this.requires().interagir().exchange(idRobot, robotColor, nouvelleBoite, ancienneBoite, celluleRobot);
				this.requires().finishedCycle().endOfCycleAlert(idRobot);
				
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
