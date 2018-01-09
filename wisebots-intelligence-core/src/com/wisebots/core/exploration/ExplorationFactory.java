package com.wisebots.core.exploration;

public class ExplorationFactory {

	public static Exploration create(String method){
		Exploration exploration = null;
		
		if(method.equalsIgnoreCase("RANDOM")){
			exploration = new RandomExploration();
		}
		else if(method.equalsIgnoreCase("MINMAX")){
			exploration = new MinMaxExploration();
		}

		return exploration;
	}
}
