package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

public class Server {
	private static ServerSocket Listener; 
	// Application Serveur
	public static void main(String[] args) throws Exception {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Entrez l'adresse IP du serveur : ");
		String ip = scanner.nextLine();
		
		if (!validator.isValidIp(ip)) {
			System.out.println("Adresse IP invalide");
			scanner.close();
			return;
		}
		
		System.out.print("Entrez le port d'écoute : ");
		String port = scanner.nextLine();
		
		if(!validator.isValidPort(port)) {
			System.out.println("Port invalide");
			scanner.close();
			return;
		}
		
		int serverPort = Integer.parseInt(port);
		System.out.println("Paramètres valides. Démarrage du serveur...");
		
		// Compteur incrémenté à chaque connexion d'un client au serveur
		int clientNumber = 1;
		
		try (ServerSocket listener = new ServerSocket()){
			// Création de la connexien pour communiquer ave les, clients
			// Association de l'adresse et du port à la connexien
			listener.setReuseAddress(true);
            InetAddress serverIP = InetAddress.getByName(ip);
            // Association de l'adresse et du port à la connexien
            listener.bind(new InetSocketAddress(serverIP, serverPort));
            System.out.format("Serveur en écoute sur %s:%d%n", serverIP, serverPort);
            
            while(true) {
            	// Important : la fonction accept() est bloquante: attend qu'un prochain client se connecte
            	Socket clientSocket = listener.accept();
            	System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress() + " : " + clientSocket.getPort());
            	// Une nouvelle connection : on incémente le compteur clientNumber 
				new ClientHandler(clientSocket, clientNumber++).start();
            }
		} catch (IOException e) {
			System.out.println("Erreur serveur : " + e.getMessage());
		} 
		
	}
}