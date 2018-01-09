package com.wisebots.core.cache;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.vipan.util.ExpiringCache;

public class ExpiringGameCache extends GameCache {

	public ExpiringCache cache;
	public List<URI> uris = new LinkedList<URI>();
	

	public ExpiringGameCache(String gamename, String method) {
		cache = new ExpiringCache(600000, 30000, 10000000, 600000);
		//cache.setTimeToLive(600000);  //1h
		//cache.setAccessTimeout(30000000);
		//cache.setCleaningInterval(600000);
	
	}

	public int size() {
		return cache.howManyObjects();
	}

	public void clear() {
		cache.clear();
	}

	public void set(int[] key, Object obj) {
		cache.admit(key, obj);
	}

	public Object get(String key){
		Object obj = cache.recover(key);
		if(obj == null)
			return new Double(0);
		
		return cache.recover(key);
	}

	public void delete(String key) {
		cache.discard(key);
	}
	


	@Override
	public HashMap<String, Object> getContent() {
		// TODO Auto-generated method stub
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
