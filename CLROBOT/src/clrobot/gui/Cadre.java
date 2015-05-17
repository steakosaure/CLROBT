package clrobot.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class Cadre extends JPanel {
	private static final long serialVersionUID = 1L;

	private Color couleur=Color.WHITE;
	public Cadre(){
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void changeCouleur(Color c){
		couleur=c;
		repaint();
	}

	public void paintComponent(Graphics g){
		Rectangle r=g.getClipBounds();
		g.setColor(couleur);
		g.fillRect(0,0,r.width,r.height);
	}
}
