package clrobots;

import java.awt.Color;

public class Boite {

	public Boite(Color couleur, int positionX, int positionY){
		this.couleur = couleur;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public Color getCouleur() {
		return couleur;
	}
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	private Color couleur;
	private int positionX;
	private int positionY;
	
	
	
}
