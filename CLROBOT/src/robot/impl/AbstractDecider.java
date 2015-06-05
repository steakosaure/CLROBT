package robot.impl;

import clrobots.Decider;
import clrobots.interfaces.Do;

public abstract class AbstractDecider<Actionable> extends Decider<Actionable> implements Do{

	public abstract void makeDecision();
	
	@Override
	public void doIt() {
		makeDecision();
	}

	@Override
	protected Do make_decision() {
		return this;
	}

}
