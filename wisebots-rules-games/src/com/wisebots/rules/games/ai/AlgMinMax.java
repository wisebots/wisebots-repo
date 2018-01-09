package com.wisebots.rules.games.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.utils.Utils;

public class AlgMinMax {
	
	public Map<Integer, Double> sucessores;
	
	public AlgMinMax(){
		sucessores = new HashMap<Integer, Double>();
	}

	public int getBestMove(Game game, int[] state, int player){
		double max = getMax(game, state, player, 0);

		for (Iterator<Integer> it = sucessores.keySet().iterator(); it.hasNext();) {  
			 Integer action = it.next();
			 double value = sucessores.get(action);  
	         if(max == value){
	        	 return action.intValue();
	         }
	    }    
		 
		return -1;
	}
	
	private double getMax(Game game, int[] state, int player, int level){
		double value = (-1)*Double.MAX_VALUE;
		if(game.isEndGame(state) || game.isVictory(state, player) || game.isVictory(state, Utils.changeTurn(player)) ){
			return evaluate(game, state, player);
		}
		
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], player);
			double nvalue = getMin(game, nstate, Utils.changeTurn(player), level+1);
			if(nvalue > value){
				value = nvalue;
			}
			if(level == 0){
				sucessores.put(new Integer(actions[i]), value);
			}
		}
	
		System.out.println(level);
		return value;
	}
	
	private double getMin(Game game, int[] state, int player, int level){
		double value = Double.MAX_VALUE;
		if(game.isEndGame(state) || game.isVictory(state, player) || game.isVictory(state, Utils.changeTurn(player)) ){
			return evaluate(game, state, Utils.changeTurn(player));
		}
		
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], player);
			double nvalue = getMax(game, nstate, Utils.changeTurn(player), level+1);
			if(nvalue < value){
				value = nvalue;
			}
		}
		return value;
	}

	private  double evaluate(Game game, int[] state, int player){
		if(game.isVictory(state, player)){
			return 100;
		}
		else if(game.isVictory(state, Utils.changeTurn(player))){
			return -50;
		}
		else{
			return 0;
		}		
	}
}
