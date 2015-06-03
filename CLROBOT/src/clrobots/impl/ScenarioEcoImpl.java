package clrobots.impl;

import java.awt.Color;

import clrobots.EcoRobot;
import clrobots.Forward;
import clrobots.Launcher;
import clrobots.ScenarioEco;
import clrobots.interfaces.CycleAlert;

public class ScenarioEcoImpl extends ScenarioEco	 {

	@Override
	protected EcoRobot make_ecoAE() {
		return new EcoRobotImpl();
	}

	@Override
	protected Forward<CycleAlert> make_fw() {
		// TODO Auto-generated method stub
		return new ForwardILauncherRobotmpl();
	}

	@Override
	protected Launcher make_launcher() {
		// TODO Auto-generated method stub
		return new LauncherImpl();
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		super.start();
		this.newDynamicAssembly("Robot 1",Color.GREEN);
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
		t.start();
	}

}
