package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import client.Command;
import client.ServerCommand;

public class FileManipulator {

	private ConcurrentHashMap<String, FileInputStream> currentHandles;

	public FileManipulator() {
		currentHandles = new ConcurrentHashMap<String, FileInputStream>();
	}

	public void handle(ServerCommandWrapper scw) {
		try {
			switch (scw.getSendCommand().command) {

			case OPEN:
				openCommand(scw);
				break;
			case READ:
				readCommand(scw);
				break;

			case WRITE:
				writeCommand(scw);
				break;
			case CLOSE:
				closeCommand(scw);
				break;
			case EOF:
				eofCommand(scw);
				break;
			case NEW:
				newCommand(scw);
			case COMPLETE:
			case ERROR:
			default:
				errorCommand(scw, new Exception("Bad Command!"));
				break;
			}
		} catch (Exception ex) {
			errorCommand(scw, ex);
		}
	}

	private synchronized void openCommand(ServerCommandWrapper scw)
			throws FileNotFoundException {
		String fileName = scw.getSendCommand().fileName;
		FileInputStream in = new FileInputStream(new File(
				scw.getSendCommand().fileName));
		currentHandles.put(fileName, in);
		FileHandle fh = new FileHandle();
		ServerCommand returnCommand = new ServerCommand(Command.COMPLETE, null,
				fileName, fh, "");
		scw.setReturnCmd(returnCommand);
	}

	private synchronized int readCommand(ServerCommandWrapper scw)
			throws IOException {
		String fileName = scw.getSendCommand().fileName;
		ServerCommand sentCommand = scw.getSendCommand();
		FileInputStream in = currentHandles.get(fileName);
		if (in == null) {
			throw new IOException("File not opened!");
		}
		ServerCommand returnCommand = new ServerCommand(Command.COMPLETE,
				sentCommand.data, fileName, sentCommand.handle, "");
		int res = in.read(returnCommand.data);
		scw.setReturnCmd(returnCommand);
		return res;
	}

	private synchronized void writeCommand(ServerCommandWrapper scw)
			throws IOException {
		ServerCommand sent = scw.getSendCommand();
		FileInputStream in = currentHandles.get(sent.fileName);
		in.close();
		currentHandles.remove(sent.fileName);
		FileOutputStream out = new FileOutputStream(new File(sent.fileName));
		out.write(sent.data);
		out.close();
		in = new FileInputStream(new File(sent.fileName));
		currentHandles.put(sent.fileName, in);
		ServerCommand reply = new ServerCommand(Command.COMPLETE, null,
				sent.fileName, sent.handle, "");
		scw.setReturnCmd(reply);
	}

	private synchronized void closeCommand(ServerCommandWrapper scw)
			throws IOException {
		ServerCommand sent = scw.getSendCommand();
		FileInputStream in = currentHandles.get(sent.fileName);
		in.close();
		currentHandles.remove(sent.fileName);
		ServerCommand reply = new ServerCommand(Command.COMPLETE, null,
				sent.fileName, sent.handle, "");
		scw.setReturnCmd(reply);
	}
	
	private synchronized void newCommand(ServerCommandWrapper scw){
		ServerCommand sent = scw.getSendCommand();
		File newFile = new File(sent.fileName);
		try {
			newFile.createNewFile();
			ServerCommand complete = new ServerCommand(Command.COMPLETE, null, sent.fileName, sent.handle, "");
			scw.setReturnCmd(complete);
		} catch (IOException e) {
			errorCommand(scw, e);
		}
		
	}

	private synchronized void eofCommand(ServerCommandWrapper scw)
			throws IOException {
		ServerCommand sent = scw.getSendCommand();
		FileInputStream in = currentHandles.get(sent.fileName);
		boolean eof = in.available() == 0;
		ServerCommand reply = new ServerCommand(Command.COMPLETE, null,
				sent.fileName, sent.handle, Boolean.toString(eof));
		scw.setReturnCmd(reply);

	}

	private synchronized void errorCommand(ServerCommandWrapper scw,
			Exception ex) {
		ServerCommand sent = scw.getSendCommand();
		ServerCommand reply = new ServerCommand(Command.ERROR, null,
				sent.fileName, sent.handle, ex.getMessage());
		scw.setReturnCmd(reply);
	}

}
