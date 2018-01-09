package com.wisebots.core;

/**
 * Qualidade de uma sequência de partidas
 * 
 * @author Adriano
 *
 */

public class Quality {

	// Quantidade de vitorias do jogador 1
	private String player1;
	
	// Quantidade de vitorias do jogador 2
	private String player2;
	
	// Quantidade de empates
	private String draw;
	
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer2() {
		return player2;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	@Override
	public String toString() {
		return "Quality [player1=" + player1 + ", player2=" + player2
				+ ", draw=" + draw + "]";
	}
	
}
