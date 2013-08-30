import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class InterfaceCreationSujet extends JPanel implements ActionListener
{
	JButton boutonCreer = new JButton("Creer sujet");
	JButton boutonReinitialise = new JButton("Reinitialiser");
	JButton boutonLier = new JButton("Lier au(x) message(s)");
	JLabel labelTitre = new JLabel("Titre du sujet : ");
	JLabel labelCorps = new JLabel("Corps du message : ");
	JTextField champTitre = new JTextField();
	JTextField champCorps = new JTextField();
	JCheckBox activProtege = new JCheckBox("Sujet protege ? ");
	boolean estLie = false;
	java.util.List listeSujetsALier;
	
	InterfaceDebut interfacePrincipale;
	
	public InterfaceCreationSujet(InterfaceDebut parInterfacePrincipale)
	{
		super();
		interfacePrincipale = parInterfacePrincipale;
		
		this.setLayout(new GridBagLayout());
		ajouteComposant(labelTitre,0,0,1,1,false);
		champTitre.setPreferredSize(new Dimension(500,25));
		ajouteComposant(champTitre,0,1,2,1,false);
		ajouteComposant(labelCorps,0,2,1,1,false);
		champCorps.setPreferredSize(new Dimension(500,400));
		ajouteComposant(champCorps,0,3,2,1,false);
		ajouteComposant(activProtege,0,4,1,1, false);
		ajouteComposant(boutonLier,1,4,1,1,false);
		ajouteComposant(boutonCreer,0,5,1,1,false);
		ajouteComposant(boutonReinitialise,1,5,1,1,false);
		boutonReinitialise.addActionListener(this);
		boutonCreer.addActionListener(this);
		boutonLier.addActionListener(this);
		
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
	
	public void actionPerformed(ActionEvent parEvt) 
	{
		int protege ;
		
		if(parEvt.getSource() == boutonReinitialise)
		{
			reinitialiser();
		}
		
		if(parEvt.getSource() == boutonLier)
		{
			FenetreLier fenetreLier = new FenetreLier(interfacePrincipale,this);
		}
		
		if(parEvt.getSource() == boutonCreer)
		{
			if(activProtege.isSelected()) protege = 1; // Si le sujet est protege
			else protege = 0;
			
			String requete1 = "insert into wiki.nouveausujet values ('" + champTitre.getText() + "'," + protege + ")" ;
			String requete2 = "insert into wiki.modificationcontenu values ('" + champTitre.getText() + "','" + champCorps.getText() + "')";
			String verif = insert(requete1);
			
			if(verif.equals("1"))
			{
				insert(requete2);
				
				if(estLie)
				{
					Iterator iter =  listeSujetsALier.iterator();
					while (iter.hasNext()) 
					{
						String nom = (String) iter.next();
						String req = "insert into wiki.liens values ('" + champTitre.getText() + "','" + nom + "')";
						insert(req);
					}
				}
				
				JOptionPane.showMessageDialog(this, "Votre message a ete ajoute avec succes.",
	                    "Confirmation d'ajout", JOptionPane.DEFAULT_OPTION);
				
				interfacePrincipale.gestionnaireDesPages.show(interfacePrincipale.panelContenu, interfacePrincipale.titreAffichage);
			}
			else
				JOptionPane.showMessageDialog(this, "Ce titre existe déjà, utilisez un autre titre.","Erreur de creation", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String insert(String req)
	{
		try {
			interfacePrincipale.statement.executeQuery (req);
			return "1";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
	}
	
	public void reinitialiser()
	{
		champTitre.setText(null);
		champCorps.setText(null);
		if(activProtege.isSelected() == true)
			activProtege.doClick();
	}
	
	public void liaison(java.util.List parListeSujetsALier)
	{
		estLie = true;
		listeSujetsALier = parListeSujetsALier;
	}
	
	
}// class