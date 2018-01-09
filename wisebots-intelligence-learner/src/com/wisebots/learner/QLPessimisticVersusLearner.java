package com.wisebots.learner;

import java.util.Calendar;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.ai.method.Learning;
import com.wisebots.constructor.BotConstructor;
import com.wisebots.constructor.ConstructorFactory;
import com.wisebots.core.BrainHelper;
import com.wisebots.core.MatchStrategy;
import com.wisebots.core.cache.CacheFactory;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.core.exploration.RandomExploration;
import com.wisebots.dataset.LearningProperties;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;
import com.wisebots.rules.games.utils.Utils;


public class QLPessimisticVersusLearner extends Learning{
	
	public static String METHOD = "QLPESSIMISTIC";
	private Game game;
	private GameCache gcache;
	private GameCache gcacheadv;
	private boolean storeMemory;
	private boolean storeSpecialist;
	
	public QLPessimisticVersusLearner(boolean storeMemory, boolean storeSpecialist){
		this.storeMemory = storeMemory;
		this.storeSpecialist = storeSpecialist;
	}
	
	public void execute(LearningProperties properties) {
		long start = Calendar.getInstance().getTimeInMillis();
		
		Integer id = properties.getId();
		String gameName = properties.getGameName();
		long trainingQL = properties.getTrainingQL();
		long trainingQLPessimistic = properties.getTrainingQLPessimistic();
		double alfa = properties.getAlfa();
		double gama = properties.getGama();
		int cache = properties.getCache();
		Integer opponent = properties.getOpponent();
		boolean learningadv = properties.isLearningadv();
		int player = properties.getPlayer();
		
		try {
			System.out.println("Running " + METHOD + " for " + gameName);
			System.out.println("Training QLearning Pessimistic versus Learner Game");
			System.out.println("-------------------------------------------------------");
			System.out.println("Alfa: " + alfa);
			System.out.println("Gama: " + gama);
			System.out.println("Cache: " + cache);
			System.out.println("Opponent: " + opponent);
			System.out.println("Training exploring q-learning " +  trainingQL + " matchs");
			System.out.println("Training exploring q-learning pessimistic" +  trainingQLPessimistic + " matchs");
			System.out.println("-------------------------------------------------------");
			
			gcache = CacheFactory.create(cache, gameName, gameName, player);
			gcacheadv = CacheFactory.create(cache, gameName, gameName, Utils.changeTurn(player));
			game = GameFactory.create(gameName); 
			AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
			MatchStrategy strat = new MatchStrategy(player, game, METHOD, specialist, storeMemory, storeSpecialist, new TerminalEvaluation(), new RandomExploration());
			
			long time1 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLVersusQL(id, opponent, learningadv, trainingQL, gcache, gcacheadv, alfa, gama);
			long time2 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearning(Learner-" + opponent + "): " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");
			
			long time3 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLPessimisticVersusQL(id, opponent, trainingQL, trainingQLPessimistic, (time2-time1), gcache, gcacheadv, alfa, gama);
			long time4 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearning(Learner-" + opponent + "): " + ((time4-time3)/1000) + " seconds or " + (time4-time3) + " ms");
			
			System.out.println("Cache size: " + gcache.size());
			BrainHelper.getInstance().insertCacheSize(id, METHOD, new Integer(gcache.size()));
			
			System.out.println("Creating memory for import lucene");
			BotConstructor lconstructor = ConstructorFactory.create(gameName, Integer.toString(id), "LUCENE", true);
			lconstructor.createMemory(gcache.getContent());
			lconstructor.close();
			
			System.out.println("Creating memory for import sqlite");
			BotConstructor sconstructor = ConstructorFactory.create(gameName, Integer.toString(id), "SQLITE", true);
			sconstructor.createMemory(gcache.getContent());
			sconstructor.close();
			
		} 
		catch (Exception e) {
			System.out.println("Erro na execucao do algoritmo: " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			long end = Calendar.getInstance().getTimeInMillis();
			System.out.println("Total time: " + (end-start) + " ms" );
		}
	}


}
