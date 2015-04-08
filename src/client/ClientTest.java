package client;

import java.io.IOException;

import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		ConnectionAPI conn = new ConnectionAPI("localhost", 48182);
		
		conn.newFile("testFile.txt");
		conn.openFile("testFile.txt");
		
		String testString = "Hello, World!";
		byte[] data = testString.getBytes("UTF-8");
		
		conn.writeFile("testFile.txt", data);
		
		byte[] readData = conn.readFile("testFile.txt", data.length);
		
		System.out.println(new String(readData, "UTF-8"));
		
		conn.closeFile("testFile.txt");
	}

}
