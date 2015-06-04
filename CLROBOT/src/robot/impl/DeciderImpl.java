package robot.impl;

import clrobots.Decider;
import clrobots.interfaces.Do;

public class DeciderImpl extends Decider implements Do{

	private String id;
	
	public DeciderImpl(String id) {
		this.id = id;
	}
	@Override
	protected Do make_decision() {
		return this;
	}

	@Override
	public void doIt() {
		System.out.println(id+" DECISION");
		this.requires().action().doIt();
	}
	

}
