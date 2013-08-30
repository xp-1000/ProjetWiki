import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FenetreLecture extends JFrame implements ActionListener
{
	JButton boutonHistorique = new JButton("Historique");
	JButton boutonFermer = new JButton("Fermer");
	JLabel labelTitre = new JLabel("Titre : ");
	JLabel labelCorps = new JLabel("Contenu :    ");
	JLabel labelSujetsLies = new JLabel("Liste des sujets liés : ");
	JLabel labelNbLecture = new JLabel("Nombre de lecture : ");
	JLabel labelAuteur;
	JLabel labelDate;
	JLabel [] listeTitres;
	JTextField champTitre = new JTextField();
	JTextField champCorps = new JTextField();
	JTextField champCorpsHisto = new JTextField();
	JComboBox listeSujetsL = new JComboBox();
	JComboBox listeSujetsH = new JComboBox();
	
	String titre, corps, auteur, dateModif, requete, requeteLect, req, histoCorps, nbLecture;
	String [] histoAuteur, histoDateModif;
	DefaultListModel listModel = new DefaultListModel();
	JList listeSujetsLies = new JList(listModel);
	int selectionUnique = 0;
	boolean enCours = false, histoEstSelectionne = false, historique = false;
	
	JPanel panel = new JPanel();
	JPanel panelHistorique = new JPanel();
	InterfaceDebut interfacePrincipale;
	
	public FenetreLecture(String parTitre, InterfaceDebut parInterfacePrincipale)
	{
		super(parTitre);
		
		titre = parTitre;
		interfacePrincipale = parInterfacePrincipale;
		
		// Si l'utilisateur n'a pas deja lu le message, on ajoute une nouvelle lecture avec la cle primaire (titre, lecteur)
		/*boolean aLu = aDejaLu("select * from wiki.lecture where titre like '" + titre + "' and lecteur like '" + interfacePrincipale.getIdentifiant().toUpperCase() + "'");
		if (aLu)
		{
			System.err.println("A DEJA LU");*/
			requeteLect = "insert into wiki.nouvellelecture values('" + titre + "')";
			insert(requeteLect);/*
		}
		else
			System.err.println("JAMAIS LU");
		*/
		requeteLect = "select count(*) from wiki.lecture where titre like '" + titre + "'";
		select5(requeteLect);
		
		// Trouver le corps du message
		requete = "select * from(select to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),auteur,texte from wiki.contenu where titre like '" + titre + "' order by datemodif desc) where rownum=1";
        select(requete);
        requete = "select titre2 from wiki.liens where titre1 like '" + titre + "'";
        select2(requete);
        //corps = corps.substring(1, corps.length()-1);
        System.out.println("Titre : " + titre + " | corps : " + corps + " | auteur : " + auteur + " | date : " + dateModif);
        
        this.setContentPane(panel);
        panel.setLayout(new GridBagLayout());
        panelHistorique.setLayout(new GridBagLayout());
		ajouteComposant(labelTitre,0,0,1,1,false);
		champTitre.setText(titre);
		champTitre.setEditable(false);
		ajouteComposant(champTitre,0,1,2,1,false);
		labelNbLecture.setText("Nombre de lecture : " + nbLecture);
		ajouteComposant(labelCorps,0,3,1,1,false);
		champCorps.setText(corps);
		champCorps.setEditable(false);
		ajouteComposant(champCorps,0,4,2,1,false);
		labelAuteur = new JLabel("Modifié par : " + auteur);
		ajouteComposant(labelAuteur,0,6,2,1,false);
		labelDate = new JLabel("Le : " + dateModif);
		ajouteComposant(labelDate,1,6,2,1,false);
		ajouteComposant(labelNbLecture,0,7,1,1,false);
		ajouteComposant(labelSujetsLies,0,8,2,1,false);
		ajouteComposant(listeSujetsL,1,8,2,1,false);
		listeSujetsL.addActionListener(this);
		listeSujetsH.addActionListener(this);
		ajouteComposant(boutonHistorique,0,10,1,1,false);
		ajouteComposant(boutonFermer,1,10,1,1,false);
		
		boutonFermer.addActionListener(this);
		boutonHistorique.addActionListener(this);
		
		/*listeSujetsLies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//listeSujetsLies.addListSelectionListener(new ListSelectionListener()
		listeSujetsLies.getSelectionModel().addListSelectionListener(new ListSelectionListener()        {
			public void valueChanged(ListSelectionEvent arg0) 
			{
				//if(selectionUnique%2!=0 && enCours == false)
				if(!arg0.getValueIsAdjusting())
				{
					System.err.println("DEBUT : " + titre);
					enCours = true;
					//ATTENTION A MODIFIER !!!!
					titre = (String) listeSujetsLies.getSelectedValue();
					System.err.println("ENTRE1 : " + titre);
					requete = "select * from(select to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),auteur,texte from wiki.contenu where titre like '" + titre + "' order by datemodif desc) where rownum=1";
			        select(requete);
			        champTitre.setText(titre);
			        champCorps.setText(corps);
			        labelAuteur.setText(dateModif);
			        labelDate.setText(dateModif);
			        listeSujetsLies.removeAll();
					listModel.removeAllElements();
					System.err.println("ENTRE2 : " + titre);
					requete = "select titre2 from wiki.liens where titre1 like '" + titre + "'";
			        select2(requete);
			        panel.updateUI();
			        enCours = false;
			        System.err.println("FIN : " + titre);
			      //  listeSujetsLies.setSelectedIndex(0);
			        //System.err.println("test : " + listeSujetsLies.getSelectedValue());
				}
				selectionUnique++;
				System.err.println("nbcliques : " + selectionUnique);
			}
		});*/
		
		setVisible(true);
		pack();
		setSize(getSize().width + 50,getSize().height + 50);
		setLocationRelativeTo(interfacePrincipale);
	}
	
	private void ajouteComposant(Component parComposant,
			int parColonne, int parLigne, int parLargeur, int parHauteur, boolean panelHisto)
	{
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx=parColonne;
		contraintes.gridy=parLigne;
		contraintes.gridwidth=parLargeur;
		contraintes.gridheight=parHauteur;
		contraintes.weightx=1;
		contraintes.fill=GridBagConstraints.BOTH;
		contraintes.insets = new Insets(10,10,0,0);
		if(panelHisto == true)
			panelHistorique.add(parComposant, contraintes);
		else
			panel.add(parComposant, contraintes);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == boutonFermer)
			this.dispose();
		
		if(e.getSource() == boutonHistorique)
		{
			if(!histoEstSelectionne)
			{
				System.out.println("DANS HISTORIQUE");
				if(!historique)
				{
					System.out.println("REQ HISTORIQUE");
					requete = "select to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),auteur from wiki.contenu where titre like '" + titre + "' order by datemodif desc";
			        select3(requete);
				}
				for(int i=0; i<histoAuteur.length; i++)
				{
					listeSujetsH.addItem("Modifié le : " + histoDateModif[i] + " par : " + histoAuteur[i]);
				}
				ajouteComposant(listeSujetsH, 0, 0, 1, 1, true);
				ajouteComposant(champCorpsHisto, 0, 1, 1, 1, true);
				ajouteComposant(panelHistorique,0,12,2,2,false);
				panelHistorique.updateUI();
				panel.updateUI();
				histoEstSelectionne = true;
			}
			else
			{	
				remove(panelHistorique);
				panel.updateUI();
				histoEstSelectionne = false;
			}
			
		}
		
		if(e.getSource() == listeSujetsL)
		{
			titre = (String) listeSujetsL.getItemAt(listeSujetsL.getSelectedIndex());
			requete = "select * from(select to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),auteur,texte from wiki.contenu where titre like '" + titre + "' order by datemodif desc) where rownum=1";
	        select(requete);
	        requeteLect = "select count(*) from wiki.lecture where titre like '" + titre + "'";
			select5(requeteLect);
	        champTitre.setText(titre);
	        champCorps.setText(corps);
	        labelAuteur.setText(auteur);
	        labelDate.setText(dateModif);
	        labelNbLecture.setText("Nombre de lecture : " + nbLecture);
	        listeSujetsL.removeAllItems();
			requete = "select titre2 from wiki.liens where titre1 like '" + titre + "'";
	        select2(requete);
	        panel.updateUI();
		}
		
		if(e.getSource() == listeSujetsH)
		{
			int index = listeSujetsH.getSelectedIndex();
			requete = "select texte from wiki.contenu where titre like '" + titre + "' and auteur like '" + histoAuteur[index] + "' and to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss') like '" + histoDateModif[index] +"'";
			select4(requete);
			
	        champCorpsHisto.setText(histoCorps);
		}
		
	}
	
	public void insert(String req)
	{
		try {
			interfacePrincipale.statement.executeQuery (req);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public String select(String req)
    {
		//System.out.println(req);
        try
        {
            //ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);

            resultat.next();	
        	dateModif = resultat.getString(1);
        	auteur = resultat.getString(2);
        	corps = resultat.getString(3);

            return "Pas de probleme";
        }
        catch(SQLException e){
            System.err.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
	
	public String select5(String req)
    {
		//System.out.println(req);
        try
        {
            //ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);

            resultat.next();	
        	nbLecture = resultat.getString(1);

            return "Pas de probleme";
        }
        catch(SQLException e){
            System.err.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
	
	public boolean aDejaLu(String req)
    {
		//System.out.println(req);
		boolean res = false;
        try
        {
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);

            //resultat.next();	
        	//nbLecture = resultat.getString(1);
            
            res = resultat.first();
        }
        catch(SQLException e){
            System.err.println("Une exception est levee :" + e);
        }
        return res;
    }
	
	public String select4(String req)
    {
		//System.out.println(req);
        try
        {
            //ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);

            resultat.next();	
            histoCorps = resultat.getString(1);

            return "Pas de probleme";
        }
        catch(SQLException e){
            System.err.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
	
        public String select2(String req)
        {
    		System.err.println(req);
            try
            {
                ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
                
                while (resultat.next())
                {
                	System.err.println(" resultat : " + resultat.getString(1));
                	listeSujetsL.addItem(resultat.getString(1));
                	//listModel.addElement(resultat.getString(1));
                }
                
                return "Pas de probleme";
            }
            catch(SQLException e){
                System.out.println("Une exception est levee :" + e);
                return "erreur";
            }
        }
        
        public String select3(String req)
        {
    		//System.out.println(req);
            try
            {
            	ArrayList<String> listeDates = new ArrayList<String>();
            	ArrayList<String> listeAuteurs = new ArrayList<String>();
                ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
                
                while (resultat.next())
                {
                	listeDates.add(resultat.getString(1));
                	listeAuteurs.add(resultat.getString(2));
                }
                
                histoDateModif = new String[listeDates.size()];
            	histoAuteur = new String[listeAuteurs.size()];
            	System.out.println("TAILLE : " + listeAuteurs.size() );
                
                Iterator iter1 =  listeDates.iterator();
                int index = 0;
				while (iter1.hasNext()) 
				{
					histoDateModif[index] = (String) iter1.next();
					index++;
				}
				index = 0;
				Iterator iter2 =  listeAuteurs.iterator();
				while (iter2.hasNext()) 
				{
	            	histoAuteur[index] = (String) iter2.next();
	            	index++;
				}
				
				for(int i = 0 ; i > histoAuteur.length ; i++)
					System.out.println("YAHOO : " + histoDateModif[i] + " " + histoAuteur[i] );
                
                return "Pas de probleme";
            }
            catch(SQLException e){
                System.err.println("Une exception est levee :" + e);
                return "erreur";
            }
        }
        
        /*public String select3(String req)
        {
    		//System.out.println(req);
            try
            {
                ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
                int i = 0;
                while (resultat.next())
                {
                	i++;
	            	//histoDateModif = resultat.getString(1);
	            	//histoAuteur = resultat.getString(2);
	            	//histoCorps = resultat.getString(3);
                }
                
                select3(req,i);
                
                return "Pas de probleme";
            }
            catch(SQLException e){
                System.err.println("Une exception est levee :" + e);
                return "erreur";
            }
        }
        
        public String select3(String req, int nb)
        {
        	histoDateModif = new String[nb];
        	histoAuteur = new String[nb];

        	System.out.println(req);
            try
            {
                ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
                int i = 0;
                while (resultat.next())
                {
                	System.out.println(resultat.getString(1));
	            	histoDateModif[i] = resultat.getString(1);
	            	histoAuteur[i] = resultat.getString(2);
	            	i++;
                }

                return "Pas de probleme";
            }
            catch(SQLException e){
                System.err.println("Une exception est levee :" + e);
                return "erreur";
            }
        }
	*/
}
