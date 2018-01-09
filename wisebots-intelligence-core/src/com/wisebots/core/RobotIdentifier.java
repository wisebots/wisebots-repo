package com.wisebots.core;

public class RobotIdentifier {

	public static String getRobotIdName(){
		return ("wise-robot-id-count");
	}
	
	public static String getRobotName(Integer id){
		return ("wise-robot-" + id);
	}
	
	public static String getCreationDateName(Integer id){
		return ("wise-robot-creation-" + id);
	}
	
	public static String getFinishedDateName(Integer id){
		return ("wise-robot-finished-" + id);
	}
	
	public static String getFinishedMemoryDateName(Integer id){
		return ("wise-robot-finished-memory-" + id);
	}
	
	public static String getRobotProfileName(Integer id){
		return ("wise-robot-profile-" + id);
	}
	
	public static String getRobotStatusName(Integer id){
		return ("wise-robot-status-" + id);
	}
	
	public static String getRobotLastCheckName(Integer id){
		return ("wise-robot-lastcheck-" + id);
	}
	
	public static String getRobotLastQualityName(Integer id){
		return ("wise-robot-iquality-" + id);
	}
}
