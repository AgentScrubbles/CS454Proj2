package client;

import java.io.IOException;

import server.FileHandle;

public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		FileHandle handle = new FileHandle();
		String fileName = "testTextFile";
		byte[] data = new byte[4];
		
		ServerCommand sc = new ServerCommand(Command.OPEN, data, fileName, handle, null);
		
		SocketConnection socket = new SocketConnection("localhost", 48182);
		ServerCommand finished = socket.request(sc);
		System.out.println(finished.command + ", " + finished.str);
	}

}
