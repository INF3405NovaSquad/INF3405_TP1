package client;

import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

//Application client
public class Client {
	
	
	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez l'adresse IP du serveur : ");
        String ip = scanner.nextLine();
        
        if (!validator.isValidIp(ip)) {
            System.out.println("Adresse IP invalide.");
            scanner.close();
            return;
        }

        System.out.print("Entrez le port du serveur : ");
        String port = scanner.nextLine();

        

        if (!validator.isValidPort(port)) {
            System.out.println("Port invalide (5000 à 5050).");
            scanner.close();
            return;
        }

        int portClient = Integer.parseInt(port);
        System.out.println("Connexion au serveur...");
        
        try (Socket socket = new Socket(ip, portClient)){
        	
        	System.out.format("Serveur lancé sur [%s:%d]", ip, portClient);
        	// Création d'un canal entrant pour recevoir les messages envoyés, par le serveur
    		DataInputStream in = new DataInputStream(socket.getInputStream());
    		// Attente de la réception d'un message envoyé par le, server sur le canal
    		String message = in.readUTF();
            System.out.println("Message du serveur : " + message);
            
        } catch (IOException e) {
        	
            System.out.println("Impossible de se connecter au serveur : " + e.getMessage());
        }
		
	}
}
 

