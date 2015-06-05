package robot.impl;

import clrobots.Agir;
import clrobots.Decider;
import clrobots.EcoRobotAgents.Robot;
import clrobots.Knowledge;
import clrobots.Percevoir;

public abstract class AbstractRobot<Actionable, Context, SelfKnowledge> extends Robot<Actionable, Context, SelfKnowledge>{

	@Override
	protected abstract Knowledge<SelfKnowledge> make_knowledge();
	
	@Override
	protected abstract Percevoir<Context, SelfKnowledge> make_percevoir();

	@Override
	protected abstract Decider<Actionable> make_decider();

	@Override
	protected abstract Agir<Actionable, SelfKnowledge> make_agir();
	
}
