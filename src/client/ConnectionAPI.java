package client;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import command.Command;
import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

public class ConnectionAPI {
	private final SocketConnection connection;
	private ConcurrentHashMap<String, FileHandle> openFiles;
	
	public ConnectionAPI(String host, int port){
		connection = new SocketConnection(host, port);
		openFiles = new ConcurrentHashMap<String, FileHandle>();
	}
	
	public FileHandle getCurrentHandle(String fileName){
		return openFiles.get(fileName);
	}
	
	public boolean newFile(String fileName) throws ClassNotFoundException, IOException{
		if(openFiles.containsKey(fileName)){
			throw new IOException("File already opened! (Created)");
		}
		ServerCommand cmd = CommandFactory.newFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			return true;
		}
		return false;
	}
	
	public boolean openFile(String fileName) throws ClassNotFoundException, IOException{
		ServerCommand cmd = CommandFactory.openFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			openFiles.put(fileName, cmd.handle);
			return true;
		}
		return false;
	}
	
	public boolean closeFile(String fileName) throws ClassNotFoundException, IOException{
		if(!openFiles.containsKey(fileName)){
			//Not open!  Throw exception
			throw new IOException("File not opened!");
		}
		ServerCommand cmd = CommandFactory.closeFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			openFiles.remove(fileName);
			return true;
		}
		return false;
	}
}
