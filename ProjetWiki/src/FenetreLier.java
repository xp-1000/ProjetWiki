import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class FenetreLier extends JFrame implements ActionListener
{
	InterfaceDebut interfacePrincipale;
	InterfaceCreationSujet interfaceCreationSujet;
	JPanel panel = new JPanel();
	JButton boutonLier = new JButton("Lier");
	JButton boutonAnnuler = new JButton("Annuler");
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList listeSujets = new JList(listModel);
	String [] listeTitres;
	JScrollPane scrollpane = new JScrollPane(listeSujets);
	
	public FenetreLier(InterfaceDebut parInterfacePrincipale, InterfaceCreationSujet parInterfaceCreationSujet)
	{
		super("Lier un message");
		
		interfacePrincipale = parInterfacePrincipale;
		interfaceCreationSujet = parInterfaceCreationSujet;
		String requete = "select titre from wiki.sujet";
        select(requete);
        
        
        this.setContentPane(panel);
        panel.setLayout(new GridBagLayout());
        ajouteComposant(scrollpane,0,0,4,2);
        ajouteComposant(boutonLier,0,5,1,1);
		ajouteComposant(boutonAnnuler,1,5,1,1);
		boutonLier.addActionListener(this);
		boutonAnnuler.addActionListener(this);
		
		setVisible(true);
		pack();
		setSize(getSize().width + 50,getSize().height + 50);
		setLocationRelativeTo(interfacePrincipale);
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
		contraintes.insets = new Insets(10,10,0,0);
		panel.add(parComposant, contraintes);
	}
	
    public String select(String req)
    {
        try
        {
            ResultSet resultat = interfacePrincipale.statement.executeQuery (req);
            
            while (resultat.next())
            {
            	listModel.addElement(resultat.getString(1));
            }
            
            return "Pas de probleme";
        }
        catch(SQLException e){
            System.out.println("Une exception est levee :" + e);
            return "erreur";
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource() == boutonAnnuler)
		{
			this.dispose();
		}
		
		if(arg0.getSource() == boutonLier)
		{
			interfaceCreationSujet.liaison(listeSujets.getSelectedValuesList());
				
			this.dispose();
		}
		
	}
      
}
