package com.wisebots.learner.explorer;

import java.util.Calendar;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.constructor.BotConstructor;
import com.wisebots.constructor.ConstructorFactory;
import com.wisebots.core.BrainHelper;
import com.wisebots.core.MatchStrategy;
import com.wisebots.core.Quality;
import com.wisebots.core.TestingController;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.cache.SimpleHashCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.core.exploration.RandomExploration;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;
import com.wisebots.rules.games.utils.Utils;

public final class GreedyExplorer {
	
	private static GreedyExplorer instance;
	
	public static GreedyExplorer getInstance(){
		if(instance == null){
			instance = new GreedyExplorer();
		}
			
		return instance;
	}
	
	private GreedyExplorer(){
	}
	
	public double checkValue(String fstate, String id, String gameName, String method, int player){
		String value = "0.0";
		try {
			BotConstructor constructor = ConstructorFactory.create(gameName, id, "SQLITE", false);
			value = constructor.search(fstate);
			if(value == null || value.equals("")){
				value = "0.0";
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return Double.parseDouble(value);
	}
	
	public double checkLastQuality(Integer id, String method){
		double quali = BrainHelper.getInstance().getPartialStatusMeme(id, method);
		System.out.println("Last quality: " + quali);
		return quali;
	}
	
	public double checkInitialQuality(Integer id, int player, String gameName, String method, long epochs, double delta){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testGreedyVersusSpecialist(id, epochs, gcache, delta);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with random: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");
		
		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();

		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		return quali;
		
	}
	
	public double checkOpponentInitialQuality(Integer id, int player, int opponent, String gameName, String method, long epochs, double delta){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		GameCache gcacheadv = new SimpleHashCache(gameName, method, Utils.changeTurn(player));
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testLearnerGreedyVersusGreedy(id, opponent, epochs, gcache, gcacheadv);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");
		
		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();

		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}

	public double checkQuality(Integer id, int player, String gameName, String method, long epochs, double delta){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testGreedyVersusSpecialist(id, epochs, gcache, delta);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
	
	public double checkSpecialistQuality(Integer id, int player, String gameName, String method, long epochs, double delta, Evaluation evaluation, Exploration exploration){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		TestingController testingController = new TestingController(game, method, specialist, exploration, evaluation);
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = testingController.testLearnerVersusSpecialist(id, gcache, player, delta);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking specialist quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality reached: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
	
	public double checkRandomQuality(Integer id, int player, String gameName, String method, long epochs, double delta){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testGreedyVersusRandom(id, epochs, gcache);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking random quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality reached: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
	
	public double checkOpponentQuality(Integer id, int player, int opponent, String gameName, String method, long epochs){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		GameCache gcacheadv = new SimpleHashCache(gameName, method, Utils.changeTurn(player));
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testLearnerGreedyVersusGreedy(id, opponent, epochs, gcache, gcacheadv);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
	
	public double checkFinalQuality(Integer id, int player, String gameName, String method, long epochs, double delta){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testGreedyVersusSpecialist(id, epochs, gcache, delta);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
	
	public double checkOpponentFinalQuality(Integer id, int opponent, int player, String gameName, String method, long epochs){
		
		Game game = GameFactory.create(gameName); 
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		MatchStrategy strat = new MatchStrategy(player, game, method, specialist, false, false, new TerminalEvaluation(), new RandomExploration());
		GameCache gcache = new SimpleHashCache(gameName, method, player);
		GameCache gcacheadv = new SimpleHashCache(gameName, method, Utils.changeTurn(player));
		
		long time1 = Calendar.getInstance().getTimeInMillis();
		Quality quality = strat.testLearnerGreedyVersusGreedy(id, opponent, epochs, gcache, gcacheadv);
		long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Checking quality time for Testing with greedy: " + ((time2-time1)/1000) + " seconds or " + (time2-time1) + " ms");

		String playerVic ="0.0";
		if(player == 1)
			playerVic = quality.getPlayer1();
		else if(player == 2)
			playerVic = quality.getPlayer2();
		
		double quali = Double.parseDouble(playerVic)+Double.parseDouble(quality.getDraw());
		System.out.println("Quality improved: " + quali);
		BrainHelper.getInstance().insertPartialStatusMeme(id, method, quali);
		
		return quali;
	}
}
