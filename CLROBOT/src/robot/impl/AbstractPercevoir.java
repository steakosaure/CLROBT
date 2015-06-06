package robot.impl;

import clrobots.Percevoir;
import clrobots.interfaces.Do;

public abstract class AbstractPercevoir<Context,SelfKnowledge, Pull> extends Percevoir<Context,SelfKnowledge, Pull> implements Do {
	
	public abstract void makePerception();
	
	@Override
	protected Do make_perception() {
		return this;
	}

	@Override
	public void doIt() {
		this.makePerception();
		this.requires().decision().doIt();
		
	}

}
