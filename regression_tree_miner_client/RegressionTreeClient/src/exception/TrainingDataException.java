package exception;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Eccezione per gestire il caso di acquisizione errata del training set.
 * 
 * @author Alessio Lucarella
 *
 */
public class TrainingDataException extends Exception {

	/**
	 * Invoca il costruttore della superclasse per inizializzare il messaggio di
	 * errore.
	 * 
	 * @param message
	 *            Stringa contenente il messaggio di errore.
	 */
	public TrainingDataException(String message) {
		super(message);
	}

}
