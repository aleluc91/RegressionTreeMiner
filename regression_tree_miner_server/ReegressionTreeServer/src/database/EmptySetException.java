package database;

/**
 * Modella la restituzione di un resultset vuoto.
 * 
 * @author Alessio Lucarella
 *
 */
public class EmptySetException extends Exception {

	/**
	 * Richiama il costruttore della superclasse per inizializzare il messaggio
	 * di errore.
	 * 
	 * @param message
	 *            Messaggio di errore.
	 */
	EmptySetException(String message) {
		super(message);
	}

}
