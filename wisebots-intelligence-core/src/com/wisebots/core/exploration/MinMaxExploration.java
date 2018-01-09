package com.wisebots.core.exploration;

import com.wisebots.ai.method.AlgMinMax;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.rules.games.Game;

public class MinMaxExploration implements Exploration{
	
	private int level;

	@Override
	public int getAction(int[] state, int player, double delta, Game game, Evaluation evaluation) {
		AlgMinMax minmax = new AlgMinMax(evaluation, level);
		return minmax.getBestMove(game, state, player, delta);
	}

	@Override
	public void setLevel(int level) {
		this.level = level;		
	}

}
