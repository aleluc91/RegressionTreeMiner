package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;


/**
 * 
 * 
 * @author Alessio Lucarella
 *
 */
public class MultiServer extends Thread{

	static Logger log = Logger.getLogger(MultiServer.class.getName());
	private ServerSocket serverSocket;
	private int port = 8080;

	/**
	 * Costruttore di classe inizializza la porta ed invoca il metodo run().
	 * 
	 * @param port
	 *            Porta su cui viene stabilita la connesione.
	 */
	public MultiServer(int port) {
		this.port = port;
		start();
	}

	/**
	 * Istanzia un oggetto istanza della classe ServerSocket che si pone in
	 * attesa di richieste di connessione da parte di un client. Ad ogni nuova
	 * richiesta di connessione si istanzia ServerOneClient.
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			log.info(InetAddress.getLocalHost().getHostAddress());
			log.info("Server started on port 8080.");
		} catch (IOException e) {
			System.out
					.println("Impossibile creare una connessione sulla porta : " + port + " il server verrà stoppato!");
		}
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new ServerOneClient(socket).start();
			} catch (IOException e) {
				System.out.println("Errore durante l'accettazione del client!");
				continue;
			}
		}
	}

}
