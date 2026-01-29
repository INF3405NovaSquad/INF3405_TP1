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
			
			while (true) {
			
				//traitement de l'image
				String imageName = in.readUTF();
				
				if(imageName.equalsIgnoreCase("/exit")) {
					break;
				}
				
				String savedName = in.readUTF();
				
				if(savedName.equalsIgnoreCase("/exit")) {
					break;
				}
				
				int imageSize = in.readInt();
				byte[] imageBytes = new byte[imageSize];
				in.readFully(imageBytes);
			
				java.io.ByteArrayInputStream image = new java.io.ByteArrayInputStream(imageBytes);
				java.awt.image.BufferedImage imageBuff = javax.imageio.ImageIO.read(image);
			
				java.time.LocalDateTime now = java.time.LocalDateTime.now();
				System.out.printf("[%s - %s:%d - %04d-%02d-%02d@%02d:%02d:%02d] : Image %s reçue pour traitement.%n",
		            	username, socket.getInetAddress(), socket.getPort(), 
		            	now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
		            	now.getHour(), now.getMinute(), now.getSecond(),
		            	imageName
					);
		
				imageBuff = Sobel.process(imageBuff);
			
				javax.imageio.ImageIO.write(imageBuff, "png", new java.io.File(savedName));
			
				java.io.ByteArrayOutputStream imageClient = new java.io.ByteArrayOutputStream();
				javax.imageio.ImageIO.write(imageBuff, "png", imageClient);
			
				byte[] processedBytes = imageClient.toByteArray();
			
				out.writeInt(processedBytes.length);
				out.write(processedBytes);
				out.flush();
			
				System.out.println("Image traitée et renvoyée au client : " + username);
			}
			
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