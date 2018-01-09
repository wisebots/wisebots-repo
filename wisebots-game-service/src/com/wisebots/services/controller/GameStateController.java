package com.wisebots.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.wisebots.core.RobotHelper;
import com.wisebots.dataset.BotProfile;
import com.wisebots.learner.QLPessimisticBothLearner;
import com.wisebots.learner.explorer.GreedyExplorer;
import com.wisebots.rules.games.Game;
import com.wisebots.rules.games.GameFactory;
import com.wisebots.rules.games.impl.ConnectFour;
import com.wisebots.rules.games.impl.TicTacToe;
import com.wisebots.utils.Utils;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class GameStateController {
	
	private List<String> implemented;
	private final Result result;
	private static final Logger logger = Logger.getLogger(GameStateController.class);

	public GameStateController(Result result) {
		implemented = new ArrayList<String>();
		implemented.add(TicTacToe.name);
		implemented.add(ConnectFour.name);
		this.result = result;
	}

	@Get("/gamecloud/{game}/{id}/{player}")
	public void redirectgame(String game, String id, String player) {		
		
		if(!implemented.contains(game)){
			result.use( Results.http() ).addHeader( "Content-Type", "text/plain" );
			result.use( Results.http() ).body("GAME CLIENT NOT IMPLEMENTED").setStatusCode( 200 );
		}
		else if(game.equalsIgnoreCase(TicTacToe.name)){
			this.result.use(Results.logic()).redirectTo(GameStateController.class).tictactoe(game, id, player);
		}
		else if(game.equalsIgnoreCase(ConnectFour.name)){
			this.result.use(Results.logic()).redirectTo(GameStateController.class).connectfour(game, id, player);
		}
	}
	
	@Get("/game/tictactoe/{id}/{player}")
	public void tictactoe(String game, String id, String player) {		
		Robot robot = new Robot();
		robot.setId(id);
		robot.setPlayer(player);
		result.include("robot", robot);	
	}
	
	@Get("/game/connectfour/{id}/{player}")
	public void connectfour(String game, String id, String player) {		
		Robot robot = new Robot();
		robot.setId(id);
		robot.setPlayer(player);
		result.include("robot", robot);	
	}

	
	@Path("/gamecloud/{gameState}")
	public void toCloud(String gameState) {		
		
		logger.info("Received gamestate. GameState -> "+gameState);
		result.use( Results.http() ).addHeader( "Content-Type", "text/plain" );
		result.use( Results.http() ).body("OK").setStatusCode( 200 );
		 
	}

	@Path("/gamecloud/{id}/{gameState}")
	public void combat(Integer id, String gameState) {		
		System.out.println("Jogo: " + id);
		
		int player = 1; //TODO isso tem que ser dinamico
		int state[] = convertState(gameState);
		System.out.println("Received gamestate. GameState -> "+Arrays.toString(state));
		
		// [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 2, 0, 2, 0]

		BotProfile profile = RobotHelper.getInstance().getBotProfile(id);
		Game game = GameFactory.create(profile.getGame());
		int[] pactions = game.getPossibleActions(state);
		
		double valuemax = -999999999;
		int actionChoosed = pactions[0]; 
		for(int action: pactions){
			int[] fstate = Utils.getStateFunction(state, action, game.getOffset());
			GreedyExplorer exp = GreedyExplorer.getInstance();
			Double d = exp.checkValue(Arrays.toString(fstate), Integer.toString(id), profile.getGame(), QLPessimisticBothLearner.METHOD, player);
		
			System.out.println("Checking value=" + d + " from " + action);
			if(d.doubleValue() >= valuemax){
				valuemax = d;
				actionChoosed = action;
			}		
		}

		System.out.println(id+"/"+gameState+":" + valuemax + "," + actionChoosed);
		result.use( Results.http() ).addHeader( "Content-Type", "text/plain" );
		result.use( Results.http() ).body(""+actionChoosed).setStatusCode( 200 );
		
	}
	
	private int[] convertState(String s) {
		s = s.replace(",", "").trim();
		int[] intArray = new int[s.length()];
		 
		for (int i = 0; i < s.length(); i++) {
			intArray[i] = Character.digit(s.charAt(i), 10);
		}
		
		return intArray;
	}
	
	public static void main(String[] args) {
		int[] state = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 2, 0, 2, 0};
		Game game = GameFactory.create("CONNECT4");
		int[] pactions = game.getPossibleActions(state);
		System.out.println(Arrays.toString(pactions));
	}

}
