package com.wisebots.core.exploration;

import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.rules.games.Game;

public interface Exploration {

	public int getAction(int[] state, int player, double delta, Game game, Evaluation evaluation);
	
	public void setLevel(int level);
}
