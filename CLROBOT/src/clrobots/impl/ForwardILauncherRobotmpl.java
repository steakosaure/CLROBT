package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import clrobots.Forward;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.Iinteragir;
import environnement.Boite;
import environnement.Cellule;
import environnement.interfaces.IEnvInfos;

public class ForwardILauncherRobotmpl extends Forward<CycleAlert, IEnvInfos, Iinteragir> {
	
	private List<AgentImpl> list = new ArrayList<AgentImpl>();


	private class AgentImpl extends Agent<CycleAlert, IEnvInfos, Iinteragir> implements CycleAlert, IEnvInfos, Iinteragir {

		@Override
		public void endOfCycleAlert(String id) {
			eco_requires().i().endOfCycleAlert(id);
		}

		@Override
		public boolean mooveRobotWithoutBox(String idRobot, Color color,
				Point oldPoint, Point newPoint) {
			return eco_requires().k().mooveRobotWithoutBox(idRobot, color, oldPoint, newPoint);
		}

		@Override
		public boolean mooveRobotWithBox(String idRobot, Color color, Boite boite,
				Point oldPoint, Point newPoint) {
			return eco_requires().k().mooveRobotWithBox(idRobot, color, boite, oldPoint, newPoint);	
		}

		@Override
		public boolean takeBox(String idRobot, Color robotColor, Point oldPoint, Point newPoint) {
			return eco_requires().k().takeBox(idRobot, robotColor, oldPoint, newPoint);
			
		}

		@Override
		public void putDownBox(Point point, Color color) {
			eco_requires().k().putDownBox(point, color);
			
		}

		@Override
		public List<Cellule> getAdjacentCells(Point robotCoord) {
			return eco_requires().j().getAdjacentCells(robotCoord);
		}
		
		@Override
		protected CycleAlert make_a() {
			return this;
		}

		@Override
		protected IEnvInfos make_b() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		protected Iinteragir make_c() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public void doNothing() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void suicide(Point cell) {
			eco_requires().k().suicide(cell);
		}

		@Override
		public void exchange(String idRobot, Color robotColor,
				Cellule nouvelleBoite, Boite ancienneBoite, Cellule celluleRobot) {
			eco_requires().k().exchange(idRobot, robotColor, nouvelleBoite, ancienneBoite, celluleRobot);
			
		}
		
	}

	@Override
	protected clrobots.Forward.Agent<CycleAlert, IEnvInfos, Iinteragir> make_Agent() {
		AgentImpl agentFw = new AgentImpl();
		list.add(agentFw);
		return agentFw;
	}

}
