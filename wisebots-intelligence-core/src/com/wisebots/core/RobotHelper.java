package com.wisebots.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.wisebots.core.cache.MemoryCache;
import com.wisebots.dataset.BotProfile;
import com.wisebots.utils.Utils;

public class RobotHelper {
	
	private static RobotHelper instance;
	
	public static RobotHelper getInstance(){
		if(instance == null){
			instance= new RobotHelper();
			return instance;
		}
		
		return instance;
	}
	
	private RobotHelper(){
		
	}
	 
	private Integer getNewIdRobot(){
		MemoryCache memory = MemoryCache.getInstance(true);
		Object obj = memory.getFromMemory(RobotIdentifier.getRobotIdName()) ;
		Integer id = new Integer(0);
		if(obj instanceof Integer){
			id = (Integer)memory.getFromMemory(RobotIdentifier.getRobotIdName());
			if(id == null){
				memory.putOnMemory(RobotIdentifier.getRobotIdName(),  new Integer(1));
				return 1;
			}
		}
		
		id = new Integer(id+1);
		memory.putOnMemory(RobotIdentifier.getRobotIdName(), id);
		return id;
	}
	
	public Integer countRobots(){
		MemoryCache memory = MemoryCache.getInstance(true);
		Integer count = new Integer(0);
		Object id = memory.getFromMemory(RobotIdentifier.getRobotIdName());
		if(id == null){
			return count;
		}
		if(id instanceof Integer){
			count = (Integer)id;
		}
		
		return count;
	}
	
	public List<BotProfile> listRobots(){
		List<BotProfile> botProfiles = new ArrayList<BotProfile>();
		Gson gson = new Gson();
		
		MemoryCache memory = MemoryCache.getInstance(true);
		int nuRobots = countRobots();
		System.out.println("Nurobots: " + nuRobots);
		for(int i=1; i<=nuRobots; i++){
			String key = RobotIdentifier.getRobotProfileName(i);
			System.out.println("key=" + key);
			
			String gBotProfile = (String)memory.getFromMemory(key);
			System.out.println("gbot: " + gBotProfile);
			
			Double iquality = getLastQualityRobot(i);
			if(iquality == null)
				iquality = new Double(0);
			
			BotProfile botProfile = gson.fromJson(gBotProfile, BotProfile.class);
			if(botProfile != null){
				botProfile.setQuality(iquality);
				botProfiles.add(botProfile);
			}
		}
		
		return botProfiles;
	}
	
	public synchronized int insertNewRobot(String name){
		MemoryCache memory = MemoryCache.getInstance(true);
		Integer idRobot = getNewIdRobot();
		String robotName = (String)memory.getFromMemory(RobotIdentifier.getRobotName(idRobot));
		
		if(robotName != null){
			System.out.println("Esse nome ja existe: " + robotName);
			return 0;
		}
		
	    memory.putOnMemory(RobotIdentifier.getRobotName(idRobot), name);
	    Utils.debugLog(this.getClass(), "RobotName created: " + RobotIdentifier.getRobotName(idRobot) + "=" + name);
	    
	    return idRobot;
	}
	
	public void insertNewRobotProfile(Integer idRobot, BotProfile profile){
		MemoryCache memory = MemoryCache.getInstance(true);

		profile.setId(idRobot.toString());
		String robotProfileName = RobotIdentifier.getRobotProfileName(idRobot);
		
		Gson gson = new Gson();
		String gBotProfile = gson.toJson(profile);
		
	    memory.putOnMemory(robotProfileName, gBotProfile);
	}
	
	public void insertStatusRobot(Integer idRobot, String status){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getRobotStatusName(idRobot), status);
	}
	
	public void insertLastCheckRobot(Integer idRobot, String lastCheck){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getRobotLastCheckName(idRobot), lastCheck);
	}
	
	public void insertLastQualityRobot(Integer idRobot, Double iquality){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getRobotLastQualityName(idRobot), iquality);
	}
	
	public void insertFinishedRobot(Integer idRobot, String date){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getFinishedDateName(idRobot), date);
	}
	
	public void insertFinishedMemoryRobot(Integer idRobot, String date){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getFinishedMemoryDateName(idRobot), date);
	}
	
	public void insertCreationRobot(Integer idRobot, String date){
		MemoryCache memory = MemoryCache.getInstance(true);
	    memory.putOnMemory(RobotIdentifier.getCreationDateName(idRobot), date);
	}
	
	
	public Double getLastQualityRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    Double iquality = (Double)memory.getFromMemory(RobotIdentifier.getRobotLastQualityName(idRobot));
	    return iquality;
	}
	
	public String getLastCheckRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    String lastCheck = (String)memory.getFromMemory(RobotIdentifier.getRobotLastCheckName(idRobot));
	    return lastCheck;
	}
	
	public String getStatusRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    String status = (String)memory.getFromMemory(RobotIdentifier.getRobotStatusName(idRobot));
	    return status;
	}
	
	public String getFinishedDateRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    String finished = (String)memory.getFromMemory(RobotIdentifier.getFinishedDateName(idRobot));
	    return finished;
	}
	
	public String getFinishedMemoryDateRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    String finished = (String)memory.getFromMemory(RobotIdentifier.getFinishedMemoryDateName(idRobot));
	    return finished;
	}
	
	public String getCreationDateRobot(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
	    String creation = (String)memory.getFromMemory(RobotIdentifier.getCreationDateName(idRobot));
	    return creation;
	}
	
	
	public BotProfile getBotProfile(Integer idRobot){
		MemoryCache memory = MemoryCache.getInstance(true);
		String key = RobotIdentifier.getRobotProfileName(idRobot);
		System.out.println("key=" + key);
		
		String gBotProfile = (String)memory.getFromMemory(key);
		System.out.println("gbot: " + gBotProfile);

		Gson gson = new Gson();
		BotProfile botProfile = gson.fromJson(gBotProfile, BotProfile.class);
		
		return botProfile;
	}
}
