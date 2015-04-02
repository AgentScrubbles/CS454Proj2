package server;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
	private static AtomicInteger current = new AtomicInteger(Integer.MIN_VALUE);
	
	public static int generate(){
		return current.getAndIncrement();
	}
	
	
}
