package com.wisebots.core.evaluation;

import com.wisebots.core.cache.GameCache;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.utils.Utils;

public class TerminalEvaluation implements Evaluation{

	@Override
	public double evaluate(Game game, int[] state, int action, int player) {
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

	@Override
	public void setGameCache(GameCache gcache) {

	}
}
