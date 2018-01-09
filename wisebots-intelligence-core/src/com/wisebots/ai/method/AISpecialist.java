package com.wisebots.ai.method;

import com.wisebots.rules.games.Game;

public interface AISpecialist {

	public int getBestMove(Game game, int[] state, int player, double delta);
}
