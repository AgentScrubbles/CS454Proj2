package command;


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
