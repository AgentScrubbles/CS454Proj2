package server;

public class ServerRunner {

	/**
	 * Entry point of the Server
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileManipulator manip = new FileManipulator();
		SocketServerComponent server = new SocketServerComponent(48182, manip);
		server.start();
	}

}
