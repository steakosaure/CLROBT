package robot.impl;

import clrobots.Agir;

public abstract class AbstractAgir<Actionable, SelfKnowledge, Push> extends Agir<Actionable, SelfKnowledge, Push>{

	@Override
	protected abstract Actionable make_action();

}
