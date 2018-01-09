package com.wisebots.learner;

import java.util.Calendar;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.ai.method.Learning;
import com.wisebots.core.MatchStrategy;
import com.wisebots.core.Quality;
import com.wisebots.core.cache.CacheFactory;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.core.exploration.RandomExploration;
import com.wisebots.dataset.LearningProperties;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;

@Deprecated
public class MinMaxVersusLearner extends Learning{
	
	public static String METHOD = "MINMAX";
	public static final String OPONENT_RANDOM = "random";
	public static final String OPONENT_MINMAX = "minmax";
	public static final int player = 1;
	
	private void executeMinMaxVersusRandom(long start, long block, Game game, GameCache gcache, MatchStrategy strat, boolean storeMemory, boolean storeSpecialist){
		long a = Calendar.getInstance().getTimeInMillis();
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		strat = new MatchStrategy(player, game, METHOD, specialist, storeMemory, storeSpecialist, new TerminalEvaluation(), new RandomExploration());
		Quality quality = strat.testMinMaxVersusRandom(block, gcache);

		System.out.println("------------------FINAL RESULTS------------------------ ");
		System.out.println("Qualidade do player 1: " + quality.getPlayer1() + " %");
		System.out.println("Qualidade do player 2: " + quality.getPlayer2() + " %");
		System.out.println("Empates: " + quality.getDraw() + " %");

		long end = Calendar.getInstance().getTimeInMillis();
		System.out.println("Tempo total de execucao: " + ((end-a)/1000) + " seconds");
		System.out.println("-------------------------------------------------------");
	}
	
	private void executeMinMaxVersusMinmax(long start, long block, Game game, GameCache gcache, MatchStrategy strat, boolean storeMemory, boolean storeSpecialist){
		long a = Calendar.getInstance().getTimeInMillis();
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		strat = new MatchStrategy(player, game, METHOD, specialist, storeMemory, storeSpecialist, new TerminalEvaluation(), new RandomExploration());
		Quality quality = strat.testMinMaxVersusMinmax(block, gcache);

		System.out.println("------------------FINAL RESULTS------------------------ ");
		System.out.println("Qualidade do player 1: " + quality.getPlayer1() + " %");
		System.out.println("Qualidade do player 2: " + quality.getPlayer2() + " %");
		System.out.println("Empates: " + quality.getDraw() + " %");

		long end = Calendar.getInstance().getTimeInMillis();
		System.out.println("Tempo total de execucao: " + ((end-a)/1000) + " seconds");
		System.out.println("-------------------------------------------------------");
	}
	
	
	private void executeGame(long start, long block, String gameName, int cache, String method, long epochs, boolean storeMemory, boolean storeSpecialist)
	throws Exception{
		GameCache gcache = CacheFactory.create(cache, gameName, gameName, 1);

		Game game = GameFactory.create(gameName); 
		 		
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, METHOD, specialist, storeMemory, storeSpecialist, new TerminalEvaluation(), new RandomExploration());

		for(int i=0; i<epochs/block; i++){
			if(method.equals(OPONENT_RANDOM)){
				executeMinMaxVersusRandom(i, block, game, gcache, strat, storeMemory, storeSpecialist);
			}
			else if(method.equals(OPONENT_MINMAX)){
				executeMinMaxVersusMinmax(i, block, game, gcache, strat, storeMemory, storeSpecialist);
			}			
		}
	}
	
	
	public void execute(String gameName, int cache, String method, long epochs, long block, boolean storeMemory, boolean storeSpecialist) {
		long start = Calendar.getInstance().getTimeInMillis();
		
		try {
			System.out.println("Running " + method + " for " + gameName);
			System.out.println("Executing Minmax");
			System.out.println("-------------------------------------------------------");
			System.out.println("Testing minmax");
			System.out.println("-------------------------------------------------------");
			
			MinMaxVersusLearner mm = new MinMaxVersusLearner();
			mm.executeGame(start, block, gameName, cache, method, epochs, storeMemory, storeSpecialist);
		} 
		catch (Exception e) {
			System.out.println("Erro na execucao do algoritmo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void execute(LearningProperties properties) {
		// TODO Auto-generated method stub
		
	}

}
