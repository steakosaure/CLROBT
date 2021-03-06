package robot.impl;

import clrobots.Agir;
import clrobots.Decider;
import clrobots.EcoRobotAgents.Robot;
import clrobots.interfaces.IPullMessage;
import clrobots.interfaces.IPushMessage;
import clrobots.interfaces.IRobotKnowledge;
import clrobots.Knowledge;
import clrobots.Percevoir;

public abstract class AbstractRobot<Actionable, Context, SelfKnowledge, Push, Pull> extends Robot<Actionable, Context, SelfKnowledge, Push, Pull>{

	@Override
	protected abstract Knowledge<SelfKnowledge> make_knowledge();
	
	@Override
	protected abstract Percevoir<Context, SelfKnowledge, Pull> make_percevoir();

	@Override
	protected abstract Decider<Actionable, SelfKnowledge, Push> make_decider();

	@Override
	protected abstract Agir<Actionable, SelfKnowledge, Push> make_agir();
	
}
