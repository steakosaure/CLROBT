package robot.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import clrobots.Knowledge;
import clrobots.interfaces.IRobotKnowledge;

public class KnowledgeImpl extends AbstractKnowledge<IRobotKnowledge> implements IRobotKnowledge{
	private Map<Color, Point> nestCoord = new HashMap<Color, Point>();
	
	@Override
	public Point getNestCoord(Color color) {
		return nestCoord.get(color);
	}

	@Override
	public void setNestsPotisions(Map<Color, Point> nestCoord) {
		this.nestCoord = nestCoord;
	}

	@Override
	protected IRobotKnowledge make_selfKnowledge() {
		return this;
	}

}
