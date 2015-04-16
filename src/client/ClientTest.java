package client;

import java.io.IOException;

import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		ConnectionAPI conn = new ConnectionAPI("localhost", 48182);
		
		//Not working yet
		//conn.readEntireFile("testFile.txt");
		
		//Open the file...
		if(!conn.openFile("testFile.txt")){
			System.out.println("File not able to open.  Has it been created?");
			conn.newFile("testFile.txt");
			conn.openFile("testFile.txt");
		}
		long size = conn.sizeOfFile("testFile.txt");
		System.out.println("Size of testFile.txt is " + size);
		byte[] data = conn.readFile("testFile.txt", (int) size);

		System.out.println(new String(data, "UTF-8"));
		conn.closeFile("testFile.txt");
	}

}
