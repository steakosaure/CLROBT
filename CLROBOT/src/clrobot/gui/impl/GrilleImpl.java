package clrobot.gui.impl;


import java.awt.Color;



import clrobot.impl.EcoNidImpl;
import clrobot.interfaces.INidManage;
import clrobots.EcoNid;
import clrobots.gui.Grille;
import clrobots.gui.GrilleGUI;

public class GrilleImpl extends Grille{

	@Override
	protected INidManage make_manage() {
		
		return new INidManage(){

			@Override
			public void instantiateNid(Color col) {
				newGrilleNid(col);
				
			}
			
		};
	}

	@Override
	protected EcoNid make_b() {
		
		return new EcoNidImpl();
	}

	@Override
	protected GrilleGUI make_gui() {
		
		return new GrilleGUIImpl();
	}

	@Override
	protected GrilleNid make_GrilleNid(Color coul) {
		return new GrilleNid() {};
	}
}
