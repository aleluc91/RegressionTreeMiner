package gui;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import connection.ServerConnection;
import exception.ConnectionException;

/**
 * Pannello pricipale che contiene tutti i sotto-pannelli per l'apprendimento e predizione.
 * 
 * @author Alessio Lucarella
 *
 */
public class TabbedPane extends JPanel{
	
	PanelDB panelDB;
	PanelFile panelFile;
	PanelPredicting panelPredict;

	/**
	 * Costruttore che istanzia i vari sotto-pannelli e i loro listener.
	 * 
	 * @throws ConnectionException
	 */
	public TabbedPane() throws ConnectionException {
		super(new GridLayout(1 , 1));
		JTabbedPane tabbedPane = new JTabbedPane();
		// copy img in src Directory and bin directory
		URL imgURL = getClass().getResource("/img/db.jpg");
		ImageIcon iconDB = new ImageIcon(imgURL);
		panelDB = new PanelDB();
		tabbedPane.addTab("DB", iconDB, panelDB, "Learn Data From Database");

		imgURL = getClass().getResource("/img/file.jpg");
		ImageIcon iconFile = new ImageIcon(imgURL);
		panelFile = new PanelFile();
		tabbedPane.addTab("FILE", iconFile, panelFile, "Learn Data From File");

		imgURL = getClass().getResource("/img/predict.jpg");
		ImageIcon iconPredict = new ImageIcon(imgURL);
		panelPredict = new PanelPredicting();
		tabbedPane.addTab("PREDICT", iconPredict, panelPredict, "Prediction");
		TabbedPaneDbListerner dbListener = new TabbedPaneDbListerner(this);
		//dbListener.updateListOfTables();
		TabbedPaneFileListener fileListener = new TabbedPaneFileListener(this);
		fileListener.updateListOfFiles();
		TabbedPanePredictingListener predictingListener = new TabbedPanePredictingListener(this);
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
}

