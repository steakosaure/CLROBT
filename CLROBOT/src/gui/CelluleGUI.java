package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CelluleGUI extends JPanel{
	private static final long serialVersionUID = 1L;
	private Point coordinates;
	private Color couleur=Color.WHITE;

	public CelluleGUI(Point coordinates){
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		changeCouleur(couleur);
		this.coordinates = coordinates;
		this.setToolTipText("("+coordinates.x+","+coordinates.y+")");
	}

	public void changeCouleur(Color c){
		couleur=c;
		repaint();
	}


	public void addBoxCouleur(Color c){
		couleur=c;
		repaint();

	}	
	public void paintComponent(Graphics g){
		Rectangle r=g.getClipBounds();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0,  this.getWidth(), this.getHeight());
		//g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);

		/*g.fillRect((int) (r.width * 0.25), (int)(r.height * 0.25),
					(int)(r.width * 0.5), (int)(r.height * 0.5));*/
		g.drawRoundRect((int) (r.width * 0.25), (int)(r.height * 0.25),
				(int)(r.width * 0.5), (int)(r.height * 0.5),10,10);
		g.fillRoundRect((int) (r.width * 0.25), (int)(r.height * 0.25),
				(int)(r.width * 0.5), (int)(r.height * 0.5),10,10);
		g.setColor(Color.RED);

		g.drawRoundRect((int) (r.width * 0.25)+2, (int)(r.height * 0.25)+2,
				(int)(r.width * 0.5) -4 , (int)(r.height * 0.5) - 4,10,10);
		g.fillRoundRect((int) (r.width * 0.25)+2, (int)(r.height * 0.25)+2,
				(int)(r.width * 0.5) -4 , (int)(r.height * 0.5) - 4,10,10);

		/*g.setColor(Color.RED);
			g.fillRect(0,0,(int) 0.25 * r.width, (int) 0.25 *r.height);

			/* g.drawRect((int)(this.getX() + 0.25 * this.getWidth()) , (int) (this.getX() + this.getWidth() - 0.25 * this.getWidth())
	        		 ,(int) (this.getY() + 0.25 * this.getHeight()) ,(int) (this.getY() + this.getHeight() - 0.25 * this.getHeight()));  
	         g.setColor(Color.RED);  
	         g.fillRect((int)(this.getX() + 0.25 * this.getWidth()) , (int) (this.getX() + this.getWidth() - 0.25 * this.getWidth())
	        		 ,(int) (this.getY() + 0.25 * this.getHeight()) ,(int) (this.getY() + this.getHeight() - 0.25 * this.getHeight()));  */
	}

	public void paintBox(Graphics g) {
		super.paintComponent(g);  

	}
}
