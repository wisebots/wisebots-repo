package com.wisebots.web.now.administration;

import java.util.Arrays;

import com.wisebots.core.cache.MemoryCache;
import com.wisebots.utils.MemoryIdentifier;

public class GameMemory {
	
	public static void main(String[] args) {
		MemoryCache memory = new MemoryCache("192.168.1.14", true);
		int[] state = new int[]{1,0,2,0,1,0,0,0,2,3};  
		Double d = (Double)memory.getFromMemory(MemoryIdentifier.getMemeName(17, "QPESSIMIST", Arrays.toString(state)));
		System.out.println("q-value: " + d);
	}
}
