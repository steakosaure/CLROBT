package environnement.interfaces;

import java.awt.Color;
import java.awt.Point;

public interface IEnvInit {
	
	public void randomInit(int nbBoxes, int nbRobots);
	public void addNest(Point nestCoordinates, Color nestColor);

}
