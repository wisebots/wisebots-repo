package com.wisebots.dataset;

public class Neuron {

	private String game;
	private String instance;
	private int[] state;
	private double value;
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public int[] getState() {
		return state;
	}
	public void setState(int[] state) {
		this.state = state;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	
	
}
