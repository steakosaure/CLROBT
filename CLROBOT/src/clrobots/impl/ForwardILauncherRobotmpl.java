package clrobots.impl;

import java.util.ArrayList;
import java.util.List;

import clrobots.Forward;
import clrobots.interfaces.CycleAlert;

public class ForwardILauncherRobotmpl extends Forward<CycleAlert> {
	
	private List<AgentImpl> list = new ArrayList<AgentImpl>();

	@Override
	protected clrobots.Forward.Agent<CycleAlert> make_Agent() {
		AgentImpl agentFw = new AgentImpl();
		list.add(agentFw);
		return agentFw;
	}

	private class AgentImpl extends Agent<CycleAlert> implements CycleAlert {

		@Override
		public void endOfCycleAlert(String id) {
			eco_requires().i().endOfCycleAlert(id);
		}

		@Override
		protected CycleAlert make_a() {
			return this;
		}
		
	}

}
