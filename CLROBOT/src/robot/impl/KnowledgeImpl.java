package robot.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import clrobots.Knowledge;
import clrobots.interfaces.IGetKnowledge;
import clrobots.interfaces.ISetKnowledge;

public class KnowledgeImpl extends Knowledge implements IGetKnowledge, ISetKnowledge{

	@Override
	protected IGetKnowledge make_getKnowledge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ISetKnowledge make_setKnowledge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNestsPotisions(Map<Point, Color> nestCoord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getNestCoord(Color color) {
		// TODO Auto-generated method stub
		return null;
	}

}
