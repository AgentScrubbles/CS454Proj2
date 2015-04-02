package server;

import java.io.Serializable;

public enum Command implements Serializable {
	OPEN,
	CLOSE,
	READ,
	WRITE,
	EOF,
	COMPLETE,
	ERROR;
}
