package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Modella lo schema di una tabella all'interno del database.
 *
 */
public class TableSchema implements Iterable<Column> {

	private List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Scansiona i metadata della tabella per ottenere informazioni che
	 * riguardano il nome delle colonne e il tipo di valore contenuto
	 * all'interno delle stesse. Se il tipo di valore è presente all'interno del
	 * HashMap contenente i tipi java definiti , viene aggiunta un oggetto
	 * Column con nome e tipo della colonna esaminata all'interno della lista ,
	 * di oggetti Column , tableSchema.
	 * 
	 * @param db
	 *            Oggetto che si occupa dell'accesso al database.
	 * @param tableName
	 *            Nome identificativo della tabella.
	 * @throws SQLException
	 *             Eccezione lanciata in caso si presentino errori durante
	 *             l'interrogazione al database.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {

		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {

			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(
						new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

		}
		res.close();

	}

	/**
	 * Restituisce la cardinalità della lista tableSchema.
	 * 
	 * @return Numero di colonne presenti nella lista.
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Restituisce la colonna indicizzata da index all'interno della lista
	 * tableSchema.
	 * 
	 * @param index
	 *            Indice della colonna da restituire.
	 * @return Oggetto Colonna indicizzata da index.
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}

	/**
	 * Restituisce l'iteratore per la lista tableSchema.
	 */
	@Override
	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

}
