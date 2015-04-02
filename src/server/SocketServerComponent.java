package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;


public class SocketServerComponent {
	
	private final int _port;
	private final AtomicBoolean _stop;
	public SocketServerComponent(int port){
		_port = port;
		_stop = new AtomicBoolean(false);
	}
	
	private void startConnection() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(_port);
			while (!_stop.get()) {

				// blocks here until a client attempts to connect
				final Socket s = ss.accept();

				// create a task for handling the connection, and
				// pass to the executor
				Runnable task = new Runnable() {
					public void run() {
						try {
							acceptRequest(s);
						} catch (IOException e) {
							System.out.println(e);
						}
					}
				};
				new Thread(task).start();

			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("Server stopped");
	}
	
	/**
	 * This method is performed in a separate thread, to allow the server to keep listening
	 * @param s
	 * @throws IOException
	 */
	private void acceptRequest(Socket s) throws IOException {
		try {
			// We expect line-oriented text input, so wrap the input stream
			// in a Scanner

			// while (scanner.hasNextLine()) {

			ObjectInputStream stream = (ObjectInputStream) s.getInputStream();
			try {
				ServerCommand cmd = (ServerCommand) stream.readObject();
				
				
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} finally {
			// close the connection in a finally block
			s.close();

		}
	}
	
	public void sendReply(ServerCommandWrapper scw){
		
	}
}
