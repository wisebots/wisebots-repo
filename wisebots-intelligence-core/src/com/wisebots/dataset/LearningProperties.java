package com.wisebots.dataset;

public class LearningProperties {

	private int id;
	private int idOpponent;
	private String gameName;
	private int player;
	private long trainingQL;
	private long trainingQLPessimistic;
	private double alfa;
	private double gama;
	private double epsilon;
	private double delta;
	private int cache;
	private int opponent;
	private boolean learningadv;
	private String evaluation;
	private int level;
	private String exploration;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public long getTrainingQL() {
		return trainingQL;
	}
	public void setTrainingQL(long trainingQL) {
		this.trainingQL = trainingQL;
	}
	public long getTrainingQLPessimistic() {
		return trainingQLPessimistic;
	}
	public void setTrainingQLPessimistic(long trainingQLPessimistic) {
		this.trainingQLPessimistic = trainingQLPessimistic;
	}
	public double getAlfa() {
		return alfa;
	}
	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}
	public double getGama() {
		return gama;
	}
	public void setGama(double gama) {
		this.gama = gama;
	}
	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public int getCache() {
		return cache;
	}
	public void setCache(int cache) {
		this.cache = cache;
	}
	public int getOpponent() {
		return opponent;
	}
	public void setOpponent(int opponent) {
		this.opponent = opponent;
	}
	public boolean isLearningadv() {
		return learningadv;
	}
	public void setLearningadv(boolean learningadv) {
		this.learningadv = learningadv;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public int getIdOpponent() {
		return idOpponent;
	}
	public void setIdOpponent(int idOpponent) {
		this.idOpponent = idOpponent;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getExploration() {
		return exploration;
	}
	public void setExploration(String exploration) {
		this.exploration = exploration;
	}
	
}
