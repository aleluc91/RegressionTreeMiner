package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import connection.ServerConnection;
import exception.ConnectionException;
import exception.TrainingDataException;
import tree.NodeInfo;
import tree.TreeDialog;

/**
 * Gestisce i listener per il pannello di apprendimento da database.
 * 
 * @author Alessio Lucarella
 *
 */
public class TabbedPaneDbListerner {

	TabbedPane tab;
	private DefaultTreeForTreeLayout<NodeInfo> tree = null;

	/**
	 * Costruttore che richiama il metodo initPanelDbListener che inizializza i listener
	 * per il pannello di apprendimento da database.
	 * 
	 * @param tab Pannello principale dell'applet.
	 */
	public TabbedPaneDbListerner(TabbedPane tab) {
		this.tab = tab;
		updateListOfTables();
		initPanelDBListener();
	}

	private void initPanelDBListener() {
		tab.panelDB.refreshButton.addActionListener(actionUpdate -> {
			updateListOfTables();
		});
		tab.panelDB.executeButton.addActionListener(actionExecute -> {
			try {
				learningFromDBAction();
				tab.panelDB.showTreeButton.setEnabled(true);
				tab.panelPredict.startButton.setEnabled(true);
			} catch (TrainingDataException e) {
				JOptionPane.showMessageDialog(tab, e.getMessage(), "Data error", JOptionPane.ERROR_MESSAGE);
				tab.panelDB.outputMsg.setText("");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(tab,
						"Error while communicating with the server! \n\n" + e.getMessage() + "\n\n",
						"Communication Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		tab.panelDB.showTreeButton.addActionListener(actionShow -> {
			showTree();
		});
	}

	/**
	 * Contatta il server comunicando la tabella selezionata , e riceve come risposta le
	 * regole dell'albero di regressione e l'albero formattato per la stampa a schermo.
	 * 
	 * @throws TrainingDataException 
	 * @throws IOException
	 */
	private void learningFromDBAction() throws TrainingDataException, IOException {
		try {
			tab.panelDB.outputMsg.setText("");
			tab.panelDB.outputMsg.append("Working ....\n");
			ServerConnection.out.writeObject(1);
			ServerConnection.out.writeObject(tab.panelDB.availableTable.getSelectedItem().toString());
			String answer = ServerConnection.in.readObject().toString();
			if (!answer.equals("OK")) {
				tab.panelDB.outputMsg.append(answer);
			}
			tab.panelDB.outputMsg.append("Starting learning phase!\n");
			ServerConnection.out.writeObject(2);
			String rules = ServerConnection.in.readObject().toString();
			answer = ServerConnection.in.readObject().toString();
			if (!answer.equals("OK")) {
				throw new TrainingDataException(answer);
			} else {
				tab.panelDB.outputMsg.append(rules.toString());
				tab.panelDB.outputMsg.append("Regression tree learned! \n");
				// TODO test ricezione tree
				ServerConnection.out.writeObject(6);
				tree = (DefaultTreeForTreeLayout<NodeInfo>) ServerConnection.in.readObject();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Contatta il server e aggiorna la combobox con le tabelle disponibili all'interno del 
	 * database. Se non ci sono tabelle la combobox viene disabilitata.
	 * 
	 */
	private void updateListOfTables() {
		try{
			ServerConnection.out.writeObject(0);
			List<String> tableList = new ArrayList<String>();
			tableList = (List<String>) ServerConnection.in.readObject();
			System.out.println("prova");
			if (tableList.size() == 0){
				tab.panelDB.availableTable.addItem("Database doesn't contain tables.");
				tab.panelDB.availableTable.setEnabled(false);
				tab.panelDB.executeButton.setEnabled(false);
			}
			else
				tableList.forEach(s -> tab.panelDB.availableTable.addItem(s));
				/*for (String s : tableList)
					tab.panelDB.availableTable.addItem(s);*/
		}catch(IOException e){
			JOptionPane.showMessageDialog(tab,
					"Error while communicating with the server! \n\n" + e.getMessage() + "\n\n",
					"Communication Error", JOptionPane.ERROR_MESSAGE);
			tab.panelDB.availableTable.setEnabled(false);
			tab.panelDB.executeButton.setEnabled(false);
		}catch(ClassNotFoundException e){
			//TODO Gestire l'eccezione
		}
	}
		
	/**
	 * 
	 */
	private void showTree(){
		if(!tree.equals(null)){
			new TreeDialog(tree);
		}
	}
	
	
}
