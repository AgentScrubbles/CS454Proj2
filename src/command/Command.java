package command;

import java.io.Serializable;

public enum Command implements Serializable {
	OPEN,
	NEW,
	CLOSE,
	READ,
	WRITE,
	EOF,
	COMPLETE,
	ERROR;
}
