package com.wisebots.rules.games.utils;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;


/**
 * Classe utilitária para realizar conversões estáticas que não tenham a ver com
 * as regras e algoritmos
 * 
 * @author Adriano
 * 
 */

public final class Utils {

	public static final String path = "C:\\2-Projects\\TicTacToe\\resources\\";
	public static final boolean DEBUG = false;

	/**
	 * Imprime uma tabela conforme tabuleiro do jogo
	 * 
	 * @param table
	 */

	public static void printTable(List<int[]> table) {
		for (int i = 0; i < table.size(); i++) {
			if (i % (Math.sqrt(table.size())) == 0)
				System.out.println("");
			System.out.print(table.get(i).toString() + " ");
		}
	}



	/**
	 * Imprime no console as estatísticas de um conjunto de épocas
	 * 
	 * @param qvalue1
	 * @param player1
	 * @param player2
	 * @param draw
	 * @param match
	 * @param totalmatch
	 * @param strategy
	 */

	public static void printStatistic(HashMap<int[], Double> qvalue1,
			long player1, long player2, long draw, long match, long totalmatch,
			String strategy) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("---------------------------" + strategy
				+ "------------------------------");
		System.out.println("Total match: " + match + "/" + totalmatch
				+ " running " + df.format((match * 100) / totalmatch) + "%");
		System.out.println("Player1 vic from total: " + player1 + " "
				+ df.format((double) player1 * 100 / match) + "%");
		System.out.println("Player2 vic from total: " + player2 + " "
				+ df.format((double) player2 * 100 / match) + "%");
		System.out.println("Draw vic from total: " + draw + " "
				+ df.format((double) draw * 100 / match) + "%");
		System.out.println("Data training: " + qvalue1.size());
		System.out
				.println("---------------------------------------------------------------------------");
	}

	/**
	 * Imprime no console os valores Q(s,a)
	 * 
	 * @param qvalue
	 */

	public static void printQValues(HashMap<int[], Double> qvalue) {
		Iterator<int[]> iterator = qvalue.keySet().iterator();

		while (iterator.hasNext()) {
			int[] key = iterator.next();
			Double value = qvalue.get(key);

			System.out.println("Q(" + transformArray(key) + "," + value + ")");
		}
	}
	
	public static void printCacheQValues(HashMap<String, Object> qvalue) {
		Iterator<String> iterator = qvalue.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			Double value = (Double)qvalue.get(key);

			System.out.println("Q(" + key + ")=" + value);
		}
	}

	public static String transformArray(int[] array) {
		String a = "";
		for (int i = 0; i < array.length; i++)
			a += array[i];
		return a;
	}

	public static String getStateFromArray(int[] array) {
		String a = "";
		for (int i = 0; i < array.length - 1; i++)
			a += array[i];
		return a;
	}

	/*
	public static int[] getArrayStateFromArray(int[] array, int offset) {
		int[] a = new int[array.length - 1];
		for (int i = 0; i < array.length - 1; i++)
			a[i] = array[i];
		return a;
	}
	*/

	public static int[] getStateActionFromArray(int[] array, int action, int offset) {
		int[] a = new int[array.length - offset + 1];
		for (int i = offset; i < array.length; i++)
			a[i-offset] = array[i];
		a[array.length-offset] = action;
		return a;
	}
	
	public static int[] getStateFunction(int[] state, int action, int offset) {
		int[] fstate = new int[state.length - offset + 1];
		for (int i = offset; i < fstate.length; i++) {
			if (i == fstate.length - 1) {
				fstate[i-offset] = action;
			} else {
				fstate[i-offset] = state[i];
			}
		}
		return fstate;
	}

	public static int getActionFromArray(int[] array) {
		String a = Integer.toString(array[array.length - 1]);
		return Integer.parseInt(a);
	}

	/**
	 * Salve os valores Q(s,a)
	 * 
	 * @param qvalue
	 */

	public static void saveQValues(HashMap<String, Double> qvalue, String name) {
		/*
		 * Iterator<Long> iterator = qvalue.keySet().iterator();
		 * 
		 * System.out.println("Tamanho: " +qvalue.size()); try { FileWriter fw =
		 * new FileWriter(path + "qvalues-" + name + ".txt"); while
		 * (iterator.hasNext()) { Long key = iterator.next(); long action =
		 * key%100; long state = (key-action)/100; Double value =
		 * qvalue.get(key) ; StringBuffer sb = new StringBuffer();
		 * sb.append("(")
		 * .append(state).append(",").append(action).append(")").append
		 * ("=").append(value).append("\n"); fw.write(sb.toString()); }
		 * fw.close(); } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	/**
	 * Salva um histórico de partidas
	 * 
	 * @param history
	 * @param qvalueHistory
	 * @param round
	 * @param name
	 */

	public static void saveHistoryRound(Vector<int[]> history,
			Vector<Double> qvalueHistory, int round, String name) {
		/*
		 * try { FileWriter fw = new FileWriter(path + name + "-round-" + round
		 * + ".txt"); for(int i=0; i<history.size(); i++){ StringBuffer sb = new
		 * StringBuffer(); if(i%2==0)
		 * sb.append("(").append(history.get(i)).append
		 * (")").append("=").append(qvalueHistory.get(i/2)).append("\n"); else
		 * sb.append("(").append(history.get(i)).append(")").append("\n");
		 * fw.write(sb.toString()); } fw.close(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * Salva um histórico de máximos valores
	 * 
	 * @param history
	 * @param round
	 */

	public static void saveHistoryMaxValue(Vector<String> history, int round) {

		try {
			FileWriter fw = new FileWriter(path + "maxvalues-round-" + round
					+ ".txt");
			for (int i = 0; i < history.size(); i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(history.get(i)).append("\n");
				fw.write(sb.toString());
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Altera o turno do jogador
	 * 
	 * @param turn
	 * @return
	 */

	public static int changeTurn(int turn) {
		if (turn == 1)
			return 2;
		return 1;
	}

	public static boolean checkProbability(double epsilon){
		boolean checked = false;
		Random rand = new Random();
		int num = rand.nextInt(100);
		if(num < epsilon*100){
			checked = true;
		}
		
		return checked;
	}
	
	
	public static void debugLog(Class cl, String log){
		if(DEBUG)
			System.out.println(Calendar.getInstance().getTimeInMillis() + " - "+ cl.getName() + ": "+ log);
	}
	
	public static void debugFileLog(String file, List<int[]> states){
		try {
			String x = "";
			for(int[] state: states){
				x += Arrays.toString(state) + " - ";
			}
			
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write((x + "\n").getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
/*
	public static void main(String[] args) throws URISyntaxException {
		try {
			MemcachedClient client;
			URI server = new URI("http://127.0.0.1:8091/pools");
			ArrayList<URI> serverList = new ArrayList<URI>();
			serverList.add(server);
			client = new MemcachedClient(serverList, "Teste2", "teste123");
			
			/*
			AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                    new PlainCallbackHandler("Administrator", "teste123"));
			*/
			/*
			MemcachedClient client = new MemcachedClient(
				     new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY)
				     .setAuthDescriptor(ad)
				     .build(),
				     AddrUtil.getAddresses("127.0.0.1:11211"));

			// client = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
		
			
			System.out.println("Pegando");
			Object spoon = client.get("spoon");
			if (spoon == null) {
			System.out.println("There is no spoon.");
			client.set("spoon", 10, "Hello World!");
			} else {
			System.out.println((String) spoon);
			}

			client.shutdown(10, TimeUnit.SECONDS);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

			System.exit(0);
	}
*/
}
