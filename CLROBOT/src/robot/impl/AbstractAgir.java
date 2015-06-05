package robot.impl;

import clrobots.Agir;

public abstract class AbstractAgir<Actionable, SelfKnowledge> extends Agir<Actionable, SelfKnowledge>{

	@Override
	protected abstract Actionable make_action();

}
