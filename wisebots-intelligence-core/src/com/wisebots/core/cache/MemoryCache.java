package com.wisebots.core.cache;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sleepycat.je.DatabaseException;
import com.wisebots.core.Meme;
import com.wisebots.utils.PersistedQueue;
import com.wisebots.utils.QueueController;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.OperationTimeoutException;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class MemoryCache{
	
    private static final Logger logger = Logger.getLogger(QueueController.class);
    private static MemoryCache instance = null;
	//private PersistedQueue queue = null;
    
    public static final String QUEUE_AUDITING_NAME = "memory-queue";
    public static final int QUEUE_CACHE = 0;

	public MemcachedClient memcachedClient;
	
	public static MemoryCache getInstance(){
		if(instance == null){
			instance = new MemoryCache();
			return instance;
		}
		
		return instance;
	}
	
	public static MemoryCache getInstance(boolean membase){
		if(instance == null){
			instance = new MemoryCache(membase);
			return instance;
		}
		
		return instance;
	}
	
	private MemoryCache(){

	}

	private MemoryCache(boolean membase) {
		AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler("default", ""));
		ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
		ConnectionFactory cf = factoryBuilder.setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build();

	    try {
			memcachedClient = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress("localhost", 11211)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public MemoryCache(String ip, boolean membase) {
        
		AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler("default", ""));
		ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
		ConnectionFactory cf = factoryBuilder.setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build();

	    try {
			memcachedClient = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress(ip, 11211)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void putOnQueue(String key, Object obj){

		PersistedQueue queue = null;
	  	File queueDir = new File("/usr/local/wisebots/queue");
        try {
			queue = new PersistedQueue(queueDir.getPath(), QUEUE_AUDITING_NAME, QUEUE_CACHE);
			
			Meme meme = new Meme();
			meme.setKey(key);
			meme.setValue((Double)obj);

	        try { 	
	    	    Gson gson = new Gson();
	    	    String sgson = gson.toJson(meme);
	        	queue.push(sgson);
	        }
	        catch (Exception e) {
	        	logger.error("Erro no empacotamento para fila", e);
			}
		} 
        catch (DatabaseException e) {
			e.printStackTrace();
		}
        finally {
            try {
				queue.close();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	public void putOnMemory(String key, Object obj) {
		key = key.replace(" ", "");
		boolean persistent = true;
		
		if(memcachedClient == null){
			System.err.println("ERROR: putOnMemory: Falha na memoria do bot");
			return;
		}
		
		while(persistent){
			try{
				if(memcachedClient.get(key) == null){
					// System.out.println("Put: " + key + "=" + obj);
					memcachedClient.add(key, 0, obj); 
		
				}
				else{
					// System.out.println("Replace: " + key + "=" + obj);
					memcachedClient.replace(key, 0, obj);
				}
				persistent = false;
			}
			catch(OperationTimeoutException timeout){
				System.err.println("Persistindo: " + timeout.getMessage());
			}
		}
	}
	
	public Object getFromMemory(String key){
		boolean persistent = true;
		
		if(memcachedClient == null){
			System.err.println("ERROR: getFromMemory: Falha na memoria do bot");
			return null;
		}
		
		Object obj = null;
		while(persistent){
			try{
				obj = memcachedClient.get(key);
				persistent = false;	
			}
			catch(OperationTimeoutException timeout){
				System.err.println("Persistindo: " + timeout.getMessage());
			}
		}
		
		return obj;
	}

	public Object get(String key){
		boolean persistent = true;
	
		if(memcachedClient == null){
			System.err.println("ERROR: get: Falha na memoria do bot");
			return new Double(0);
		}
		
		Object obj = null;
		while(persistent){
			try{
				obj = memcachedClient.get(key);
				if(obj == null){
					return new Double(0);
				}
				persistent = false;
			}
			catch(OperationTimeoutException timeout){
				System.err.println("Persistindo: " + timeout.getMessage());
			}
		}
		
		return memcachedClient.get(key);
	}

	public void delete(String key) {
		if(memcachedClient == null){
			System.err.println("ERROR: delete: Falha na memoria do bot");
			return;
		}
		memcachedClient.delete(key);
	}
}