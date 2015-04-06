package client;

import java.io.IOException;

import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		FileHandle handle = new FileHandle();
		String fileName = "testTextFile";
		byte[] data = new byte[4];
		
		ServerCommand sc = CommandFactory.newFileCommand("MyTestFile.txt");
		
		SocketConnection socket = new SocketConnection("localhost", 48182);
		ServerCommand finished = socket.request(sc);
		System.out.println(finished.command + ", " + finished.str);
	}

}
