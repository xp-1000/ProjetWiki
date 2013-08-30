import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class InterfaceGestion extends JPanel implements ActionListener
{
	//static boolean estConnecte = false;
	static int estConnecte = 0;
	
	
	InterfaceDebut interfacePrincipale;
	JPanel panelConnexion = new JPanel();
	JPanel panelInfos = new JPanel();
	FenetreMessages fenetreMessages;
	
	Icon icon = new ImageIcon("message.png");
	JButton boutonConnexion = new JButton("Se connecter");
	JButton boutonNouvSuj = new JButton("Nouveau sujet");
	JButton boutonNouvRech = new JButton("Nouvelle recherche");
	JButton boutonMessages = new JButton("0",icon);
	JButton boutonRaffraichir = new JButton("Raffraichir");
	JTextField champIdentifiant = new JTextField(8);
	JPasswordField champMotDePasse = new JPasswordField(8);
	JLabel labelIdentifiant = new JLabel("Identifiant : ");
	JLabel labelMotDePasse = new JLabel("Mot de Passe : ");
	JLabel labelStatus = new JLabel("Statut : ");
	JLabel labelEtat = new JLabel("Deconnecte(e)");
	JLabel labelMessages = new JLabel("Messages ");
	
	int nbClics=0;
	
	public InterfaceGestion(InterfaceDebut parInterfacePrincipale)
	{
		super();
		
		interfacePrincipale = parInterfacePrincipale;
		this.setLayout(new GridLayout(2,1,5,5));
		panelConnexion.setLayout(new GridBagLayout());
		panelInfos.setLayout(new FlowLayout());
		
		boutonNouvSuj.setEnabled(false);
		boutonNouvRech.setEnabled(false);
		boutonMessages.setEnabled(false);
		boutonRaffraichir.setEnabled(false);
		
		// Les composants graphiques 
		this.ajouteComposant(labelIdentifiant,0,0,1,1);
		this.ajouteComposant(champIdentifiant,1,0,1,1);
		this.ajouteComposant(labelMotDePasse,0,1,1,1);
		this.ajouteComposant(champMotDePasse,1,1,1,1);
		this.ajouteComposant(boutonConnexion,0,3,2,1);
		this.ajouteComposant(labelStatus,0,2,2,1);
		labelEtat.setForeground(new Color(153,0,0)); //rouge
		this.ajouteComposant(labelEtat,1,2,2,1);
		this.ajouteComposant(boutonNouvSuj,0,4,2,1);
		this.ajouteComposant(boutonNouvRech,0,5,2,1);
		//this.ajouteComposant(labelMessages,0,6,1,1);
		this.ajouteComposant(boutonMessages,0,6,1,1);
		this.ajouteComposant(boutonRaffraichir,1,6,1,1);
		this.add(panelConnexion);
		this.add(panelInfos);
		
		// Mise a l'ecoute
		boutonConnexion.addActionListener(this);
		boutonNouvSuj.addActionListener(this);
		boutonNouvRech.addActionListener(this);
		boutonMessages.addActionListener(this);
		boutonRaffraichir.addActionListener(this);
		
	}
	
	private void ajouteComposant(Component parComposant,
			int parColonne, int parLigne, int parLargeur, int parHauteur)
	{
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx=parColonne;
		contraintes.gridy=parLigne;
		contraintes.gridwidth=parLargeur;
		contraintes.gridheight=parHauteur;
		contraintes.weightx=1;
		contraintes.fill=GridBagConstraints.BOTH;
		contraintes.insets = new Insets(20,20,0,0);
		panelConnexion.add(parComposant, contraintes);
	}
	
	public void actionPerformed(ActionEvent parEvt) 
	{
		if(parEvt.getSource() == boutonConnexion)
		{
			System.out.println("NBMESS : " +  interfacePrincipale.getNbMessages());
			nbClics++;
			
			if(nbClics%2!=0)
			{
				
				if(interfacePrincipale.connexion(champIdentifiant.getText(), new String(champMotDePasse.getPassword())) == 1)
				{
					labelEtat.setText("Connecte(e)");
					labelEtat.setForeground(new Color(0,153,0)); //vert
					boutonConnexion.setText("Se deconnecter");
					champIdentifiant.setEditable(false);
					champMotDePasse.setEditable(false);
					
					boutonNouvSuj.setEnabled(true);
					boutonNouvRech.setEnabled(true);
					boutonMessages.setEnabled(true);
					boutonRaffraichir.setEnabled(true);
				}
				else
					nbClics++;
			}
			else
			{
				interfacePrincipale.deconnexion();
				champIdentifiant.setText("");
				champMotDePasse.setText("");
				labelEtat.setText("Deconnecte(e)");
				labelEtat.setForeground(new Color(153,0,0)); //rouge
				boutonConnexion.setText("Se connecter");
				champIdentifiant.setEditable(true);
				champMotDePasse.setEditable(true);
				
				boutonNouvSuj.setEnabled(false);
				boutonNouvRech.setEnabled(false);
				boutonMessages.setEnabled(false);
				boutonRaffraichir.setEnabled(false);
			}
			
			
			/*
			estConnecte = interfacePrincipale.connexion(champIdentifiant.getText(), new String(champMotDePasse.getPassword()));
			
			if(estConnecte == 1)
			{	
				labelEtat.setText("Connecte(e)");
				labelEtat.setForeground(new Color(0,153,0)); //vert
				boutonConnexion.setText("Se deconnecter");
				
				boutonNouvSuj.setEnabled(true);
				boutonNouvRech.setEnabled(true);
			}
			
			else
			{
				labelEtat.setText("Deconnecte(e)");
				labelEtat.setForeground(new Color(153,0,0)); //rouge
				boutonConnexion.setText("Se connecter");
				
				//boutonNouvSuj.setEnabled(false);
				//boutonNouvRech.setEnabled(false);
			}
				*/
		}
		
		if(parEvt.getSource() == boutonNouvSuj)
		{
			interfacePrincipale.reinitialiserCreationSujet();
			interfacePrincipale.afficherCreatSuj();
		}
		
		if(parEvt.getSource() == boutonNouvRech)
		{
			interfacePrincipale.reinitialiserAffichage();
			interfacePrincipale.afficherRecherche();
		}
		
		if(parEvt.getSource() == boutonMessages)
		{
			if(interfacePrincipale.getNbMessages() > 0)
				fenetreMessages = new FenetreMessages(interfacePrincipale);
			else
				JOptionPane.showMessageDialog(this, "Vous n'avez pas de message en attente de validation");
		}
		
		if(parEvt.getSource() == boutonRaffraichir)
		{
			interfacePrincipale.verifMessages();
		}
	}
	
	public void setNbMessages(int nbMess)
	{
		boutonMessages.setText(String.valueOf(nbMess));
	}

}