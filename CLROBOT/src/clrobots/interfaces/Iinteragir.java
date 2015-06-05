package clrobots.interfaces;

import java.awt.Color;
import java.awt.Point;

import environnement.Boite;

public interface Iinteragir {
	public void mooveRobotWithoutBox(String idRobot, Color color, Point oldPoint, Point newPoint);
	public void mooveRobotWithBox(String idRobot, Color color, Boite boite, Point oldPoint, Point newPoint);
	public void takeBox(String idRobot, Color robotColor, Point point);
	public void putDownBox(Point point);
}
