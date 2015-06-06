package robot.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import clrobots.EcoRobotAgents;
import clrobots.Forward;
import clrobots.RobotForwardAssemblyEco;
import clrobots.impl.EcoRobotImpl;
import clrobots.impl.ForwardILauncherRobotmpl;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ICreateRobot;
import clrobots.interfaces.IPullMessage;
import clrobots.interfaces.IPushMessage;
import clrobots.interfaces.IRobotKnowledge;
import clrobots.interfaces.Iinteragir;
import environnement.Cellule;
import environnement.interfaces.IEnvInfos;

public class RobotForwardAssemblyEcoImpl extends RobotForwardAssemblyEco<Iinteragir, IEnvInfos, IRobotKnowledge, IPushMessage, IPullMessage> implements ICreateRobot {

	int id;
	
	public RobotForwardAssemblyEcoImpl() {
		id = 0;
	}
	
	@Override
	protected ICreateRobot make_createRobot() {
		return this;
	}

	@Override
	protected EcoRobotAgents<Iinteragir, IEnvInfos, IRobotKnowledge, IPushMessage, IPullMessage> make_ecoAE() {
		return new EcoRobotImpl();
	}

	@Override
	protected Forward<CycleAlert, IEnvInfos, Iinteragir, IPushMessage, IPullMessage> make_fw() {
		return new ForwardILauncherRobotmpl();
	}

	@Override
	public String createNewRobotcreateNewRobot(Color color, Cellule position,
			Map<Color, Point> nests) {
		String robotId = "Robot"+id;
		newDynamicAssembly(robotId, color, position, nests);
		id++;
		return robotId;
		
	}


}
