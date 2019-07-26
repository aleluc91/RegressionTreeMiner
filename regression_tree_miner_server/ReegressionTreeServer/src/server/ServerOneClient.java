package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.apache.log4j.Logger;

import data.Data;
import data.TrainingDataException;
import database.DatabaseConnectionException;
import database.DatabaseSchema;
import database.DbAccess;
import tree.LeafNode;
import tree.NodeInfo;
import tree.RegressionTree;
import tree.SplitNode;

class ServerOneClient extends Thread {

	static Logger log = Logger.getLogger(ServerOneClient.class);

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private final static String PATH = "data/";
	private String tableName;
	private RegressionTree tree = null;
	private String socketName = null;

	/**
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public ServerOneClient(Socket socket) throws IOException {
		this.socket = socket;
		this.socketName = socket.getInetAddress().getHostName();
		try {
			log.info("Connection established with " + socketName + " on port " + socket.getPort());
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			log.info("Client : " + socketName + " stream successful initialized.");
			Path path = Paths.get("data");
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			socket.close();
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean run = true;
		int choice = 0;
		while (run) {
			try {
				choice = ((Integer) in.readObject()).intValue();
				log.info("Client " + socketName + " : richiesta operazione numero " + choice + ".");
				switch (choice) {
				case 0:
					DbAccess db = new DbAccess();
					try {
						db.initConnection();
						log.info("Client " + socketName + " : connessione al database avvenuta con successo.");
					} catch (DatabaseConnectionException e1) {
						// TODO Gestione dell'errore
						log.error("Client " + socketName + " : " + e1.getMessage(), e1);
						e1.printStackTrace();
					}
					DatabaseSchema dbSchema = new DatabaseSchema(db);
					log.info("Client " + socketName + " : schema del database inizializzato.");
					out.writeObject(dbSchema.getListOfTables());
					log.info("Client " + socketName + " : lista della tabelle disponibili inviata.");
					break;
				case 1:
					// TODO Sistemare meglio il blocco try-catch
					tableName = in.readObject().toString();
					log.info("Client " + socketName + " : richiesta tabella " + tableName);
					out.writeObject("OK");
					log.info("Client " + socketName + " : messaggio 'OK' inviato al client.");

					break;
				case 2:
					Data trainingSet = null;
					try {
						log.info("Client " + socketName + " : fase di apprendimento dal database avviata.");
						trainingSet = new Data(tableName);
						log.info("Client " + socketName + " : training set appreso.");
						tree = new RegressionTree(trainingSet);
						log.info("Client " + socketName + " : regression tree costruito.");
						tree.salva(PATH + tableName + ".dmp");
						System.out.println("we");
						log.info("Client " + socketName + " : file creato in " + PATH + tableName + ".dmp");
						out.writeObject(tree.printRules());
						log.info("Client " + socketName + " : regole inviate al client.");
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (TrainingDataException e) {
						out.writeObject("");
						out.writeObject(e.getMessage());
						break;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					out.writeObject("OK");
					break;
				case 3:
					File folder = new File(PATH);
					if(folder.exists()){
						File listOfFiles[] = folder.listFiles();
						List<String> result = new ArrayList<String>();
						for (File f : listOfFiles) {
							if (f.isFile())
								result.add(new String(f.getName()));
						}
						log.info("Client : " + socketName + " : lista dei file appresa.");
						out.writeObject(result);
						log.info("Client : " + socketName + " : lista dei file inviata al client.");
					}else{
						log.warn("Client " + socketName + " : non ci sono file sul server." );
						out.writeObject(null);
						log.info("Client " + socketName + " : lista vuota inviata al client.");
					}
					break;
				case 4:
					log.info("Client " + socketName + " : avviata fase di apprendimento da file.");
					String fileName = (String) in.readObject();
					log.info("Client " + socketName + " : richiesto file " + fileName + ".dmp");
					tree = RegressionTree.carica("data/" + fileName);
					log.info("Client " + socketName + " : regression tree costruito.");
					out.writeObject(tree.printRules());
					log.info("Client " + socketName + " : regole inviate al client.");
					out.writeObject("OK");
					break;
				case 5:
					try {
						out.writeObject(predictClass(tree));
					} catch (UnknownValueException e) {
						out.writeObject(e.getMessage());
						log.error("Client " + socketName + " : ", e);
						break;
					}
					break;
				case 6:
					if (!tree.equals(null)) {
						out.writeObject(tree.getTree());
						log.info("Client " + socketName + " : struttura dell'albero inviata al client.");
					}

					break;
				case 7:
					run = false;
					log.info("Client " + socketName + " : disconnesso.");
					break;
				}
			} catch (IOException e) {
				run = false;
				log.error("Client " + socketName + " : connessione con il client persa.");
			} catch (ClassNotFoundException e) {
				
			}
		}
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			log.error("Client " + socketName + " : errore durante la chiusura degli stream.");
		}
	}

	/**
	 * Invia al client le informazioni di ciascuno split dell'albero e per il
	 * corrispondente attributo acquisisce il valore da predire inviato dal
	 * client.Se il nodo root corrente è foglia termina l'acquisizione e invia
	 * al client la predizione per l'attributo di classe, altrimenti invoca
	 * ricorsivamente sul figlio di root in childTree[] individuato dal valore
	 * inviato dal client.
	 * 
	 * @param tree
	 *            Albero di regressione su cui effettuare la predizione.
	 * @return Oggetto Double contenente il valore di classe predetto per
	 *         l'esempio acquisito.
	 * @throws UnknownValueException
	 *             L'eccezione viene sollevata quando quando la risposta
	 *             ricevuta dal client non permetta di selezionare un ramo
	 *             valido del nodo di split.
	 */
	public Double predictClass(RegressionTree tree) throws UnknownValueException {
		Double predictedValue = 0.0;
		try {
			if (tree.getRoot() instanceof LeafNode) {
				out.writeObject("OK");
				predictedValue = ((LeafNode) tree.getRoot()).getPredictedClassValue();
			} else {
				out.writeObject("QUERY");
				int max = ((SplitNode) tree.getRoot()).getNumberOfChildren();
				out.writeObject(((SplitNode) tree.getRoot()).formulateQuery());
				int choice = 0;
				try {
					choice = ((Integer) in.readObject()).intValue();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (choice > max - 1 || choice < 0)
					throw new UnknownValueException("The answer should be an integer between 0 and " + (max - 1) + "!");
				predictedValue = predictClass(tree.getChildTree(choice));
			}
			return predictedValue;
		} catch (IOException e) {
			e.printStackTrace();
			return predictedValue;
		}
	}

}
