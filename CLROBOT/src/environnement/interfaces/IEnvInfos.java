package environnement.interfaces;

import java.awt.Point;
import java.util.List;

import environnement.Cellule;

public interface IEnvInfos {
	
	public List<Cellule> getAdjacentCells(Point robotCoord);

}
