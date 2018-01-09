package com.wisebots.ai.method;

import com.wisebots.rules.games.Game;
import com.wisebots.utils.Utils;


/**
 * Método de busca MinMax adaptado para o jogo da velha (heurística)
 * 
 * @author Adriano
 *
 */

public class MinMax {
	private Game game;
	
	public MinMax(Game game){
		this.game = game;
	}
	
	public int getMove(int[] state, int player) {

		int[] pactions = game.getPossibleActions(state);
		if (pactions.length == 0)
			return 0;

		int action = 0;
		int value = -999999;
		for(int i=0; i<pactions.length; i++){
			int[] nstate = game.makeAction(state, pactions[i], player);
			if(game.isVictory(nstate, player))
				return pactions[i];
			else{
				int minmax = getMinMax(nstate, Utils.changeTurn(player), player);
				// System.out.println("Minmax de " + nstate + "=" + minmax);
				if(minmax > value){
					value = minmax;
					action = pactions[i];
				}
			}
		}	
		
		return action;
	}
	
	private int getMinMax(int[] state, int turn, int player){
		int minmax = 0;
		int[] pactions = game.getPossibleActions(state);
		for(int i=0; i<pactions.length; i++){
			int[] nstate = game.makeAction(state, pactions[i], turn);
			if( game.isVictory(nstate, turn)){
				if(turn != player)
					minmax += -1;
				else
					minmax += 1;
			}
			else if (game.isEndGame(nstate)){
				minmax += 0;
			}
			else{
				minmax += getMinMax(nstate, Utils.changeTurn(turn), player);
			}
		}
		
		return minmax;
	}

}
