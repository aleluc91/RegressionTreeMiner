package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Pannello che gestisce la predizione del valore di classe.
 * 
 * @author Alessio Lucarella
 *
 */
class PanelPredicting extends JPanel{
	
	JTextArea queryMsg = new JTextArea(4, 50);
	JTextField answer = new JTextField(20);
	JButton startButton = new JButton("START");
	JButton executeButton = new JButton("Continue");
	JLabel labelPredict = new JLabel("Predicted class : ");
	JLabel predictedClass = new JLabel("");

	/**
	 * Costruttore che invoca il metodo init per inizializzare i componenti del pannello
	 * di predizione.
	 */
	PanelPredicting() {
		init();
	}
	
	/** 
	 * Inizializza tutte le componenti che verranno visualizzate all'interno del pannello
	 * di preidizione.
	 */
	private void init(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new FlowLayout());
		JPanel queryPanel = new JPanel();
		queryMsg.setFont(new Font("Serif", Font.BOLD, 15));
		upPanel.add(queryMsg);
		add(upPanel);
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new FlowLayout());
		answer.setFont(new Font("Serif", Font.BOLD, 15));
		answer.setPreferredSize(new Dimension(300, 42));
		middlePanel.add(answer);
		startButton.setEnabled(false);
		startButton.setFont(new Font("Serif", Font.BOLD, 20));
		executeButton.setEnabled(false);
		executeButton.setFont(new Font("Serif", Font.BOLD, 20));
		middlePanel.add(startButton);
		middlePanel.add(executeButton);
		add(middlePanel);
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new FlowLayout());
		labelPredict.setFont(new Font("Serif", Font.BOLD, 40));
		downPanel.add(labelPredict);
		predictedClass.setFont(new Font("Serif" , Font.BOLD , 40));
		predictedClass.setForeground(Color.RED);
		downPanel.add(predictedClass);
		add(downPanel);
	}

}
