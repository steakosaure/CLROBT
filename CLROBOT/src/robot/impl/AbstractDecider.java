package robot.impl;

import clrobots.Decider;
import clrobots.interfaces.Do;

public abstract class AbstractDecider<Actionable, SelfKnowledge, Push> extends Decider<Actionable, SelfKnowledge, Push> implements Do{

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
