package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Pannello che gestisce l'apprendimento da database.
 * 
 * @author Alessio Lucarella
 *
 */
class PanelDB extends JPanel {

	JComboBox<String> availableTable = new JComboBox<String>();
	JTextArea outputMsg = new JTextArea();
	JButton refreshButton = new JButton();
	JButton executeButton = new JButton("LEARN");
	JButton showTreeButton = new JButton("SHOW TREE");

	/**
	 * Costruttore che invoca il metodo init per inizializzare i componenti del pannello
	 * di apprendimento da database.
	 */
	PanelDB() {
		init();
	}
	
	/** 
	 * Inizializza tutte le componenti che verranno visualizzate all'interno del pannello
	 * di apprendimento da database.
	 */
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new FlowLayout());
		JLabel label = new JLabel("Table : ");
		label.setFont(new Font("Serif", Font.BOLD, 40));
		upPanel.add(label);
		availableTable.setFont(new Font("Serif", Font.BOLD, 20));
		availableTable.setPreferredSize(new Dimension(300, 42));
		upPanel.add(availableTable);
		ImageIcon icon = new ImageIcon(getClass().getResource("/img/refresh.png"));
		refreshButton.setIcon(icon);
		upPanel.add(refreshButton);
		icon = new ImageIcon(getClass().getResource("/img/start.png"));
		executeButton.setIcon(icon);
		executeButton.setFont(new Font("Serif", Font.BOLD, 20));
		upPanel.add(executeButton);
		add(upPanel);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		JPanel upBottomPanel = new JPanel();
		upBottomPanel.setLayout(new BoxLayout(upBottomPanel, BoxLayout.X_AXIS));
		upBottomPanel.setAlignmentX(RIGHT_ALIGNMENT);
		upBottomPanel.setPreferredSize(new Dimension(100, 40));
		showTreeButton.setEnabled(false);
		upBottomPanel.add(showTreeButton);
		bottomPanel.add(upBottomPanel);
		outputMsg.setEditable(false);
		outputMsg.setFont(new Font("Serif", Font.BOLD, 12));
		JScrollPane scrollingArea = new JScrollPane(outputMsg);
		bottomPanel.add(scrollingArea);
		add(bottomPanel);
	}
}
