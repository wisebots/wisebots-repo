package com.wisebots.web.now.administration;

import com.wisebots.core.RobotIdentifier;
import com.wisebots.core.cache.MemoryCache;


public class ClearMemory {

	public static void main(String[] args) {
		MemoryCache memory = new MemoryCache("192.168.1.14", true);
		Integer count = (Integer)memory.getFromMemory(RobotIdentifier.getRobotIdName());
		System.out.println("Count: " + count);
		for(int i=1; i<=count; i++){
			memory.delete(RobotIdentifier.getRobotProfileName(i));
			memory.delete(RobotIdentifier.getRobotName(i));
		}
		memory.delete(RobotIdentifier.getRobotIdName());
	}
}
