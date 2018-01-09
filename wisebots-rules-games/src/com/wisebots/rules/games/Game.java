package com.wisebots.rules.games;

/**
 * Essa interface pode ser implementada por qualquer jogo a medida que se queira treinar um agente
 * baseado em m�todos de machine learning e/ou algoritmos de busca
 * 
 * @author Adriano
 *
 */


public interface Game {
	
	
	public int[] initialize();
	
	public int getOffset();
	
	public String getGameVersion();
	
	public String getName();
	
	public int gameSizeState();
	
	/**
	 * Obt�m as poss�veis a��es de um estado
	 * @param state
	 * @return
	 */

	public int[] getPossibleActions(int[] state);
	
	/**
	 * Realiza uma a��o em um estado para um jogador
	 * @param state
	 * @param action
	 * @param player
	 * @return
	 */
	
	public int[] makeAction(int[] state, int action, int player);
	
	/**
	 * Verifica se n�o existem mais lances para serem jogados para um estado 
	 * @param state
	 * @return
	 */
	
	public boolean isEndGame(int[] state);
	
	/**
	 * Verifica se um jogador, dado um estado, est� em condi��o de vit�ria
	 * @param state
	 * @param player
	 * @return
	 */
	
	public boolean isVictory(int[] state, int player);

}
