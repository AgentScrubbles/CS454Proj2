package client;

import java.io.Serializable;

import server.FileHandle;

public class ServerCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8252737106450144374L;
	

	public final FileHandle handle;
	public final Command command;
	public final String fileName;
	public final byte[] data;
	public final String str;
	
	
	public ServerCommand(Command command, byte[] data, String fileName, FileHandle handle, String str){
		this.command = command;
		this.data = data;
		this.handle = handle;
		this.fileName = fileName;
		this.str = str;
	}
	
}
