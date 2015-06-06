package gui.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

public class DrawService extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int widthRatio;
	private int heightRatio;
	private int virtualGridWidth;
	private int virtualGridHeight;
	private UIFrame ds;

	private ConcurrentHashMap<Point, Color> currentRobots;
	private ConcurrentHashMap<Point, Color> currentBoxes;
	private ConcurrentHashMap<Point, Color> currentNests;
	private ConcurrentHashMap<Point, Color[]> currentRobotsWithBox;

	public DrawService(int w, int h){
		ds = new UIFrame(this);
		virtualGridHeight = h;
		virtualGridWidth = w;
		currentRobots = new ConcurrentHashMap<Point, Color>();
		currentBoxes = new ConcurrentHashMap<Point, Color>();
		currentNests = new ConcurrentHashMap<Point, Color>();
		currentRobotsWithBox = new ConcurrentHashMap<Point, Color[]>();
	}

	private void drawGrid(Graphics g) {
		ratio(this.getWidth(), this.getHeight());
		for (int x = 1; x < virtualGridWidth + 1; x++) {
			g.drawLine((x * widthRatio), 0, (x * widthRatio), this.getHeight());
		}
		for (int y = 1; y < virtualGridHeight + 1; y++) {
			g.drawLine(0, (y * heightRatio), this.getWidth(), (y * heightRatio));
		}
	}

	private void ratio(int w, int h) {
		widthRatio = Math.round(w / virtualGridWidth);
		heightRatio = Math.round(h / virtualGridHeight);
	}

	// Callable only in paintComponent(), or if you know what your doing
	private void printShape(Shape s, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(s);
		g2d.fill(s);
		
	}

	// Callable only in paintComponent(), or if you know what your doing
	private void drawAt(int x, int y, Color c, Graphics g) {
		g.setColor(c);
		Rectangle r = new Rectangle(x * widthRatio + 2, y * heightRatio + 2,
				widthRatio - 4, heightRatio - 4);
		printShape(r, g);
		
	}
	
	public void drawBoxesAt(int x, int y, Color c) {
		currentBoxes.put(new Point(x, y), c);
		this.repaint();
	}
	
	public void drawNestAt(int x, int y, Color c) {

		currentNests.put(new Point(x, y), c);
	}
	
	public void drawNestAt(int x, int y, Color c, Graphics g) {
		g.setColor(Color.BLACK);
		Graphics2D g2d = (Graphics2D) g;
		Rectangle r = new Rectangle(x * widthRatio, y * heightRatio,
				widthRatio, heightRatio);
		g2d.draw(r);
		g2d.fill(r);
		g.setColor(c);
		g2d.drawRect(x * widthRatio + 2, y * heightRatio + 2 , widthRatio-4, heightRatio-4);
		g2d.fillRect(x * widthRatio + 2, y * heightRatio + 2 , widthRatio-4, heightRatio-4);
	}
	
	public void drawBoxesAt(int x, int y, Color c, Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRoundRect((int) ((x * widthRatio) + widthRatio*0.25), (int)(y *  heightRatio + heightRatio * 0.25),
				(int)(widthRatio * 0.5), (int)(heightRatio * 0.5),5,5);
		g.fillRoundRect((int) (x * widthRatio + widthRatio*0.25), (int)(y *  heightRatio + heightRatio * 0.25),
				(int)(widthRatio * 0.5), (int)(heightRatio * 0.5 ),5,5);

		g.setColor(c);
		g.drawRoundRect((int) ((x * widthRatio) + widthRatio*0.25 + 2), (int)(y *  heightRatio + heightRatio * 0.25 + 2),
				(int)(widthRatio * 0.5 - 4), (int)(heightRatio * 0.5 - 4),5,5);
		g.fillRoundRect((int) (x * widthRatio + widthRatio * 0.25 + 2), (int)(y *  heightRatio + heightRatio * 0.25 + 2),
				(int)(widthRatio * 0.5 - 4), (int)(heightRatio * 0.5 - 4),5,5);
	}

	// Use this method to store a point to draw.
	public void drawAt(int x, int y, Color c) {
		currentRobots.put(new Point(x, y), c);
		this.repaint();
	}
	
	public void drawRobotAt(int x, int y, Color c) {
		currentRobots.put(new Point(x, y), c);
		this.repaint();
	}
	
	public void drawRobotWithBoxAt(int x, int y, Color cRobot, Color cBoite) {
		Color[] colors = new Color[2];
		colors[0] = cRobot;
		colors[1] = cBoite;
		currentRobotsWithBox.put(new Point(x, y), colors);
		this.repaint();
	}
	
	public void drawRobotWithBoxAt(int x, int y, Color cRobot, Color cBoite, Graphics g) {
		drawAt(x, y, cRobot, g);
		drawBoxesAt(x, y, cBoite, g);
	}

	public void clear(int x, int y) {
		currentRobots.remove(new Point(x,y));
		currentBoxes.remove(new Point(x,y));
		currentRobotsWithBox.remove(new Point(x,y));
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
		for (Entry<Point, Color> entry : currentRobots.entrySet()) {
			Point p = entry.getKey();
			drawAt(p.x, p.y, entry.getValue(), g);
			
		}
			
		for (Entry<Point, Color> entry : currentBoxes.entrySet()) {
			Point p = entry.getKey();
			drawBoxesAt(p.x, p.y, entry.getValue(), g);
		}
		
		for (Entry<Point, Color> entry : currentNests.entrySet()) {
			Point p = entry.getKey();
			drawNestAt(p.x, p.y, entry.getValue(), g);
		}
		
		for (Entry<Point, Color[]> entry : currentRobotsWithBox.entrySet()) {
			Point p = entry.getKey();
			drawRobotWithBoxAt(p.x, p.y, entry.getValue()[0], entry.getValue()[1], g);
		}


	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawGrid(g);
	}
}
