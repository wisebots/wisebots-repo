package com.wisebots.core.evaluation;

import java.util.Arrays;

import com.wisebots.core.cache.GameCache;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.utils.Utils;

public class QvalueEvaluation implements Evaluation{

	private GameCache gcache;
	
	@Override
	public double evaluate(Game game, int[] state, int action, int player) {
		if(game.isVictory(state, player)){
			return 100;
		}
		else if(game.isVictory(state, Utils.changeTurn(player))){
			return -50;
		}
		else if(game.isEndGame(state)){
			return 0;
		}		
		else{
			return evaluateQvalue(state, action, game);
		}
	}
	
	@Override
	public void setGameCache(GameCache gcache){
		this.gcache = gcache;
	}
	
	private double evaluateQvalue(int[] state, int action, Game game){
		int[] fstate = Utils.getStateFunction(state, action, game.getOffset());
		return (Double)gcache.get(Arrays.toString(fstate));
	}
}
