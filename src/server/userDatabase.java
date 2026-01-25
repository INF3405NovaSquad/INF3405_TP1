package server;

import java.io.*;
import java.util.HashMap;


public class userDatabase {
	
	private static final String nameFile = "users.csv";
	private HashMap<String, String> users = new HashMap<String, String>();
	
	public userDatabase() throws IOException {
		loadUsers();
	}
	
	private void loadUsers() throws IOException {
		File file = new File(nameFile);
		
		if (!file.exists()) {
			file.createNewFile();
			return;
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		
		while((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			
			if (parts.length == 2) {
				users.put(parts[0], parts[1]);
			}
		}
		
		reader.close();
	}
	
	public synchronized boolean authenticate(String username, String password) throws IOException {
		if (users.containsKey(username)) {
			return users.get(username).equals(password);
		} else {
			users.put(username, password);
			saveUser(username, password);
			return true;
		}
	}
	
	private void saveUser(String username, String password) throws IOException {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile, true));
		writer.write(username + "," + password);
		writer.newLine();
		writer.close();
		
	}

}
