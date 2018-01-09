package com.wisebots.core.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.wisebots.utils.MemoryIdentifier;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

public class MembaseCache extends GameCache {

	public MemcachedClient memcachedClient;
	private int counter = 0;

	public MembaseCache(String gamename, String method) {
		AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler("default", ""));
		ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
		ConnectionFactory cf = factoryBuilder.setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build();

	    try {
			memcachedClient = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress("localhost", 11211)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public int size() {
		return counter;
	}

	public void clear() {
		
	}

	public void set(int[] key, Object obj) {
		boolean trying = true;
		while(trying){
			try{
				if(memcachedClient.get(Arrays.toString(key)) == null){
					memcachedClient.add(Arrays.toString(key), 1, obj);
					counter++;
				}
				else{
					memcachedClient.replace(Arrays.toString(key), 0, obj);
				}
				trying = false;
			}
			catch(Exception e){
				System.out.println("Trying again with membase: " + e.getMessage());
			}
		}
	}

	public Object get(String key){
		boolean trying = true;
		Object obj = null;
		while(trying){
			try{
				obj = memcachedClient.get(key);
				if(obj == null){
					return new Double(0);
				}
				trying = false;
			}
			catch(Exception e){
				System.out.println("Trying again with membase: " + e.getMessage());
			}
		}
		return obj;
	}
	
	public Object getSpecified(Integer id, String method, int[] fstate){
		String key = MemoryIdentifier.getMemeName(id, method, fstate);
		Object obj = null;
		boolean trying = true;
		while(trying)
			try{
				obj = memcachedClient.get(key);
				if(obj == null){
					return new Double(0);
				}
				trying = false;
			}
			catch(Exception e){
				System.out.println("Trying again with membase: " + e.getMessage());
			}
		
		return obj;
	}

	public void delete(String key) {
		memcachedClient.delete(key);
	}

	@Override
	public HashMap<String, Object> getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}

