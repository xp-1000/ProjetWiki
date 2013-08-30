import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;


public class InterfaceAffichage extends JPanel
{
	InterfaceRecherche interfaceRechSuj;
	InterfaceAffichageSujet interfaceAfficSuj;
	
	public InterfaceAffichage(InterfaceDebut interfacePrincipale)
	{
		super();
		this.setLayout(new BorderLayout());
		interfaceRechSuj = new InterfaceRecherche(interfacePrincipale, this);
		interfaceAfficSuj = new InterfaceAffichageSujet(interfaceRechSuj.getNbBoutons(), interfaceRechSuj.getNomsBoutons(), interfacePrincipale);
		//System.out.println(interfaceRechSuj.getNbBoutons());
		
		JScrollPane scrollpane = new JScrollPane(interfaceAfficSuj);
		
		this.add("North",interfaceRechSuj);
		this.add("Center",scrollpane);
		
		Border cadre = BorderFactory.createTitledBorder("Recherche Wiki");
		interfaceRechSuj.setBorder(cadre);
	}
	
	public void miseAJour()
	{
		System.out.println("interfaceAffichage => miseAJour");
		interfaceAfficSuj.updateAffichage(interfaceRechSuj.getNbBoutons(), interfaceRechSuj.getNomsBoutons());
	}
	
	public void reinitialiserAffichage()
	{
		this.remove(interfaceAfficSuj);
		interfaceAfficSuj.updateAffichage();
		JScrollPane scrollpane = new JScrollPane(interfaceAfficSuj);
		this.add("Center",scrollpane);
		interfaceRechSuj.reinitialiser();
		this.updateUI();
	}
}