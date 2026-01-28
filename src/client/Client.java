package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
        	
        	System.out.format("Serveur lancé sur [%s:%d]%n", ip, portClient); 
        	// Création d'un canal entrant pour recevoir les messages envoyés, par le serveur
    		DataInputStream in = new DataInputStream(socket.getInputStream());
    		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    		
    		System.out.print("Nom d'utilisateur : ");
    		String username = scanner.nextLine();
    		
    		System.out.print("Mot de passe : ");
    		String password = scanner.nextLine();
    		
    		out.writeUTF(username);
    		out.writeUTF(password);
    		
    		String response = in.readUTF();
    		
    		if (response.equals("OK")) {
    			System.out.println("Connexion accepté");
    			String messageConnexion = in.readUTF();
    			
    			System.out.println(messageConnexion);
    			
    			
    			//envoi de l'image
    			Scanner scannerImage = new Scanner(System.in);
    			
    			System.out.print("Nom du fichier à envoyer (doit être dans le même dossier que l'exécutable) : ");
    			String imagePath = scannerImage.nextLine();
    			java.io.File file = new java.io.File(imagePath);
    			
    			if (!file.exists()) {
    				System.out.println("Fichier introuvable.");
    				return;
    			}
    			
    			System.out.print("Nom que vous voulez donner à l'image traitée (ex: photoTraitee.png) : ");
    			String processedName = scannerImage.nextLine();
    			
    			byte[] fileBytes = new byte[(int) file.length()];
    			try (java.io.FileInputStream f_in = new java.io.FileInputStream(file)){
    				f_in.read(fileBytes);
    			}
    			
    			out.writeUTF(file.getName());
    			out.writeUTF(processedName);
    			
    			out.writeInt(fileBytes.length);
    			out.write(fileBytes);
    			out.flush();
    			
    			System.out.println("Image envoyée pour traitement...");
    			
    			int processedSize = in.readInt();
    			byte[] processedByte = new byte[processedSize];
    			in.readFully(processedByte);
    			
    			java.io.File outFile = new java.io.File(processedName);
    			try(java.io.FileOutputStream f_out = new java.io.FileOutputStream(outFile)){
    				f_out.write(processedByte);
    			}
    			
    			System.out.println("Image traitée reçue ! Emplacement : " + outFile.getAbsolutePath());
    			scannerImage.close();
    			
    		} else {
    			System.out.println("Erreur dans la saisie du mot de passe");
    			socket.close();
    			return;
    		}
    		
    		// Attente de la réception d'un message envoyé par le, server sur le canal
    		String message = in.readUTF();
            System.out.println("Message du serveur : " + message);
            
        } catch (IOException e) {
        	
            System.out.println("Vous êtes déconnecté.");
        }
		
	}
}
 

