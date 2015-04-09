package command;

/**
 * Factory class to make the special serializable 'ServerCommand' types.  Since each one has it's own properties
 * and options, it ended up being much easier to instantiate it within this factory for each type.  This
 * makes the Server and Client API's much more similar.
 * 
 * For example, the 'complete' message does not need a file handle or file name, the fact that it is 'complete'
 * is enough for the client to know.  Where the write message must provide the handle, the filename, and the
 * data to write.  This will enforce these rules.
 * @author Robert
 *
 */
public class CommandFactory {
	public static ServerCommand newFileCommand(String fileName){
		return new ServerCommand(Command.NEW, null, fileName, new FileHandle(), "");
	}
	
	public static ServerCommand readFileCommand(String fileName, FileHandle handle, byte[] arr){
		return new ServerCommand(Command.READ, arr, fileName, handle, "");
	}
	
	public static ServerCommand writeFileCommand(String fileName, FileHandle handle, byte[] arr){
		return new ServerCommand(Command.WRITE, arr, fileName, handle, "");
	}
	
	public static ServerCommand openFileCommand(String fileName){
		return new ServerCommand(Command.OPEN, null, fileName, new FileHandle(), "");
	}
	
	public static ServerCommand closeFileCommand(String fileName){
		return new ServerCommand(Command.CLOSE, null, fileName, new FileHandle(), "");
	}
	
	public static ServerCommand completeFileCommand(String fileName, String message){
		return new ServerCommand(Command.COMPLETE, null, fileName, new FileHandle(), message);
	}
	
	public static ServerCommand completeFileCommand(String fileName, String message, FileHandle handle){
		return new ServerCommand(Command.COMPLETE, null, fileName, handle, message);
	}
	
	public static ServerCommand errorFileCommand(String fileName, String message){
		return new ServerCommand(Command.ERROR, null, fileName, new FileHandle(), message);
	}
	
	public static ServerCommand errorFileCommand(String fileName, Exception e){
		return new ServerCommand(Command.ERROR, null, fileName, new FileHandle(), e.getLocalizedMessage());
	}
}
