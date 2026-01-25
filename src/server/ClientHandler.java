package server;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import server.userDatabase;

public class ClientHandler extends Thread { // pour traiter la demande de chaque client sur un socket particulier
	private Socket socket; 
	private int clientNumber; 
	
	public ClientHandler(Socket socket, int clientNumber) {
		
		this.socket = socket;
		this.clientNumber = clientNumber; 
		System.out.println("New connection with client#" + clientNumber + " at" + socket.getInetAddress());
		
	}
	
	@Override
	public void run() { // Création de thread qui envoi un message à un client
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
			
			String username = in.readUTF();
			String password = in.readUTF();
			
			userDatabase database = new userDatabase();
			boolean accepted = database.authenticate(username, password);
			
			if(!accepted) {
				out.writeUTF("Denied");
				out.writeUTF("Erreur dans la saisie du mot de passe");
				return;
			}
			
			// création de canal d’envoi
			out.writeUTF("OK");
			out.writeUTF("Hello from server - you are client#" + clientNumber);
			
		} catch (IOException e) {
			System.out.println("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");}
			}
		System.out.println("Connection with client# " + clientNumber+ " closed");
		}
	
}