package gui;

import java.io.IOException;

import javax.swing.JOptionPane;

import connection.ServerConnection;
import exception.UnknownValueException;

class TabbedPanePredictingListener {

	private TabbedPane tab;

	TabbedPanePredictingListener(TabbedPane tab) {
		this.tab = tab;
		init();
	}

	void init() {
		tab.panelPredict.startButton.addActionListener(actionStart -> {
			tab.panelPredict.predictedClass.setText("");
			tab.panelPredict.startButton.setEnabled(false);
			startPredictingAction();
		});
		tab.panelPredict.executeButton.addActionListener(actionContinue -> {
			try {
				continuePredictingAction();
			} catch (UnknownValueException e) {
				JOptionPane.showMessageDialog(tab, e.getMessage(), "Value error",
						JOptionPane.ERROR_MESSAGE);
				tab.panelPredict.startButton.setEnabled(true);
				tab.panelPredict.executeButton.setEnabled(false);
			}
		});
	}
	
	void startPredictingAction() {
		try {
			ServerConnection.out.writeObject(5);
			System.out.println("Starting prediction phase!");
			String answer = ServerConnection.in.readObject().toString();
			if (answer.equals("QUERY")) {
				// Formualting query, reading answer
				answer = ServerConnection.in.readObject().toString();
				tab.panelPredict.queryMsg.setText(answer);
				tab.panelPredict.answer.setText("");
				tab.panelPredict.executeButton.setEnabled(true);
			} else if (answer.equals("OK")) { // Reading prediction
				answer = ServerConnection.in.readObject().toString();
				tab.panelPredict.predictedClass.setText("Predicted class:" + answer);
				tab.panelPredict.queryMsg.setText("");
				tab.panelPredict.startButton.setEnabled(true);
				tab.panelPredict.executeButton.setEnabled(false);
			} else // Printing error message
			{
				tab.panelPredict.queryMsg.setText(answer);
				tab.panelPredict.startButton.setEnabled(true);
				tab.panelPredict.executeButton.setEnabled(false);
			}

		} catch (IOException | ClassNotFoundException e) {
			tab.panelPredict.queryMsg.setText(e.toString());
			tab.panelPredict.startButton.setEnabled(true);
			tab.panelPredict.executeButton.setEnabled(false);
		}
	}
	
	void continuePredictingAction() throws UnknownValueException {
		try {
			if (!tab.panelPredict.answer.getText().equals("")) {
				ServerConnection.out.writeObject(new Integer(tab.panelPredict.answer.getText()));
				String answer = ServerConnection.in.readObject().toString();
				System.out.println(answer);
				if (answer.equals("QUERY")) {
					// Formualting query, reading answer
					answer = ServerConnection.in.readObject().toString();
					tab.panelPredict.queryMsg.setText(answer);
					tab.panelPredict.answer.setText("");
				} else if (answer.equals("OK")) { // Reading prediction
					answer = ServerConnection.in.readObject().toString();
					tab.panelPredict.predictedClass.setText(answer);
					tab.panelPredict.queryMsg.setText("");
					tab.panelPredict.startButton.setEnabled(true);
					tab.panelPredict.executeButton.setEnabled(false);
					tab.panelPredict.answer.setText("");
				} else {
					throw new UnknownValueException(answer);
				}
			} else {
				throw new UnknownValueException("Insert a value in the textfield to continue prediction!");
			}
		} catch (IOException | ClassNotFoundException e) {
			tab.panelPredict.queryMsg.setText(e.toString());
			tab.panelPredict.startButton.setEnabled(true);
			tab.panelPredict.executeButton.setEnabled(false);
		}
	}

}
