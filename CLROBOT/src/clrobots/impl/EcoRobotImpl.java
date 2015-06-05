package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robot.impl.KnowledgeImpl;
import clrobots.Agir;
import clrobots.Decider;
import clrobots.EcoRobotAgents;
import clrobots.Knowledge;
import clrobots.Percevoir;
import clrobots.interfaces.Do;
import clrobots.interfaces.ICreateRobot;
import environnement.Cellule;

public class EcoRobotImpl extends EcoRobotAgents implements ICreateRobot {

	private Map<String,Runnable> robotsMap;
	private Map<String, Robot> listRobot;
	
	public EcoRobotImpl() {
		robotsMap = new HashMap<String, Runnable>();
		listRobot = new HashMap<String, Robot>();
	}

	@Override
	protected Robot make_Robot(String id, Color color) {
		RobotImpl robot = new RobotImpl(id, color);
		robotsMap.put(id, robot);
		listRobot.put(id, robot);
		return robot;
	}
	

	@Override
	protected ICreateRobot make_createRobot() {
		return this;
	}

	@Override
	public void createNewRobot(Color color) {
		make_Robot("Robot" + listRobot.size(), color);
	}
	
	private class RobotImpl extends Robot implements Runnable{

		private String id;
		private Color color;
		private Point coord;
		private List<Cellule> adjacentCells;
		
		@Override
		protected void start() {
			synchronized (robotsMap) {
				eco_requires().threads().setAgentsMap(robotsMap);
			}
		}
		
		public RobotImpl(String id, Color color, Point initialPosition, Map<Color, Point> nestsCoord) {
			this.id = id;
			this.color = color;
			this.coord = initialPosition;
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
		 
		private class PercevoirImpl extends Percevoir implements Do{

			private String id;
			
			public PercevoirImpl(String id) {
				this.id = id;
			}
			@Override
			public void doIt() {
				System.out.println(id+" Perception");
				this.requires().decision().doIt();
			}

			@Override
			protected Do make_perception() {
				return this;
			}

		}

		
		private class DeciderImpl extends Decider implements Do{

			private String id;
			
			public DeciderImpl(String id) {
				this.id = id;
			}
			@Override
			protected Do make_decision() {
				return this;
			}

			@Override
			public void doIt() {
				System.out.println(id+" DECISION");
				this.requires().action().doIt();
			}
			

		}

		
		private class AgirImpl extends Agir implements Do{

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


		@Override
		protected Knowledge make_knowledge() {
			return new KnowledgeImpl();
		}
	}
		


}
