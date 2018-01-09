package com.wisebots.utils;

public class QueueThread implements Runnable{

	private long ttl = 0L;
	private String queuedir = "";
	
	public QueueThread(long ttl, String queuedir){
		this.ttl = ttl;
		this.queuedir = queuedir;
	}
	
	@Override
	public void run() {
		QueueExecutor.getInstance(ttl, queuedir);
	}

}
