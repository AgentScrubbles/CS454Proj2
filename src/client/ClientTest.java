package client;

import java.io.IOException;

import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		ConnectionAPI conn = new ConnectionAPI("localhost", 48182);
		
		conn.readEntireFile("testFile.txt");
	}

}
