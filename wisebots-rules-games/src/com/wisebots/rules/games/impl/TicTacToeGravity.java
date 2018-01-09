package com.wisebots.rules.games.impl;

import java.util.Arrays;

import com.wisebots.rules.games.Game;


/**
 * Regras de um jogo da velha
 *
 * @author Adriano
 *
 */

public class TicTacToeGravity implements Game{

	public final static String name = "TICTACTOEGRAVITY";
	public final static String version = "1.0";
	
	public static final int P1 = 1;
	public static final int P2 = 2;
	
	public int TABLESIZE = 16;
	public int LINEVICTORY = 4;
	public int OFFSET = 0; 

	public TicTacToeGravity(){
	}

	public TicTacToeGravity(int t){
		this.LINEVICTORY = t;
		this.TABLESIZE = t*t;
	}

	public int[] initialize(){
		int size = gameSizeState();
		return new int[size];
	}
	
	public int[] makeAction(int[] state, int action, int player){
		int nstate[] = Arrays.copyOf(state, state.length);
		nstate[action] = player;		
		return nstate;
	}


	public int[] getPossibleActions(int[] state){
		if( isVictory(state, P1) || isVictory(state, P2))
			return new int[0];

		int[] pactions = new int[0];
		int qta = 0;
		for(int i=0; i<TABLESIZE; i++){
			if(state[i] == 0 && (i+Math.sqrt(TABLESIZE))>TABLESIZE){
				qta++;
			}			
			if(state[i] == 0 && (i+Math.sqrt(TABLESIZE))<TABLESIZE && state[i+(int)Math.sqrt(TABLESIZE)] != 0){
				qta++;
			}
		}

		pactions = new int[qta];
		int j=0;
		for(int k=0; k<TABLESIZE; k++){
			if(state[k] == 0 && (k+Math.sqrt(TABLESIZE))>TABLESIZE){
				pactions[j] = k;
				j++;
			}
			else if(state[k] == 0 && (k+Math.sqrt(TABLESIZE))<TABLESIZE && state[k+(int)Math.sqrt(TABLESIZE)] != 0){
				pactions[j] = k;
				j++;
			}
		}

		return pactions;
	}

	public boolean isEndGame(int[] state){
		int[] pactions = getPossibleActions( state);
		if(pactions.length == 0)
			return true;

		return false;

	}

	public boolean isVictory(int[] state, int player){

		for(int i=0; i<LINEVICTORY; i++){
			int qtline = 1;
			for(int j=i*LINEVICTORY; j<LINEVICTORY-1; j++){
				if(state[j] == player && state[j] == state[j+1])
					qtline++;
			}
			if(qtline == LINEVICTORY){
				return true;
			}
		}

		for(int i=0; i<LINEVICTORY; i++){
			int qtcolumn = 1;
			for(int j=0; j<LINEVICTORY-1; j++){
				if(state[j*LINEVICTORY+i] == player && state[j*LINEVICTORY+i] == state[(j+1)*LINEVICTORY+i])
					qtcolumn++;
			}
			if(qtcolumn == LINEVICTORY){
				return true;
			}
		}

		for(int i=0; i<LINEVICTORY; i++){
			int qtdiagmain = 1;
			for(int j=0; j<LINEVICTORY-1; j++){
				if(state[j*(LINEVICTORY+1)] == player && state[j*(LINEVICTORY+1)] == state[(j+1)*(LINEVICTORY+1)])
					qtdiagmain++;
			}
			if(qtdiagmain == LINEVICTORY){
				return true;
			}
		}

		for(int i=0; i<LINEVICTORY; i++){
			int qtdiaginversa = 1;
			for(int j=0; j<LINEVICTORY-1; j++){
				if(state[j*(LINEVICTORY-1)] == player && state[j*(LINEVICTORY-1)] == state[(j+1)*(LINEVICTORY-1)])
					qtdiaginversa++;
			}
			if(qtdiaginversa == LINEVICTORY){
				return true;
			}
		}

		return false;
	}

	public int gameSizeState() {
		return TABLESIZE;
	}

	@Override
	public String getGameVersion() {
		return name + version;
	}
	
	@Override
	public int getOffset() {
		return OFFSET;
	}
	
	public String getName(){
		return name;
	}

}