package com.wisebots.utils;

import java.util.Arrays;

public class MemoryIdentifier {

	public static String getMemeName(Integer id, String method, int[] state){
		return getMemeName(id, method, Arrays.toString(state));
	}
	
	public static String getMemeName(Integer id, String method, String state){
		return (id+"-"+method+"-"+state).replace(" ", "");
	}
	
	public static String getPartialKBName(Integer id, String method){
		return (id + "-" + method + "-partial");
	}
	
	public static String getFinalKBName(Integer id, String method){
		return (id + "-" + method + "-final");
	}
	
	public static String getPartialMemoryName(Integer id, String method){
		return (id + "-" + method + "-memory-partial");
	}
	
	public static String getFinalMemoryName(Integer id, String method){
		return (id + "-" + method + "-memory-final");
	}
	
	public static String getCacheSizeName(Integer id, String method){
		return (id + "-" + method + "-cache-size");
	}
	
	public static String getPauseName(Integer id, String method){
		return (id + "-" + method + "-pause");
	}
	
	public static String getTrainingAxisX(Integer id, String method){
		return (id + "-" + method + "-training-axisx");
	}
	
	public static String getTrainingAxisY(Integer id, String method){
		return (id + "-" + method + "-training-axisy");
	}
	
	public static String getTestingAxisX(Integer id, String method){
		return (id + "-" + method + "-testing-axisx");
	}
	
	public static String getTestingAxisY(Integer id, String method){
		return (id + "-" + method + "-testing-axisy");
	}
	
	public static String getTrainingHistoryStatistics(Integer id, String method){
		return (id + "-" + method + "-training-history");
	}
	
	public static String getTestingHistoryStatistics(Integer id, String method){
		return (id + "-" + method + "-testinghistory");
	}
	
	public static String getSpecialistResult(Integer id, String method, int[] state, int action, int turn){
		return (id + "-" + method + "-" + Arrays.toString(state) + "-" + action + "-" + turn + "-specialistresult").replace(" ", "");
	}
}
