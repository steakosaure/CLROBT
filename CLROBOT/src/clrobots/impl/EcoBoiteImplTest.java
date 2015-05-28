package clrobots.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import test.EcoBoite;
import clrobots.Boite;
import clrobots.interfaces.IBoiteInfo;
import clrobots.interfaces.ICreateBoite;

public class EcoBoiteImplTest extends EcoBoite implements IBoiteInfo{


	private List<BoiteSpeciesImpl> boitesSpecies;
	
	public EcoBoiteImplTest(){
		boitesSpecies = new ArrayList<BoiteSpeciesImpl>();	
	}
	@Override
	protected IBoiteInfo make_boitesInfos() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected ICreateBoite make_boiteCreate() {
		// TODO Auto-generated method stub
		return new ICreateBoite(){

			@Override
			public void createNewBox(Color couleur, int positionX, int positionY) {
				boitesSpecies.add(new BoiteSpeciesImpl(couleur, positionX, positionY));
			}
			
		};
	}

	@Override
	public List<clrobots.Boite> getBoites() {
		List<Boite> boites = new ArrayList<Boite>();
		
		for(BoiteSpeciesImpl boite : boitesSpecies){
			boites.add(boite.getBoite());
		}
		
		return boites;
	}
	
	private class BoiteSpeciesImpl extends BoiteSpecies{
		
		Boite boite;
		
		public BoiteSpeciesImpl(Color couleur, int positionX, int positionY)
		{
			boite = new Boite(couleur, positionX, positionY);
		}
		
		public Boite getBoite(){
			return boite;
		}
		
		
		
		
		
	}

}
