package com.wisebots.ai.method;

import java.util.Arrays;

import com.wisebots.core.cache.GameCache;
import com.wisebots.core.cache.SimpleHashCache;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.utils.Utils;

@Deprecated
public class MinMaxImpl {

	private Game game;
	@SuppressWarnings("unused")
	private GameCache cache;
	private static int wplayer;
	private static int achoosed;
	private static double valuechoosed;
	private static int depthTarget;

	public MinMaxImpl(Game game) {
		this.game = game;
	}

	public int getMove(int[] node, boolean maximize, int depth, GameCache cache, int player) {
		depthTarget = depth;
		achoosed = -1;
		valuechoosed = Integer.MIN_VALUE;
		wplayer = player;
		
		// this.cache = cache;

		@SuppressWarnings("unused")
		double value = staticScore(node, game, cache, player, maximize, depth);
		//System.out.println("  Escolhida acao " + achoosed + " para player " + player + " com valor " + value + " do estado " + Arrays.toString(node));

		return achoosed;
	}

	public static double staticScore(int[] node, Game game, GameCache cache, int player, boolean maximize, int depth) {

		if (game.isEndGame(node)) {
			if(game.isVictory(node, Utils.changeTurn(player))){				
				if (wplayer == Utils.changeTurn(player))
					return Integer.MAX_VALUE;
				else
					return Integer.MIN_VALUE;
			}
			else{
				return (Double) cache.get(Arrays.toString(node));
			}
		}
		else if(depth == 0){
			//System.out.println(Arrays.toString(node));
			//System.out.println("Retornando cache do node: " + (Double) cache.get(Arrays.toString(node)));
			return (Double) cache.get(Arrays.toString(node));
		}
		else{
			return bestScore(node, game, cache, player, maximize, depth);
		}
	}

	private static double bestScore(int[] node, Game game, GameCache cache, int player, boolean maximize, int depth) {

		// initialise it to the worst
		double best = worst(maximize);

		int[] moves = game.getPossibleActions(node);
		//if(depth == depthTarget)
		//	System.out.println("     Possibilidades: " + Arrays.toString(moves));
		for (int move : moves) {
			int[] nstate = game.makeAction(node, move, player);

			double childScore = staticScore(nstate, game, cache, Utils.changeTurn(player), !maximize, depth - 1);
			
			//if(depth == depthTarget)
			//	System.out.println("Testing Better than: " + childScore + "/" + best + "/" + valuechoosed + "/" + depth  + "/" + depthTarget);
			if (betterThan(childScore, best, maximize)){
				// System.out.println("Better than: " + childScore + "/" + best + "/" + valuechoosed + "/" + depth  + "/" + depthTarget);
				if(depth == depthTarget && valuechoosed <= childScore){
					// System.out.println("        Observando " + move + " em " + depth + "/" + depthTarget + "/" + valuechoosed + "/" + childScore);
					achoosed = move;
					valuechoosed = childScore;
				}
				best = childScore;
			}
		}
		return best;
	}

	// this should perhaps be a method of NodeInterface
	public static double worst(boolean maximize) {
		if (maximize)
			return Integer.MIN_VALUE;
		else
			return Integer.MAX_VALUE;
	}

	public static boolean betterThan(double x, double y, boolean maximize) {
		return ((x >= y) && maximize) || ((x < y) && !maximize);
	}

	/*
	public static void main(String[] args) {
		boolean maximize = true;
		int player = 1;
		int depth = 3;
		int[] node = new int[]{0,0,0,0,0,0,0,0,0};
		Game game = new TicTacToe();
		GameCache cache = new SimpleHashCache("tictactoe", "qpessimist");
		cache.set(new int[]{0,0,1,2,1,0,0,0,0}, new Double(100));

		MinMaxImpl mm = new MinMaxImpl(game);
		mm.getMove(node, maximize, depth, cache, player);
	}
	*/
}