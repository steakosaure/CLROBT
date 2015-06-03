package robot.impl;

import clrobots.Percevoir;
import clrobots.interfaces.Do;

public class PercevoirImpl extends Percevoir implements Do{

	private String id;
	
	public PercevoirImpl(String id) {
		this.id = id;
	}
	@Override
	public void doIt() {
		System.out.println(id+" Perception");
		this.requires().decision().doIt();
	}

	@Override
	protected Do make_perception() {
		return this;
	}

}
