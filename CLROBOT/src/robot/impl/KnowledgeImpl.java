package robot.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import clrobots.Knowledge;
import clrobots.interfaces.IRobotKnowledge;

public class KnowledgeImpl extends AbstractKnowledge<IRobotKnowledge> implements IRobotKnowledge{

	@Override
	public Point getNestCoord(Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNestsPotisions(Map<Point, Color> nestCoord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected IRobotKnowledge make_selfKnowledge() {
		// TODO Auto-generated method stub
		return this;
	}

}
