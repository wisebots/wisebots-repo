package com.wisebots.ai.method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.evaluation.TerminalEvaluation;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.rules.games.utils.Utils;

public class AlgMinMax implements AISpecialist{
	
	public Map<Integer, Double> sucessores;
	private Evaluation evaluation;
	private int levelmax;
	
	public AlgMinMax(Evaluation evaluation){
		this.evaluation = evaluation;
		this.levelmax = 999999;
	}
	
	public AlgMinMax(Evaluation evaluation, int levelmax){
		this.evaluation = evaluation;
		this.levelmax = levelmax;
	}

	public int getBestMove(Game game, int[] state, int player, double delta){
		if(Utils.checkProbability(1-delta)){
			Randomization rand = new Randomization(game);
			return rand.getRandomMove(state);
		}
		
		sucessores = new HashMap<Integer, Double>();
		double max = getMax(game, state, -1, player, 0);

		Vector<Integer> actions = new Vector<Integer>();
		for (Iterator<Integer> it = sucessores.keySet().iterator(); it.hasNext();) {  
			 Integer action = it.next();
			 double value = sucessores.get(action);  
	         if(max == value){
	        	 actions.add(action.intValue());
	         }
	    }    
		
		int choose = 0;
		if(actions.size() > 1){
			Random rand = new Random();
			choose = rand.nextInt(actions.size());
		}

		return actions.get(choose);
	}
	
	private double getMax(Game game, int[] state, int action, int player, int level){
		double value = (-1)*Double.MAX_VALUE;
		if(game.isEndGame(state) || game.isVictory(state, player) || game.isVictory(state, Utils.changeTurn(player)) ){
			return evaluation.evaluate(game, state, action, player);
		}
		
		if(level >= levelmax){
			return evaluation.evaluate(game, state, action, player);
		}
		
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], player);
			double nvalue = getMin(game, nstate, actions[i], Utils.changeTurn(player), level+1);
			if(nvalue > value){
				value = nvalue-1;
			}
			if(level == 0){
				sucessores.put(new Integer(actions[i]), value);
			}
		}
	
		// System.out.println(level);
		return value;
	}
	
	private double getMin(Game game, int[] state, int action, int player, int level){
		double value = Double.MAX_VALUE;
		if(game.isEndGame(state) || game.isVictory(state, player) || game.isVictory(state, Utils.changeTurn(player)) ){
			return evaluation.evaluate(game, state, action, Utils.changeTurn(player));
		}
		
		if(level >= levelmax){
			return evaluation.evaluate(game, state, action, player);
		}
		
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], player);
			double nvalue = getMax(game, nstate, actions[i], Utils.changeTurn(player), level+1);
			if(nvalue < value){
				value = nvalue-1;
			}
		}
		return value;
	}
	
	public static void main(String[] args) {
		Game game = new TicTacToe();
		AlgMinMax minmax = new AlgMinMax(new TerminalEvaluation());
		int[] state = new int[]{1,2,1,0,1,2,0,2,0};
		
		System.out.println("Specialist request: " + Arrays.toString(state) + "," + 1 + "," + 1.0);
		int a = minmax.getBestMove(game, state, 1, 1.0);
		System.out.println("a=" + a);
	}
}
