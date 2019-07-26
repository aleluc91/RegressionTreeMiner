package exception;

/**
 * Eccezione per gestire il caso di acqusizione di valore mancante o fuori range
 * di un attributo di un nuovo esempio da classificare.
 * 
 * @author Alessio Lucarella
 *
 */
public class UnknownValueException extends Exception {

	/**
	 * Invoca il costruttore della superclasse per settare il messaggio di
	 * errore.
	 * 
	 * @param message
	 *            Stringa contenente il messaggio di errore.
	 */
	public UnknownValueException(String message) {
		super(message);
	}

}
