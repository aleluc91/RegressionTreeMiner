package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSchema {
	
	private List<String> listOfTables = new ArrayList<String>();
	
	public DatabaseSchema(DbAccess db){
		try {
			DatabaseMetaData md = db.getConnection().getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while(rs.next()){
				listOfTables.add(new String(rs.getString("TABLE_NAME")));
			}
		} catch (SQLException e) {
			// TODO Gestire l'eccezione.
			e.printStackTrace();
		}
	}
	
	public List<String> getListOfTables(){
		return listOfTables;
	}
	
}
