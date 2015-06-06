package clrobots.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clrobots.Launcher;
import clrobots.interfaces.Callable;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ITakeThreads;

public class LauncherImpl extends Launcher implements Callable, CycleAlert, ITakeThreads {


	private Map<String,Runnable> robots = new HashMap<String,Runnable>();
	private int nbRobotsPerCycle = 0;
	private int nbFinishedCycles = 0;
	private ExecutorService execService = null;
	boolean stop = false;
	private int speed = 500;

	@Override
	public void run() {

		synchronized(robots){
			nbRobotsPerCycle = robots.size();
			System.out.println("RUN!!!!!  "+ robots.size());


			if(!(execService == null)){

				synchronized (robots) {
					for(Runnable e: robots.values() )
						execService.execute(e);
				}

			}
		}
	}

	@Override
	public void stop() {
		stop = true;
		System.out.println("STOP!");

	}

	@Override
	public void setAgentsMap(Map<String, Runnable> robots) {
		synchronized(this.robots){

			if(execService != null)
				execService.shutdown();

			execService = Executors.newFixedThreadPool(robots.size() + 1);
			if(this.robots.size() == 0 && !stop) {

				
				this.robots.clear();
				this.robots.putAll(robots);
				this.run();
			} else {
				this.robots.clear();
				this.robots.putAll(robots);
				System.out.println("ICI "+ this.robots.size());
				
			}
		}

	}

	@Override
	public void endOfCycleAlert(String id) {
		System.out.println(new Date() + " : Agent Etat "+id+" a fini son cycle!");
		nbFinishedCycles++;

		System.out.println("nbFinished = "+nbFinishedCycles+ " :  size = "+robots.size() + " : nbAgentPC = "+ nbRobotsPerCycle);
		if(nbFinishedCycles == 	nbRobotsPerCycle && !stop){
			nbFinishedCycles = 0;
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.run();
		} else if(stop) {
			synchronized (robots) {
				robots.remove(id);

				if(robots.size() == 0) {
					execService.shutdownNow();
				}
			}
		}
	}

	@Override
	protected Callable make_call() {
		return this;
	}

	@Override
	protected CycleAlert make_finishedCycle() {
		return this;
	}

	@Override
	protected ITakeThreads make_threads() {
		return this;
	}

}
