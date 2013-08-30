import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class FenetreMessages extends JFrame implements ActionListener
{
	InterfaceDebut interfacePrincipale;
	
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList listeSujets = new JList(listModel);
	JButton boutonValider = new JButton("Valider");
	JButton boutonrefuser = new JButton("Refuser");
	JLabel labelDemandeur = new JLabel("Demandeur : ");
	JLabel labelDate = new JLabel("Date : ");
	JLabel labelContenuActuel = new JLabel("Contenu actuel : ");
	JLabel labelContenuModifie = new JLabel("Contenu modifié : ");
	JLabel valeurDemandeur = new JLabel("");
	JLabel valeurDate = new JLabel("");
	JTextArea champsContenuModifie = new JTextArea();
	JTextArea champscontenuActuel = new JTextArea();
	
	String[] listeDemandeurs;
	String[] listeDates;
	String titre, demandeur, dateModif, contenuActuel, contenuModifie;
	int selectionUnique = 0;
	boolean requeteEnCours = false;
	
	public FenetreMessages(InterfaceDebut parInterfacePrincipale)
	{
		super("Validation des messages en attente");
		interfacePrincipale = parInterfacePrincipale;
		this.setLayout(new GridBagLayout());
		
        select2("select titre,to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),demandeur from wiki.liredemande");

        listeSujets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeSujets.addListSelectionListener(new ListSelectionListener()
        {
			public void valueChanged(ListSelectionEvent arg0) 
			{
				if(requeteEnCours == false)
				{
					if(selectionUnique%2!=0)
					{
						System.err.println("index selectionne actuellement : " + listeSujets.getSelectedIndex());
						titre = (String) listeSujets.getSelectedValue();
						demandeur = listeDemandeurs[listeSujets.getSelectedIndex()];
						dateModif = listeDates[listeSujets.getSelectedIndex()];
						contenuActuel = select("select * from(select texte from wiki.contenu where titre like '" + titre + "' order by datemodif desc) where rownum=1");
						contenuModifie = select("select texte from wiki.liredemande where titre like '" + titre
									+ "' and demandeur like '" + demandeur + "' and to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss')='" + dateModif + "'");
						valeurDemandeur.setText(demandeur);
						valeurDate.setText(dateModif);
						champscontenuActuel.setText(contenuActuel.substring(1, contenuActuel.length()-1));
						champsContenuModifie.setText(contenuModifie.substring(1, contenuModifie.length()-1));
					}
					selectionUnique++;
				}
			}
		});
        ajouteComposant(new JScrollPane(listeSujets),0,0,1,4,false);
        ajouteComposant(labelDemandeur,1,0,1,1,false);
        ajouteComposant(valeurDemandeur,1,1,1,1,false);
        ajouteComposant(labelDate,1,2,1,1,false);
        ajouteComposant(valeurDate,1,3,1,1,false);
        ajouteComposant(labelContenuActuel,0,4,1,1,false);
        ajouteComposant(labelContenuModifie,1,4,1,1,false);
        ajouteComposant(champscontenuActuel,0,5,1,1,false);
        ajouteComposant(champsContenuModifie,1,5,1,1,false);
        boutonValider.addActionListener(this);
        boutonrefuser.addActionListener(this);
        ajouteComposant(boutonValider,0,6,1,1,false);
        ajouteComposant(boutonrefuser,1,6,1,1,false);
        
        for(int i = 0 ; i < listeDates.length ; i++)
        System.out.println(listeDates[i] + "     " + listeDemandeurs[i]);
        
		setVisible(true);
		pack();
		setSize(getSize().width + 50,getSize().height + 50);
		setLocationRelativeTo(interfacePrincipale);
	}

	public void actionPerformed(ActionEvent evt) 
	{
		if(evt.getSource() == boutonValider)
		{
			//insert("update wiki.modificationcontenu set texte='" + contenuModifie.substring(1, contenuModifie.length()-1) + "' where titre like '" 
			//		+ titre + "' and texte like '" + contenuActuel.substring(1, contenuActuel.length()-1) + "'");
			insert("insert into wiki.modificationcontenu values ('" + titre + "','" + contenuModifie.substring(1, contenuModifie.length()-1) + "')");
			insert("delete from wiki.liredemande where titre like '" + titre + "' and demandeur like '" + demandeur 
					+ "' and to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss')='" + dateModif + "'");
			interfacePrincipale.verifMessages();
			listeSujets.removeAll();
			listModel.removeAllElements();
			select2("select titre,to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),demandeur from wiki.liredemande");
			listeSujets.setSelectedIndex(0);
			if(listModel.getSize() == 0)
				this.dispose();
		}
		
		if(evt.getSource() == boutonrefuser)
		{
			insert("delete from wiki.liredemande where titre like '" + titre + "' and demandeur like '" + demandeur 
					+ "' and to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss')='" + dateModif + "'");
			interfacePrincipale.verifMessages();
			listeSujets.removeAll();
			listModel.removeAllElements();
			select2("select titre,to_char(datemodif, 'DD/MM/YYYY hh24:mi:ss'),demandeur from wiki.liredemande");
			listeSujets.setSelectedIndex(0);
			if(listModel.getSize() == 0)
				this.dispose();
		}
	}
	
	private void ajouteComposant(Component parComposant,
			int parColonne, int parLigne, int parLargeur, int parHauteur, boolean relative)
	{
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx=parColonne;
		contraintes.gridy=parLigne;
		contraintes.gridwidth=parLargeur;
		contraintes.gridheight=parHauteur;
		contraintes.weightx=1;
		if(relative == true)
			contraintes.fill=GridBagConstraints.RELATIVE;
		else
			contraintes.fill=GridBagConstraints.BOTH;
		contraintes.insets = new Insets(10,10,0,0);
		this.add(parComposant, contraintes);
	}
	
	public void insert(String req)
	{
		System.err.println(req);
		try {
			interfacePrincipale.statement.executeQuery (req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public void select2(String req)
    {
		requeteEnCours = true;
		listeDemandeurs = new String[interfacePrincipale.getNbMessages()];
		listeDates = new String[interfacePrincipale.getNbMessages()];
        try
        {
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
            int i = -1;
            while (resultat.next())
            {
            	System.err.println("incrementation de i ");
            	i++;
            	System.err.println("Ajout date ");
            	listeDates[i] = resultat.getString(2);
            	System.err.println("Ajout demandeur ");
            	listeDemandeurs[i] = resultat.getString(3);
            	System.err.println("Ajout sujet a la jlist ");
            	System.err.println(resultat.getString(1));
            	listModel.addElement(resultat.getString(1));
            }
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
        }
        System.err.println("index selectionne apres select2: " + listeSujets.getSelectedIndex());
        requeteEnCours = false;
    }

}
