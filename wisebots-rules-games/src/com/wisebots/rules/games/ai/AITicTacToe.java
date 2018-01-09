package com.wisebots.rules.games.ai;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.ai.AlgMinMax;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.rules.games.utils.Utils;

public class AITicTacToe {
	
	public static final int PLAYER1 = 1;
	public static final int PLAYER2 = 2;

	public static int getBestAction(int turn, int[] state, double delta){
		
		Game game = new TicTacToe();
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], turn);
			if(game.isVictory(nstate, turn)){
				return actions[i];
			}
		}
		for(int i=0; i<actions.length; i++){
			int[] nstate = game.makeAction(state, actions[i], Utils.changeTurn(turn));
			if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return actions[i];
			}
		}

		if(pertence(actions,4)){
			return 4;
		}
		else if(pertence(actions,0)){
			int[] nstate = game.makeAction(state, 0, turn);
			if(!checkDanger(game,nstate,Utils.changeTurn(turn))) return 0;
		}
		else if(pertence(actions,2)){
			int[] nstate = game.makeAction(state, 2, turn);
			if(!checkDanger(game,nstate,Utils.changeTurn(turn))) return 2;
		}	
		else if(pertence(actions,6)){
			int[] nstate = game.makeAction(state,6, turn);
			if(!checkDanger(game,nstate,Utils.changeTurn(turn))) return 6;
		}
		else if(pertence(actions,8)){
			int[] nstate = game.makeAction(state, 8, turn);
			if(!checkDanger(game,nstate,Utils.changeTurn(turn))) return 8;
		}
		
		int k = -1;
		while(k == -1 || actions[k] == 0 || actions[k] == 2 || actions[k] == 6 || actions[k] == 8){
			Random rand = new Random();
			k = rand.nextInt(actions.length);
		}
		
		System.out.println("----------> Random: " + actions[k]);
		return actions[k];

	}
	
	public static boolean checkDanger(Game game, int state[], int turn){
		int[] actions = game.getPossibleActions(state);
		for(int i=0; i<actions.length; i++){
			int qtdanger = 0;
			int[] nstate = game.makeAction(state, actions[i], turn);
			for(int j=0; j<actions.length; j++){
				int[] nnstate = game.makeAction(nstate, actions[j], turn);
				if(game.isVictory(nnstate, turn)){
					qtdanger++;
				}
				if(qtdanger>1){
					return true;
				}
			}
			System.out.println("--------------> Danger from " + Arrays.toString(state) + ": " + qtdanger + " on " + turn);
		}

		
		return false;
	}
	
	public static int testGame(Game game) {
		int turn = 1;

		int[] state = game.initialize();

		List<int[]> path = new ArrayList<int[]>();
		System.out.println("----- NEW GAME -----");
		while (!game.isEndGame(state)) { 
			int[] actions = game.getPossibleActions(state);
			Random rand = new Random();
			int act = rand.nextInt(actions.length);
			int[] nstate = game.makeAction(state, actions[act], turn);
			path.add(nstate);

			if (game.isVictory(nstate, turn)) {
				printList(path);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
			
			int action = getBestAction(turn, nstate, 1.0);
			System.out.println("---> " + action + ": " + Arrays.toString(nstate) + " from turn " + turn);
			state = game.makeAction(nstate, action, turn);
			System.out.println("---> nstate: " + Arrays.toString(state) + " from turn " + turn);
			path.add(state);
			
			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
		}

		return 0;
	}
	
	public static int testMinmaxGame(Game game) {
		int turn = 1;

		int[] state = game.initialize();

		List<int[]> path = new ArrayList<int[]>();

		while (!game.isEndGame(state)) { 
			int[] actions = game.getPossibleActions(state);
			Random rand = new Random();
			int act = rand.nextInt(actions.length);
			int[] nstate = game.makeAction(state, actions[act], turn);
			path.add(nstate);

			if (game.isVictory(nstate, turn)) {
				printList(path);
				return turn;
			} else if(game.isVictory(nstate, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(nstate)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
			
			int action = new AlgMinMax().getBestMove(game, nstate, turn);
			// System.out.println("---> " + action + ": " + Arrays.toString(nstate) + " from turn " + turn);
			state = game.makeAction(nstate, action, turn);
			// System.out.println("---> nstate: " + Arrays.toString(state) + " from turn " + turn);
			path.add(state);
			
			if (game.isVictory(state, turn)) {
				return turn;
			} else if(game.isVictory(state, Utils.changeTurn(turn))){
				return Utils.changeTurn(turn);
			} else if(game.isEndGame(state)){
				return 0;
			}
			
			turn = Utils.changeTurn(turn);
		}

		return 0;
	}
	
	public static boolean pertence(int[] array, int x){
		for(int k: array){
			if(k==x)
				return true;
		}
		return false;
	}
	
	public static void printList(List<int[]> lista){
		for(int[] array: lista){
			System.out.println(Arrays.toString(array));
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		
		int epochs = 1000;
		int player1vics = 0;
		int player2vics = 0;
		int draws = 0;
		
		for(int i=0; i<epochs; i++){
			Game game = new TicTacToe();
			int v = testMinmaxGame(game);
			if(v==1){
				player1vics++;
			}
			else if(v==2){
				player2vics++;
			}
			else if(v==0){
				draws++;
			}
		}
		
		DecimalFormat df = new 	DecimalFormat("0.00");
		String p1 = df.format(Double.parseDouble(""+player1vics)*100/epochs);
		String p2 = df.format(Double.parseDouble(""+player2vics)*100/epochs);
		String pd = df.format(Double.parseDouble(""+draws)*100/epochs);
		System.out.println(p1 + "%/" + p2 + "%/" + pd + "%");
		
	}

}
