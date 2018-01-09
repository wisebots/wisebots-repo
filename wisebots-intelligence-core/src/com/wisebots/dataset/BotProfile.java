package com.wisebots.dataset;

public class BotProfile {
	
	private String id;
	private String name;
	private String description;
	private String game; 
	private double alfa;
	private double gama;
	private double epsilon;
	private double delta;
	private int cache;
	private int block;
	private int player;
	private long trainingExploring;
	private long trainingGreedy;
	private long trainingPessimist;
	private String queuedir;
	private long memoryReview;
	private double quality;
	private double iquality;
	private String method;
	private String techopponent;
	private String opponent;
	private String ebot;
	private String exploration;
	private String evaluation;
	private int level;
	
	private String creationDate;
	private String finishedDate;
	private String finishedMemoryDate;
	
	private BotGenesis botGenesis;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public long getTrainingExploring() {
		return trainingExploring;
	}
	public void setTrainingExploring(long trainingExploring) {
		this.trainingExploring = trainingExploring;
	}
	public long getTrainingGreedy() {
		return trainingGreedy;
	}
	public void setTrainingGreedy(long trainingGreedy) {
		this.trainingGreedy = trainingGreedy;
	}
	public long getTrainingPessimist() {
		return trainingPessimist;
	}
	public void setTrainingPessimist(long trainingPessimist) {
		this.trainingPessimist = trainingPessimist;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
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
	public int getCache() {
		return cache;
	}
	public void setCache(int cache) {
		this.cache = cache;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQueuedir() {
		return queuedir;
	}
	public void setQueuedir(String queuedir) {
		this.queuedir = queuedir;
	}
	public long getMemoryReview() {
		return memoryReview;
	}
	public void setMemoryReview(long memoryReview) {
		this.memoryReview = memoryReview;
	}
	public double getQuality() {
		return quality;
	}
	public void setQuality(double quality) {
		this.quality = quality;
	}
	public double getIquality() {
		return iquality;
	}
	public void setIquality(double iquality) {
		this.iquality = iquality;
	}
	public BotGenesis getBotGenesis() {
		return botGenesis;
	}
	public void setBotGenesis(BotGenesis botGenesis) {
		this.botGenesis = botGenesis;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}
	public String getFinishedMemoryDate() {
		return finishedMemoryDate;
	}
	public void setFinishedMemoryDate(String finishedMemoryDate) {
		this.finishedMemoryDate = finishedMemoryDate;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getTechopponent() {
		return techopponent;
	}
	public void setTechopponent(String techopponent) {
		this.techopponent = techopponent;
	}
	public String getOpponent() {
		return opponent;
	}
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	public String getEbot() {
		return ebot;
	}
	public void setEbot(String ebot) {
		this.ebot = ebot;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
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
	public String getExploration() {
		return exploration;
	}
	public void setExploration(String exploration) {
		this.exploration = exploration;
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
	
}
