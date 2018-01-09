package com.wisebots.rules.games;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

import com.wisebots.rules.games.impl.ConnectFour;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.rules.games.impl.TicTacToeGravity;

public class GameComplexity {
	 
	public int iter;
	
	private long maxtimepacts = 0;
	private long maxtimemakeacts = 0;
	private long medtimepacts = 0;
	private long medtimemakeacts = 0;
	private int maxact = 0;
	private BigInteger probsmax = new BigInteger("0");
	private int statesize = 0;
	
	public GameComplexity(int iter){
		this.iter = iter;
	}

	public String calculate(Game game){
		
		int states = game.gameSizeState();
		int[] state = new int[states];
		statesize = state.length;

		for(int i=0; i<iter; i++){
			int player = 1;
			state = new int[states];
			BigInteger probs = new BigInteger("1");
			while(!game.isEndGame(state)){
				long a = Calendar.getInstance().getTimeInMillis();
				int[] acts = game.getPossibleActions(state);
				long b = Calendar.getInstance().getTimeInMillis();
				medtimepacts += (b-a);
				if(maxtimepacts < (b-a))
					maxtimepacts = (b-a);
				
				int act = new Random().nextInt(acts.length);
				probs = probs.multiply(new BigInteger(""+acts.length));
		
				long c = Calendar.getInstance().getTimeInMillis();
				state= game.makeAction(state, acts[act], player);
				long d = Calendar.getInstance().getTimeInMillis();
				medtimemakeacts += (d-c);
				if(maxtimemakeacts < (d-c))
					maxtimemakeacts = (d-c);
				
				if(player==1)
					player=2;
				else if(player == 2)
					player=1;

				for(int k=0; k<state.length; k++){
					if(state[k] > maxact)
						maxact = state[k];
				}
				
				if(probs.compareTo(probsmax) == 1){
					probsmax = new BigInteger("0");
					probsmax = probsmax.add(probs);
				}
			}
		}
		
		medtimepacts = medtimepacts/iter;
		medtimemakeacts = medtimemakeacts/iter;
		
		/*
		System.out.println("Maxtimeacts: " + maxtimepacts);
		System.out.println("Maxtimemakeacts: " + maxtimemakeacts);
		System.out.println("Medtimeacts: " + medtimepacts);
		System.out.println("Medimemakeacts: " + medtimemakeacts);
		System.out.println("Maxact: " + maxact);
		System.out.println("Probsmax: " + probsmax.toString());
		System.out.println("Complexity order: " + probsmax.toString().length());
		*/
		
		String  v = calculate(maxact, statesize, medtimepacts, medtimemakeacts, probsmax.toString().length());
		
		return v;
	}
	
	
	public static String calculate(int maxact, int statesize, long medtimepacts, long medtimemakeacts, int complexityorder){
		double alfa = 0.7;
		
		DecimalFormat df = new DecimalFormat("0.00");
		String value = df.format(alfa*((double)(complexityorder+(maxact*statesize))) + (1-alfa)*(((double)medtimepacts+medtimemakeacts)/(medtimepacts*medtimemakeacts+1)));
		
		return value;
	}

	public long getMaxtimepacts() {
		return maxtimepacts;
	}

	public void setMaxtimepacts(long maxtimepacts) {
		this.maxtimepacts = maxtimepacts;
	}

	public long getMaxtimemakeacts() {
		return maxtimemakeacts;
	}

	public void setMaxtimemakeacts(long maxtimemakeacts) {
		this.maxtimemakeacts = maxtimemakeacts;
	}

	public long getMedtimepacts() {
		return medtimepacts;
	}

	public void setMedtimepacts(long medtimepacts) {
		this.medtimepacts = medtimepacts;
	}

	public long getMedtimemakeacts() {
		return medtimemakeacts;
	}

	public void setMedtimemakeacts(long medtimemakeacts) {
		this.medtimemakeacts = medtimemakeacts;
	}

	public int getMaxact() {
		return maxact;
	}

	public void setMaxact(int maxact) {
		this.maxact = maxact;
	}

	public BigInteger getProbsmax() {
		return probsmax;
	}

	public void setProbsmax(BigInteger probsmax) {
		this.probsmax = probsmax;
	}

	public int getStatesize() {
		return statesize;
	}

	public void setStatesize(int statesize) {
		this.statesize = statesize;
	}
	
	
	
	/*
	public static void main(String[] args) {
		Game game = new TicTacToe();
		String vtictactoe = calculate(game);
		System.out.println("Tictactoe: " + vtictactoe);
		
		game = new ConnectFour();
		String vconnectfour = calculate(game);
		System.out.println("Connect4: " + vconnectfour);
		
		game = new TicTacToeGravity();
		String vgravity = calculate(game);
		System.out.println("TicTacToe Gravity: " + vgravity);
		
		game = new TicTacToe(4);
		String vtictactoe4 = calculate(game);
		System.out.println("Tictactoe4: " + vtictactoe4);
		
		game = new TicTacToe(5);
		String vtictactoe5 = calculate(game);
		System.out.println("Tictactoe5: " + vtictactoe5);
	}
	*/
}
