package com.wisebots.core;

import java.text.DecimalFormat;

import java.util.Date;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.core.persistence.CloudGame;

import com.wisebots.rules.games.Game;
import com.wisebots.utils.StatisticsUtils;
import com.wisebots.utils.Utils;

/**
 * Classe que gerencia as estratégias de partidas, ou seja, como e quantas vezes vão ser combinados os algoritmos
 * para se chegar a um resultado consistente
 *
 * @author Adriano
 *
 */


public class MatchStrategy { 

	public static final int P1 = 1;
	public static final int P2 = 2;
	public static final int DRAW = 0;
	public int player = P1;
	private TrainingController trainingController;
	private TestingController testingController;
	private Game game;
	public String method;
	
	public MatchStrategy(int player, Game game, String method, AISpecialist specialist, boolean loadStore, boolean loadSpecialist, Evaluation evaluation, Exploration exploration){
		this.trainingController = new TrainingController(game, method, specialist, loadStore, loadSpecialist, evaluation, exploration);
		this.testingController = new TestingController(game, method, specialist, exploration, evaluation);
		this.player = player;
		this.game = game;
		this.method = method;
	}
	
	public void trainingQLVersusSpecialist(Integer id, long epochs, GameCache gcache, double alfa, double gama, double epsilon, double delta){
		long a = new Date().getTime();
		Utils.debugLog(this.getClass(), "Exploring Q-Learning x Specialist at " + a);

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		for(int i=0; i<=epochs; i++){
			
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLVersusSpecialist(id, gcache, i, alfa, gama, epsilon, delta);
			}
			else if(player == P2){
				victory = trainingController.trainingSpecialistVersusQL(id, gcache, i, alfa, gama, epsilon, delta);
			}
			
			if(victory==P1){
				player1++; 
			}else if(victory==P2){
				player2++; 
			}else{
				draw++;
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}

			long b = new Date().getTime();
			long ngraph = epochs/30;
			long ny = epochs/5;
			if(i==0){
				System.out.println("Testing initial quality for " + player + " to " + epochs + " epochs");
				Quality initialQuality = testingController.testLearnerVersusSpecialist(id, null, player, delta);
				System.out.println("-----------> Quality From [" + i + "]: " + player + ": " + initialQuality.toString());
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, initialQuality, null);
			}

			else if(i%ngraph==0 && i>1010){
				System.out.println("Testing quality for " + player + " at " + i + " epochs");
				Quality quality = testingController.testLearnerVersusSpecialist(id, gcache, player, delta);
				System.out.println("-----------> Quality From [" + i + "]: " + player + ": " + quality.toString());
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, null, quality);
			}
			else if (i%ny==0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, null, null);
			}

			Utils.debugLog(this.getClass(), "Victory: " + victory);
		}

		Utils.debugLog(this.getClass(), "P1: " + player1 + "/P2: " + player2 + "/DRAW: " + draw) ;
		
		long b = new Date().getTime();
		Utils.debugLog(this.getClass(), "Ended Q-Learning x Random at " + (b-a));
	}
	
	public void trainingQLPessimisticVersusSpecialist(Integer id, long oldepochs, long epochs, long oldtime, GameCache gcache, double alfa, double gama, double epsilon, double delta){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		long a = new Date().getTime();
		for(int i=0; i<=epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLPessimisticVersusSpecialist(id, gcache, alfa, gama, epsilon, delta);
			}
			else if(player == P2){
				victory = trainingController.trainingSpecialistVersusQLPessimistic(id, gcache, alfa, gama, delta);
			}
			
			if(victory==P1){
				player1++;
			}
			else if(victory==P2){
				player2++; 
			}
			else{
				draw++;
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}
			
			long b = new Date().getTime();
			long ngraph = epochs/30;
			long ny = epochs/5;
			if((i+oldepochs)%ngraph==0 && i != 0 ){
				System.out.println("Testing quality for " + player + " at " + i + " epochs");
				Quality quality = testingController.testLearnerVersusSpecialist(id, gcache, player, delta);
				System.out.println("-----------> Quality From [" + i + "]: " + player + ": " + quality.toString());
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, null, quality);				
			}
			else if((i+oldepochs)%ny==0 && i!= 0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "trainingQLPessimisticVersusRandom");
		}
	}
	

	public void trainingQLVersusQL(Integer id, Integer opponent, boolean learning, long epochs, GameCache gcache, GameCache gcacheadv, double alfa, double gama){
		long a = new Date().getTime();
		Utils.debugLog(this.getClass(), "Exploring Q-Learning x Q-Learning at " + a);

		int player1 = 0;
		int player2 = 0;
		int draw = 0;
		
		int p1stat = 0;
		int p2stat = 0;
		int drawstat = 0;

		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLVersusLearner(id, opponent, gcache, gcacheadv, i, alfa, gama, learning);
			}
			else if(player == P2){
				victory = trainingController.trainingLearnerVersusQL(id, opponent, gcache, gcacheadv, i, alfa, gama);
			}
			if(victory==P1){
				player1++; p1stat++;
			}
			else if(victory==P2){
				player2++; p2stat++;
			}
			else{
				draw++; drawstat++;
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}
			
			long b = new Date().getTime();
			long ngraph = epochs/30;
			long ny = epochs/5;
			if(i == 0){
				Quality initialQuality = testLearnerGreedyVersusGreedy(id, opponent, 1000, gcache, gcacheadv);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, initialQuality, null);
			}
			else if(i%ngraph==0 ){
				StatisticsUtils.generateTrainingDirectMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, p1stat, p2stat, drawstat, (p1stat+p2stat+drawstat));
				p1stat = 0; p2stat = 0; drawstat = 0;
			}
			else if (i%ny==0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "trainingQLVersusQL");
		}
	}
	
	public void trainingQLPessimisticVersusQL(Integer id, Integer opponent, long oldepochs, long epochs, long oldtime, GameCache gcache, GameCache gcacheadv, double alfa, double gama){
		long a = new Date().getTime();
		int player1 = 0;
		int player2 = 0;
		int draw = 0;
		
		int p1stat = 0;
		int p2stat = 0;
		int drawstat = 0;

		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLPessimisticVersusLearner(id, opponent, gcache, gcacheadv, alfa, gama);
			}
			else if(player == P2){
				victory = trainingController.trainingLearnerVersusQLPessimistic(id, opponent, gcache, gcacheadv, alfa, gama);
			}
			
			if(victory==P1){
				player1++; p1stat++;
			}
			else if(victory==P2){
				player2++; p2stat++;
			}
			else{
				draw++; drawstat++;
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}
			
			long ngraph = epochs/30;
			long ny = epochs/5;
			long b = new Date().getTime();
			if((i+oldepochs)%ngraph==0){
				StatisticsUtils.generateTrainingDirectMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, p1stat, p2stat, drawstat, (p1stat+p2stat+drawstat));
				p1stat = 0; p2stat = 0; drawstat = 0;
			}
			else if((i+oldepochs)%ny==0 ){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "GREEDY - TESTE");
		}
	}	
	
	public void trainingQLBoth(Integer id, Integer idOpponent, long epochs, GameCache gcache, GameCache gcacheadv, double alfa, double gama, double epsilon, double delta){
		long a = new Date().getTime();
		Utils.debugLog(this.getClass(), "Exploring Q-Learning Both at " + a);

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		for(int i=0; i<epochs; i++){
			int victory = trainingController.trainingQLBoth(id, idOpponent, gcache, gcacheadv, i, alfa, gama, epsilon, delta);

			if(victory==P1){
				player1++; 
			}
			else if(victory==P2){
				player2++; 
			}
			else{
				draw++;
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}
			
			long b = new Date().getTime();
			long ngraph = epochs/30;
			long ny = epochs/5;
			if(i == 0){
				Quality initialQuality = testGreedyVersusGreedy(id, idOpponent, 1000, gcache, gcacheadv);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, initialQuality, null);
			}
			else if(i%ngraph==0 ){
				Quality quality = testGreedyVersusGreedy(id, idOpponent, 1000, gcache, gcacheadv);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, null, quality);
			}
			else if (i%ny==0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, (b-a)/1000, method, gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "trainingQLVersusQL");
		}
	}

	
	public void trainingQLPessimisticBoth(Integer id, Integer idOpponent, long oldepochs, long epochs, long oldtime, GameCache gcache, GameCache gcacheadv, double alfa, double gama){
		long a = new Date().getTime();
		Utils.debugLog(this.getClass(), "Exploring Q-Learning Pessimistic Both at " + a);

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		for(int i=0; i<epochs; i++){
			int victory = trainingController.trainingQLPessimisticBoth(id, idOpponent, gcache, gcacheadv, i, alfa, gama);

			if(victory==P1){
				player1++; 
			}
			else if(victory==P2){
				player2++; 
			}
			else{
				draw++; 
			}
			
			if(i%50000==0){ 
				CloudGame cloud = new CloudGame(game.getName(), id, gcache, true);
				cloud.run();
			}
			
			long ngraph = epochs/30;
			long ny = epochs/5;
			long b = new Date().getTime();
			if((i+oldepochs)%ngraph==0 ){
				Quality quality = testGreedyVersusGreedy(id, idOpponent, 1000, gcache, gcacheadv);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, null, quality);
			}
			else if ((i+oldepochs)%ny==0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, (b-a)/1000+oldtime, method, gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "trainingQLVersusQL");
		}
	}
	
	
	//TODO Esses metodos ficam aqui mesmo?!?
	
	
	public Quality testLearnerGreedyVersusGreedy(Integer id, int opponent, long epochs, GameCache gcache, GameCache gcacheadv){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		Quality quality = new Quality();

		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = testingController.testGreedyVersusLearnerGreedy(id, opponent, gcache, gcacheadv);
			}
			else if(player == P2){
				victory = testingController.testLearnerGreedyVersusGreedy(id, opponent, gcache, gcacheadv);
			}

			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "GREEDY - TESTE");
		}

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}	
	
	public Quality testGreedyVersusGreedy(Integer id, int opponent, long epochs, GameCache gcache, GameCache gcacheadv){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		Quality quality = new Quality();

		System.out.println("Quantidade de testes: " + epochs);
		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = testingController.testGreedyVersusGreedy(id, opponent, gcache, gcacheadv);
			}
			else if(player == P2){
				victory = testingController.testGreedyVersusGreedy(opponent, id, gcache, gcacheadv);
			}

			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "GREEDY - TESTE");
		}
		// System.out.println("Partial Results: " + player1 + ";" + player2 + ";" + draw);

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}	

	public Quality testGreedyVersusSpecialist(Integer id, long epochs, GameCache gcache, double delta){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		Quality quality = new Quality();

		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = testingController.testGreedyVersusSpecialist(id, gcache, delta);
				// System.out.println("Test specialist for player 1 victory from " + victory);
			}
			else if(player == P2){
				victory = testingController.testSpecialistVersusGreedy(id, gcache, delta);
				// System.out.println("Test specialist for player 2 victory from " + victory);
			}
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;
			
			// System.out.println(player1 + ";" + player2 + ";" + draw);
			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "GREEDY VERSUS SPECIALIST - TRAINING");
		}

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}
	
	public Quality testGreedyVersusRandom(Integer id, long epochs, GameCache gcache){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		Quality quality = new Quality();
		for(int i=0; i<epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.testGreedyVersusRandom(id, gcache);
			}
			else if (player == P2){
				victory = trainingController.testRandomVersusGreedy(id, gcache);
			}
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "GREEDY - TESTE");
		}

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}
	


	@Deprecated
	public Quality testRandomVersusRandom(long epochs, GameCache gcache){
		int player1 = 0;
		int player2 = 0;
		int draw = 0;
		
		Quality quality = new Quality();

		for(int i=0; i<epochs; i++){
			int victory = trainingController.testRandomVersusRandom();
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			Utils.printStatistics(null, player1, player2, draw, i, epochs, "RANDOM x RANDOM");
		}
		
		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}
	
	
	


	@Deprecated
	public Quality testMinMaxVersusMinmax(long epochs, GameCache gcache){
		int player1 = 0;
		int player2 = 0;
		int draw = 0;
		// Quality é o objeto que armazena os resultados dos jogadores
		Quality quality = new Quality();

		for(int i=0; i<epochs; i++){
			int victory = trainingController.testMinMaxVersusMinMax(gcache);
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			// Salva os resultados das partidas a cada 1000 jogos
			Utils.printStatistics(null, player1, player2, draw, i, epochs, "MINMAX VERSUS MINMAX");

		}

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}
	
	

	@Deprecated
	public Quality testMinMaxVersusRandom(long epochs, GameCache gcache){
		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		Quality quality = new Quality();
		for(int i=0; i<epochs; i++){
			int victory = 0; 
			if(player == P1){
				victory = trainingController.testMinMaxVersusRandom(gcache);
			}
			else if(player2 == P2){
				victory = trainingController.testMinMaxVersusRandom(gcache);
				// victory = controller.testRandomVersusMinMax(gcache); -- IPLEMENTAR
			}
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;

			Utils.printStatistics(null, player1, player2, draw, i, epochs, "MINMAX VERSUS RANDOM");
		}

		DecimalFormat df = new DecimalFormat("0.00");
		quality.setPlayer1(df.format((double)player1*100/epochs));
		quality.setPlayer2(df.format((double)player2*100/epochs));
		quality.setDraw(df.format((double)draw*100/epochs));

		return quality;
	}	
	

	
	@Deprecated
	public void trainingQLVersusRandom(Integer id, long epochs, GameCache gcache, double alfa, double gama){
		long a = new Date().getTime();
		Utils.debugLog(this.getClass(), "Exploring Q-Learning and Random at " + a);

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		for(int i=0; i<=epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLVersusRandom(id, gcache, i, alfa, gama);
			}
			else if(player == P2){
				victory = trainingController.trainingRandomVersusQL(id, gcache, i, alfa, gama);
			}
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;
			
			long ngraph = epochs/30;
			long ny = epochs/5;
			if(i==0){
				Quality initialQuality = testRandomVersusRandom(1000, gcache);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "RANDOM", gcache, initialQuality, null);
				StatisticsUtils.generateTestingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "RANDOM", gcache, initialQuality, null);
			}
			else if(i%ngraph==0 ){
				Quality trainQuality = testGreedyVersusRandom(id, 10000, gcache);
				Quality testQuality = testGreedyVersusRandom(id, 10000, gcache);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "QLEARNING", gcache, null, trainQuality);
				StatisticsUtils.generateTestingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "QLEARNING", gcache, null, testQuality);
			}
			else if (i%ny==0){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "QLEARNING", gcache, null, null);
				StatisticsUtils.generateTestingMemoGraphic(id, player, i, epochs, ngraph, ny, a, "QLEARNING", gcache, null, null);
			}

			Utils.debugLog(this.getClass(), "Victory: " + victory);
		}

		Utils.debugLog(this.getClass(), "P1: " + player1 + "/P2: " + player2 + "/DRAW: " + draw) ;
		
		long b = new Date().getTime();
		Utils.debugLog(this.getClass(), "Ended Q-Learning x Random at " + (b-a));
	}





	@Deprecated
	public void trainingQLPessimisticVersusRandom(Integer id, long oldepochs, long epochs, long oldtime, GameCache gcache, double alfa, double gama){

		int player1 = 0;
		int player2 = 0;
		int draw = 0;

		for(int i=0; i<=epochs; i++){
			int victory = 0;
			if(player == P1){
				victory = trainingController.trainingQLPessimisticVersusRandom(id, gcache, alfa, gama);
			}
			else if(player == P2){
				victory = trainingController.trainingRandomVersusQLPessimistic(id, gcache, alfa, gama);
			}
			if(victory==P1)
				player1++;
			else if(victory==P2)
				player2++;
			else
				draw++;
			
			long ngraph = epochs/30;
			long ny = epochs/5;
			long b = new Date().getTime();
			if((i+oldepochs)%ngraph==0 ){
				Quality initialQuality = testRandomVersusRandom(1000, gcache);
				Quality trainQuality = testGreedyVersusRandom(id, 10000, gcache);
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, b-oldtime, "QLPESSIMISTIC", gcache, initialQuality, trainQuality);
				StatisticsUtils.generateTestingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, b-oldtime, "QLPESSIMISTIC", gcache, initialQuality, trainQuality);
			}
			else if((i+oldepochs)%ny==0 ){
				StatisticsUtils.generateTrainingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, b-oldtime, "QLPESSIMISTIC", gcache, null, null);
				StatisticsUtils.generateTestingMemoGraphic(id, player, i+oldepochs, epochs, ngraph, ny, b-oldtime, "QLPESSIMISTIC", gcache, null, null);
			}

			Utils.printStatistics(gcache, player1, player2, draw, i, epochs, "trainingQLPessimisticVersusRandom");
		}
	}



	public static void main(String[] args) {
		int player2 = 556;
		int i =1000;
		DecimalFormat df = new 	DecimalFormat("0.00");
		System.out.println("Player2: "+ df.format(Double.parseDouble(""+player2)*100/i) + " %");
	}
}
