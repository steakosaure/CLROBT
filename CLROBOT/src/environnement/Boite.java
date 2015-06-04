package environnement;

import java.awt.Color;

public class Boite {

	public Boite(Color couleur){
		this.couleur = couleur;

	}
	
	public Color getCouleur() {
		return couleur;
	}
	
	private Color couleur;
	private int positionX;
	private int positionY;
	
	
	
}
