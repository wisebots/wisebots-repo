package com.wisebots.core.persistence;

import com.wisebots.constructor.BotConstructor;
import com.wisebots.constructor.ConstructorFactory;
import com.wisebots.core.cache.GameCache;

public class CloudGame implements Runnable{
	
	private String gameName;
	private int id;
	private GameCache gcache;
	private boolean create;
	
	public CloudGame(String gameName, int id, GameCache gcache, boolean create){
		this.gameName = gameName;
		this.id = id; 
		this.gcache = gcache;
		this.create = create;
	} 

	public void run() {
		try{
			System.out.println("Creating memory for import sqlite");
			BotConstructor sconstructor = ConstructorFactory.create(gameName, Integer.toString(id), "SQLITE", create);
			sconstructor.createMemory(gcache.getContent());
			sconstructor.close();
		}
		catch(Exception e){
			System.out.println("Erro tentando criar memoria no SQLITE");
			e.printStackTrace();
		}
	}

}
