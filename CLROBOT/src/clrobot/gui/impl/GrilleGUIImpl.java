package clrobot.gui.impl;



import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import clrobot.gui.Cadre;
import clrobots.gui.GrilleGUI;

public class GrilleGUIImpl extends GrilleGUI{
	
	private JFrame myFrameInfo = new JFrame("");
	final JPanel addNidPanel = new JPanel();
	JFrame myFrame = new JFrame();
	JPanel panel = new JPanel();
	private String macoul="";
	private Cadre[][] tabCadre =new Cadre[15][15]; 
	
	@Override
	protected void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {	
				
				GridLayout grille = new GridLayout(15,15);
			    panel.setLayout(grille);
			    			    
			    for (int i=0;i<15;i++)
			    	for(int j=0;j<15;j++){
			    		Cadre c=new Cadre();
			    		tabCadre[i][j]=c;
			    		panel.add(c);
			    		
			    	}
				
			    myFrame.setContentPane(panel);
			    myFrame.setTitle("Robots !!");
			    myFrame.setBounds(100,100,900,600);
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
								requires().manage().instantiateNid(Color.RED);
							else if(colNid.getText().toLowerCase().equals("green"))
								requires().manage().instantiateNid(Color.GREEN);
							else if(colNid.getText().toLowerCase().equals("blue"))
								requires().manage().instantiateNid(Color.BLUE);
							colNid.setText("");
						}
					}
				});
				
				myFrameInfo.setContentPane(addNidPanel);
				myFrameInfo.setTitle("Info Nid sur la grille 15x15 !!");
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
				panelInfo.add(new JLabel("x(0-14)"));
				
				JTextField prx=new JTextField(); 
				prx.setColumns(2);
				panelInfo.add(prx);
				panelInfo.add(new JLabel("y(0-14)"));
				
				JTextField pry=new JTextField(); 
				pry.setColumns(2);
				panelInfo.add(pry);
				
				final JButton bPlus = new JButton("+");
				bPlus.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						 
						int posx = Integer.parseInt(prx.getText());
						 int posy =  Integer.parseInt(pry.getText());
						Cadre c=tabCadre[posx][posy];
						
						Color mycol = null;
						if (macoul.toLowerCase().equals("red"))
							mycol=Color.RED;
						else if(macoul.toLowerCase().equals("blue"))
							mycol=Color.BLUE;
						else if(macoul.toLowerCase().equals("green"))
							mycol=Color.GREEN;	
						c.changeCouleur(mycol);
			    		tabCadre[posx][posy]=c;
			    		
						requires().creer().creer(posx, posy);
						
						for (int i=0;i<15;i++)
						    	for(int j=0;j<15;j++){
						    		
						    		panel.add(tabCadre[i][j]);
						    		
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
}
