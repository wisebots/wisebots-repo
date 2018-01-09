package com.wisebots.ai.method;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.wisebots.core.cache.GameCache;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.rules.games.Game;
import com.wisebots.utils.Utils;


/**
 * Metodo de aprendizado de maquina - Q-Learning
 * 
 * @author Adriano
 *
 */

public class QLearning {

	public static int counter = 1;
	private Game game;
	private Evaluation evaluation;
	private Exploration exploration;
	
	public QLearning(Game game, Evaluation evaluation, Exploration exploration){
		this.game = game;
		this.evaluation = evaluation;
		this.exploration = exploration;
	}

	/**
	 * 
	 * @param qvalue
	 * @param state
	 * @param action
	 * @param turn
	 * @param round
	 * @return
	 */
	
	public double[] updatePessismisticValue(GameCache gcache, List<int[]> states, List<Integer> actions, int turn, int round, double alfa, double gama) {
		double[] qvalues = new double[states.size()];
		if(states.size() > 1 ){
			for(int i=states.size()-2; i>=0; i--){
				
				qvalues[i] = updatePessimistcQValue(gcache, states.get(i), actions.get(i), turn, round, alfa, gama);
		
			}
		}
		return qvalues;
	}
	
	
	private double updatePessimistcQValue(GameCache gcache, int[] state, int action, int turn, int round, double alfa, double gama) {
		
		Double value = 0.0;
		Object obj = gcache.get(Arrays.toString(Utils.getStateActionFromArray(state, action, game.getOffset())));
		if(obj instanceof String){
			if(obj != null && !obj.equals("")){
				value = Double.parseDouble((String)obj);
			}
		}
		else{
			value = (Double)obj; // valor do estado do aprendizado
		}
		if (value == null) {
			value = 0.0;
		}
		

		Double qsa = value;
		
		double reward = 0.0;
		int[] stateone = game.makeAction(state, action, turn); 
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			reward = getReward(stateone, turn);
		}
	
		int[] statetomax = game.makeAction(state, action, turn); 		
		int[] envactions2  = game.getPossibleActions(statetomax); 		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se o ambiente chegou num estado terminal
				reward = getReward(envstate2, turn);
			}
		}
		
		if(reward == 0){
			value = qsa + alfa * (reward + gama * (minvalue(gcache, state, action, turn)) - qsa); // Atualizando Q(s,a)
		}
		else{
			value = reward;
		}

		//System.out.println("    PQV: Q(" + state+ "," + action + ")=" + value);
		gcache.set(Utils.getStateActionFromArray(state, action, game.getOffset()), value);
		return value;
	}
	
	public double updateQValue(GameCache gcache, int[] state, int action, int turn, int round, double alfa, double gama) {
		int[] fstate = Utils.getStateActionFromArray(state, action, game.getOffset()); // estado do aprendizado

		Double value = 0.0;
		Object v = gcache.get(Arrays.toString(fstate));
		if(v instanceof String){
			if(v != null && !v.equals("")){
				value = Double.parseDouble((String)v);
			}
		}
		else{
			value = (Double)v; // valor do estado do aprendizado
		}

		if (value == null) {
			value = 0.0;
		}

		Double qsa = value;
		
		double reward = 0.0;
		int[] stateone = game.makeAction(state, action, turn); 
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			reward = getReward(stateone, turn);
		}
	
		int[] statetomax = game.makeAction(state, action, turn); 		
		int[] envactions2  = game.getPossibleActions(statetomax); 		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action

			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se o ambiente chegou num estado terminal
				reward = getReward(envstate2, turn);
			}
		}
		
		if(reward == 0){
			double maxvalue = maxvalue(gcache, state, action, turn);
			value = qsa + alfa * (reward + gama * (maxvalue) - qsa); // Atualizando Q(s,a)
		}
		else{
			value = reward;
		}
		counter++;
	
		gcache.set(Utils.getStateActionFromArray(state, action, game.getOffset()), value);
		
		return value;
	}	
	
	public double updateQValueRewarded(GameCache gcache, int[] state, int action, int turn, int round, double alfa, double gama) {
		int[] fstate = Utils.getStateActionFromArray(state, action, game.getOffset()); // estado do aprendizado

		Double value = 0.0;
		Object v = gcache.get(Arrays.toString(fstate));
		if(v instanceof String){
			if(v != null && !v.equals("")){
				value = Double.parseDouble((String)v);
			}
		}
		else{
			value = (Double)v;// valor do estado do aprendizado
		}
		
		if (value == null) {
			value = 0.0;
		}

		Double qsa = value;
		
		double reward = 0.0;
		int[] stateone = game.makeAction(state, action, turn); 
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			reward = getReward(stateone, turn);
		}
	
		int[] statetomax = game.makeAction(state, action, turn); 		
		int[] envactions2  = game.getPossibleActions(statetomax); 		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action

			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se o ambiente chegou num estado terminal
				reward = getReward(envstate2, turn);
			}
		}
		
		if(reward == 0){
			double maxvalue = maxvalue(gcache, state, action, turn);
			reward = getEstimatedReward(gcache, statetomax, action, turn);
			value = qsa + alfa * (reward + gama * (maxvalue) - qsa); // Atualizando Q(s,a)
		}
		else{
			value = reward;
		}
		counter++;
	
		gcache.set(Utils.getStateActionFromArray(state, action, game.getOffset()), value);
		
		return value;
	}	
	
	
	/**
	 * 
	 * @param state
	 * @param action
	 * @param turn
	 * @return
	 */

	public double maxvalue(GameCache gcache, int[] state, int action, int turn) {
		double maxvalue = -9999999;

		// System.out.println("==: " + state + "/" + action);
		int[] stateone = game.makeAction(state, action, turn);
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			maxvalue = getReward(stateone, turn);
			return maxvalue;
		}
		
		// EXPLORING BRANCH TRICK Caso tenha ja como ganhar, nao tem que continuar jogando ...
		int[] checkactions = game.getPossibleActions(state);
		for(int k=0; k<checkactions.length; k++){
			int[] checkstate = game.makeAction(state, checkactions[k], turn);
			if(game.isVictory(checkstate, turn)){ // Verifica se e um estado terminal
				maxvalue = (-1)*getReward(checkstate, turn);
				return maxvalue;
			}
		}
		
	
		int[] statetomax = game.makeAction(state, action, turn); // Estado para ser maximizado	Q(s') - falta a'
		int[] envactions2  = game.getPossibleActions(statetomax); // Actions to environment
		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se e um estado terminal
				maxvalue = getReward(envstate2, turn);
				return maxvalue;
			}
		}
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			int[] actionslinha = game.getPossibleActions(envstate2);
				
			for(int m=0; m<actionslinha.length; m++){ // Para cada a' desse estado achar o maior valor
				int[] statemax = game.makeAction(envstate2, actionslinha[m], turn); // Estado to max Q(s',a')
				// System.out.print(state + "/" + statetomax + "/" + envstate2 + "/" + statemax + "\n");	
				if(game.isEndGame(statemax) || game.isVictory(statemax, turn)){ // Verifica se e um estado terminal
					double reward = getReward(statemax, turn);
					if(maxvalue < reward){
						maxvalue = reward;
					}
				}
				else{
					int[] fstatemax = Utils.getStateActionFromArray(envstate2, actionslinha[m], game.getOffset());
					Double v = 0.0;
					Object qv = gcache.get(Arrays.toString(fstatemax));
					if(qv instanceof String){
						if(qv != null && !qv.equals(""))
							v = Double.parseDouble((String)qv);
					}
					else{
						v = (Double)gcache.get(Arrays.toString(fstatemax));
					}
	
					if(v >= maxvalue){
						maxvalue = v;
					}
				}
			}
			
		}		
		return maxvalue;
	}
	
	public double minvalue(GameCache gcache, int[] state, int action, int turn) {
		double maxvalue = 99999999;

		int[] stateone = game.makeAction(state, action, turn);
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			maxvalue = getReward(stateone, turn);
			return maxvalue;
		}
	
		int[] statetomax = game.makeAction(state, action, turn); // Estado para ser maximizado	Q(s') - falta a'			
		int[] envactions2  = game.getPossibleActions(statetomax); // Actions to environment
		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se e um estado terminal
				maxvalue = getReward(envstate2, turn);
				return maxvalue;
			}
		}
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			int[] actionslinha = game.getPossibleActions(envstate2);
			
			for(int m=0; m<actionslinha.length; m++){ // Para cada a' desse estado achar o maior valor
				int[] statemax = game.makeAction(envstate2, actionslinha[m], turn); // Estado to max Q(s',a')
				// System.out.print(state + "/" + statetomax + "/" + envstate2 + "/" + statemax + "\n");	
				if(game.isEndGame(statemax) || game.isVictory(statemax, turn)){ // Verifica se e um estado terminal
					double reward = getReward(statemax, turn);
					if(maxvalue > reward){
						if(state.equals(new BigInteger("0")))
							System.out.println(" 1==>" + reward); //TODO nunca entra aqui - entender
						maxvalue = reward;
					}
				}
				else{
					int[] fenvstate2 = Utils.getStateActionFromArray(envstate2, actionslinha[m], game.getOffset());
					Object obj = gcache.get(Arrays.toString(fenvstate2));
					Double v = 0.0;
					if(obj instanceof String){
						v = Double.parseDouble((String)obj);
					}
					else{
						v = (Double)gcache.get(Arrays.toString(fenvstate2));
					}
					if(v <= maxvalue){			
						maxvalue = v;
					}
				}
			}
			
		}		
		return maxvalue;
	}	
	
	public double getEstimatedReward(GameCache gcache, int[] state, int action, int turn) {
		double qtVicDraw = 0.0;
		double qtLoss = 0.0;
		double qtTotal = 0.0;

		// System.out.println("==: " + state + "/" + action);
		int[] stateone = game.makeAction(state, action, turn);
		if(game.isEndGame(stateone) || game.isVictory(stateone, turn)){ // Verifica se e um estado terminal
			return 100;
		}
	
		int[] statetomax = game.makeAction(state, action, turn); // Estado para ser maximizado	Q(s') - falta a'
		int[] envactions2  = game.getPossibleActions(statetomax); // Actions to environment
		
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			if(game.isEndGame(envstate2) || game.isVictory(envstate2, Utils.changeTurn(turn))){ // Verifica se e um estado terminal
				qtLoss++;
				qtTotal++;
			}
		}
		for(int k=0; k<envactions2.length; k++){
			int[] envstate2 = game.makeAction(statetomax, envactions2[k], Utils.changeTurn(turn)); // Environment action
			int[] actionslinha = game.getPossibleActions(envstate2);
				
			for(int m=0; m<actionslinha.length; m++){ // Para cada a' desse estado achar o maior valor
				int[] statemax = game.makeAction(envstate2, actionslinha[m], turn); // Estado to max Q(s',a')

				if(game.isEndGame(statemax) || game.isVictory(statemax, turn)){ // Verifica se e um estado terminal
					qtVicDraw++;
				}
				qtTotal++;
			}
		}		
		
		double reward = (qtVicDraw/qtTotal)*100 + (qtLoss/qtTotal)*(-50);
		
		return reward;
	}
	
	/**
	 * 
	 * @param state
	 * @param turn
	 * @return    
	 */
	
	public double getReward(int[] state, int turn){
		if (game.isVictory(state, turn)) { // Estado terminal - vitoria player 1
			return 100.0;
		} else if (game.isVictory(state, Utils.changeTurn(turn))) { // Estado terminal - vitoria player 2
			return -50.0;
		} else if (game.getPossibleActions(state).length == 0) { // Estado terminal - empate
			return 20.0;
		}
		return 0.0;
	}

	/**
	 * Obtém a ação de acordo com a melhor possibilidade da função de avaliação
	 * Se empatar, pega aleatório
	 * @param qvalue
	 * @param state
	 * @param turn
	 * @return
	 */

	public int getQValueMemoryMove(int id, GameCache gcache, int[] state, int turn) {
		double vmax = -99999;
		int[] pactions = game.getPossibleActions(state);
		
		Vector<Integer> choosed = new Vector<Integer>();
		for (int i = 0; i < pactions.length; i++) {
			// long nstate = game.makeAction(state, pactions[i], turn); // Avaliando um possível estado do jogo
			int[] fstate = Utils.getStateActionFromArray(state, pactions[i], game.getOffset()); // Obtendo o estado da função do aprendizado
			
			Double v = (Double)gcache.getSpecified(id, "QLPESSIMISTIC", fstate); // Obtendo o valor do estado da função do aprendizado
			if (v == null){
				v = 0.0;
			}
			if (v > vmax) {
				vmax = v;
				choosed = new Vector<Integer>();
				choosed.add(pactions[i]);
			}
			else if( v == vmax){
				choosed.add(pactions[i]);
			}
		}

		int choose = 0;
		
		if(choosed.size() > 0){
			Random rand = new Random();
			choose = rand.nextInt(choosed.size());
		}
		
		return choosed.get(choose);
	}
	
	/**
	 * e-greedy
	 * @param qvalue
	 * @param state
	 * @param turn
	 * @return
	 */
	
	public double[] getGreedyQValueArray(GameCache gcache, int[] state, int turn) {
		int[] pactions = game.getPossibleActions(state);
		
		double[] greedyarray = new double[pactions.length];
		for (int i = 0; i < pactions.length; i++) {
			
			int[] fstate = Utils.getStateActionFromArray(state, pactions[i], game.getOffset());
			Double v = (Double)gcache.get(Arrays.toString(fstate)); // Obtendo o valor do estado da função do aprendizado
			if (v == null){
				v = 0.0;
			}
			greedyarray[i] = v;
		}
		return greedyarray;
	}
	
	//TODO Esse metodo nao eh do Qlearning. Eh de um metodo de E-Greedy
	
	public int getGreedyQValueMove(Integer id, String method, GameCache gcache, int[] state, int turn) {
		double vmax = -99999;
		int[] pactions = game.getPossibleActions(state);
		if(pactions.length == 0){
			return -1;
		}
		
		Vector<Integer> choosed = new Vector<Integer>();
		for (int i = 0; i < pactions.length; i++) {
			
			int[] fstate = Utils.getStateActionFromArray(state, pactions[i], game.getOffset());
			Double v = 0.0;

			Object kv = gcache.get(Arrays.toString(fstate));
			if(kv instanceof String){
				v = Double.parseDouble((String)kv);
			}
			else{
				v = (Double)kv; // Obtendo o valor do estado da funcao do aprendizado
			}
			
			// System.out.println("  " + fstate + "=" + v);
			if (v == null){
				v = 0.0;
			}
			if (v > vmax) {
				vmax = v;
				choosed = new Vector<Integer>();
				choosed.add(pactions[i]);
			}
			else if( v == vmax){
				choosed.add(pactions[i]);
			}
		}

		
		int choose = 0;
		
		if(choosed.size() > 1){
			evaluation.setGameCache(gcache);
			int action = exploration.getAction(state, turn, 1.0, game, evaluation);
			return action;
			
			/* 
			Random rand = new Random();
			choose = rand.nextInt(choosed.size());
			//TODO Colocar algo melhor que RANDOM aqui*/
		}
		else{
			choose = 0;
		}
		
		return choosed.get(choose);

	}	
	
}
