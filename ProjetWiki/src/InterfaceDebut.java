import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;


public class InterfaceDebut extends JFrame
{
	boolean estConnecte = false;
	int nbMessages = 0;
	JPanel conteneurPere = new JPanel();
	JPanel panelContenu = new JPanel();
	CardLayout gestionnaireDesPages = new CardLayout();
	InterfaceAffichage panelAffichage = new InterfaceAffichage(this);
	InterfaceGestion panelGestion = new InterfaceGestion(this);
	InterfaceCreationSujet panelCreatSuj = new InterfaceCreationSujet(this);
	String titreCreatSuj= "Creation de sujet";
	String titreAffichage = "Liste des sujets";
	String identifiant, motDePasse;
	
    public Connection laConnexion;
    public Statement statement;

	public InterfaceDebut (String parTitre)
	{
		super(parTitre);
		
		setContentPane(conteneurPere);
		conteneurPere.setLayout(new BorderLayout(2,2));
		
		panelContenu.setLayout(gestionnaireDesPages);
		panelContenu.add(titreAffichage,panelAffichage);
		panelContenu.add(titreCreatSuj,panelCreatSuj);
		
		// On ajoute le panelContenu au conteneur pere
		conteneurPere.add(panelGestion, "West");
		conteneurPere.add(panelContenu, "Center");
		
		Border cadre = BorderFactory.createTitledBorder("Gestion Wiki");
		panelGestion.setBorder(cadre);
		setVisible(true);
		setSize(1250,680);
		setLocation(15,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


    public void initialisation() throws SQLException
    {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    }
	
    public void verifMessages()
    {
    	String requeteVerifMess = "select count(*) from wiki.liredemande";
    	String resultat = select(requeteVerifMess);
    	resultat = resultat.substring(1, resultat.length()-1);
    	nbMessages = Integer.parseInt(resultat);
    	System.out.println(nbMessages);
    	panelGestion.setNbMessages(nbMessages);
    	
    }
    
	public int connexion(String parIdentifiant, String parMotDePasse) 
	{	
		identifiant = parIdentifiant;
		motDePasse = parMotDePasse;
		
		
		try
        {
            initialisation();
            
           // laConnexion = DriverManager.getConnection("jdbc:oracle:thin:@setna:1521:info", identifiant, motDePasse);
            laConnexion = DriverManager.getConnection("jdbc:oracle:thin:@agonzalez.selfip.net:1521:xe", parIdentifiant, parMotDePasse);
            statement = laConnexion.createStatement ();

            System.out.println("connecte avec : " + identifiant); 
            estConnecte = true;
            verifMessages();
            return 1;
        }
		
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(this, "La connexion a echoue. Veuillez verifier vos identifiants.",
                    "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            
            return 0;
        }
		
	}
	
	public void deconnexion()
	{
		try {
            laConnexion.close();
            System.out.println("deconnecte");
            panelGestion.setNbMessages(0);
            estConnecte = false;
        }
        catch(SQLException e) { System.out.println ("Une exception est levee :" + e); }
	}
	
	public void afficherCreatSuj()
	{
		gestionnaireDesPages.show(panelContenu, titreCreatSuj);
	}
	
	public void afficherRecherche()
	{
		gestionnaireDesPages.show(panelContenu, titreAffichage);
	}
	
	public void reinitialiserAffichage()
	{
		panelAffichage.reinitialiserAffichage();		
	}
	
	public void reinitialiserCreationSujet()
	{
		panelCreatSuj.reinitialiser();		
	}
	
	public String getIdentifiant()
	{
		return identifiant;
	}
	
	public String getMotDePasse()
	{
		return motDePasse;
	}
	
	public boolean getEstConnecte()
	{
		return estConnecte;
	}
	
	public int getNbMessages()
	{
		return nbMessages;
	}
	
	public String select(String req)
    {
        try
        {
            ArrayList<String> liste = new ArrayList<String>();
            ResultSet resultat = this.statement.executeQuery (req);
            
            while (resultat.next())
            {
            	liste.add(resultat.getString(1));
            }
            System.out.println(liste.toString());
            return liste.toString();
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
            return "erreur";
        }
    }
}
