package com.wisebots.core.exploration;

import com.wisebots.ai.method.Randomization;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.rules.games.Game;

public class RandomExploration implements Exploration{

	@Override
	public int getAction(int[] state, int player, double delta, Game game, Evaluation evaluation) {
		Randomization randomization = new Randomization(game);
		return randomization.getRandomMove(state);
	}

	@Override
	public void setLevel(int level) {

	}

}
