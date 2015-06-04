package environnement.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import clrobots.Environnement;
import clrobots.interfaces.IEnvInfos;
import clrobots.interfaces.Iinteragir;
import environnement.Boite;
import environnement.CellStatus;
import environnement.Cellule;
import environnement.Nest;
import environnement.interfaces.IEnvInit;

public class EnvironnementImpl extends Environnement implements IEnvInfos, Iinteragir, IEnvInit{
	
	private List<Boite> boiteList = new ArrayList<Boite>();
	private Map<Point, Cellule> cellList = new HashMap<Point, Cellule>();
	private List<Nest> nests = new ArrayList<Nest>(); 
	
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
			}
		}
		
		/*Creation des nids*/
		addNest(new Point(8,5), Color.RED);
		addNest(new Point(25,25), Color.GREEN);
		addNest(new Point(40,40), Color.BLUE);
		
		/*Ajout des boites*/
		for (int i = 0; i < nbBoxes; i++){
			Point boxPos = generateBoite(availableCellsPos);
			availableCellsPos.remove(boxPos);
		}
		
		
	}

	@Override
	public void addNest(Point nestCoordinates, Color nestColor) {
		if (cellList.get(nestCoordinates).getStatus() == CellStatus.FREE){
			Nest nest = new Nest(nestCoordinates, nestColor);
			this.nests.add(nest);
			cellList.get(new Point(8,5)).setNest(nest);
		}
	}

	public Point generateBoite(List<Point> availableCells){
		Color color;
		int cellIndex;
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
		return availableCells.get(cellIndex);
	}

}
