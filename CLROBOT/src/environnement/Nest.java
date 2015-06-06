package environnement;

import java.awt.Color;
import java.awt.Point;

public class Nest {
	
	private Color nestColor;
	private int nbBoxes;
	private Point point;
	
	
	public Nest(Point point, Color color){
		this.point = point;
		this.nestColor = color;
		this.nbBoxes = 0;
	}

	public void addBox() {
		this.nbBoxes ++;
	}
	
	public Color getNestColor() {
		return nestColor;
	}
}
