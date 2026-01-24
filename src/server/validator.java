package server;

public class validator {
	
	public static boolean isValidIp(String ip) {
		if (ip == null) {
			return false;
		}
		
		String[] parts = ip.split("\\.");
		if (parts.length != 4) {
			return false;
		}
		
		for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
	}
	
	 public static boolean isValidPort(String portStr) {
	        try {
	            int port = Integer.parseInt(portStr);
	            return port >= 5000 && port <= 5050;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
}
