package client;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import command.Command;
import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

/**
 * A simple to use Java API for the DFS file system.  Clients should only use this object
 * to connect to a server.
 * @author Robert
 *
 */
public class ConnectionAPI {
	private final SocketConnection connection;
	private ConcurrentHashMap<String, FileHandle> openFiles;
	
	/**
	 * Required ConnectionAPI constructor, must specify both port and host
	 * @param host
	 * 		Host of the server running.
	 * @param port
	 * 		Port of the server running.
	 */
	public ConnectionAPI(String host, int port){
		connection = new SocketConnection(host, port);
		openFiles = new ConcurrentHashMap<String, FileHandle>();
	}
	
	/**
	 * Get the current handle of an open file
	 * @param fileName
	 * 		Filename to retrieve
	 * @return
	 * 		Handle of that file, null if not open
	 */
	public FileHandle getCurrentHandle(String fileName){
		return openFiles.get(fileName);
	}
	
	public boolean fileIsOpen(String fileName){
		return openFiles.containsKey(fileName);
	}
	
	/**
	 * Creates a new file on the remote system.  Does NOT open the file
	 * @param fileName
	 * 		Filename of the file to create
	 * @return
	 * 		True if successful, false otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
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
	
	/**
	 * Opens a file for reading or writing
	 * @param fileName
	 * 		File to open
	 * @return
	 * 		True if successful, false otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean openFile(String fileName) throws ClassNotFoundException, IOException{
		ServerCommand cmd = CommandFactory.openFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			openFiles.put(fileName, cmd.handle);
			return true;
		}
		return false;
	}
	
	/**
	 * Closes a file after reading or writing
	 * @param fileName
	 * 		Name of file
	 * @return
	 * 		True if successful, False otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
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
	
	/**
	 * Read from the filename
	 * @param fileName
	 * 		Filename of the file
	 * @param amount
	 * 		Amount of data to read (will be length)
	 * @return
	 * 		Read data
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public byte[] readFile(String fileName, int amount) throws IOException, ClassNotFoundException{
		if(!openFiles.containsKey(fileName)){
			throw new IOException("File not opened!");
		}
		FileHandle handle = openFiles.get(fileName);
		ServerCommand cmd = CommandFactory.readFileCommand(fileName, handle, new byte[amount]);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			return complete.data;
		}
		return null;
	}
	
	public boolean writeFile(String fileName, byte[] data) throws ClassNotFoundException, IOException{
		if(!openFiles.containsKey(fileName)){
			throw new IOException("File not opened!");
		}
		FileHandle handle = openFiles.get(fileName);
		ServerCommand cmd = CommandFactory.writeFileCommand(fileName, handle, data);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			return true;
		}
		return false;
		
	}
}
