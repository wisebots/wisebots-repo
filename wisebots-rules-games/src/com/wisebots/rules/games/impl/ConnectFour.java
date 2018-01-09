package com.wisebots.rules.games.impl;

import java.util.Arrays;

import com.wisebots.rules.games.Game;


public class ConnectFour implements Game {
	
	public final static String name = "CONNECTFOUR";
	public final static String version = "1.0";
	
	public static final int P1 = 1;
	public static final int P2 = 2;
	public static final int DRAW = 0;
	
	public int TABLELINE = 6;
	public int TABLECOL = 7;
	public int LINEVICTORY = 4;
	public int OFFSET = 0; 
	
	public int[] initialize(){
		int size = gameSizeState();
		return new int[size];
	}

	public boolean isVictory(int[] state, int player){

		for(int i=0; i<TABLELINE; i++){
			int qstart = i*TABLECOL;
			for(int k=0; k<TABLECOL-LINEVICTORY+1;k++){
				int c = qstart+k;
				if(state[c] == player){
					int qtline = 1;
					for(int v=1; v<LINEVICTORY; v++){
						if(state[c]==state[c+v]){
							qtline++;
						}
					}
					if(qtline==LINEVICTORY){
						// System.out.println("LINEVICTORY");
						
						return true;
					}
				}
			}
		}
		
		for(int i=0; i<TABLECOL; i++){
	
			int qstart = TABLELINE-i;
			for(int k=0; k<TABLELINE-LINEVICTORY+1;k++){
				int c = qstart+k*TABLECOL;
				if(state[c] == player){
					int qtcol = 1;
					for(int v=1; v<LINEVICTORY; v++){
						if(state[c]==state[c+v*TABLECOL]){

							qtcol++;
						}
					}
					if(qtcol==LINEVICTORY){
						//System.out.println(Arrays.toString(state));
						return true;
					}
				}
			}
		}
		
		for(int i=0; i<TABLELINE-LINEVICTORY+1; i++){
			int qstart = i*TABLECOL;

			for(int k=0; k<TABLECOL-LINEVICTORY+1; k++){
				int c = qstart+k;
				// System.out.println("To observando c: " + c);
				if(state[c] == player){
					// System.out.println("Identifiquei " + player + " em " + c);
					int qtdiagprinc = 0;
					for(int v=0; v<LINEVICTORY; v++){
						if(state[c] == state[c+v*(TABLECOL+1)]){
							// System.out.println(c+v*(TABLECOL+1) + "/" + state[c] );
	
							qtdiagprinc++;
						}
					}
					if(qtdiagprinc==LINEVICTORY){
						// System.out.println("DIAGINVERSA=" +qtdiagprinc + "=" + avalia);
						return true;
					}
				}
			}
		}

		for(int i=TABLECOL-1; i>TABLELINE-LINEVICTORY+1; i--){
			// System.out.println("Variando ** de " + (TABLECOL-1) + " ate " + (TABLELINE-LINEVICTORY+1));
			int qstart = (TABLECOL * (i-(TABLECOL-LINEVICTORY))) - 1;
			// System.out.println("QSTARTS: " + qstart);
			for(int k=0; k<TABLECOL-LINEVICTORY+1; k++){
				// System.out.println("Variando de: " + k + " ate " + (TABLECOL-LINEVICTORY+1));
				int c = qstart-k;
				// System.out.println("To observando c: " + c);
				if(state[c] == player){
					// System.out.println("Identifiquei " + player + " em " + c);
					int qtdiaginv = 0;
					for(int v=0; v<LINEVICTORY; v++){
						// System.out.println(c+v*(TABLELINE) + "/" + state[c] );
						if(state[c] == state[c+v*(TABLELINE)]){							
							qtdiaginv++;
						}
					}
					if(qtdiaginv==LINEVICTORY){
						//System.out.println("DIAGINVVICTORY=" +qtdiaginv);
						return true;
					}
				}
			}
		}

		return false;
	}
	
	
	public static void main(String[] args) {
		
	
		int[] state = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 2, 2, 1, 0, 0, 2, 2, 2, 1, 1, 0, 0, 1, 2, 2, 1, 1, 1};
		ConnectFour game = new ConnectFour();
		boolean b = game.isVictory(state, 2);
		System.out.println(b);
		// int[] pactions = game.getPossibleActions(state);
		// System.out.println(Arrays.toString(pactions));
	}
	
	
	public int[] getPossibleActions(int[] state) {
		if( isVictory(state, P1) || isVictory(state, P2)){
			return new int[0];
		}

		int[] pactions = new int[0];
		int qta = 0;
		for(int i=0; i<TABLELINE; i++){
			int qstart = i*TABLECOL;
			for(int j=qstart; j<(qstart+TABLECOL); j++){
				if(state[j]==0 && (j+TABLECOL)>TABLECOL*TABLELINE-1){
					qta++;
				}
				else if(state[j] == 0 && state[j+TABLECOL]!=0){
					qta++;
				}
			}
		}

		pactions = new int[qta];
		int act=0;
		for(int i=0; i<TABLELINE; i++){
			int qstart = i*TABLECOL;
			for(int j=qstart; j<(qstart+TABLECOL); j++){
				if(state[j]==0 && (j+TABLECOL)>TABLECOL*TABLELINE-1){
					pactions[act] = j;
					act++;
				}
				else if(state[j] == 0 && state[j+TABLECOL]!=0){
					pactions[act] = j;
					act++;
				}
			}
		}

		return pactions;
	}

	public int[] makeAction(int[] state, int action, int player){
		int nstate[] = Arrays.copyOf(state, state.length);
		nstate[action] = player;		
		return nstate;
	}
	
	public boolean isEndGame(int[] state){
		int[] pactions = getPossibleActions( state);
		if(pactions.length == 0)
			return true;

		return false;

	}
	
	public int gameSizeState() {
		return TABLELINE*TABLECOL;
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
