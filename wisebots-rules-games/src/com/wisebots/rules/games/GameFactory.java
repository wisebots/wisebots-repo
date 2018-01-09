package com.wisebots.rules.games;

import com.wisebots.rules.games.impl.CardPoints;
import com.wisebots.rules.games.impl.ConnectFour;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.rules.games.impl.TicTacToeGravity;

public class GameFactory {

	public static Game create(String name){
		if(name.equalsIgnoreCase(TicTacToe.name)){
			return new TicTacToe();
		}
		else if(name.equalsIgnoreCase(TicTacToeGravity.name)){
			return new TicTacToeGravity();
		}
		else if(name.equalsIgnoreCase(ConnectFour.name)){
			return new ConnectFour();
		}
		else if(name.equalsIgnoreCase(CardPoints.name)){
			return new CardPoints();
		}
		
		return null;
	}
}
