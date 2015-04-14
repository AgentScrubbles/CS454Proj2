package client;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import command.Command;
import command.CommandFactory;
import command.FileHandle;
import command.ServerCommand;

/**
 * A simple to use Java API for the DFS file system. Clients should only use
 * this object to connect to a server.
 * 
 * @author Robert
 *
 */
public class ConnectionAPI {
	private final SocketConnection connection;
	private ConcurrentHashMap<String, FileHandle> openFiles;

	/**
	 * Required ConnectionAPI constructor, must specify both port and host
	 * 
	 * @param host
	 *            Host of the server running.
	 * @param port
	 *            Port of the server running.
	 */
	public ConnectionAPI(String host, int port) {
		connection = new SocketConnection(host, port);
		openFiles = new ConcurrentHashMap<String, FileHandle>();
	}

	/**
	 * Get the current handle of an open file
	 * 
	 * @param fileName
	 *            Filename to retrieve
	 * @return Handle of that file, null if not open
	 */
	public FileHandle getCurrentHandle(String fileName) {
		return openFiles.get(fileName);
	}

	public boolean fileIsOpen(String fileName) {
		return openFiles.containsKey(fileName);
	}

	/**
	 * Creates a new file on the remote system. Does NOT open the file
	 * 
	 * @param fileName
	 *            Filename of the file to create
	 * @return True if successful, false otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean newFile(String fileName) throws ClassNotFoundException,
			IOException {
		if (openFiles.containsKey(fileName)) {
			throw new IOException("File already opened! (Created)");
		}
		ServerCommand cmd = CommandFactory.newFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if (complete.command == Command.COMPLETE) {
			return true;
		}
		return false;
	}

	public void newFileAsync(final String fileName, final ICallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					newFile(fileName);
					callback.done();
				} catch (ClassNotFoundException | IOException e) {
					callback.error(e.getLocalizedMessage());
				}
			}

		}).start();
	}

	/**
	 * Opens a file for reading or writing
	 * 
	 * @param fileName
	 *            File to open
	 * @return True if successful, false otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean openFile(String fileName) throws ClassNotFoundException,
			IOException {
		ServerCommand cmd = CommandFactory.openFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if (complete.command == Command.COMPLETE) {
			openFiles.put(fileName, cmd.handle);
			return true;
		}
		return false;
	}

	public void openFileAsync(final String fileName, final ICallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					openFile(fileName);
					callback.done();
				} catch (Exception ex) {
					callback.error(ex.getLocalizedMessage());
				}
			}

		}).start();
	}

	/**
	 * Closes a file after reading or writing
	 * 
	 * @param fileName
	 *            Name of file
	 * @return True if successful, False otherwise
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean closeFile(String fileName) throws ClassNotFoundException,
			IOException {
		if (!openFiles.containsKey(fileName)) {
			// Not open! Throw exception
			throw new IOException("File not opened!");
		}
		ServerCommand cmd = CommandFactory.closeFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if (complete.command == Command.COMPLETE) {
			openFiles.remove(fileName);
			return true;
		}
		return false;
	}

	public void closeFileAsync(final String fileName, final ICallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					closeFile(fileName);
					callback.done();
				} catch (Exception e) {
					callback.error(e.getLocalizedMessage());
				}
			}

		}).start();
	}
	
	/**
	 * Test I did, see if I could make this read an entire file by running chunks.
	 * 
	 * I have not tested with files whose size overflows a standard int
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public byte[] readEntireFile(String fileName) throws ClassNotFoundException, IOException{
		openFile(fileName); //Open the file
		long size = sizeOfFile(fileName);
		byte[] file = new byte[(int) size]; //entire file of data
		int blockSize = 20; //Can be any integer, 20 sounded good to me
		int i = 0;
		for(i = 0; i < size; i += blockSize){ //Go through entire size
			byte[] data = readFile(fileName, i, blockSize); //Data read by this block
			int count = 0;
			for(int j = i; j < blockSize; j++){ //Copy local block to file
				file[j] = data[count++];
			}
		}
		if(i < size){ //Copy remaining data to file
			blockSize = (int) (size - i);
			byte[] data = readFile(fileName, i, blockSize); //Read last off block
			int count = 0;
			for(int j = i; j < blockSize; j++){ //Copy into final sections of file
				file[j] = data[count++];
			}
		}
		closeFile(fileName);
		return file;
	}
	
	/**
	 * Gets the size of the file, file may be open or closed.
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public long sizeOfFile(String fileName) throws ClassNotFoundException, IOException{
		ServerCommand cmd = CommandFactory.sizeFileCommand(fileName);
		ServerCommand complete = connection.request(cmd);
		if(complete.command == Command.COMPLETE){
			return Long.parseLong(complete.str);
		}
		return -1;
	}

	/**
	 * Read from the filename
	 * 
	 * @param fileName
	 *            Filename of the file
	 * @param amount
	 *            Amount of data to read (will be length)
	 * @return Read data
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public byte[] readFile(String fileName, int amount) throws IOException,
			ClassNotFoundException {
		return readFile(fileName, amount, 0);
	}
	
	public byte[] readFile(String fileName, int amount, int offset) throws ClassNotFoundException, IOException{
		if (!openFiles.containsKey(fileName)) {
			throw new IOException("File not opened!");
		}
		FileHandle handle = openFiles.get(fileName);
		ServerCommand cmd = CommandFactory.readFileCommand(fileName, handle,
				new byte[amount]);
		ServerCommand complete = connection.request(cmd);
		if (complete.command == Command.COMPLETE) {
			return complete.data;
		}
		return null;
	}

	public boolean writeFile(String fileName, byte[] data)
			throws ClassNotFoundException, IOException {
		if (!openFiles.containsKey(fileName)) {
			throw new IOException("File not opened!");
		}
		FileHandle handle = openFiles.get(fileName);
		ServerCommand cmd = CommandFactory.writeFileCommand(fileName, handle,
				data);
		ServerCommand complete = connection.request(cmd);
		if (complete.command == Command.COMPLETE) {
			return true;
		}
		return false;

	}

	public void writeFileAsync(final String fileName, final byte[] data,
			final ICallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					writeFile(fileName, data);
					callback.done();
				} catch (Exception ex) {
					callback.error(ex.getLocalizedMessage());
				}
			}

		}).start();
	}
}
