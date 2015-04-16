package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Future;

import command.ServerCommand;

public class SocketConnection {
	private final String _host;
	private final int _port;
	
	public SocketConnection(String host, int port){
		_host = host;
		_port = port;
	}
	
	public ServerCommand request(ServerCommand input) throws IOException, ClassNotFoundException{
		Socket s = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		ServerCommand output = null;
		try{
			s = new Socket(_host, _port);
			outputStream = new ObjectOutputStream(s.getOutputStream());
			outputStream.writeObject(input);
			inputStream = new ObjectInputStream(s.getInputStream());
			output = (ServerCommand) inputStream.readObject();
		} finally {
			if(outputStream != null){
				outputStream.close();
			}
			if(inputStream != null){
				inputStream.close();
			}
			if(s != null){
				s.close();
			}
		}
		return output;
	}
	
	
	/**
	 * Non functional, does not work without async working
	 * @param input
	 * @return
	 */
	public Future<ServerCommand> requestAsync(final ServerCommand input){
		//final ServerRequestFuture f = new ServerRequestFuture();
		Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					ServerCommand finished = request(input);
					f.set(finished); //Notifies any pending threads
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		new Thread(r).start();
		return f; //Returns immediately
	}
	
}
