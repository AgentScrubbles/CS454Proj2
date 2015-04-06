package server;

public class ServerRunner {

	public static void main(String[] args) {
		
		FileManipulator manip = new FileManipulator();
		SocketServerComponent server = new SocketServerComponent(48182, manip);
		server.start();
	}

}
