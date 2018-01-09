package com.wisebots.learner.test;

import com.wisebots.ai.method.Learning;
import com.wisebots.learner.MinMaxVersusLearner;
import com.wisebots.utils.QueueThread;

public final class ScenarioLearnerTest {

	public static void plan1(){
		String gameName = "tictactoe";
		long trainingExploring = 100000;
		long trainingPessimist = 100000;
		long testingGreedy = 0;
		long block = 1000;
		double alfa = 0.7;
		double gama = 0.7;
		int cache = 0;
		long timeReadQueue = 10000 * 60;
		String queuedir = "/Users/adrianobrito/Documents/queue";
		double iquality = 100;
		int id = 999;
		
		//Learning learning = new QLPessimisticRandomWithTestPhaseLearner(true);
	//	String expected = learning.verifyTotalTime(id, trainingExploring, cache, block, gameName, alfa, gama);
		//System.out.println("Time expected: " + expected + " secs");
		
		QueueThread q = new QueueThread(timeReadQueue, queuedir);
		new Thread(q).start();
		
		//learning.execute(id, gameName, trainingExploring, trainingPessimist, testingGreedy, block, alfa, gama, cache, iquality);
	}
	
	public static void plan2(){
		String gameName =  "tictactoe";
		String method = "minmax";
		Long epochs = 1000L;
		Long block = 10L;
		Integer cache = 0;
		
		Learning learning = new MinMaxVersusLearner();
	//	learning.execute(gameName, cache, method, epochs, block);
	}
	
	public static void main(String[] args) {

		
		ScenarioLearnerTest.plan1();
	}
}
