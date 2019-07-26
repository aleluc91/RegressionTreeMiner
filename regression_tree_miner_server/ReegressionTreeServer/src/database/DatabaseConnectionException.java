package database;

/**
 * Modella il fallimento della connesione al database.
 * 
 * @author Alessio Lucarella
 *
 */
public class DatabaseConnectionException extends Exception {

	/**
	 * Invoca il costruttore della superclasse per avvalorare il campo String
	 * contenente il messaggio di errore.
	 * 
	 * @param message
	 *            Messaggio di errore.
	 */
	public DatabaseConnectionException(String message) {
		super(message);
	}

}
