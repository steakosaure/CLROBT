package clrobots.interfaces;

import java.awt.Color;
import java.awt.Point;

import environnement.Boite;
import environnement.Cellule;

public interface Iinteragir {
	public boolean mooveRobotWithoutBox(String idRobot, Color color, Point oldPoint, Point newPoint);
	public boolean mooveRobotWithBox(String idRobot, Color color, Boite boite, Point oldPoint, Point newPoint);
	public boolean takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint);
	public void putDownBox(Point point, Color c);
	public void doNothing();
	public void suicide(Point cell);
	public void exchange(String idRobot, Color robotColor, Cellule nouvelleBoite, Boite ancienneBoite, Cellule celluleRobot);
	//public void changeBox(String idRobot, Color color, Point oldPoint, Point newPoint, Boite oldBoite, Boite newBoite);
}
