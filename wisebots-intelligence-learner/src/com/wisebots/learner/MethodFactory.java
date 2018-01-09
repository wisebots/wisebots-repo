package com.wisebots.learner;

import com.wisebots.ai.method.Learning;



public class MethodFactory {

	@SuppressWarnings("deprecation")
	public static Learning create(String method, String techopponent, String opponent, boolean loadMembase){
		Learning learning = null;
		
		if(method.equalsIgnoreCase("QLPESSIMISTIC") && techopponent.equalsIgnoreCase("RANDOM")){
			learning = new QLPessimisticRandomLearner(loadMembase, loadMembase);
		}
		else if(method.equalsIgnoreCase("QLPESSIMISTIC") && techopponent.equalsIgnoreCase("SPECIALIST")){
			learning = new QLPessimisticVersusSpecialist(loadMembase, loadMembase);
		}
		else if (method.equalsIgnoreCase("QLPESSIMISTIC") && techopponent.equalsIgnoreCase("LEARNER")){
			learning = new QLPessimisticVersusLearner(loadMembase, loadMembase);
		}
		else if(method.equalsIgnoreCase("MINMAX") && techopponent.equalsIgnoreCase("LEARNER")){
			learning = new QLPessimisticVersusLearner(loadMembase, loadMembase);
		}
		else if(method.equalsIgnoreCase("QLPESSIMISTIC") && techopponent.equalsIgnoreCase("BOTHLEARNER")){
			learning = new QLPessimisticBothLearner(loadMembase, loadMembase);
		}
		
		return learning;
	}
}
