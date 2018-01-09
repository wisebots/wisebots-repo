package com.wisebots.core.cache;

public final class CacheFactory {
	
	public static GameCache create(int cache, String gameName, String method, int player){
		GameCache gcache = null;
		
		if(cache == 4){
			gcache = new MembaseCache(gameName, method);  
		}
		else if(cache == 3){
			gcache = new SQLiteCache(gameName, method);  
		}
		else if(cache == 2){
			gcache = new LuceneCache(gameName, method);  
		}
		else if(cache == 1){
			gcache = new ExpiringGameCache(gameName, method);  
		}
		else{	
			gcache = new SimpleHashCache(gameName, method, player);
		}
		
		return gcache;
	}

}
