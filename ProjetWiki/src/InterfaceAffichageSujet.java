import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabSelectionHandler;


public class InterfaceAffichageSujet extends JPanel //implements ActionListener
{
	JButton tabBoutons[] ;
	FenetreModif fenetreModif;
	FenetreLecture fenetreLecture;
	InterfaceDebut interfacePrincipale;
	int boutonEnCours;
	
	public InterfaceAffichageSujet(int nbBoutons, ArrayList<String> nomsBoutons, InterfaceDebut parInterfacePrincipale)
	{
		super();
		this.setLayout(new GridBagLayout());
		
		interfacePrincipale = parInterfacePrincipale;
		updateAffichage(nbBoutons, nomsBoutons);	
	}

	private void ajouteComposant(Component parComposant,
			int parColonne, int parLigne, int parLargeur, int parHauteur)
	{
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.anchor = GridBagConstraints.NORTHWEST;
		contraintes.gridx=parColonne;
		contraintes.gridy=parLigne;
		contraintes.gridwidth=parLargeur;
		contraintes.gridheight=parHauteur;
		contraintes.weightx=1;
		contraintes.weighty=1;
		contraintes.fill=GridBagConstraints.NORTH;
		contraintes.insets = new Insets(10,10,0,0);
		this.add(parComposant, contraintes);
	}
	
	public void updateAffichage()
	{
		this.removeAll();
		updateUI();
	}
	
	public void updateAffichage(int nbBoutons, ArrayList<String> arrayList)
	{
		this.removeAll();
		tabBoutons = new JButton[nbBoutons];
		for(int i=0; i<nbBoutons; i++)
		{
			//tabBoutons[i] = new JButton(tabSujets[i]);
			tabBoutons[i] = new JButton(arrayList.get(i));
			tabBoutons[i].setBackground(Color.white);
			tabBoutons[i].setHorizontalAlignment(SwingConstants.LEFT);
			tabBoutons[i].setBorder(BorderFactory.createEmptyBorder());
			//tabBoutons[i].addActionListener(this);
			boutonEnCours = i;
			tabBoutons[i].addMouseListener(new MouseAdapter() {
			      public void mousePressed(MouseEvent evt) {
			    	  boutonEnCours = -1;
			    	  for(int i=0; i<tabBoutons.length; i++)
			    	 if(evt.getSource().equals(tabBoutons[i]) == true)
			    		 boutonEnCours = i;
			    	  
			    	  String requeteCreateur = "select createur from wiki.sujet where titre like '" + tabBoutons[boutonEnCours].getText() + "'";
			    	  String createur = select(requeteCreateur);
			    	  createur = createur.substring(1, createur.length()-1);
  
			    	  
						JPopupMenu popupMenu = new JPopupMenu();
						JMenuItem LirePopMenu = new JCheckBoxMenuItem("Visualiser");
						JMenuItem modifierPopMenu = new JCheckBoxMenuItem("Modifier");
						JMenuItem supprimerPopMenu = new JCheckBoxMenuItem("Supprimer");
						modifierPopMenu.addActionListener(new ActionListener() 
						{
							public void actionPerformed(ActionEvent evt) 
							{
								fenetreModif = new FenetreModif(tabBoutons[boutonEnCours].getText(), interfacePrincipale);
							}
						});
						supprimerPopMenu.addActionListener(new ActionListener() 
						{
						      public void actionPerformed(ActionEvent evt) 
						      {
						    	  System.out.println(evt.getSource());
						      }
						});
						LirePopMenu.addActionListener(new ActionListener() 
						{
						      public void actionPerformed(ActionEvent evt) 
						      {
						    	  fenetreLecture = new FenetreLecture(tabBoutons[boutonEnCours].getText(), interfacePrincipale);
						      }
						});
						System.out.println(interfacePrincipale.getIdentifiant());
						System.out.println(createur);
						popupMenu.add(LirePopMenu);
						popupMenu.add(modifierPopMenu);
						/*if (createur.equalsIgnoreCase(interfacePrincipale.getIdentifiant()) == true)
						{
							popupMenu.add(supprimerPopMenu);
						}*/		
						popupMenu.show(evt.getComponent(),evt.getX(),evt.getY());
			        }
			      });
			
			ajouteComposant(tabBoutons[i],0,i,1,1);
		}
		updateUI();
	}
	
	
	public String select(String req)
    {
		System.out.println(req);
        try
        {
            ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
            
            while (resultat.next())
            {
            	liste.add(resultat.getString(1));
            }
            return liste.toString();
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
	
	public void insert(String req)
	{
		try {
			interfacePrincipale.statement.executeQuery (req);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public void actionPerformed(ActionEvent e)
	{
		for(int i=0; i<tabBoutons.length; i++)
		{
			if(e.getSource() == tabBoutons[i])
			{
				System.out.println("Bonjour " + i);
				//fenetreModif = new FenetreModif(tabBoutons[i].getText(), interfacePrincipale);
				
				
				boutonEnCours = i;
				JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem LirePopMenu = new JCheckBoxMenuItem("Visualiser");
				JMenuItem modifierPopMenu = new JCheckBoxMenuItem("Modifier");
				JMenuItem supprimerPopMenu = new JCheckBoxMenuItem("Supprimer");
				modifierPopMenu.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent evt) 
					{
						fenetreModif = new FenetreModif(tabBoutons[boutonEnCours].getText(), interfacePrincipale);
					}
				});
				supprimerPopMenu.addActionListener(new ActionListener() 
				{
				      public void actionPerformed(ActionEvent evt) 
				      {
				    	  boolean estSupprime = planning.suppression(getIndiceSalle(),resa);
				    	  if(estSupprime == true)
				    	  {
				    		  miseAJour();
				    		  MethodesPourFichier.ecritureFichier(planning,fichier);
				    	  }
				      }
				});
				LirePopMenu.addActionListener(new ActionListener() 
				{
				      public void actionPerformed(ActionEvent evt) 
				      {
				    	  int indiceA = Integer.parseInt(groupe.getSelection().getActionCommand());
				    	  int indiceM = mois.getSelectedIndex();
				    	  int indiceS = model.getIndiceSalle();
				    	  InterfaceReservation nouvelleReservation = new InterfaceReservation(affichage, "Nouvelle réservation", planning, fichier, rowIndex, colIndex, indiceM, indiceA, indiceS);
				      }
				});
				if (resa != null)
				{
					// Ajouter les popups modifier et supprimer
					popupMenu.add(modifierPopMenu);
					popupMenu.add(supprimerPopMenu);
				}						
				// Si la cellule ciblée ne contient pas de réservation
				else
					//Ajouter le popum nouvelle réservation
					popupMenu.add(creerPopMenu);
				// Affichier le popupMenu contenant les popups
				//popupMenu.show();
				//popupMenu.show(e.getComponent(),e.getX(),e.getY());
			
				
				
				
			}
		}
		// TODO Auto-generated method stub
		
	}*/

}
