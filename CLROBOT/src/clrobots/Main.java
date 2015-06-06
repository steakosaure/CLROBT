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
		gui.drawBoxesAt(0, 0, Color.WHITE);
		gui.drawNestAt(10, 10, Color.RED);
		gui.drawRobotWithBoxAt(15, 20, Color.GREEN, Color.RED);
	}

}
