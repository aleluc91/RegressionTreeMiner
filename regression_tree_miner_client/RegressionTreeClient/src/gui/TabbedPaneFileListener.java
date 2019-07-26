package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import connection.ServerConnection;
import exception.TrainingDataException;
import tree.NodeInfo;
import tree.TreeDialog;

class TabbedPaneFileListener {
	
	TabbedPane tab;
	private DefaultTreeForTreeLayout<NodeInfo> tree = null;

	public TabbedPaneFileListener(TabbedPane tab) {
		this.tab = tab;
		initPanelFileListener();
	}

	void initPanelFileListener() {
		tab.panelFile.refreshButton.addActionListener(actionUpdate -> {
			tab.panelFile.availableFiles.removeAllItems();
			updateListOfFiles();
		});
		tab.panelFile.executeButton.addActionListener(actionExecute -> {
			try {
				learningFromFileAction();
				tab.panelFile.showTreeButton.setEnabled(true);
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
		tab.panelFile.showTreeButton.addActionListener(actionShow -> {
			showTree();
		});
	}

	void learningFromFileAction() throws TrainingDataException, IOException {
		try {
			tab.panelFile.outputMsg.setText("");
			tab.panelFile.outputMsg.append("Working ....\n");
			// Storing table from db
			ServerConnection.out.writeObject(4);
			ServerConnection.out.writeObject(tab.panelFile.availableFiles.getSelectedItem().toString());
			String rules = ServerConnection.in.readObject().toString();
			String answer = ServerConnection.in.readObject().toString();
			if (!answer.equals("OK")) {
				throw new TrainingDataException(answer);
			} else {
				tab.panelFile.outputMsg.append(rules.toString());
				tab.panelFile.outputMsg.append("Regression tree learned! \n");
				// TODO test ricezione tree
				ServerConnection.out.writeObject(6);
				tree = (DefaultTreeForTreeLayout<NodeInfo>) ServerConnection.in
						.readObject();
			}
		} catch (ClassNotFoundException e) {

		}
	}
	
	void updateListOfFiles() {
		try{
			ServerConnection.out.writeObject(3);
			List<String> fileList = new ArrayList<String>();
			fileList = (List<String>) ServerConnection.in.readObject();
			if(fileList == null){
				tab.panelFile.availableFiles.addItem("There are no files on the server.");
				tab.panelFile.availableFiles.setEnabled(false);
				tab.panelFile.executeButton.setEnabled(false);
			}
			else if (fileList.size() == 0) {
				tab.panelFile.availableFiles.addItem("There are no files on the server.");
				tab.panelFile.availableFiles.setEnabled(false);
				tab.panelFile.executeButton.setEnabled(false);
			} else{
				//fileList.forEach(s -> tab.panelFile.availableFiles.addItem(s));
				for (String s : fileList)
					tab.panelFile.availableFiles.addItem(s);
				tab.panelFile.availableFiles.setEnabled(true);
				tab.panelFile.executeButton.setEnabled(true);
			}
		}catch(IOException e){
			JOptionPane.showMessageDialog(tab,
					"Error while communicating with the server! \n\n" + e.getMessage() + "\n\n",
					"Communication Error", JOptionPane.ERROR_MESSAGE);
			tab.panelFile.availableFiles.setEnabled(false);
			tab.panelFile.executeButton.setEnabled(false);
		}catch(ClassNotFoundException e){
			
		}
		
	}
	
	void showTree(){
		if(!tree.equals(null)){
			new TreeDialog(tree);
		}
	}
	
}
