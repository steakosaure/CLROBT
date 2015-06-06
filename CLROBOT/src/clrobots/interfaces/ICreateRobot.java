package clrobots.interfaces;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import environnement.Cellule;

public interface ICreateRobot {
	
	public String createNewRobotcreateNewRobot(Color color, Cellule position, Map<Color,Point> nests);

}
