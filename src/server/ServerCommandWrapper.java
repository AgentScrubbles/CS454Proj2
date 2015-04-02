package server;

import java.net.Socket;

public class ServerCommandWrapper {
	
	private final Socket _socket;
	private ServerCommand _sentCmd;
	private ServerCommand _returnCmd;
	private final int _id;
	
	public ServerCommandWrapper(ServerCommand cmd, Socket s){
		_sentCmd = cmd;
		_socket = s;
		_id = IDGenerator.generate();
	}
	
	public synchronized void setReturnCmd(ServerCommand completeCommand){
		this._returnCmd = completeCommand;
	}
	
	public synchronized ServerCommand getReturnCommand(){
		return _returnCmd;
	}
	
	public synchronized ServerCommand getSendCommand(){
		return _sentCmd;
	}
	
	public synchronized Socket getReplySocket(){
		return _socket;
	}
	
	public synchronized boolean isComplete(){
		return _returnCmd != null;
	}
	
	@Override
	public int hashCode(){
		return _id;
	}
}
