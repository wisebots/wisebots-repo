package com.wisebots.learner;

import java.util.Calendar;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.ai.method.Learning;
import com.wisebots.core.BrainHelper;
import com.wisebots.core.MatchStrategy;
import com.wisebots.core.cache.CacheFactory;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.core.exploration.RandomExploration;
import com.wisebots.dataset.LearningProperties;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;

public class QLPessimisticVersusSpecialist extends Learning{
	
	public static String METHOD = "QLPESSIMISTIC";
	private Game game;
	private GameCache gcache;
	private boolean storeMemory;
	private boolean storeSpecialist;
			
	public QLPessimisticVersusSpecialist(boolean storeMemory, boolean storeSpecialist){
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
		double epsilon = properties.getEpsilon();
		double delta = properties.getDelta();
		int cache = properties.getCache();
		int player = properties.getPlayer();
		
		try {
			System.out.println("Running " + METHOD + " for " + gameName);
			if(player == 1){
				System.out.println("Training QLearning Pessimistic versus Specialist");
			}
			if(player == 2){
				System.out.println("Training Specialist versus QLearning Pessimistic");
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Player: " + player);
			System.out.println("Alfa: " + alfa);
			System.out.println("Gama: " + gama);
			System.out.println("Delta" + delta);
			System.out.println("Epsilon: " + epsilon);
			System.out.println("Cache: " + cache);
			System.out.println("Training epochs q-learning " +  trainingQL + " matchs");
			System.out.println("Training epochs q-learning pessimistic " +  trainingQLPessimistic + " matchs");
			System.out.println("-------------------------------------------------------");
			
			gcache = CacheFactory.create(cache, gameName, METHOD, player);
			game = GameFactory.create(gameName); 
			AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
			MatchStrategy strat = new MatchStrategy(player, game, METHOD, specialist, storeMemory, storeSpecialist, new TerminalEvaluation(), new RandomExploration());
			
			long time1 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLVersusSpecialist(id, trainingQL, gcache, alfa, gama, epsilon, delta);
			long time2 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearning(Specialist): " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");
			
			long time3 = Calendar.getInstance().getTimeInMillis();
			strat.trainingQLPessimisticVersusSpecialist(id, trainingQL, trainingQLPessimistic, (time2-time1)/1000, gcache, alfa, gama, epsilon, delta);
			long time4 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Time for Training QLearningPessimistic(Specialist): " + ((time4-time3)/1000) + " seconds or " + (time4-time3) + " ms");
			
			System.out.println("Cache size: " + gcache.size());
			BrainHelper.getInstance().insertCacheSize(id, METHOD, new Integer(gcache.size()));
			
			// Utils.printCacheQValues(gcache.getContent());
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
