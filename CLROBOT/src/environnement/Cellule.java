package environnement;

import java.awt.Color;
import java.awt.Point;

public class Cellule {
	private Point coord;
	private CellStatus status;
	private Boite box;
	private Nest nest;
	private Color robotColor;
	private String robotId;
	
	
	public Cellule(Point coordinates){
		coord = coordinates;
		status = CellStatus.FREE;
		box = null;
		nest = null;
		setRobotColor(null);
		setRobotId(null);
	}
	
	public Point getCoordinates(){
		return coord;
	}
	
	public void robotCaryingBox(String robotId, Color robotColor, Boite box) {
		this.setRobotId(robotId);
		this.setRobotColor(robotColor);
		this.box = box;
		if (this.status != CellStatus.NEST){
			this.status = CellStatus.ROBOTWITHBOX;
		}
	}
	
	public void robotNotCaryingBox(String robotId, Color robotColor) {
		this.setRobotId(robotId);
		this.setRobotColor(robotColor);
		if (this.status != CellStatus.NEST){
			this.status = CellStatus.ROBOT;
		}
	}
	
	public void setEmpty(){
		box = null;
		setRobotColor(null);
		setRobotId(null);
		if (this.status != CellStatus.NEST){
			this.status = CellStatus.FREE;
		}
	}
	
	public void setBox(Boite box){
		this.box = box;
		this.status = CellStatus.BOX;
	}
	
	public Boite getBox(){
		return this.box;
	}
	
	public void setNest(Nest nest){
		this.nest = nest;
		this.status = CellStatus.NEST;
	}
	
	public Nest getNest() {
		return nest;
	}
	
	public CellStatus getStatus(){
		return this.status;
	}
	
	public void addBoxtoNest(){
		if (nest != null){
			nest.addBox();
			this.box = null;
		}
	}

	public Color getRobotColor() {
		return robotColor;
	}
	
	private void setRobotColor(Color robotColor) {
		this.robotColor = robotColor;
	}

	public String getRobotId() {
		return robotId;
	}

	private void setRobotId(String robotId) {
		this.robotId = robotId;
	}

}
