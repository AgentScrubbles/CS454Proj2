package client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerRequestFuture implements Future<ServerCommand>{

	private ServerCommand completedObject;
	private AtomicBoolean isCancelled;
	
	
	public ServerRequestFuture(){
		isCancelled = new AtomicBoolean(false);
	}
	
	public synchronized void set(ServerCommand cmd){
		completedObject = cmd;
		notifyAll();
	}
	
	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		isCancelled.set(true);
		notifyAll();
		return true;
	}

	@Override
	public synchronized ServerCommand get() throws InterruptedException, ExecutionException {
		while(!isCancelled.get() && completedObject == null){
			wait();
		}
		if(isCancelled.get()){
			//TODO
		}
		return completedObject;
	}

	@Override
	public synchronized ServerCommand get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		long currentTime = System.currentTimeMillis();
		while(!isCancelled.get() && completedObject == null && (System.currentTimeMillis() - currentTime < timeout)){
			wait(timeout);
		}
		if(System.currentTimeMillis() - currentTime < timeout){
			throw new TimeoutException();
		}
		if(isCancelled.get()){
			//TODO
		}
		return completedObject;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled.get();
	}

	@Override
	public synchronized boolean isDone() {
		return completedObject != null;
	}


}
