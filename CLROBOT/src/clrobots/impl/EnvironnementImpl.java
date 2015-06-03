package clrobots.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clrobots.Boite;
import clrobots.Environnement;
import clrobots.interfaces.IEnvInfos;
import clrobots.interfaces.Igui;
import clrobots.interfaces.Iinteragir;

public class EnvironnementImpl extends Environnement{
	
	private List<Boite> boiteList = new ArrayList<Boite>();

	@Override
	protected Igui make_gui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iinteragir make_interagir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IEnvInfos make_envInfos() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addBoite(){
		Color color;
		int x, y;
		Random rand = new Random();
		float randomValue = rand.nextFloat();
		if (randomValue < 0.33){
			color = Color.RED;
		} else if (randomValue >= 0.33 && randomValue < 0.66) {
			color = Color.GREEN;
		} else {
			color = Color.BLUE;
		}
		x = rand.nextInt(50) + 1;
		y = rand.nextInt(50) + 1;
		Boite boite = new Boite(color,x,y);
		boiteList.add(boite);
	}

}
