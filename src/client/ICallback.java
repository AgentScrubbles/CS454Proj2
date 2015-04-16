package client;

/**
 * Used for my async class
 * @author Robert
 *
 */
public interface ICallback {
	void done();
	void error(String error);
}
