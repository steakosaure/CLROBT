package clrobots;

import java.awt.Color;

import gui.impl.DrawService;

public class Main {

	public static void main(String[] args) {
		/*test.EcoBoite.Component eco = new EcoBoiteImplTest().newComponent();
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		eco.boiteCreate().createNewBox(Color.BLUE, 1, 5);
		eco.boiteCreate().createNewBox(Color.RED, 9, 0);
		eco.boiteCreate().createNewBox(Color.BLUE, 0, 5);
		eco.boiteCreate().createNewBox(Color.RED, 1, 9);
		eco.boiteCreate().createNewBox(Color.GREEN, 1, 7);
		eco.boiteCreate().createNewBox(Color.GREEN, 10, 8);
		eco.boiteCreate().createNewBox(Color.BLUE, 9, 5);
		
		List<Boite> boites = eco.boitesInfos().getBoites();
	
		for(Boite boite : boites){
			System.out.println("("+boite.getPositionX()+","+boite.getPositionY()+") :" + boite.getCouleur());
		}*/
		
		//new ScenarioEcoImpl().newComponent();
		DrawService gui = new DrawService(50, 50);
		gui.drawAt(0, 0, Color.BLACK);
		gui.drawAt(49, 49, Color.BLACK);
	}

}
