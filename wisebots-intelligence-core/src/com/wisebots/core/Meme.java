package com.wisebots.core;

import java.io.Serializable;

public class Meme implements Serializable {

	private static final long serialVersionUID = 3419827841287638143L;
	
	private String key;
	private Double value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
