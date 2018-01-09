package com.wisebots.web.now;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.wisebots.ai.method.Learning;
import com.wisebots.core.BrainHelper; 
import com.wisebots.core.RobotHelper;
import com.wisebots.core.evaluation.Evaluation;
import com.wisebots.core.evaluation.EvaluationFactory;
import com.wisebots.core.exploration.Exploration;
import com.wisebots.core.exploration.ExplorationFactory;
import com.wisebots.dataset.BotGenesis; 
import com.wisebots.dataset.BotHistory;
import com.wisebots.dataset.BotProfile;
import com.wisebots.dataset.LearningProperties;

import com.wisebots.learner.MethodFactory;
import com.wisebots.learner.QLPessimisticVersusSpecialist;
import com.wisebots.learner.explorer.GreedyExplorer; 
import com.wisebots.rules.games.GameComplexity;
import com.wisebots.rules.games.GameFactory;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;

@Resource
public class WisebotController { 

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(WisebotController.class);
	private final Result result;
	private final Validator validator;

	public WisebotController(Result result, Validator validator) {
		this.result = result;
		this.validator = validator;
	}

	@Path("/now")
	public List<BotProfile> now() {	
		List<BotProfile> botProfiles = RobotHelper.getInstance().listRobots();
		return botProfiles;
	} 
	
	@Post("/now/create")
	public void create(BotProfile botProfile) 
	throws Exception{		
		
		if(!botProfile.getEbot().equals("0")){
			botProfile.setId(botProfile.getEbot());
			result.include(botProfile);
			return;
		}
		
		BotGenesis botGenesis = new BotGenesis();
		GameComplexity complexity = new GameComplexity(1000);
		String complex = complexity.calculate(GameFactory.create(botProfile.getGame()));
		botGenesis.setComplexity(complex);
		botGenesis.setMaxact(complexity.getMaxact());  
		botGenesis.setMaxtimemakeacts(complexity.getMaxtimemakeacts());
		botGenesis.setMaxtimepacts(complexity.getMaxtimepacts());
		botGenesis.setMedtimeacts(complexity.getMedtimepacts());
		botGenesis.setMedtimemakeacts(complexity.getMedtimemakeacts());
		botGenesis.setProbsmax(complexity.getProbsmax().toString());
		botGenesis.setStatesize(complexity.getStatesize());
		
		Integer idRobot = RobotHelper.getInstance().insertNewRobot(botProfile.getName());
		if(idRobot == 0)
			throw new Exception("Fail on creation robot. Probabily name already exists");
		
		botProfile.setBotGenesis(botGenesis);
		
		RobotHelper.getInstance().insertNewRobotProfile(idRobot, botProfile);
		botProfile.setId(""+idRobot);

		result.include(botGenesis);
		result.include(botProfile);
		validator.onErrorForwardTo(WisebotController.class).now();		
	}
	
	@Path("/now/list")
	public List<BotProfile> list() 
	throws Exception{		
		List<BotProfile> botProfiles = RobotHelper.getInstance().listRobots();
		for(BotProfile botProfile: botProfiles){
			Double iquality = RobotHelper.getInstance().getLastQualityRobot(Integer.parseInt(botProfile.getId()));
			if(iquality == null)
				iquality = new Double(0);
			botProfile.setIquality(iquality);
		} 
		return botProfiles; 
	}

	@Post("/now/ajax/genesis/{id}/{botname}/{game}/{player}/{method}/{techopponent}/{opponent}/{trainingExploring}/{trainingGreedy}/{trainingPessimist}/{alfa}/{gama}/{epsilon}/{delta}/{block}/{cache}/{quality}/{exploration}/{evaluation}/{level}")
	public void genesis(String id, String botname, String game, String player, String method, String techopponent, String opponent, String trainingExploring, String trainingGreedy, String trainingPessimist, String alfa, String gama, String epsilon, String delta, String block, String cache, String quality, String exploration, String evaluation, String level){

		BotGenesis botGenesis = new BotGenesis();

		System.out.println("Params: botname=" + botname);
		System.out.println("Params: game=" + game);
		System.out.println("Params: player=" + player);
		System.out.println("Params: trainingExploring=" + trainingExploring);
		System.out.println("Params: trainingGreedy=" + trainingGreedy);
		System.out.println("Params: trainingPessimist=" +  trainingPessimist);
		System.out.println("Params: techopponnent=" +  techopponent);
		System.out.println("Params: opponent=" +  opponent);
		System.out.println("Params: alfa=" + alfa);
		System.out.println("Params: gama=" + gama);
		System.out.println("Params: epsilon=" + epsilon);
		System.out.println("Params: delta=" + delta);
		System.out.println("Params: block=" + block); 
		System.out.println("Params: cache=" + cache);
		System.out.println("Params: quality=" + quality); 
		
		if(trainingExploring == null)
			trainingExploring = "0";
		if(trainingPessimist == null)
			trainingPessimist = "0"; 
		if(trainingGreedy == null) 
			trainingGreedy = "0";  
		if(block == null)
			block = "0";	
		if(opponent == null)
			opponent = "0";
		
		int idOpponent = 0;
		if(techopponent.equals("BOTHLEARNER")){
			System.out.println("Creating ghost robot");
			BotGenesis nbotGenesis = new BotGenesis();
			GameComplexity complexity = new GameComplexity(1000);
			String complex = complexity.calculate(GameFactory.create(game));
			nbotGenesis.setComplexity(complex);
			nbotGenesis.setMaxact(complexity.getMaxact());  
			nbotGenesis.setMaxtimemakeacts(complexity.getMaxtimemakeacts());
			nbotGenesis.setMaxtimepacts(complexity.getMaxtimepacts());
			nbotGenesis.setMedtimeacts(complexity.getMedtimepacts());
			nbotGenesis.setMedtimemakeacts(complexity.getMedtimemakeacts());
			nbotGenesis.setProbsmax(complexity.getProbsmax().toString());
			nbotGenesis.setStatesize(complexity.getStatesize());
			
			idOpponent = RobotHelper.getInstance().insertNewRobot(botname + "_adv");

			BotProfile nbotProfile = new BotProfile();
			nbotProfile.setBotGenesis(nbotGenesis);
			
			RobotHelper.getInstance().insertNewRobotProfile(idOpponent, nbotProfile);
			nbotProfile.setId(""+idOpponent);
		}
		System.out.println("Id Bot Opponent: " + idOpponent);
		
		LearningProperties properties = new LearningProperties();
		properties.setId(new Integer(id));
		properties.setIdOpponent(idOpponent);
		properties.setGameName(game);
		properties.setPlayer(Integer.parseInt(player));
		properties.setTrainingQL(Long.parseLong(trainingExploring));
		properties.setTrainingQLPessimistic(Long.parseLong(trainingPessimist));
		properties.setAlfa(Double.parseDouble(alfa));
		properties.setGama(Double.parseDouble(gama));
		properties.setEpsilon(Double.parseDouble(epsilon));
		properties.setDelta(Double.parseDouble(delta));
		properties.setCache(Integer.parseInt(cache));
		properties.setOpponent(Integer.parseInt(opponent));
		properties.setLearningadv(false);
		properties.setExploration(exploration);
		properties.setEvaluation(evaluation);
		properties.setLevel(Integer.parseInt(level));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String creationDate = sdf.format(new Date());
		RobotHelper.getInstance().insertCreationRobot(Integer.parseInt(id), creationDate);
		
		RobotHelper.getInstance().insertStatusRobot(Integer.parseInt(id), "training");
		Learning learning = MethodFactory.create(method, techopponent, opponent, false);
		learning.execute(properties);
		
		String finalDate = sdf.format(new Date());
		RobotHelper.getInstance().insertFinishedRobot(Integer.parseInt(id), finalDate);
		botGenesis.setCreationDate(creationDate);
		botGenesis.setFinishedDate(finalDate);
		
		RobotHelper.getInstance().insertStatusRobot(Integer.parseInt(id), "testing");
		double finalQuality = 0.0;
		//if(opponent.equals("0")){
		//TODO Isso ta uma merda.. refactoring quando puder
		finalQuality = GreedyExplorer.getInstance().checkOpponentInitialQuality(new Integer(id), Integer.parseInt(player), idOpponent, new String(game), method, 1000L, Double.parseDouble(delta));
		//}
		//else{
			// finalQuality = GreedyExplorer.getInstance().checkOpponentFinalQuality(new Integer(id), Integer.parseInt(opponent), Integer.parseInt(player), new String(game), method, 1000L);
		//}
		botGenesis.setFinalStatusMemory(finalQuality + "");
		if(finalQuality == 100){
			botGenesis.setImagestatus("gold.png");
			botGenesis.setStatusMemory("Strong bot ");
		}
		else if(finalQuality > 80){
			botGenesis.setImagestatus("semigold.png");
			botGenesis.setStatusMemory("Partial strong bot ");
		}
		else{
			botGenesis.setImagestatus("nogold.png");
			botGenesis.setStatusMemory("Weak bot ");
		}
		
		// learning.createBrain(new Integer(id));    
		// RobotHelper.getInstance().insertStatusRobot(Integer.parseInt(id), "creating brain");
		// String finalMemoryDate = sdf.format(new Date());
		// RobotHelper.getInstance().insertFinishedMemoryRobot(Integer.parseInt(id), finalMemoryDate);
		
		RobotHelper.getInstance().insertStatusRobot(Integer.parseInt(id), "success");
		System.out.println("Robot cache q-size memory: " + BrainHelper.getInstance().getCacheSizeMemory(new Integer(id), QLPessimisticVersusSpecialist.METHOD));
		System.out.println("Robot created success!!!");
		result.include(botGenesis);
	}
	
	@Get("/now/ajax/testphase/{id}/{opponent}/{player}/{method}/{gamename}/{epochs}/{delta}/{eval}/{expo}")
	public void testphase(Integer id, int opponent, String player, String method, String gamename, Integer epochs, String delta, String eval, String expo){ 

		BotGenesis botGenesis = new BotGenesis();
		if(id == 0){
			botGenesis.setStatusMeme("0.0");
		}
		else{
			if(opponent == 0){
				Evaluation evaluation = EvaluationFactory.create(eval);
				Exploration exploration = ExplorationFactory.create(expo);
				double quality = GreedyExplorer.getInstance().checkSpecialistQuality(id, Integer.parseInt(player), gamename, method, epochs, Double.parseDouble(delta), evaluation, exploration);
				double idr = GreedyExplorer.getInstance().checkRandomQuality(id, Integer.parseInt(player), gamename, method, epochs, Double.parseDouble(delta));
				int cache = BrainHelper.getInstance().getCacheSizeMemory(id, method);
				botGenesis.setStatusMeme("Last quality [epochs=" + epochs + "]: " + Double.toString(quality) + " % / Cache state: " + cache + " / IDR: " + idr + " %");
			}
			else{
				double quality = GreedyExplorer.getInstance().checkOpponentQuality(id, opponent, Integer.parseInt(player), gamename, method, epochs);
				int cache = BrainHelper.getInstance().getCacheSizeMemory(id, method);
				double idr = GreedyExplorer.getInstance().checkRandomQuality(id, Integer.parseInt(player), gamename, method, epochs, Double.parseDouble(delta));
				botGenesis.setStatusMeme("Last quality [epochs=" + epochs + "]: " + Double.toString(quality) + " % / Cache state: " + cache + " / IDR: " + idr + " %");
			}
		}
		
		String status = RobotHelper.getInstance().getStatusRobot(id);
		if(status != null && !status.equals("training")){
			botGenesis.setMaxact(-1);
		}
		else{
			botGenesis.setMaxact(60);
		}
		
		result.include(botGenesis);
		
	}
	 
	@Post("/now/ajax/pause/{id}/{method}/{gamename}")
	public void pause(Integer id, String method, String gamename){ 
		BrainHelper.getInstance().insertPause(id, method, "1");
	}
	 
	@Post("/now/ajax/unpause/{id}/{method}/{gamename}")
	public void unpause(Integer id, String method, String gamename){
		// TODO load memory from memcache to work again
		BrainHelper.getInstance().insertPause(id, method, "0");
	}
	
	@Get("/now/ajax/statistics/training/{id}/{method}/{gamename}")
	public void statstraining(Integer id, String method, String gamename){
		// TODO load memory from memcache to work again
		String axisx = BrainHelper.getInstance().getTrainingAxisx(id, method);
		String axisy = BrainHelper.getInstance().getTrainingAxisy(id, method);
		
		// System.out.println("AXISX: " + axisx);
		// System.out.println("AXISY: " + axisy);
		
		BotGenesis botGenesis = new BotGenesis();
		botGenesis.setAxisx(axisx);
		botGenesis.setAxisy(axisy);
		
		String status = RobotHelper.getInstance().getStatusRobot(id);
		if(status != null && !status.equals("training")){
			botGenesis.setMaxact(-1);
		}
		else{
			botGenesis.setMaxact(5);
		}
		
		result.include(botGenesis);
	}
	
	@Get("/now/ajax/statistics/testing/{id}/{method}/{gamename}")
	public void statstesting(Integer id, String method, String gamename){
		// TODO load memory from memcache to work again
		String axisx = BrainHelper.getInstance().getTestingAxisx(id, method);
		String axisy = BrainHelper.getInstance().getTestingAxisy(id, method);
		
		// System.out.println("AXISX: " + axisx);
		// System.out.println("AXISY: " + axisy);
		
		BotGenesis botGenesis = new BotGenesis();
		botGenesis.setAxisx(axisx);
		botGenesis.setAxisy(axisy);
		
		String status = RobotHelper.getInstance().getStatusRobot(id);
		if(status != null && !status.equals("training")){
			botGenesis.setMaxact(-1);
		}
		else{
			botGenesis.setMaxact(5);
		}
		
		result.include(botGenesis);
	}
	
	@Path("/now/ajax/table/training/{id}/{method}/{gamename}")
	public List<BotHistory> tabletraining(String id, String method, String gamename){
	
		List<BotHistory> botHistoryList = new ArrayList<BotHistory>();
		String history = BrainHelper.getInstance().getTrainingHistoryStatistic(new Integer(id), method);
		// System.out.println("History: " + history); 
		
		String status = RobotHelper.getInstance().getStatusRobot(Integer.parseInt(id));

		StringTokenizer st = new StringTokenizer(history, ";");
		while(st.hasMoreTokens()){
			String line = st.nextToken();
			StringTokenizer nst = new StringTokenizer(line, ",");
			BotHistory botHistory = new BotHistory();
			botHistory.setGameName(gamename);
			while(nst.hasMoreTokens()){
				botHistory.setId(nst.nextToken());
				botHistory.setMethod(nst.nextToken());
				botHistory.setNustates(nst.nextToken());
				botHistory.setEpochs(nst.nextToken());
				botHistory.setTime(nst.nextToken());
				botHistory.setQuality(nst.nextToken());
				botHistory.setVictory(nst.nextToken());
				botHistory.setDraw(nst.nextToken());
				if(status != null && !status.equals("training")){
					botHistory.setTimerefresh("-1");
				}
				else{
					botHistory.setTimerefresh("5");
				}
				
				
			}
			botHistoryList.add(botHistory);
		}
		
		// 	System.out.println("Tamanho do history: " + botHistoryList.size());
		if(botHistoryList.size() == 0)
			return null;
		
		return botHistoryList;
	}
	
	@Path("/now/ajax/table/testing/{id}/{method}/{gamename}")
	public List<BotHistory> tabletesting(String id, String method, String gamename){
	
		List<BotHistory> botHistoryList = new ArrayList<BotHistory>();
		String history = BrainHelper.getInstance().getTestingHistoryStatistic(new Integer(id), method);
		// System.out.println("History: " + history); 
		
		String status = RobotHelper.getInstance().getStatusRobot(Integer.parseInt(id));

		StringTokenizer st = new StringTokenizer(history, ";");
		while(st.hasMoreTokens()){
			String line = st.nextToken();
			StringTokenizer nst = new StringTokenizer(line, ",");
			BotHistory botHistory = new BotHistory();
			botHistory.setGameName(gamename);
			while(nst.hasMoreTokens()){
				botHistory.setId(nst.nextToken());
				botHistory.setMethod(nst.nextToken());
				botHistory.setEpochs(nst.nextToken());
				botHistory.setTime(nst.nextToken());
				botHistory.setQuality(nst.nextToken());
				botHistory.setVictory(nst.nextToken());
				botHistory.setDraw(nst.nextToken());
				if(status != null && !status.equals("training")){
					botHistory.setTimerefresh("-1");
				}
				else{
					botHistory.setTimerefresh("5");
				}
				
				
			}
			botHistoryList.add(botHistory);
		}
		
		// System.out.println("Tamanho do history: " + botHistoryList.size());
		if(botHistoryList.size() == 0)
			return null;
		
		return botHistoryList;
	}
	
	@Post("/now/ajax/check/{id}/{botname}/{gamename}/{iquality}")
	public void check(String id, String botname, String gamename, String iquality){ 
		BotGenesis botGenesis = new BotGenesis();

		Double statusMeme = BrainHelper.getInstance().getPartialStatusMeme(new Integer(id), QLPessimisticVersusSpecialist.METHOD);
		RobotHelper.getInstance().insertLastQualityRobot(Integer.parseInt(id), statusMeme);
		
		String finalStatus = "loading";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.S");
		String lastcheck = sdf.format(new Date());
		
		botGenesis.setStatusMeme(Double.toString(statusMeme));
		if(statusMeme == Double.parseDouble(iquality))
			finalStatus = "success";
			
		botGenesis.setFinalStatusMeme(finalStatus);
		RobotHelper.getInstance().insertStatusRobot(Integer.parseInt(id), finalStatus);
		
		botGenesis.setLastcheck(lastcheck);
		RobotHelper.getInstance().insertLastCheckRobot(Integer.parseInt(id), lastcheck);
		
		String creationDate = RobotHelper.getInstance().getCreationDateRobot(Integer.parseInt(id));
		botGenesis.setCreationDate(creationDate);
		
		String finishedDate = RobotHelper.getInstance().getFinishedDateRobot(Integer.parseInt(id));
		botGenesis.setFinishedDate(finishedDate);
		
		String finishedMemoryDate = RobotHelper.getInstance().getFinishedMemoryDateRobot(Integer.parseInt(id));
		botGenesis.setFinishedMemoryDate(finishedMemoryDate);
		
		result.include(botGenesis);
	}

}
