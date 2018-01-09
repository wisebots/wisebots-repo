package com.wisebots.core.evaluation;

public class EvaluationFactory {

	public static Evaluation create(String method){
		Evaluation evaluation = null;
		
		if(method.equalsIgnoreCase("TERMINAL")){
			evaluation = new TerminalEvaluation();
		}
		else if(method.equalsIgnoreCase("QVALUE")){
			evaluation = new QvalueEvaluation();
		}

		return evaluation;
	}
}
