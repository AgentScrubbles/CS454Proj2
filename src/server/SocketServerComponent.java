package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import command.ServerCommand;

/**
 * The component used to listen for incoming connections.  This will call the file manipulator
 * @author Robert
 *
 */
public class SocketServerComponent {
	
	private final int _port;
	private final AtomicBoolean _stop;
	private final FileManipulator fileManip;
	public SocketServerComponent(int port, FileManipulator fileManip){
		_port = port;
		this.fileManip = fileManip;
		_stop = new AtomicBoolean(false);
	}
	
	
	/**
	 * After creation, this will start the listener
	 */
	public void start(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				startConnection();
			}
			
		}).start();
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
		System.out.println("Request received from " + s.getInetAddress().toString());
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			inputStream = new ObjectInputStream(s.getInputStream());
			outputStream = new ObjectOutputStream(s.getOutputStream());
			try {
				ServerCommand cmd = (ServerCommand) inputStream.readObject();
				ServerCommandWrapper wrapper = new ServerCommandWrapper(cmd, s);
				fileManip.handle(wrapper);
				outputStream.writeObject(wrapper.getReturnCommand());
				outputStream.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} finally {
			// close the connection in a finally block
			if(inputStream != null){
				inputStream.close();
			}
			if(outputStream != null){
				outputStream.close();
			}
			s.close();

		}
	}
	
	public void sendReply(ServerCommandWrapper scw){
		
	}
}
