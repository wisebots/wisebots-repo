package com.wisebots.rules.games;

/**
 * Essa interface pode ser implementada por qualquer jogo a medida que se queira treinar um agente
 * baseado em métodos de machine learning e/ou algoritmos de busca
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
	 * Obtém as possíveis ações de um estado
	 * @param state
	 * @return
	 */

	public int[] getPossibleActions(int[] state);
	
	/**
	 * Realiza uma ação em um estado para um jogador
	 * @param state
	 * @param action
	 * @param player
	 * @return
	 */
	
	public int[] makeAction(int[] state, int action, int player);
	
	/**
	 * Verifica se não existem mais lances para serem jogados para um estado 
	 * @param state
	 * @return
	 */
	
	public boolean isEndGame(int[] state);
	
	/**
	 * Verifica se um jogador, dado um estado, está em condição de vitória
	 * @param state
	 * @param player
	 * @return
	 */
	
	public boolean isVictory(int[] state, int player);

}
