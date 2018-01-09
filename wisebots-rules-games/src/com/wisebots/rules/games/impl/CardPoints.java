package com.wisebots.rules.games.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.wisebots.rules.games.Game;

public class CardPoints implements Game {

	public final static String name = "CARDPOINTS";
	public final static String version = "1.0";
	public static final int P1 = 0;
	public static final int P2 = 1;
	
	public int LINEGAME = 6;
	public int BESTSCORE = 1;
	public int OFFSET = 2; //SUMPOINTS

	public CardPoints(){	
	}
	
	public int[] initialize(){
		int size = gameSizeState();
		int[] state = new int[size];
		
		List<Integer> cards = new ArrayList<Integer>();
		cards.add(1);cards.add(2);cards.add(3);cards.add(4);cards.add(5);cards.add(6);cards.add(7);cards.add(8);cards.add(9);cards.add(10);cards.add(11);cards.add(12);cards.add(13);
		state[0] = 0; // pontuacao p1 (offset)
		state[1] = 0; // pontuacao p2 (offset)
		state[2] = 0; // empate (estado do jogo)
		for(int i=OFFSET+BESTSCORE; i<state.length; i++){
			int x = new Random().nextInt(cards.size());
			state[i] = cards.get(x);
			cards.remove(x);
		}
		
		return state;
	}
	

	@Override
	public String getGameVersion() {
		return version;
	}

	@Override
	public int gameSizeState() {
		return OFFSET + BESTSCORE + LINEGAME;
	}

	@Override
	public int[] getPossibleActions(int[] state) {
		
		if(state.length == OFFSET+BESTSCORE){
			return new int[]{};
		}
		
		if(state.length == OFFSET+BESTSCORE+1){
			return new int[]{OFFSET+BESTSCORE};
		}
		
		int[] actions = new int[2];
		actions[0] = OFFSET+BESTSCORE;
		actions[1] = state.length-1;
		
		return actions;
	}

	@Override
	public int[] makeAction(int[] state, int action, int player) {
		int nstate[] = new int[state.length-1];
		for(int i=0; i<nstate.length; i++){
			if(i < action){
				nstate[i] = state[i];
			}
			else{
				nstate[i] = state[i+1];
			}
		}
		nstate[player-1] += state[action];
		if(nstate[1] > nstate[0]){
			nstate[2] = 1;
 		}
		else if(nstate[1] < nstate[0]){
			nstate[2] = 2;
		}
		else{
			nstate[2] = 0;
		}
		
		return nstate;
	}

	@Override
	public boolean isEndGame(int[] state) {
		if(state.length == OFFSET+BESTSCORE){
			return true;
		}
			
		return false;
	}

	@Override
	public boolean isVictory(int[] state, int player) {
		if(state[OFFSET] == player && state.length == OFFSET+BESTSCORE)
			return true;
		
		return false;
	}
	
	@Override
	public int getOffset() {
		return OFFSET;
	}
	
	public static void main(String[] args) {
		CardPoints game = new CardPoints();
		int[] state = game.initialize();
		System.out.println(Arrays.toString(state));
		
		int turn = P1;
		while(!game.isEndGame(state)){
			int[] actions = game.getPossibleActions(state);
			state = game.makeAction(state, actions[0], turn);
			System.out.println(Arrays.toString(state));
			if(turn == P1){
				turn = P2;
			}else{
				turn = P1;
			}
		}
		if(game.isVictory(state, P1)){
			System.out.println("Victory from P1: " + state[P1]);
		}
		if(game.isVictory(state, P2)){
			System.out.println("Victory from P2: " + state[P2]);
		}
		
	}
	
	public String getName(){
		return name;
	}

}
