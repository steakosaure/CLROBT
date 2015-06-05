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
		robotColor = null;
		robotId = null;
	}
	
	public Point getCoordinates(){
		return coord;
	}
	
	public void robotCaryingBox(String robotId, Color robotColor, Boite box) {
		this.robotId = robotId;
		this.robotColor = robotColor;
		this.box = box;
		if (this.status != CellStatus.NEST){
			this.status = CellStatus.ROBOTWITHBOX;
		}
	}
	
	public void robotNotCaryingBox(String robotId, Color robotColor) {
		this.robotId = robotId;
		this.robotColor = robotColor;
		if (this.status != CellStatus.NEST){
			this.status = CellStatus.ROBOT;
		}
	}
	
	public void setEmpty(){
		box = null;
		robotColor = null;
		robotId = null;
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
	
	public CellStatus getStatus(){
		return this.status;
	}
	
	public void addBoxtoNest(){
		if (nest != null){
			nest.addBox();
			this.box = null;
		}
	}

}
