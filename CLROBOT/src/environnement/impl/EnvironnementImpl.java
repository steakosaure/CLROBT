package environnement.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import clrobots.Environnement;
import clrobots.interfaces.IRobotKnowledge;
import clrobots.interfaces.Iinteragir;
import environnement.Boite;
import environnement.CellStatus;
import environnement.Cellule;
import environnement.Nest;
import environnement.interfaces.IEnvInfos;
import environnement.interfaces.IEnvInit;
import gui.interfaces.IUpdateUi;

public class EnvironnementImpl extends Environnement<Iinteragir, IEnvInfos, IEnvInit, IUpdateUi> implements IEnvInfos, Iinteragir, IEnvInit{
	
	private List<Boite> boiteList = new ArrayList<Boite>();
	private Map<Point, Cellule> cellList = new HashMap<Point, Cellule>();
	private List<Nest> nests = new ArrayList<Nest>(); 
	private Map<Color, Point> nestsCoords = new HashMap<Color, Point>();
	
	@Override
	protected Iinteragir make_interagir() {
		return this;
	}

	@Override
	protected IEnvInfos make_envInfos() {
		return this;
	}
	
	@Override
	protected IEnvInit make_envInit() {
		return this;
	}

	@Override
	public void randomInit(int nbBoxes, int nbRobots) {
		List<Point> availableCellsPos = new ArrayList<>();
		
		/*Initialisation des cellules*/
		for(int i = 0; i < 50; i++) {
			for(int j = 0; j < 50; j++) {
				Point p = new Point(i,j);
				cellList.put(p, new Cellule(p));
				availableCellsPos.add(p);
			}
		}
		
		/*Creation des nids*/
		addNest(new Point(8,5), Color.RED);
		addNest(new Point(25,25), Color.GREEN);
		addNest(new Point(40,40), Color.BLUE);

		this.requires().updateOutput().updateCell(cellList.get(new Point(8,5)));
		this.requires().updateOutput().updateCell(cellList.get(new Point(25,25)));
		this.requires().updateOutput().updateCell(cellList.get(new Point(40,40)));
		
		/*Ajout des boites*/
		for (int i = 0; i < nbBoxes; i++){
			Point boxPos = generateBoite(availableCellsPos);
			
			this.requires().updateOutput().updateCell(cellList.get(boxPos));
			availableCellsPos.remove(boxPos);
		}
		
		for (int i = 0; i < nbRobots; i++){
			Point robotPos = generateNewRobot(availableCellsPos, nestsCoords);
			this.requires().updateOutput().updateCell(cellList.get(robotPos));
			availableCellsPos.remove(robotPos);
		}
		
		
	}

	@Override
	public void addNest(Point nestCoordinates, Color nestColor) {
		if (cellList.get(nestCoordinates).getStatus() == CellStatus.FREE && !nestsCoords.keySet().contains(nestColor)){
			nestsCoords.put(nestColor, nestCoordinates);
			Nest nest = new Nest(nestCoordinates, nestColor);
			this.nests.add(nest);
			cellList.get(nestCoordinates).setNest(nest);
		}
	}

	public Point generateBoite(List<Point> availableCells){
		Color color;
		int cellIndex;
		System.out.println(availableCells.size());
		cellIndex = new Random().nextInt(availableCells.size());
		
		float randomValue = new Random().nextInt(3);
		
		if (randomValue == 0){
			color = Color.RED;
		} else if (randomValue == 1) {
			color = Color.GREEN;
		} else {
			color = Color.BLUE;
		}
		
		Boite boite = new Boite(color);
		boiteList.add(boite);
		this.cellList.get(availableCells.get(cellIndex)).setBox(boite);
		return availableCells.get(cellIndex);
	}
	
	public Point generateNewRobot(List<Point> availableCells, Map<Color,Point>nestPos, Color color){
		int cellIndex;
		cellIndex = new Random().nextInt(availableCells.size());
		String robotId = this.requires().createRobot().createNewRobotcreateNewRobot(color, this.cellList.get(availableCells.get(cellIndex)), nestPos);

		this.cellList.get(availableCells.get(cellIndex)).robotNotCaryingBox(robotId, color);
		return availableCells.get(cellIndex);
	}
	
	public Point generateNewRobot(List<Point> availableCells, Map<Color,Point>nestPos){
		Color color;
		
		float randomValue = new Random().nextInt(3);
		
		if (randomValue == 0){
			color = Color.RED;
		} else if (randomValue == 1) {
			color = Color.GREEN;
		} else {
			color = Color.BLUE;
		}
		
		return generateNewRobot(availableCells, nestPos, color);
	}

	@Override
	public boolean mooveRobotWithoutBox(String idRobot, Color color, Point oldPoint, Point newPoint) {
		synchronized(cellList) {
			if (cellList.get(newPoint).getStatus() == CellStatus.FREE){
				cellList.get(oldPoint).setEmpty();
				cellList.get(newPoint).robotNotCaryingBox(idRobot, color);
				this.requires().updateOutput().updateCell(cellList.get(newPoint));
				System.out.println("STATUT "+cellList.get(oldPoint).getStatus());
				this.requires().updateOutput().updateCell(cellList.get(oldPoint));
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint) {
		synchronized(cellList) {
			if (cellList.get(newPoint).getStatus() == CellStatus.BOX){
				cellList.get(oldPoint).setEmpty();
				cellList.get(newPoint).robotCaryingBox(idRobot, robotColor, cellList.get(newPoint).getBox());
				this.requires().updateOutput().updateCell(cellList.get(newPoint));
				System.out.println("STATUT "+cellList.get(oldPoint).getStatus());
				this.requires().updateOutput().updateCell(cellList.get(oldPoint));
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public boolean mooveRobotWithBox(String idRobot, Color color, Boite boite,
			Point oldPoint, Point newPoint) {
		
		synchronized (cellList) {
			if (cellList.get(newPoint).getStatus() == CellStatus.FREE){
				cellList.get(oldPoint).setEmpty();
				cellList.get(newPoint).robotCaryingBox(idRobot, color, boite);
				this.requires().updateOutput().updateCell(cellList.get(newPoint));
				System.out.println("STATUT "+cellList.get(oldPoint).getStatus());
				this.requires().updateOutput().updateCell(cellList.get(oldPoint));
				return true;
			} else {
				return false;
			}
			
		}
	}

	@Override
	public void putDownBox(Point point) {
		synchronized (cellList) {
			cellList.get(point).addBoxtoNest();
	}	
	}

	@Override
	public List<Cellule> getAdjacentCells(Point robotCoord) {
		List<Cellule> adjacentCells = new ArrayList<Cellule>();
		List<Integer> coordX = new ArrayList<Integer>();
		List<Integer> coordY = new ArrayList<Integer>();
		
		if(robotCoord.x != 0) {
			coordX.add(new Integer(robotCoord.x-1));
		}
		
		if(robotCoord.x != 49) {
			coordX.add(new Integer(robotCoord.x+1));
		}
		
		if(robotCoord.y != 0) {
			coordY.add(new Integer(robotCoord.y-1));
		}
		
		if(robotCoord.y != 49) {
			coordY.add(new Integer(robotCoord.y+1));
		}
		
			
		for(Integer x : coordX) {
			adjacentCells.add(cellList.get(new Point(x,robotCoord.y)));
			
			for(Integer y: coordY) {
				adjacentCells.add(cellList.get(new Point(x,y)));
				
				if(coordX.indexOf(x) == 0)
					adjacentCells.add(cellList.get(new Point(robotCoord.x,y)));
			}
		}
		
		return adjacentCells;
	}


}
