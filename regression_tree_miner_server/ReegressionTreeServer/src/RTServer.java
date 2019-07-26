import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import data.Data;
import data.TrainingDataException;
import server.MultiServer;
import server.UnknownValueException;
import tree.RegressionTree;


public class RTServer {
	
	private static int port = 8080;

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		new MultiServer(port);
	}

}
