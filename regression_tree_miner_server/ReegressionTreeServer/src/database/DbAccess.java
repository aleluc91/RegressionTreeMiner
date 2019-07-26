package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Realizza l'accesso alla base di dati.
 * 
 * @author Alessio Lucarella
 *
 */
public class DbAccess {

	private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final String DATABASE = "MapDb";
	private final String PORT = "3306";
	private final String USER_ID = "MapUser";
	private final String PASSWORD = "map";
	private Connection conn;
	

	/**
	 * Impartisce al class loader l'ordine di caricare il driver mysql , e
	 * inizializza la connessione riferita da conn.
	 * 
	 * @throws DatabaseConnectionException
	 *             Eccezione sollevata in caso di fallimento nella connessione
	 *             al database.
	 */
	public void initConnection() throws DatabaseConnectionException {
		if (conn == null) {
			try {
				Class.forName(DRIVER_CLASS_NAME).newInstance();
			} catch (InstantiationException e1) {
				throw new DatabaseConnectionException("Impossibile carcicare la classe driver!");
			} catch (IllegalAccessException e1) {
				throw new DatabaseConnectionException("Problema di accesso durante il caricamento!");
			} catch (ClassNotFoundException e1) {
				throw new DatabaseConnectionException("Impossibile istanziare il driver!");
			}
			try {
				conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE, USER_ID,
						PASSWORD);
			} catch (SQLException e) {
				throw new DatabaseConnectionException("Errore durante la connessione al database!");
			}

		}
	}

	/**
	 * Restituisce la connessione al database.
	 * 
	 * @return conn Attuale connessione al database.
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Chiude la connessione con il database.
	 * 
	 * @throws SQLException
	 *             Eccezione lanciata in caso di errori durante la chiusa del
	 *             database.
	 */
	public void closeConnection() throws SQLException {
		if (conn != null)
			conn.close();
	}

}
