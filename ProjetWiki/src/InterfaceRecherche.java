import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;


public class InterfaceRecherche extends JPanel implements ActionListener
{
	InterfaceDebut interfacePrincipale;
	InterfaceAffichage interfaceAffic;
	
	JButton boutonRecherche = new JButton("Rechercher");
	JButton boutonReinitialise = new JButton("Reinitialiser");
	JLabel labelPattern = new JLabel("Mots cles : ");
	JLabel labelAuteur = new JLabel("Auteur :     ");
	JLabel labelPeriode = new JLabel(" à : ");
	JTextField champPattern = new JTextField(20);
	JTextField champAuteur = new JTextField(20);
	String[] listeType = {"Dans le sujet seulement","Dans le corps seulement","Dans les deux"};
	String[] listeJours = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	String[] listeMois = {"01","02","03","04","05","06","07","08","09","10","11","12"};
	String[] listeAnnes = {"2011","2012","2013"};
	
	JComboBox choixCible = new JComboBox(listeType);
	
	JComboBox choixJour = new JComboBox(listeJours);
	JComboBox choixMois = new JComboBox(listeMois);
	JComboBox choixAnnee = new JComboBox(listeAnnes);
	JComboBox choixJour2 = new JComboBox(listeJours);
	JComboBox choixMois2 = new JComboBox(listeMois);
	JComboBox choixAnnee2 = new JComboBox(listeAnnes);
	
	JCheckBox activDate = new JCheckBox("Sur une période de : ");
	JCheckBox activTriNlecture = new JCheckBox("Trier par nombre de lectures");
	
	int nbBoutons;
	ArrayList<String> nomsBoutons;
	
	public InterfaceRecherche(InterfaceDebut parInterfacePrincipale, InterfaceAffichage parInterfaceAffic)
	{
		super();
		interfacePrincipale = parInterfacePrincipale;
		interfaceAffic = parInterfaceAffic;
		
		//Avec flow + gridbag
		//this.setLayout(new GridBagLayout());
		this.setLayout(new GridLayout(4,1,5,5));
		JPanel panelPattern = new JPanel();
		panelPattern.setLayout(new FlowLayout(FlowLayout.LEFT));
		champPattern.setPreferredSize(new Dimension(300,25));
		panelPattern.add(labelPattern);
		panelPattern.add(champPattern);
		panelPattern.add(choixCible);
		this.add(panelPattern);
		//ajouteComposant(panelPattern,0,0,1,1,true);
		
		JPanel panelAuteurEtDate = new JPanel();
		panelAuteurEtDate.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel panelAuteur = new JPanel();
		panelAuteur.setLayout(new FlowLayout(FlowLayout.LEFT));
		//panelAuteur.add(activAuteur);
		panelAuteur.add(labelAuteur);
		champAuteur.setPreferredSize(new Dimension(300,25));
		panelAuteur.add(champAuteur);
		panelAuteur.add(activTriNlecture);
		panelAuteurEtDate.add(panelAuteur);
		//ajouteComposant(panelAuteur,0,1,1,1,true);
		
		JPanel panelDate = new JPanel();
		panelDate.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDate.add(activDate);
		activDate.addActionListener(this);
		panelDate.add(choixJour);
		choixJour.setEnabled(false);
		panelDate.add(choixMois);
		choixMois.setEnabled(false);
		panelDate.add(choixAnnee);
		choixAnnee.setEnabled(false);
		panelDate.add(labelPeriode);
		panelDate.add(choixJour2);
		choixJour2.setEnabled(false);
		panelDate.add(choixMois2);
		choixMois2.setEnabled(false);
		panelDate.add(choixAnnee2);
		choixAnnee2.setEnabled(false);
		
		//ajouteComposant(panelDate,0,2,1,1,true);
		
		panelAuteurEtDate.add(panelDate);
		//this.add(panelAuteurEtDate);
		this.add(panelAuteur);
		this.add(panelDate);
		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new FlowLayout(FlowLayout.LEFT));
		boutonRecherche.setPreferredSize(new Dimension(150, 30));
		boutonReinitialise.setPreferredSize(new Dimension(150, 30));
		panelBoutons.add(boutonRecherche);
		panelBoutons.add(boutonReinitialise);
		this.add(panelBoutons);
		boutonRecherche.addActionListener(this);
		boutonReinitialise.addActionListener(this);
		

	}

	public void actionPerformed(ActionEvent parEvt) 
	{
		String requete;
		
		if(parEvt.getSource() == activDate)
			if(activDate.isSelected() == true)
			{
				choixJour.setEnabled(true);
				choixMois.setEnabled(true);
				choixAnnee.setEnabled(true);
				choixJour2.setEnabled(true);
				choixMois2.setEnabled(true);
				choixAnnee2.setEnabled(true);
			}
			else
			{
				choixJour.setEnabled(false);
				choixMois.setEnabled(false);
				choixAnnee.setEnabled(false);
				choixJour2.setEnabled(false);
				choixMois2.setEnabled(false);
				choixAnnee2.setEnabled(false);
			}
			
		if(parEvt.getSource() == boutonRecherche)
		{
			String rechPattern = champPattern.getText();
			String rechAuteur = champAuteur.getText();
	        
			if(interfacePrincipale.getEstConnecte() == true)
			{
				if(rechPattern.equals(""))
			    {
					if(rechAuteur.equals(""))
					{
						if(!activDate.isSelected())
						{
							if(!activTriNlecture.isSelected())
							{
								// Si pas de mots cles, pas d'auteur, pas de date et pas de tri
								requete = "select distinct titre from wiki.contenu";
								select(requete);
								interfaceAffic.miseAJour();
								System.err.println("Si pas de mots cles, pas d'auteur, pas de date et pas de tri");
								
							}
							else
							{
								//TRIIIIIIIII
								// Si pas de mots cles, pas d'auteur, pas de date mais tri
								requete = "select distinct titre from wiki.contenu order by (select count(*) from wiki.lecture where titre like titre)";
								select(requete);
								interfaceAffic.miseAJour();
								System.err.println("Si pas de mots cles, pas d'auteur, pas de date mais tri");
							}
						}
							// Si pas de mots cles, pas d'auteur et pas de date : erreur
						//	JOptionPane.showMessageDialog(this, "Vous devez entrer un motif de recherche.",
					      //  		"Erreur de syntaxe", JOptionPane.ERROR_MESSAGE);
						
						else 
						{
							if(!activTriNlecture.isSelected())
							{
								// Si pas de mots cles, pas d'auteur mais une date et pas de tri
								requete = "select distinct titre from wiki.contenu where to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() +
										"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
								select(requete);
								interfaceAffic.miseAJour();
								System.err.println("Si pas de mots cles, pas d'auteur mais une date et pas de tri");
							}
							else
							{
								//TRIIIIIIIII
								// Si pas de mots cles, pas d'auteur mais date et tri
								System.err.println("Si pas de mots cles, pas d'auteur mais date et tri");
							}
						}
					}
			        	
			        else // Si pas de mots cles mais un auteur
			        {
			        	if(activDate.isSelected()) 
			        	{
			        		if(!activTriNlecture.isSelected())
							{
			        			// pas de mots-cles mais auteur et date et pas de tri
				        		requete = "select distinct titre from wiki.contenu where auteur like '%" + rechAuteur.toUpperCase() + 
				     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
				     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
						        select(requete);
						        interfaceAffic.miseAJour();
						        System.err.println("pas de mots-cles mais auteur et date et pas de tri");
							}
			        		else
			        		{
			        			// pas de mots-cles mais auteur et date et tri"
			        			System.err.println("pas de mots-cles mais auteur et date et tri");
			        		}
			        	}
			        	else 
			        	{
			        		if(!activTriNlecture.isSelected())
							{
			        			// pas de mots-cle ni de date mais un auteur et pas de tri
					        	requete = "select distinct titre from wiki.sujet where createur like '%" + rechAuteur.toUpperCase() + "%'" ;
						        select(requete);
					        	interfaceAffic.miseAJour();
					        	System.err.println("pas de mots-cle ni de date mais un auteur et pas de tri");
							}
			        		else
			        		{
			        			//pas de mots-cle ni de date mais un auteur et tri
			        			System.err.println("pas de mots-cle ni de date mais un auteur et tri");
			        		}
			        	}
			        }
			    }
			    else // Si mots cles
			    {
			     	if(rechAuteur.equals("")) // Si uniquement mots cles
			     	{
			     		if(!activDate.isSelected()) 
			        	{
			     			if(!activTriNlecture.isSelected())
							{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
					     			requete = "select distinct titre from wiki.sujet where lower(titre) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans sujet uniquement sans date ni tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans corps uniquement sans date ni tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' or lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans les deux sans date ni tri");
					     		}
							}
			     			else
			     			{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
					     			requete = "select distinct titre from wiki.sujet where lower(titre) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans sujet uniquement sans date mais avec tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans corps uniquement sans date mais avec tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' or lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans les deux sans date mais avec tri");
					     		}
			     			}	
			        	}
			     		else
			     		{
			     			if(!activTriNlecture.isSelected())
							{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
					     			requete = "select distinct titre from wiki.sujet where lower(titre) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans sujet uniquement avec date pas de tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans corps uniquement avec date pas de tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' or lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans les deux avec date pas de tri");
					     		}
							}
			     			else
			     			{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
					     			requete = "select distinct titre from wiki.sujet where lower(titre) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans sujet uniquement avec date et tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans corps uniquement avec date et tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' or lower(texte) like '%" + rechPattern.toLowerCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle sans auteur dans les deux avec date et tri");
					     		}
			     			}
			     		}
			     	}
			     	else // Si mots cles et auteur
			     	{
			     		if(!activDate.isSelected()) // Si mots clés et auteur mais pas date
			     		{
			     			if(!activTriNlecture.isSelected())
							{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
			     					requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le sujet uniquement sans date ni tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le corps uniquement sans date ni tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans les deux sans date ni tri");
					     		}
							}
			     			else
			     			{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
			     					requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le sujet uniquement sans date mais avec tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le corps uniquement sans date mais avec tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + "%' and auteur like '%" + rechAuteur.toUpperCase() + "%'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans les deux sans date mais avec tri");
					     		}
			     			}
			     		}
			     		else // Si mots clés, auteur et date
			     		{
			     			if(!activTriNlecture.isSelected())
							{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
			     					requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le sujet uniquement avec date mais sans tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le corps uniquement avec date mais sans tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans les deux avec date mais sans tri");
					     		}
							}
			     			else
			     			{
			     				if(choixCible.getSelectedIndex()==0) // Dans le sujet uniquement
					     		{
			     					requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le sujet uniquement avec date mais et tri");
					     		}
					     		if(choixCible.getSelectedIndex()==1) // Dans le corps uniquement
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans le corps uniquement avec date mais et tri");
					     		}
					     		if(choixCible.getSelectedIndex()==2) // Dans les deux
					     		{
					     			requete = "select distinct titre from wiki.contenu where lower(titre) like '%" + rechPattern.toLowerCase() + 
					     					"%' and auteur like '%" + rechAuteur.toUpperCase() + 
					     					"%' and to_char(datemodif, 'DD/MM/YYYY') between '" + choixJour.getSelectedItem() + "/" + choixMois.getSelectedItem() + "/" + choixAnnee.getSelectedItem() + 
					     					"' and '"+ choixJour2.getSelectedItem() + "/" + choixMois2.getSelectedItem() + "/" + choixAnnee2.getSelectedItem() + "'";
							        select(requete);
							        interfaceAffic.miseAJour();
							        System.err.println("mots-cle et auteur dans les deux avec date mais et tri");
					     		}
			     			}
			     		}
			     	}
			    }
			}
			else
				JOptionPane.showMessageDialog(this, "Vous devez etre connecte pour effectuer une recherche");
		} // if bouton recherche
			
		if(parEvt.getSource() == boutonReinitialise)
		{
			reinitialiser();
		}
	}

	
	public void select(String req)
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
            //System.out.println(liste.toString());
            setNbBoutons(liste.size());
            
            setNomsBoutons(liste);
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
        }
    }
	
	public void setNbBoutons(int parNbBoutons)
	{
		nbBoutons = parNbBoutons;
		//System.out.println(nbBoutons +" boutons");
	}
	
	public int getNbBoutons()
	{
		return nbBoutons;
	}
	
	public void setNomsBoutons(ArrayList<String> parNoms)
	{
		nomsBoutons = parNoms;
	}
	
	public ArrayList<String> getNomsBoutons()
	{
		return nomsBoutons;
	}
	
	public void reinitialiser()
	{
		System.out.println("interfaceRecherche => reinitialiser");
		champPattern.setText(null);
		champAuteur.setText(null);
		choixCible.setSelectedIndex(0);
		choixJour.setSelectedIndex(0);
		choixMois.setSelectedIndex(0);
		choixAnnee.setSelectedIndex(0);
		if(activDate.isSelected() == true)
			activDate.doClick();
	}
	
} // class InterfaceRecherche

