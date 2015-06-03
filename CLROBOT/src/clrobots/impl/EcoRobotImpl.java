package clrobots.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robot.impl.AgirImpl;
import robot.impl.DeciderImpl;
import robot.impl.PercevoirImpl;
import clrobots.Agir;
import clrobots.Decider;
import clrobots.EcoRobot;
import clrobots.Percevoir;

public class EcoRobotImpl extends EcoRobot {

	private Map<String,Runnable> robotsMap;
	private List<Runnable> listRunnable;
	
	public EcoRobotImpl() {
		robotsMap = new HashMap<String, Runnable>();
		listRunnable = new ArrayList<Runnable>();
	}

	@Override
	protected Robot make_Robot(String id, Color color) {
		RobotImpl robot = new RobotImpl(id, color);
		robotsMap.put(id, robot);
		return robot;
	}
	
	private class RobotImpl extends Robot implements Runnable{

		private String id;
		private Color color;
		
		@Override
		protected void start() {
			synchronized (robotsMap) {
				eco_requires().threads().setAgentsMap(robotsMap);
			}
		}
		
		
		public RobotImpl(String id, Color color) {
			this.id = id;
			this.color = color;
		}
		
		@Override
		public void run() {
			this.parts().percevoir().perception().doIt();
		}

		@Override
		protected Percevoir make_percevoir() {
			// TODO Auto-generated method stub
			return new PercevoirImpl(id);
		}

		@Override
		protected Decider make_decider() {
			// TODO Auto-generated method stub
			return new DeciderImpl(id);
		}

		@Override
		protected Agir make_agir() {
			// TODO Auto-generated method stub
			return new AgirImpl(id);
		}
		
	}

}
