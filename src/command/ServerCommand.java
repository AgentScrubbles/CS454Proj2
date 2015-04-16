package command;

import java.io.Serializable;

/**
 * This class wraps the Command enum with any required data that may be needed.  Both parties
 * know which data is needed and what can be ignored.  This is immutable, so it is safe to pass
 * knowing that data will not be corrupted.
 * @author Robert
 *
 */
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
