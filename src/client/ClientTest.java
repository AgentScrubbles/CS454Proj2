package client;

import java.io.IOException;

import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;


/**
 * My very basic test class.  Opens a file, the reads to it.
 * @author Robert
 *
 */
public class ClientTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		ConnectionAPI conn = new ConnectionAPI("localhost", 48182);
		
		//Not working yet
		//conn.readEntireFile("testFile.txt");
		
		//Open the file...
		if(!conn.openFile("testFile.txt")){
			System.out.println("File not able to open.  Has it been created?");
			conn.newFile("testFile.txt"); //Create a new file if not already created
			conn.openFile("testFile.txt"); //Open the file, since creating a new file does not open it
		}
		long size = conn.sizeOfFile("testFile.txt"); //What's the size of it?
		System.out.println("Size of testFile.txt is " + size);
		byte[] data = conn.readFile("testFile.txt", (int) size); //Read the entire file

		System.out.println(new String(data, "UTF-8")); //Print it..
		conn.closeFile("testFile.txt"); //Make sure to close!
	}

}
