package com.wisebots.core.evaluation;

import com.wisebots.core.cache.GameCache;
import com.wisebots.rules.games.Game;

public interface Evaluation {

	public double evaluate(Game game, int[] state, int action, int player);
	
	public void setGameCache(GameCache gcache);
}
