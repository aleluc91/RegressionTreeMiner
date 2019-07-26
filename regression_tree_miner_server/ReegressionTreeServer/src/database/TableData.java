package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Modella l'insieme delle transazioni collezionate in una tabella
 *
 */
public class TableData {

	private DbAccess db;

	/**
	 * Inizializza il valore del membro db.
	 * 
	 * @param db
	 *            Oggetto DBAccess contenente la connesione al database.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione
	 * per estrarre le tuple distinte da tale tabella. Per ogni tupla del
	 * resultset , si crea un oggetto, istanza della classe Example , il cui
	 * riferimento va incluso nella lista da restituire. In particolare , per la
	 * tupla corrente nel resultset , si estraggono i valori dei singoli campi ,
	 * e li si aggiungono all'oggetto della classe Example che si sta
	 * costruendo.
	 * 
	 * @param table
	 *            Nome della tabella all'interno del database.
	 * @return
	 * @throws SQLException
	 *             Eccezione lanciata in presenza di errori nella esecuzione
	 *             della query.
	 * @throws EmptySetException
	 *             Eccezione lanciata se il resultset è vuoto.
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db, table);

		String query = "select ";

		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			Column c = tSchema.getColumn(i);
			if (i > 0)
				query += ",";
			query += c.getColumnName();
		}
		/*
		 * if (tSchema.getNumberOfAttributes() == 0) throw new SQLException();
		 */
		query += (" FROM " + table);

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty = true;
		while (rs.next()) {
			empty = false;
			Example currentTuple = new Example();
			for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
				if (tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i + 1));
				else
					currentTuple.add(rs.getString(i + 1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if (empty)
			throw new EmptySetException("Il resultset restituito è vuoto");
		return transSet;

	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti
	 * ordinati di column e popolare un insieme da restituire.
	 * 
	 * @param table
	 *            Nome della tabella.
	 * @param column
	 *            Colonna della tabella analizzata.
	 * @return Insieme di valori distinti ordinati in modalità ascendente che
	 *         l'attributo identificato da nome column assume nella tabella
	 *         identificata dal nome table.
	 * @throws SQLException
	 *             Eccezione lanciata in presenza di errori nella query.
	 */
	public Set<Object> getDistinctValues(String table, Column column) throws SQLException {
		// TODO Verificare la correttezza del metodo
		Set<Object> ts = new TreeSet<Object>();
		Statement statement = db.getConnection().createStatement();
		String query = "SELECT DISTINCT " + column.getColumnName() + " FROM " + table;
		ResultSet rs = statement.executeQuery(query);
		if (column.isNumber())
			while (rs.next())
				ts.add(rs.getDouble(column.getColumnName()));
		else
			while (rs.next())
				ts.add(rs.getString(column.getColumnName()));
		return ts;
	}

}
