package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import exception.ConnectionException;

public class ServerConnection {
	
	private static ServerConnection conn;
	public static Socket socket;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	public static String ip;
	public static int port;
	
	public static ServerConnection getConnection() throws ConnectionException{
		if(conn == null){
			try {
				return conn = new ServerConnection(ip , port);
			} catch (IOException e) {
				 throw new ConnectionException("Impossibile collegarsi al server. Controlla se l'ip e la porta sono corretti!");
			}
		}
		else
			return conn;
	}
	
	private ServerConnection(String ip , int port) throws IOException{
		InetAddress addr = InetAddress.getByName(ip); // ip
		System.out.println("addr = " + addr);
		socket = new Socket(addr, port); // Port
	}
	
	public Socket getSocket(){
		return socket;
	}

}
