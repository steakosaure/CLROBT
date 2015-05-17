package clrobot.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import clrobot.interfaces.ICreateNid;
import clrobot.interfaces.INidInfo;
import clrobots.EcoNid;

public class EcoNidImpl extends EcoNid {

	@Override
	protected Nid make_Nid(Color couleur) {
		
		return new Nid(){
			private final AtomicInteger balancex = new AtomicInteger();
			private final AtomicInteger balancey = new AtomicInteger();
			@Override
			protected INidInfo make_nidinfo() {
				
				return new INidInfo(){

					@Override
					public ArrayList<String> getInfo() {
						String px=String.valueOf(balancex.get());
						String py=String.valueOf(balancex.get());
						String col="";
						if(couleur.equals(Color.BLUE))
							col="blue";
						else if (couleur.equals(Color.RED))
							col="red";
						else
							col="green";
						ArrayList<String> info=new ArrayList<String>();
						info.add(col);
						info.add(px);
						info.add(py);
						System.out.println("mes ifnos "+col+" "+px+ " "+py);
						return info;
					}
					
				};
			}

			@Override
			protected ICreateNid make_creer() {
				
				return new ICreateNid(){

					@Override
					public void creer(int posx, int posy) {
						balancex.set(posx);
						balancey.set(posy);
						System.out.println("ma coul:"+couleur.toString()+"pos:"+balancex+","+balancey);
					}
					
				};
			}
			
		};
	}

	
}
	
