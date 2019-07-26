
//<applet code='RT' width='500' height='800'>
//</applet>

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import connection.ServerConnection;
import exception.ConnectionException;
import exception.InvalidConnectionValue;
import gui.TabbedPane;

public class RT extends JApplet {

	private JLabel lblIp;
	private JTextField txtIp = new JTextField(20);
	private JLabel lblPort;
	private JTextField txtPort = new JTextField(20);
	private JButton connect = new JButton("Avvia!");
	private TabbedPane tab;

	public void init() {
		initConnectPanel();
	}

	private void initConnectPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		lblIp = new JLabel("IP :");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		add(lblIp, cs);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		add(txtIp, cs);
		lblPort = new JLabel("PORT :");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		add(lblPort, cs);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		add(txtPort, cs);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 3;
		connect.addActionListener(new ConnectListener());
		add(connect, cs);
	}

	class ConnectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				startConnection();
			} catch (ConnectionException e) {
				JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Errore di connessione",
						JOptionPane.ERROR_MESSAGE);
			} catch (InvalidConnectionValue e) {
				JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Dati non validi",
						JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Dati non validi",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void startConnection() throws ConnectionException, InvalidConnectionValue, NumberFormatException {
		if (txtIp.getText().equals("") || txtPort.getText().equals(""))
			throw new InvalidConnectionValue("Perfavore inserisci entrambi i valori!");
		ServerConnection.ip = txtIp.getText();
		try {
			ServerConnection.port = Integer.valueOf(txtPort.getText());
		} catch (NumberFormatException e) {
			throw new InvalidConnectionValue("Perfavore inserisci un valore intero per il valore port!");
		}
		ServerConnection.getConnection();
		try {
			getContentPane().removeAll();
			ServerConnection.out = new ObjectOutputStream(ServerConnection.socket.getOutputStream());
			ServerConnection.in = new ObjectInputStream(ServerConnection.socket.getInputStream());
			tab = new TabbedPane();
			getContentPane().setLayout(new GridLayout(1, 1));
			getContentPane().add(tab);
			validate();
		} catch (IOException e) {
			throw new ConnectionException("Errore durante l'inizializzazione degli stream!");
		}
	}

	@Override
	public void destroy() {
		if (ServerConnection.out != null && ServerConnection.in != null) {
			try {
				ServerConnection.out.writeObject(7);
				ServerConnection.out.close();
				ServerConnection.in.close();
			} catch (IOException e) {

			}
		}
	}

}
