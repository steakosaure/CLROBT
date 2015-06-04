package robot.impl;

import clrobots.Agir;
import clrobots.interfaces.Do;

public class AgirImpl extends Agir implements Do{

	String id;
	public AgirImpl(String id){
		this.id = id;
	}
	
	@Override
	protected Do make_action() {
		return this;
	}

	@Override
	public void doIt() {
		System.out.println(id+ " ACTION");
		this.requires().finishedCycle().endOfCycleAlert(id);
	}

}
