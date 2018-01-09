package com.wisebots.ai.method;

import java.util.Random;

import com.wisebots.rules.games.Game;

/**
 * M�todo rand�mico de gera��o de a��es
 * 
 * @author Adriano
 *
 */

public class Randomization {
	
	private Game game;
	
	public Randomization(Game game){
		this.game = game;
	}
	
	public int getRandomMove(int[] state) {

		int[] pactions = game.getPossibleActions(state);
		if (pactions.length == 0)
			return 0;

		Random rand = new Random();
		int pos = rand.nextInt(pactions.length);

		return pactions[pos];
	}
}
