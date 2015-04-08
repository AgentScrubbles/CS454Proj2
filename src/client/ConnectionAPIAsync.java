package client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Extends the ConnectionAPI to also give us Async methods.
 * 
 * This is experimental for my own doing
 * 
 * @author Robert
 *
 */
public class ConnectionAPIAsync extends ConnectionAPI {

	ExecutorService execService;

	public ConnectionAPIAsync(String host, int port) {
		super(host, port);
		execService = Executors.newCachedThreadPool();
	}

	public void newFileAsync(final String fileName, final ICallback callback) {
		execService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					newFile(fileName);
					callback.done();
				} catch (ClassNotFoundException | IOException e) {
					callback.error(e.getLocalizedMessage());
				}
			}

		});
	}

	public void openFileAsync(final String fileName, final ICallback callback) {
		execService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					openFile(fileName);
					callback.done();
				} catch (Exception ex) {
					callback.error(ex.getLocalizedMessage());
				}
			}

		});
	}

	public void closeFileAsync(final String fileName, final ICallback callback) {
		execService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					closeFile(fileName);
					callback.done();
				} catch (Exception e) {
					callback.error(e.getLocalizedMessage());
				}
			}

		});
	}

	public void writeFileAsync(final String fileName, final byte[] data,
			final ICallback callback) {
		execService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					writeFile(fileName, data);
					callback.done();
				} catch (Exception ex) {
					callback.error(ex.getLocalizedMessage());
				}
			}

		});
	}
}
