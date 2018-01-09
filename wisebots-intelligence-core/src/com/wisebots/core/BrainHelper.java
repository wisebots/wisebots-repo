package com.wisebots.core;

import java.util.Map;
import java.util.Set;

import com.wisebots.core.cache.GameCache;
import com.wisebots.core.cache.MemoryCache;
import com.wisebots.utils.MemoryIdentifier;
import com.wisebots.utils.Utils;

public class BrainHelper {
	
	private static BrainHelper instance;
		
	public static BrainHelper getInstance(){
		if(instance == null){
			instance = new BrainHelper();
			return instance;
		}
		
		return instance;
	}
	
	private BrainHelper(){
		
	}
	
	public void insertMeme(GameCache gcache, Integer id, String method){
		Utils.debugLog(this.getClass(), "Gamecache " + gcache.size());
		Map<String,Object> qvalues = gcache.getContent();
		Set<String> keys = qvalues.keySet();
	    MemoryCache memory = MemoryCache.getInstance(true);
		for(String key: keys){
			Utils.debugLog(this.getClass(), "Accessing neuron: " + key);
			Double value = (Double)qvalues.get(key);
			if(value!=0){
				String name = MemoryIdentifier.getMemeName(id, method, key);
				memory.putOnMemory(name, value);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
			}
			else{
				Utils.debugLog(this.getClass(), "Neuron 0 ignored: " + key);
			}
		}
	}
	
	public void insertPartialStatusMeme(Integer id, String method, Double value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getPartialKBName(id, method);
		if(value!=0){
			memory.putOnMemory(name, value);
		}
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public void insertFinalStatusMeme(Integer id, String method, Double value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getFinalKBName(id, method);
		if(value!=0){
			memory.putOnMemory(name, value);
		}
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public void insertPause(Integer id, String method, String pause){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getPauseName(id, method);

		memory.putOnMemory(name, pause);
		
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + pause);
	}
	
	public void insertPartialStatusMemory(Integer id, String method, Double value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getPartialMemoryName(id, method);
		if(value!=0){
			memory.putOnMemory(name, value);
		}
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public void insertFinalStatusMemory(Integer id, String method, Double value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getFinalMemoryName(id, method);
		if(value!=0){
			memory.putOnMemory(name, value);
		}	
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public void insertCacheSize(Integer id, String method, Integer size){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getCacheSizeName(id, method);
	    memory.putOnMemory(name, size);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + size);
	}
	
	public void insertTrainingAxisX(Integer id, String method, String value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String axisx = getTrainingAxisx(id, method);
		
		String name = MemoryIdentifier.getTrainingAxisX(id, method);
		if(axisx.equals("")){
			axisx += value;
		}
		else{
			axisx += "," + value ;
		}
		memory.putOnMemory(name, axisx);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public String getTrainingAxisx(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTrainingAxisX(id, method);
	    String axisx = (String)memory.getFromMemory(name);
	    if(axisx == null || axisx.equals("")){
	    	axisx = "";
	    }
	
		if(axisx.length() > 0 && axisx.charAt(axisx.length()-1) == ','){
			axisx = axisx.substring(0,axisx.length()-1);
		}
		
	    return axisx;
	}
	
	public void insertTrainingAxisY(Integer id, String method, long value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String axisy = getTrainingAxisy(id, method);
		
		String name = MemoryIdentifier.getTrainingAxisY(id, method);
		if(value<0){
			axisy += "| ";
		}
		else{
			axisy += "|" + value;
		}
		memory.putOnMemory(name, axisy);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public String getTrainingAxisy(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTrainingAxisY(id, method);
	    String axisy = (String)memory.getFromMemory(name);
	    if(axisy == null || axisy.equals("")){
	    	axisy = "";
	    }
		
	    return axisy;
	}
	
	public void insertTestingAxisX(Integer id, String method, String value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String axisx = getTestingAxisx(id, method);
		
		String name = MemoryIdentifier.getTestingAxisX(id, method);
		if(axisx.equals("")){
			axisx += value;
		}
		else{
			axisx += "," + value ;
		}
		memory.putOnMemory(name, axisx);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public String getTestingAxisx(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTestingAxisX(id, method);
	    String axisx = (String)memory.getFromMemory(name);
	    if(axisx == null || axisx.equals("")){
	    	axisx = "";
	    }
	
		if(axisx.length() > 0 && axisx.charAt(axisx.length()-1) == ','){
			axisx = axisx.substring(0,axisx.length()-1);
		}
		
	    return axisx;
	}
	
	public void insertTestingAxisY(Integer id, String method, long value){
		MemoryCache memory = MemoryCache.getInstance(true);
		String axisy = getTestingAxisy(id, method);
		
		String name = MemoryIdentifier.getTestingAxisY(id, method);
		if(value<0){
			axisy += "| ";
		}
		else{
			axisy += "|" + value;
		}
		memory.putOnMemory(name, axisy);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + value);
	}
	
	public String getTestingAxisy(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTestingAxisY(id, method);
	    String axisy = (String)memory.getFromMemory(name);
	    if(axisy == null || axisy.equals("")){
	    	axisy = "";
	    }
		
	    return axisy;
	}
	
	
	public void insertTrainingHistoryStatistics(Integer id, String method, String statistic){
		MemoryCache memory = MemoryCache.getInstance(true);
		String oldstats = getTrainingHistoryStatistic(id, method);
		
		String name = MemoryIdentifier.getTrainingHistoryStatistics(id, method);
		if(oldstats == null || oldstats.equals("")){
			oldstats = statistic;
		}
		else{
			oldstats += ";" + statistic;
		}
			
		memory.putOnMemory(name, oldstats);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + oldstats);
	}
	
	public String getTrainingHistoryStatistic(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTrainingHistoryStatistics(id, method);
	    String history = (String)memory.getFromMemory(name);
	    if(history == null || history.equals("")){
	    	history = "";
	    }
		
	    return history;
	}
	
	public void insertTestingHistoryStatistics(Integer id, String method, String statistic){
		MemoryCache memory = MemoryCache.getInstance(true);
		String oldstats = getTestingHistoryStatistic(id, method);
		
		String name = MemoryIdentifier.getTestingHistoryStatistics(id, method);
		if(oldstats == null || oldstats.equals("")){
			oldstats = statistic;
		}
		else{
			oldstats += ";" + statistic;
		}
			
		memory.putOnMemory(name, oldstats);
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + oldstats);
	}
	
	public String getTestingHistoryStatistic(Integer id, String method){

		MemoryCache memory = MemoryCache.getInstance(true);
	    String name = MemoryIdentifier.getTestingHistoryStatistics(id, method);
	    String history = (String)memory.getFromMemory(name);
	    if(history == null || history.equals("")){
	    	history = "";
	    }
		
	    return history;
	}
	

	public Double getMeme(int[] fstate, Integer id, String method){

	    MemoryCache memory = MemoryCache.getInstance(true);

	    String name = MemoryIdentifier.getMemeName(id, method, fstate);
	    Double d = (Double)memory.get(name);
		
	    return d;
	}
	
	public Double getPartialStatusMeme(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getPartialKBName(id, method);
		Object o = memory.get(name);
		if(o == null)
			return 0.0;
		
		return (Double)o;
	}
	
	public Double getFinalStatusMeme(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getFinalKBName(id, method);
		Object o = memory.get(name);
		if(o == null)
			return 0.0;
		
		return (Double)o;
	}
	
	public Double getPartialStatusMemory(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getPartialMemoryName(id, method);
		Object o = memory.get(name);
		if(o == null)
			return 0.0;
		
		return (Double)o;
	}
	
	public Double getFinalStatusMemory(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getFinalMemoryName(id, method);
		Object o = memory.get(name);
		if(o == null)
			return 0.0;
		
		return (Double)o;
	}
	
	public Integer getCacheSizeMemory(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getCacheSizeName(id, method);
		Object o = memory.get(name);
		if(o == null)
			return 0;
		
		if(o instanceof Double){
			return 0;
		}
		
		System.out.println("Cache size memory=" + o);
		return (Integer)o;
	}
	
	public String getPause(Integer id, String method){
		MemoryCache memory = MemoryCache.getInstance(true);
		String pause = MemoryIdentifier.getPauseName(id, method);
		Object o = memory.get(pause);
		if(o == null)
			return "0";
		
		return (String)pause;
	}
	
	public void insertSpecialistResult(Integer id, String method, int[] state, int action, int turn, String result){
		MemoryCache memory = MemoryCache.getInstance(true);
		String name = MemoryIdentifier.getSpecialistResult(id, method, state, action, turn);

		memory.putOnMemory(name, result);
		
	    Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + result);
	}
	
	
	public String getSpecialistResult(Integer id, String method, int[] state, int action, int turn){
		MemoryCache memory = MemoryCache.getInstance(true);
		String result = MemoryIdentifier.getSpecialistResult(id, method, state, action, turn);
		Object o = memory.getFromMemory(result);
		if(o == null)
			return "-100";
		
		//TODO checar um dia essa gambiarra escrota... eu coloquei String e ta devolvendo Double
		return (String)o;
	}
}
