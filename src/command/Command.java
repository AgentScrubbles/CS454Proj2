package command;

import java.io.Serializable;

/**
 * The basic commands needed for all functions.
 * @author Robert
 *
 */
public enum Command implements Serializable {
	OPEN,
	SIZE,
	NEW,
	CLOSE,
	READ,
	WRITE,
	EOF,
	COMPLETE,
	ERROR;
}
