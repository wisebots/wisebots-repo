package com.wisebots.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.wisebots.ai.method.AISpecialist;
import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.ai.method.QLearning;
import com.wisebots.ai.method.Randomization;
import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.utils.Utils;

public class TestingController {

	public static final int DRAW = 0;
	
	public static final int P1 = 1;
	public static final int P2 = 2;

	public int player = P1;
	public String method;
	private Game game;
	
	boolean debugState = false;
	
	// Metodos de aprendizado ou busca implementados
	private QLearning qlearner;
	private AISpecialist specialist;
	private Randomization randomization;
	
	public TestingController(Game game, String method, AISpecialist specialist, Exploration exploration, Evaluation evaluation){
		this.method = method;
		this.game = game;
		this.qlearner = new QLearning(game, evaluation, exploration);
		this.specialist = specialist;
		this.randomization = new Randomization(game);
	}
	
	public int[] startGame() {
		return game.initialize();
	}

	public Quality testLearnerVersusSpecialist(Integer id, GameCache gcache, int player, double delta){

		int passed = 0;
		int drawed = 0;
		int notpassed = 0;

		Quality quality = new Quality();
		
		if(gcache == null){
			quality.setPlayer1("0.00");
			quality.setPlayer2("0.00");
			quality.setDraw("0.00");
			return quality;
		}

		int epochs = 0;
		Map<String, Object> qvalues = gcache.getContent();
		if(qvalues == null || qvalues.isEmpty()){
			quality.setPlayer1("0.00");
			quality.setPlayer2("0.00");
			quality.setDraw("0.00");
			return quality;
		}
		System.out.println("Quantidade de testes: " + qvalues.size());
		
		Iterator<String> iterator = qvalues.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("Failures: \n");
		while (iterator.hasNext()) {
			int[] fstate = Utils.getArrayFromString(iterator.next());
			int[] state = Utils.getStateArrayFromArray(fstate);
			int action = Utils.getActionFromArray(fstate);
			System.out.println("      Testing state " + Arrays.toString(state) + ", action=" +action + ", value=" + qvalues.get(Arrays.toString(fstate)));
			
			String result = BrainHelper.getInstance().getSpecialistResult(id, method, state, action, player);
			int victory = testGreedyFromStateVersusSpecialist(state, action, player, id, gcache, delta);
			if(victory == Double.parseDouble(result) || (victory == player && Double.parseDouble(result) == 0)){
				if(victory == 0){
					drawed++;
				}
				else{
					passed++;
				}
			}
			else{
				sb.append("     " + epochs + "[" + player + "]. " + Arrays.toString(state) + "-action:" + action + ", spec: " + result + ", greedy: " + victory + ", results: " + passed + ";" + notpassed + ";" + drawed + "\n");
				notpassed++;
			}
			System.out.println("     " + epochs + "[" + player + "]. " + Arrays.toString(state) + ", spec: " + result + ", greedy: " + victory + ", results: " + passed + ";" + notpassed + ";" + drawed);
			
			epochs++;
		}
		System.out.println("Result from player " + player + ": " + passed + ";" + notpassed + ";" + drawed + ";" + epochs);
		System.out.println(sb.toString());
		
		DecimalFormat df = new DecimalFormat("0.00");
		if(player == P1){
			quality.setPlayer1(df.format((double)passed*100/epochs));
			quality.setPlayer2(df.format((double)notpassed*100/epochs));
			quality.setDraw(df.format((double)drawed*100/epochs));
		}
		else if(player == P2){
			quality.setPlayer1(df.format((double)notpassed*100/epochs));
			quality.setPlayer2(df.format((double)passed*100/epochs));
			quality.setDraw(df.format((double)drawed*100/epochs));
		}

		System.out.println(quality.toString());
		return quality;
	}
	
	public int testSpecialistVersusSpecialist(AISpecialist specialist, int[] state, int action, int player){
		int result = testSpecialistVersusSpecialist(state, action, player);
		// System.out.println("[ON TRAINING]: " + Arrays.toString(state) + ";" + action + " for " + player + ": " + result);
		return result;
	}
	
	
	/*-------- START OF STAGE TEST --------- */
	
	public int testGreedyVersusGreedy(Integer id, int opponent, GameCache gcache, GameCache gcacheadv) {
		int turn = 1;

		int[] state = startGame();
		
		while (!game.isEndGame(state)) {
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); 
			int[] nstate = game.makeAction(state, action, turn);
			// System.out.println("   " + Arrays.toString(state) + ",action=" + action); 

			if (game.isVictory(nstate, turn)) { 
				// System.out.println("   Winner: " + turn);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				// System.out.println("   Winner: " + Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				// System.out.println("   Draw");
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(opponent, method, gcacheadv, nstate, turn);
			state = game.makeAction(nstate, action, turn);
			// System.out.println("   " + Arrays.toString(nstate) + ",action=" + action); 

			if (game.isVictory(state, turn)) {
				// System.out.println("   Winner: " + turn);
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				// System.out.println("   Winner: " + Utils.changeTurn(turn));
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				// System.out.println("   Draw");
				return 0;
			}
			turn = Utils.changeTurn(turn);

		}

		// System.out.println("   Draw?");
		return DRAW;
	}	
	
	
	
	public int testGreedyVersusLearnerGreedy(Integer id, int opponent, GameCache gcache, GameCache gcacheadv) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) {
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn); 
			int[] nstate = game.makeAction(state, action, turn);

			if (game.isVictory(nstate, turn)) { 
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, nstate, turn);
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
	
	public int testLearnerGreedyVersusGreedy(Integer id, int opponent, GameCache gcache, GameCache gcacheadv) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) {
			int action = 0;

			action = qlearner.getQValueMemoryMove(opponent, gcacheadv, state, turn);
			
			int[] nstate = game.makeAction(state, action, turn);

			if (game.isVictory(nstate, turn)) { 
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn); 
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

	public int testGreedyVersusSpecialist(Integer id, GameCache gcache, double delta) {
		int turn = 1;

		int[] state = startGame();

		// System.out.println("-------------------------");
		while (!game.isEndGame(state)) { 
			int action = 0;

			action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			// System.out.println("[" + id + "-1] State: " + Arrays.toString(state) + "; action: " + action);

			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = specialist.getBestMove(game, nstate, turn, delta);

			// System.out.println("[" + id + "-2] State: " + Arrays.toString(nstate) + "; action: " + action);

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
	
	public int testGreedyFromStateVersusSpecialist(int[] state, int firstaction, int turn, Integer id, GameCache gcache, double delta) {

		int action = -1;
		while (!game.isEndGame(state)) { 
			if(action == -1){
				action = firstaction;
			}
			else{
				action = qlearner.getGreedyQValueMove(id, method, gcache, state, turn);
			}
			
			System.out.println("         P1. " + Arrays.toString(state) + ",action=" + action);
			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = specialist.getBestMove(game, nstate, turn, delta);

			System.out.println("         P2. " + Arrays.toString(nstate) + ",action=" + action);
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
	
	public int testSpecialistVersusGreedy(Integer id, GameCache gcache, double delta) {
		int turn = 1;

		int[] state = startGame();

		while (!game.isEndGame(state)) { 
			int action = 0;

			action = specialist.getBestMove(game, state, turn, delta);
			int[] nstate = game.makeAction(state, action, turn);
			
			if (game.isVictory(nstate, turn)) {
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}

			turn = Utils.changeTurn(turn);
			action = qlearner.getGreedyQValueMove(id, method, gcache, nstate, turn);
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
	
	public int testSpecialistVersusSpecialist(int[] state, int initialaction, int turn) {

		int action = -1;
		while (!game.isEndGame(state)) {
			if(action == -1){
				action = initialaction;
			}
			else{
				action = specialist.getBestMove(game, state, turn, 1.0);
			}
			
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
	
	/*-------- END OF PHASE TEST --------- */
	
	@SuppressWarnings("unused")
	@Deprecated
	private List<int[]> getSortCache(GameCache gcache, int ply){
		List<int[]> sorted = new ArrayList<int[]>();
		
		Map<String, Object> map = gcache.getContent();
		
		
		return sorted;
	}
	
	public static void main(String[] args) {
		Game game = new TicTacToe();
		AISpecialist specialist = new AlgMinMax(new TerminalEvaluation());
		TestingController coach = new TestingController(game, "MINMAX", specialist, null, null);
		
		int player = 1;
		int[] state = {0,2,0,0,2,1,1,0,0};
		int turn = 1;
		int action = 0;
		int result = coach.testSpecialistVersusSpecialist(specialist, state, action, turn);
		if(result == player){
			System.out.println("VENCE");
		}
		else if(result == Utils.changeTurn(player)){
			System.out.println("PERDE");
		}
		else{
			System.out.println("EMPATA");
		}
		
		System.out.println("Result: " + result);
	}
}
