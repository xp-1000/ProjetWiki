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

import javax.swing.*;

public class FenetreModif extends JFrame implements ActionListener
{
	JButton boutonEnregistrer = new JButton("Enregistrer");
	JButton boutonAnnuler = new JButton("Annuler");
	JLabel labelTitre = new JLabel("Titre : ");
	JLabel labelCorps = new JLabel("Contenu :    ");
	JTextField champTitre = new JTextField();
	JTextField champCorps = new JTextField();
	
	String titre, corps, createur, protege;
	String requeteCorps, requeteCreateur, requeteProtege;
	InterfaceDebut interfacePrincipale;
	
	public FenetreModif(String parTitre, InterfaceDebut parInterfacePrincipale)
	{
		super(parTitre);
		
		titre = parTitre;
		interfacePrincipale = parInterfacePrincipale;
		
		// Trouver le corps du message
		requeteCorps = "select * from(select texte from wiki.contenu where titre like '" + titre + "' order by datemodif desc) where rownum=1";
        corps = select(requeteCorps);
        corps = corps.substring(1, corps.length()-1);
        System.out.println(corps);
        
        // Trouver le createur
        requeteCreateur = "select createur from wiki.sujet where titre like '" + titre + "'";
        createur = select(requeteCreateur);
        createur = createur.substring(1, createur.length()-1);
        System.out.println(createur);
        
        // Trouver le protege
        requeteProtege = "select protege from wiki.sujet where titre like '" + titre + "'";
        protege = select(requeteProtege);
        protege = protege.substring(1, protege.length()-1);
        System.out.println(protege);
        
		this.setLayout(new GridBagLayout());
		ajouteComposant(labelTitre,0,0,1,1,false);
		champTitre.setText(titre);
		champTitre.setEditable(false);
		ajouteComposant(champTitre,0,1,2,1,false);
		
		ajouteComposant(labelCorps,0,2,1,1,false);
		champCorps.setText(corps);
		ajouteComposant(champCorps,0,3,2,1,false);
		
		ajouteComposant(boutonEnregistrer,0,5,1,1,false);
		ajouteComposant(boutonAnnuler,1,5,1,1,false);
		
		boutonAnnuler.addActionListener(this);
		boutonEnregistrer.addActionListener(this);
		
		setVisible(true);
		pack();
		setSize(getSize().width + 50,getSize().height + 50);
		setLocationRelativeTo(interfacePrincipale);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== boutonAnnuler)
		{
			this.dispose();
		}
		
		if(e.getSource()== boutonEnregistrer)
		{
			//System.out.println("protege = " + protege + " resultat du test protege : "+ protege.equals("1") + " resultat du test createur : " + createur.equalsIgnoreCase(interfacePrincipale.getIdentifiant()));
			//System.out.println((protege.equals("1") && createur.equalsIgnoreCase(interfacePrincipale.getIdentifiant())) || protege.equals("0"));
			if((protege.equals("1") && createur.equalsIgnoreCase(interfacePrincipale.getIdentifiant())) || protege.equals("0")) // et l'utilisateur est le createur
			{
				//System.out.println("message modifié");
				//System.out.println("insert into wiki.modificationcontenu values ('" + champTitre.getText() + "','" + champCorps.getText() + "')");
				String requeteModif = "insert into wiki.modificationcontenu values ('" + titre + "','" + champCorps.getText() + "')";
				insert(requeteModif);
				JOptionPane.showMessageDialog(this, "Le message a ete modifié instantanément.",
	                    "Confirmation d'ajout", JOptionPane.DEFAULT_OPTION);
				this.dispose();
			}
			else
			{
				String requeteModif = "insert into wiki.ecriredemande values ('" + champTitre.getText() + "','" + champCorps.getText() + "')";
				insert(requeteModif);
				JOptionPane.showMessageDialog(this, "Le message est en cours de validation par le créateur.",
	                    "Confirmation d'ajout", JOptionPane.DEFAULT_OPTION);
				this.dispose();
			}
			
			
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
	
	public String select(String req)
    {
		System.out.println(req);
        try
        {
            ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
            
            while (resultat.next())
            {
            	System.out.println(resultat.getString(1));
            	liste.add(resultat.getString(1));
            }
            return liste.toString();
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
	
}
