package gui.impl;

import environnement.CellStatus;
import environnement.Cellule;
import environnement.interfaces.IEnvInit;
import gui.CelluleGUI;
import gui.interfaces.IUpdateUi;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import clrobots.GUI;

public class GUIImpl extends GUI<IUpdateUi, IEnvInit> implements IUpdateUi, Runnable {

	private Map<Color, Integer> nbBoxes;
	private Map<Color, Integer> nbRobots;
	DrawService panel; 
	//pri
	
	
	@Override
		protected void start() {
			super.start();
		    panel = new DrawService(50, 50); 
		    
		    
		}
	
/*
	private JFrame myFrameInfo = new JFrame("");
	final JPanel addNidPanel = new JPanel();
	JFrame myFrame = new JFrame();
	JPanel panel = new JPanel();
	private String macoul="";
	private CelluleGUI[][] tabCelluleGUI =new CelluleGUI[50][50]; 
	
	@Override
	protected void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {	
				
				GridLayout grille = new GridLayout(50,50);
			    panel.setLayout(grille);
			    			    
			    for (int i=0;i<50;i++)
			    	for(int j=0;j<50;j++){
			    		CelluleGUI c=new CelluleGUI(new Point(i,j));
			    		tabCelluleGUI[i][j]=c;
			    		panel.add(c);
			    		
			    	}
				
			    myFrame.setContentPane(panel);
			    myFrame.setTitle("Robots !!");
			    myFrame.setBounds(100,100,1000,800);
			    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    myFrame.setVisible(true);
			    
			    
			    final JLabel labcoulNid=new JLabel("Couleur Nid (RED,GREEN,BLUE)");
			    final JTextField colNid = new JTextField();
			    colNid.setColumns(8);
			    final JButton bAddNid = new JButton("Add");
				
				
				addNidPanel.add(labcoulNid);
				addNidPanel.add(colNid);
				addNidPanel.add(bAddNid);
				
				bAddNid.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!colNid.getText().isEmpty()) {
							macoul=colNid.getText().toLowerCase();
							if(colNid.getText().toLowerCase().equals("red"))
								requires().initEnvironnement().addNest(nestCoordinates, nestColor);
								else if(colNid.getText().toLowerCase().equals("green"))
								requires().manage().instantiateNid(Color.GREEN);
							else if(colNid.getText().toLowerCase().equals("blue"))
								requires().manage().instantiateNid(Color.BLUE);
							colNid.setText("");
						}
					}
				});
				
				myFrameInfo.setContentPane(addNidPanel);
				myFrameInfo.setTitle("Ajout des nids sur la grille 50x50 !!");
				myFrameInfo.pack();
				myFrameInfo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				myFrameInfo.setVisible(true);
					
				
			}
		});
	}
	
	
	@Override
	protected NidGUI make_NidGUI() {
		return new NidGUI() {
			private final JPanel panelInfo = new JPanel();
			@Override
			protected void start() {
				panelInfo.add(new JLabel(macoul));
				panelInfo.add(new JLabel("x(0-49)"));
				
				JTextField prx=new JTextField(); 
				prx.setColumns(2);
				panelInfo.add(prx);
				panelInfo.add(new JLabel("y(0-49)"));
				
				JTextField pry=new JTextField(); 
				pry.setColumns(2);
				panelInfo.add(pry);
				
				final JButton bPlus = new JButton("+");
				bPlus.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						 
						int posx = Integer.parseInt(prx.getText());
						 int posy =  Integer.parseInt(pry.getText());
						CelluleGUI c= tabCelluleGUI[posx][posy];
						
						Color mycol = null;
						if (macoul.toLowerCase().equals("red"))
							mycol=Color.RED;
						else if(macoul.toLowerCase().equals("blue"))
							mycol=Color.BLUE;
						else if(macoul.toLowerCase().equals("green"))
							mycol=Color.GREEN;	
						c.changeCouleur(mycol);
			    		tabCelluleGUI[posx][posy]=c;
			    		
						requires().creer().creer(posx, posy);
						
						for (int i=0;i<49;i++)
						    	for(int j=0;j<49;j++){
						    		
						    		panel.add(tabCelluleGUI[i][j]);
						    		
						    	}
						panel.revalidate();
						
						
					}
				});
				panelInfo.add(bPlus);
				addNidPanel.add(panelInfo);
				myFrameInfo.pack();
				
			}
		};
		
	}
*/
	@Override
	protected IUpdateUi make_updateGUI() {
		return this;
	}

	@Override
	public void updateCell(Cellule cell) {
		int x = cell.getCoordinates().x;
		int y = cell.getCoordinates().y;
		
		switch(cell.getStatus()) {
		case BOX:
				panel.drawBoxesAt(x, y, cell.getBox().getCouleur());
				  break;
				  
		case ROBOT: 
					panel.drawRobotAt(x, y, cell.getRobotColor());
					break;
		case ROBOTWITHBOX: panel.drawRobotWithBoxAt(x, y, cell.getRobotColor(), cell.getBox().getCouleur());
					break;
		case NEST: panel.drawNestAt(x, y, cell.getNest().getNestColor());
				    break; 
		default: panel.clear(x, y);
			break; 
		}
	}

	@Override
	public void run() {
		this.requires().initEnvironnement().randomInit(200, 2);
	}


}