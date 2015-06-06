package clrobots.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import robot.impl.RobotForwardAssemblyEcoImpl;
import clrobots.EcoRobotAgents;
import clrobots.Environnement;
import clrobots.Forward;
import clrobots.GUI;
import clrobots.Launcher;
import clrobots.RobotForwardAssemblyEco;
import clrobots.ScenarioEco;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ICreateRobot;
import clrobots.interfaces.IRobotKnowledge;
import clrobots.interfaces.Iinteragir;
import environnement.Cellule;
import environnement.impl.EnvironnementImpl;
import environnement.interfaces.IEnvInfos;
import environnement.interfaces.IEnvInit;
import gui.impl.GUIImpl;
import gui.interfaces.IUpdateUi;

public class ScenarioEcoImpl extends ScenarioEco<Iinteragir, IEnvInfos, IEnvInit, IRobotKnowledge, IUpdateUi> implements Runnable{

	GUIImpl gui;
	@Override
	protected Launcher make_launcher() {
		return new LauncherImpl();
	}
	
	@Override
	protected Environnement<Iinteragir, IEnvInfos, IEnvInit, IUpdateUi> make_environnement() {
		return new EnvironnementImpl();
	}

	@Override
	protected GUI<IUpdateUi, IEnvInit> make_gui() {
		gui = new GUIImpl();
		return gui;
	}

	@Override
	protected RobotForwardAssemblyEco<Iinteragir, IEnvInfos, IRobotKnowledge> make_rfAssemblyEco() {
		return new RobotForwardAssemblyEcoImpl();
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		super.start();
		this.run();
		
		/*this.newDynamicAssembly("Robot 1",Color.GREEN);
		this.newDynamicAssembly("Robot 2",Color.RED);
		this.newDynamicAssembly("Robot 3", Color.RED);
		this.newDynamicAssembly("Robot 4", Color.BLUE);
		System.out.println("Start BigEco");
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("debut ajout");
				ScenarioEcoImpl.this.newDynamicAssembly("Robot 5", Color.BLUE);
				System.out.println("ajout");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				
				ScenarioEcoImpl.this.newDynamicAssembly("Robot 6", Color.BLUE);
				ScenarioEcoImpl.this.newDynamicAssembly("Robot 7", Color.BLUE);
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				
				ScenarioEcoImpl.this.parts().launcher().call().stop();
			}
			
		});
		t.start();*/
	}

	@Override
	public void run() {
		Thread t = new Thread (new Runnable(){

			@Override
			public void run() {
				gui.run();
			}
			
		});
		
		t.start();
	}
	
	

	


}
