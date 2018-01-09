package com.wisebots.core.cache;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHashCache extends GameCache {
	
	public static Map<String, Object> cache1 = new ConcurrentHashMap<String, Object>();
	public static Map<String, Object> cache2 = new ConcurrentHashMap<String, Object>();
	private int player;

	public SimpleHashCache(String gamename, String method, int player) {
		this.player = player;
	}

	public int size() {
		if(player == 1){
			return cache1.size();
		}
		else  if(player == 2){
			return cache2.size();
		}
		
		return -1;
	}

	public void clear() {
		if(player == 1){
			cache1 = new ConcurrentHashMap<String, Object>();
		}
		else  if(player == 2){
			cache2 = new ConcurrentHashMap<String, Object>();
		}
	}

	public void set(int[] key, Object obj) {
		String k = Arrays.toString(key);
		if(obj instanceof Double){
			Double v = (Double)obj;
			if(v.doubleValue() == 0){
				return;
			}
		}
		
		if(player == 1){
			cache1.put(k, obj);
		}
		else  if(player == 2){
			cache2.put(k, obj);
		}
	}

	public Object get(String key){
		if(player == 1){
			Object obj = cache1.get(key);
			if(obj == null)
				return new Double(0);
			
			return cache1.get(key);
		}
		else  if(player == 2){
			Object obj = cache2.get(key);
			if(obj == null)
				return new Double(0);
			
			return cache2.get(key);
		}
		
		return null;
	}

	public void delete(String key) {
		if(player == 1){
			cache1.remove(key);
		}
		else if(player == 2){
			cache2.remove(key);
		}
	}
	
	public Map<String, Object> getContent(){
		if(player == 1){
			return cache1;
		}
		else if(player == 2){
			return cache2;
		}
		
		return null;
	}

	@Override
	public Object getSpecified(Integer id, String method, int[] fstate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
