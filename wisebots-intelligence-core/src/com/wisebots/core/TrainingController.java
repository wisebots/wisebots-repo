package com.wisebots.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.QLearning;
import com.wisebots.ai.method.Randomization;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.cache.MemoryCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.rules.games.Game;
import com.wisebots.utils.MemoryIdentifier;
import com.wisebots.utils.Utils;

/**
 * Classe controladora dos jogos, responsável por realizar cada partida com seus respectivos tipos de adversários
 *
 * @author Adriano
 *
 */

public class TrainingController {

	public static final int P1 = 1;
	public static final int P2 = 2;
	public static final int DRAW = 0;
	public static final boolean debugState = false;

	private Game game;
	private boolean storeMemory;
	private boolean storeSpecialist;
	private String method;

	// Métodos de aprendizado ou busca implementados
	private QLearning qlearner;
	private Randomization randomization;
	private AISpecialist specialist;
	private TestingController coach;
	private Evaluation evaluation;
	private Exploration exploration;
	
	// Mem—ria cerebral do jogo
	public MemoryCache memory;
	public BrainHelper brain;

	public TrainingController( Game game, String method, AISpecialist specialist, boolean storeMemory, boolean storeSpecialist, Evaluation evaluation, Exploration exploration){
		this.game = game;
		this.qlearner = new QLearning(game, evaluation, exploration);
		this.randomization = new Randomization(game);
		this.specialist = specialist;
		this.memory = MemoryCache.getInstance();
		this.storeMemory = storeMemory;
		this.storeSpecialist = storeSpecialist;
		this.method = method;
		this.brain = BrainHelper.getInstance();
		this.coach = new TestingController(game, method, specialist, exploration, evaluation);
		this.evaluation = evaluation;
		this.exploration = exploration;
	}

	public int[] startGame() {
		return game.initialize();
	}
	
	/*-------- START OF TRAINING SPECIALIST METHODS --------- */

	public int trainingQLVersusSpecialist(Integer id, GameCache gcache, int round, double alfa, double gama, double epsilon, double delta) {
		int turn = 1;

		int[] state = startGame();

		Utils.debugLog(this.getClass(), "Starting match recursive for player 1 qlearning for " + round + " rounds");
		List<int[]> pathstate = new ArrayList<int[]>(); 
		while (!game.isEndGame(state)) { 
			int action = 0;
			if(Utils.checkProbability(epsilon)){
				action = randomization.getRandomMove(state);
			}
			else{
				action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			}
			
			int[] nstate = game.makeAction(state, action, turn); 
			Utils.debugLog(this.getClass(), "State P1: " + Arrays.toString(nstate) + "," + action);
			pathstate.add(state);
			
			double q = qlearner.updateQValue(gcache, state, action, turn, round, alfa, gama); 
			Utils.debugLog(this.getClass(), "Qvalue: " + q);
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(state, action, game.getOffset()));
				
				memory.putOnMemory(name, q);
				// System.out.println("      Neuron saved: " + name + "=" + q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}
			
			if(storeSpecialist){
				String result = brain.getSpecialistResult(id, method, state, action, turn);
				if(result.equals("-100")){
					int r = coach.testSpecialistVersusSpecialist(specialist, state, action, turn);
					brain.insertSpecialistResult(id, method, state, action, turn, Integer.toString(r));
				}
			}

			if (game.isVictory(nstate, turn)) { 
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			Utils.debugLog(this.getClass(), "Specialist request: " + Arrays.toString(nstate) + "," + turn + "," + delta);
			action = specialist.getBestMove(game, nstate, turn, delta);

			Utils.debugLog(this.getClass(), "Specialist response: " + action);
			Utils.debugLog(this.getClass(), "State P2: " + Arrays.toString(state) + "," + action);
			
			state = game.makeAction(nstate, action, turn);
			Utils.debugLog(this.getClass(), "State P2 played: " + Arrays.toString(state) + "," + action);

			if (game.isVictory(state, turn)) {
				pathstate.add(nstate);
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		Utils.debugLog(this.getClass(), "Draw ");
		return DRAW;
	}
	
	public int trainingSpecialistVersusQL(Integer id, GameCache gcache, int round, double alfa, double gama, double epsilon, double delta) {
		int turn = 1;

		int[] state = startGame();

		Utils.debugLog(this.getClass(), "Starting match recursive for player 2 qlearning for " + round + " rounds");
		List<int[]> pathstate = new ArrayList<int[]>(); 
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = specialist.getBestMove(game, state, turn, delta);
			int[] nstate = game.makeAction(state, action, turn);
			Utils.debugLog(this.getClass(), "State P1: " + Arrays.toString(nstate) + "," + action);
			pathstate.add(state);

			if (game.isVictory(nstate, turn)) { 
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			if(Utils.checkProbability(epsilon)){
				action = randomization.getRandomMove(state);
			}
			else{
				action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			}
			state = game.makeAction(nstate, action, turn);
			
			double q = qlearner.updateQValue(gcache, nstate, action, turn, round, alfa, gama); 
			Utils.debugLog(this.getClass(), "Qvalue: " + q);
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(nstate, action, game.getOffset()));
	
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}
			
			if(storeSpecialist){
				String result = brain.getSpecialistResult(id, method, nstate, action, turn);
				if(result.equals("-100")){
					int r = coach.testSpecialistVersusSpecialist(specialist, nstate, action, turn);
					brain.insertSpecialistResult(id, method, nstate, action, turn, Integer.toString(r));
				}
			}
			
			Utils.debugLog(this.getClass(), "State P2: " + Arrays.toString(state) + "," + action);

			if (game.isVictory(state, turn)) { 
				pathstate.add(nstate);
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		Utils.debugLog(this.getClass(), "Draw ");
		return DRAW;
	}
	
	public int trainingQLPessimisticVersusSpecialist(Integer id, GameCache gcache, double alfa, double gama, double epsilon, double delta) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) {
			int action = 0;

			if(Utils.checkProbability(epsilon)){
				action = randomization.getRandomMove(state);
			}
			else{
				action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			}

			int[] nstate = game.makeAction(state, action, turn);
			if(storeSpecialist){
				String result = brain.getSpecialistResult(id, method, nstate, action, turn);
				if(result.equals("-100")){
					int r = coach.testSpecialistVersusSpecialist(specialist, state, action, turn);
					brain.insertSpecialistResult(id, method, state, action, turn, Integer.toString(r));
				}
			}

			pathstate.add(state);
			pathaction.add(action);

			if (game.isVictory(nstate, turn)) { 
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = specialist.getBestMove(game, nstate, turn, delta);
			state = game.makeAction(nstate, action, turn);

			if (game.isVictory(state, turn)) {
				pathstate.add(state);
				pathaction.add(action);
				// Utils.debugFileLog("/usr/local/wisebots/logs/failures.txt", pathstate);	
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, turn, 0, alfa, gama); 
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							int[] statememo = Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset());
							String name = MemoryIdentifier.getMemeName(id, method, statememo);
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}	
	
	public int trainingSpecialistVersusQLPessimistic(Integer id, GameCache gcache, double alfa, double gama, double delta) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = specialist.getBestMove(game, state, turn, delta);
			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				pathstate.add(nstate);
				pathaction.add(action);
				// Utils.debugFileLog("/usr/local/wisebots/logs/failures.txt", pathstate);	
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							int[] statememo = Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset());
							String name = MemoryIdentifier.getMemeName(id, method, statememo);
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}


			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn);
			state = game.makeAction(nstate, action, turn);
			
			if(storeSpecialist){
				String result = brain.getSpecialistResult(id, method, nstate, action, turn);
				if(result.equals("-100")){
					int r = coach.testSpecialistVersusSpecialist(specialist, nstate, action, turn);
					brain.insertSpecialistResult(id, method, nstate, action, turn, Integer.toString(r));
				}
			}
			
			pathstate.add(nstate);
			pathaction.add(action);

			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}	
	
	/*-------- END OF TRAINING SPECIALIST METHODS --------- */
	
	
	/*-------- START OF TRAINING STATIC LEARNER METHODS --------- */
	
	public int trainingQLVersusLearner(Integer id, Integer opponent, GameCache gcache, GameCache gcacheadv, int round, double alfa, double gama, boolean learning) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);

			int[] nstate = game.makeAction(state, action, turn);
			double q = qlearner.updateQValue(gcache, state, action, turn, round, alfa, gama); // Q(s,a) e-greedy
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(state, action, game.getOffset()));
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}

			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, nstate, turn);
			state = game.makeAction(nstate, action, turn);
			
			if(learning){
				qlearner.updateQValue(gcacheadv, nstate, action, turn, round, alfa, gama); // Q(s,a)
			}
			
			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		return DRAW;
	}
	
	public int trainingLearnerVersusQL(Integer id, Integer opponent, GameCache gcache, GameCache gcacheadv, int round, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, state, turn);
			int[] nstate = game.makeAction(state, action, turn);

			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn);
			state = game.makeAction(nstate, action, turn);
			
			double q = qlearner.updateQValue(gcache, nstate, action, turn, round, alfa, gama); 
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(nstate, action, game.getOffset()));
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}

			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		return DRAW;
	}

	public int trainingQLPessimisticVersusLearner(Integer id, Integer opponent, GameCache gcache, GameCache gcacheadv, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); 

			int[] nstate = game.makeAction(state, action, turn);

			pathstate.add(state);
			pathaction.add(action);

			if (game.isVictory(nstate, turn)) { 
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, nstate, turn);

			state = game.makeAction(nstate, action, turn);

			if (game.isVictory(state, turn)) {
				pathstate.add(state);
				pathaction.add(action);
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset()));
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}
	
	public int trainingLearnerVersusQLPessimistic(Integer id, Integer opponent, GameCache gcache, GameCache gcacheadv, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, state, turn);
			int[] nstate = game.makeAction(state, action, turn);

			if (game.isVictory(nstate, turn)) {
				pathstate.add(state);
				pathaction.add(action);
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset()));
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);

						}
					}
				}
				
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn); 
			state = game.makeAction(nstate, action, turn);
			
			pathstate.add(nstate);
			pathaction.add(action);

			if (game.isVictory(state, turn)) { 
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}

			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}
	
	/*-------- END OF TRAINING STATIC LEARNER METHODS --------- */
	
	
	/*-------- START OF TRAINING BOTH LEARNERS --------- */
	
	public int trainingQLBoth(Integer id, Integer idOpponent, GameCache gcache, GameCache gcacheadv, int round, double alfa, double gama, double epsilon, double delta) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) { 
			int action = 0;

			if(Utils.checkProbability(epsilon)){
				evaluation.setGameCache(gcache);
				action = exploration.getAction(state, turn, delta, game, evaluation);
			}
			else{
				action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			}
			int[] nstate = game.makeAction(state, action, turn);
			
			double q = qlearner.updateQValueRewarded(gcache, state, action, turn, round, alfa, gama); 
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(state, action, game.getOffset()));
				memory.putOnMemory(name, q);

				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}

			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			if(Utils.checkProbability(epsilon)){
				evaluation.setGameCache(gcacheadv);
				action = exploration.getAction(nstate, turn, delta, game, evaluation);
			}
			else{
				action = qlearner.getGreedyQValueMove(idOpponent, method, gcacheadv, nstate, turn);
			}
			state = game.makeAction(nstate, action, turn);
			
			double qOpponent = qlearner.updateQValue(gcacheadv, nstate, action, turn, round, alfa, gama); 
			
			if(storeMemory && qOpponent!=0){
				String name = MemoryIdentifier.getMemeName(idOpponent, method, Utils.getStateFunction(nstate, action, game.getOffset()));
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}

			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		return DRAW;
	}
	
	
	public int trainingQLPessimisticBoth(Integer id, Integer idOpponent, GameCache gcache, GameCache gcacheadv, int round, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		List<int[]> pathstateadv = new ArrayList<int[]>();
		List<Integer> pathactionadv = new ArrayList<Integer>();
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); 
			int[] nstate = game.makeAction(state, action, turn);

			pathstate.add(state);
			pathaction.add(action);

			if (game.isVictory(nstate, turn)) {
				pathstateadv.add(state);
				pathactionadv.add(action);
				
				double[] qvalues = qlearner.updatePessismisticValue(gcacheadv, pathstateadv, pathactionadv, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstateadv.size() > 1 ){
					for(int i=pathstateadv.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(pathstateadv.get(i), pathactionadv.get(i), game.getOffset()));
							memory.putOnMemory(name, qvalues[i]);
							
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(idOpponent, method, gcacheadv, nstate, turn); 
			state = game.makeAction(nstate, action, turn);
			
			pathstateadv.add(state);
			pathactionadv.add(action);

			if (game.isVictory(state, turn)) {
				pathstate.add(state);
				pathaction.add(action);
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset()));
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}
	
	/*-------- END OF TRAINING BOTH LEARNERS --------- */
	
	
	
	/*-------- START OF TRAINING RANDOM --------- */

	public int testRandomVersusGreedy(Integer id, GameCache gcache) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> statesFailed = new ArrayList<int[]>();
		while (!game.isEndGame(state)) { 
			int action = 0;
			
			statesFailed.add(state);
			action = randomization.getRandomMove(state);
			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				statesFailed.add(nstate);
				if(debugState){
					Utils.debugFileLog("/usr/local/wisebots/logs/failures_" + id + ".txt", statesFailed);
				}
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn); 

			statesFailed.add(nstate);
			state = game.makeAction(nstate, action, turn);
			
			if (game.isVictory(state, turn)) { 
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}

			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}
	
	public int testGreedyVersusRandom(Integer id, GameCache gcache) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> statesFailed = new ArrayList<int[]>();
		while (!game.isEndGame(state)) { 
			int action = 0;
			
			statesFailed.add(state);
			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); // e-greedy
			
			int[] nstate = game.makeAction(state, action, turn);

			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = randomization.getRandomMove(nstate);

			statesFailed.add(nstate);
			state = game.makeAction(nstate, action, turn);

			if (game.isVictory(state, turn)) {
				statesFailed.add(state);
				if(debugState){
					Utils.debugFileLog("/usr/local/wisebots/logs/failures_" + id + ".txt", statesFailed);
				}
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}
	
	/*-------- END OF TRAINING RANDOM --------- */
	
	@Deprecated
	public int testMinMaxVersusRandom(GameCache cache) {
		int turn = 1;

		int[] state = startGame();
		int nply = 0;
		
		while (!game.isEndGame(state)) {
			int action = 0;
			System.out.println("  state 1: " + Arrays.toString(state));
			if(nply == 0){
				action = randomization.getRandomMove(state); // Atribui uma aleatorização ao primeiro lance
			}
			else{
				action = specialist.getBestMove(game, state, turn, 0);
			}
			state = game.makeAction(state, action, turn);
			System.out.println("  state 1: " + Arrays.toString(state));

			if (game.isVictory(state, turn)) {
				System.out.println("Victory from " + turn + " em " + state);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
			action = randomization.getRandomMove(state);

			int[] nstate = game.makeAction(state, action, turn);
			System.out.println("  state 2: " + Arrays.toString(state));
			
			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
			nply++;
		}

		return DRAW;
	}	
	
	
	
	
	
	@Deprecated
	public int trainingQLVersusRandom(Integer id, GameCache gcache, int round, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		Utils.debugLog(this.getClass(), "Starting match recursive for player 1 qlearning for " + round + " rounds");
		List<int[]> pathstate = new ArrayList<int[]>(); // Lista de debug
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = randomization.getRandomMove(state);
			int[] nstate = game.makeAction(state, action, turn); // s'
			Utils.debugLog(this.getClass(), "State P1: " + Arrays.toString(nstate) + "," + action);
			pathstate.add(state);
			
			double q = qlearner.updateQValue(gcache, state, action, turn, round, alfa, gama); // Q(s,a)
			Utils.debugLog(this.getClass(), "Qvalue: " + q);
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(state, action, game.getOffset()));
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}

			if (game.isVictory(nstate, turn)) { // Verifica se o jogador 1 venceu
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = randomization.getRandomMove(nstate);
			state = game.makeAction(nstate, action, turn);
			Utils.debugLog(this.getClass(), "State P2: " + Arrays.toString(state) + "," + action);

			if (game.isVictory(state, turn)) { // Verifica se o jogador 2 venceu
				pathstate.add(nstate);
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		Utils.debugLog(this.getClass(), "Draw ");
		return DRAW;
	}
	
	@Deprecated
	public int trainingRandomVersusQL(Integer id, GameCache gcache, int round, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		Utils.debugLog(this.getClass(), "Starting match recursive for player 2 qlearning for " + round + " rounds");
		List<int[]> pathstate = new ArrayList<int[]>(); // Lista de debug
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = randomization.getRandomMove(state);
			int[] nstate = game.makeAction(state, action, turn); // s'
			Utils.debugLog(this.getClass(), "State P1: " + Arrays.toString(nstate) + "," + action);
			pathstate.add(state);

			if (game.isVictory(nstate, turn)) { // Verifica se o jogador 1 venceu
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = randomization.getRandomMove(nstate);
			state = game.makeAction(nstate, action, turn);
			
			double q = qlearner.updateQValue(gcache, nstate, action, turn, round, alfa, gama); // Q(s,a)
			Utils.debugLog(this.getClass(), "Qvalue: " + q);
			
			if(storeMemory && q!=0){
				String name = MemoryIdentifier.getMemeName(id, method, Utils.getStateFunction(nstate, action, game.getOffset()));
				memory.putOnMemory(name, q);
				Utils.debugLog(this.getClass(), "Neuron saved: " + name + "=" + q);
			}
			Utils.debugLog(this.getClass(), "State P2: " + Arrays.toString(state) + "," + action);

			if (game.isVictory(state, turn)) { // Verifica se o jogador 2 venceu
				pathstate.add(nstate);
				Utils.debugLog(this.getClass(), "Victory from "+ turn);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);
		}

		Utils.debugLog(this.getClass(), "Draw ");
		return DRAW;
	}

	@Deprecated
	public int trainingQLPessimisticVersusRandom(Integer id, GameCache gcache, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) {
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); 

			int[] nstate = game.makeAction(state, action, turn);

			pathstate.add(state);
			pathaction.add(action);

			if (game.isVictory(nstate, turn)) { 
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = randomization.getRandomMove(nstate);

			state = game.makeAction(nstate, action, turn);

			if (game.isVictory(state, turn)) {
				pathstate.add(state);
				pathaction.add(action);
				// Utils.debugFileLog("/usr/local/wisebots/logs/failures.txt", pathstate);	
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, turn, 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							int[] statememo = Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset());
							String name = MemoryIdentifier.getMemeName(id, method, statememo);
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}	

	@Deprecated
	public int testMinMaxVersusMinMax(GameCache cache) {
		int turn = 1;

		int[] state = startGame();
		int nply = 0;
		while (!game.isEndGame(state)) {
			int action = 0;
			//System.out.println("  state: " + Arrays.toString(state));
			if(nply == 0){
				action = randomization.getRandomMove(state); // Atribui uma aleatorização ao primeiro lance
				//System.out.println("Random: " + action);
			}
			else{
				action = specialist.getBestMove(game, state, turn, 0);
			}
			state = game.makeAction(state, action, turn);
			//System.out.println("  state 1: (a=" + action + ") " + Arrays.toString(state));
			
			if (game.isVictory(state, turn)) {
				// System.out.println("Victory from " + turn + " em " + Arrays.toString(state));
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
			nply++;
		}

		return DRAW;
	}
	
	@Deprecated
	public int trainingRandomVersusQLPessimistic(Integer id, GameCache gcache, double alfa, double gama) {
		int turn = 1;

		int[] state = startGame();

		List<int[]> pathstate = new ArrayList<int[]>();
		List<Integer> pathaction = new ArrayList<Integer>();
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = randomization.getRandomMove(state);
			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				pathstate.add(nstate);
				pathaction.add(action);
				// Utils.debugFileLog("/usr/local/wisebots/logs/failures.txt", pathstate);	
				
				double[] qvalues = qlearner.updatePessismisticValue(gcache, pathstate, pathaction, Utils.changeTurn(turn), 0, alfa, gama); // Q(s,a)
				
				if(pathstate.size() > 1 ){
					for(int i=pathstate.size()-2; i>=0; i--){
						if(storeMemory && qvalues[i]!=0){
							int[] statememo = Utils.getStateFunction(pathstate.get(i), pathaction.get(i), game.getOffset());
							String name = MemoryIdentifier.getMemeName(id, method, statememo);
							memory.putOnMemory(name, qvalues[i]);
							Utils.debugLog(this.getClass(), "Neuron pessimistic saved: " + name + "=" + qvalues[i]);
						}
					}
				}
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}


			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn);
			state = game.makeAction(nstate, action, turn);
			
			pathstate.add(nstate);
			pathaction.add(action);

			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				Utils.debugLog(this.getClass(), "Victory from "+ Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		return DRAW;
	}	
	

	
	

	@Deprecated
	public int testRandomVersusRandom() {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) { 
			int action = randomization.getRandomMove(state);
			state = game.makeAction(state, action, turn);

			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
		}

		return DRAW;
	}

}


