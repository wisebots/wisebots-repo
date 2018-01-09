package com.wisebots.learner;

import java.util.Calendar;

import com.wisebots.ai.method.Learning;
import com.wisebots.core.BrainHelper;
import com.wisebots.core.MatchStrategy;
import com.wisebots.core.cache.CacheFactory;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.evaluation.EvaluationFactory;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.core.exploration.ExplorationFactory;
import com.wisebots.core.persistence.CloudGame;
import com.wisebots.dataset.LearningProperties;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;
import com.wisebots.rules.games.utils.Utils;


public class QLPessimisticBothLearner extends Learning{
	
	public static String METHOD = "QLPESSIMISTIC";
	private Game game;
	private GameCache gcache;
	private GameCache gcacheadv;
	private boolean storeMemory;
	private boolean storeSpecialist;
			
	public QLPessimisticBothLearner(boolean storeMemory, boolean storeSpecialist){
		this.storeMemory = storeMemory;
		this.storeMemory = storeSpecialist;
	}
	
	public void execute(LearningProperties properties) {
		long start = Calendar.getInstance().getTimeInMillis();
		
		Integer id = properties.getId();
		Integer idOpponent = properties.getIdOpponent();
		String gameName = properties.getGameName();
		long trainingQL = properties.getTrainingQL();
		long trainingQLPessimistic = properties.getTrainingQLPessimistic();
		double alfa = properties.getAlfa();
		double gama = properties.getGama();
		double epsilon = properties.getEpsilon();
		double delta = properties.getDelta();
		int cache = properties.getCache();
		int player = properties.getPlayer();
		String eval = properties.getEvaluation();
		int level = properties.getLevel();
		String expo = properties.getExploration();
		
		try {
			System.out.println("Running " + METHOD + " for " + gameName);
			if(player == 1){
				System.out.println("Training QLearning Both for 1: " + id + " x " + idOpponent);
			}
			if(player == 2){
				System.out.println("Training QLearning Both for 2: " + id + " x " + idOpponent);
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Player: " + player);
			System.out.println("Alfa: " + alfa);
			System.out.println("Gama: " + gama);
			System.out.println("Epsilon: " + epsilon);
			System.out.println("Delta: " + delta);
			System.out.println("Cache: " + cache);
			System.out.println("Exploration: " + expo);
			System.out.println("Eval: " + eval);
			System.out.println("Level: " + level);
			System.out.println("Training epochs q-learning " +  trainingQL + " matchs");
			System.out.println("Training epochs q-learning pessimistic " +  trainingQLPessimistic + " matchs");
			System.out.println("-------------------------------------------------------");
			
			gcache = CacheFactory.create(cache, gameName, Integer.toString(id), player);
			gcacheadv = CacheFactory.create(cache, gameName, Integer.toString(idOpponent), Utils.changeTurn(player));
			game = GameFactory.create(gameName); 
			// AISpecialist specialist = new AlgMinMax();
			
			Evaluation evaluation = EvaluationFactory.create(eval);
			
			Exploration exploration = ExplorationFactory.create(expo);
			exploration.setLevel(level);
			
			MatchStrategy strat = new MatchStrategy(player, game, METHOD, null, storeMemory, storeSpecialist, evaluation, exploration);
			
			long time1 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLBoth(id, idOpponent, trainingQL, gcache, gcacheadv, alfa, gama, epsilon, delta);
			long time2 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearning(Both): " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");
			
			long time3 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLPessimisticBoth(id, idOpponent, trainingQL, trainingQLPessimistic, (time2-time1)/1000, gcache, gcacheadv, alfa, gama);
			long time4 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearningPessimistic(Both): " + ((time4-time3)/1000) + " seconds or " + (time4-time3) + " ms");
			
			System.out.println("Final Cache size Player 1: " + gcache.size());
			System.out.println("Final Cache size Player 2: " + gcacheadv.size());
			BrainHelper.getInstance().insertCacheSize(id, METHOD, new Integer(gcache.size()));
			
			gcache.close();
			gcacheadv.close();
			
			/*
			System.out.println("Creating memory for import lucene");
			BotConstructor lconstructor = ConstructorFactory.create(gameName, Integer.toString(id), "LUCENE");
			lconstructor.createMemory(gcache.getContent());
			lconstructor.close();
			
			*/
			
			CloudGame cloud = new CloudGame(gameName, id, gcache, true);
			cloud.run();
			
			CloudGame cloudAdv = new CloudGame(gameName, idOpponent, gcacheadv, true);
			cloudAdv.run();
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
