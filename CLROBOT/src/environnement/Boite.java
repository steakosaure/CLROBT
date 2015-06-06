package environnement;

import java.awt.Color;

public class Boite {

	public Boite(Color couleur){
		this.couleur = couleur;

	}
	
	public Boite(Boite boite) {
		this.couleur = boite.couleur;
	}
	
	public Color getCouleur() {
		return couleur;
	}
	
	private Color couleur;
	
	
	
}
