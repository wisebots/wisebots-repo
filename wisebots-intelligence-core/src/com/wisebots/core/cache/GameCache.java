package com.wisebots.core.cache;

import java.util.Map;

public abstract class GameCache {
	
	public abstract int size();
	
	public abstract void clear();
	
	public abstract void close();
	
	public abstract void set(int[] key, Object obj);
	
	public abstract Object get(String key);
	
	public abstract Object getSpecified(Integer id, String method, int[] fstate);
	
	public abstract void delete(String key);
	
	public abstract Map<String, Object> getContent();
}
