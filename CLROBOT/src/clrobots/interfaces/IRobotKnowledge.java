package clrobots.interfaces;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

public interface IRobotKnowledge {
	public Point getNestCoord(Color color);
	public void setNestsPotisions(Map<Color, Point> nestCoord);
}
