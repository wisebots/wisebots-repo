package com.wisebots.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.wisebots.core.BrainHelper;
import com.wisebots.core.Quality;
import com.wisebots.core.cache.GameCache;

public final class StatisticsUtils {

	public static void generateTrainingMemoGraphic(int id, int player, long instances, long epochs, long ngraph, long ny, double initialTime, String method, GameCache gcache, Quality initialQuality, Quality quality){

		String playerVic ="0.0";
		String playerInitVic ="0.0";
		
		if(initialQuality != null){
			if(player == 1)
				playerInitVic = initialQuality.getPlayer1();
			else if(player == 2)
				playerInitVic = initialQuality.getPlayer2();
		}
		
		if(quality != null){
			if(player == 1)
				playerVic = quality.getPlayer1();
			else if(player == 2)
				playerVic = quality.getPlayer2();
		}
		
		if(instances==0){
			DecimalFormat df = new 	DecimalFormat("0.00");

			BrainHelper.getInstance().insertTrainingAxisX(id, method, df.format((Double.parseDouble(playerInitVic) + Double.parseDouble(initialQuality.getDraw()))));
			BrainHelper.getInstance().insertTrainingAxisY(id, method, 0);
			
			NumberFormat f = NumberFormat.getNumberInstance();  
			String statistic = id + "," + method + "," + gcache.size() + "," + f.format(0L).replace(",", ".")  + "," + df.format(Double.parseDouble(""+initialTime)) + "," + df.format((Double.parseDouble(playerInitVic) + Double.parseDouble(initialQuality.getDraw()))) + "," + df.format(Double.parseDouble(playerInitVic)) + "," + df.format(Double.parseDouble(initialQuality.getDraw()));
			BrainHelper.getInstance().insertTrainingHistoryStatistics(id, method, statistic);
		}
		
		if(instances%(ngraph)==0 && instances!=0){
			DecimalFormat df = new 	DecimalFormat("0.00");
			BrainHelper.getInstance().insertTrainingAxisX(id, method, df.format((Double.parseDouble(playerVic) + Double.parseDouble(quality.getDraw()))));
			
			NumberFormat f = NumberFormat.getNumberInstance();  
			String statistic = id + "," + method + "," + gcache.size() + "," + f.format(instances).replace(",", ".")  + "," + df.format(Double.parseDouble(""+initialTime)) + "," + df.format((Double.parseDouble(playerVic) + Double.parseDouble(quality.getDraw())))+ "," + df.format(Double.parseDouble(playerVic)) + "," + df.format(Double.parseDouble(quality.getDraw()));
			BrainHelper.getInstance().insertTrainingHistoryStatistics(id, method, statistic);
			
			BrainHelper.getInstance().insertCacheSize(id, method, new Integer(gcache.size()));
		}
		
		if(instances%ny==0 && instances!=0){
			BrainHelper.getInstance().insertTrainingAxisY(id, method, instances);
		}

	}
	
	public static void generateTrainingDirectMemoGraphic(int id, int player, long instances, long epochs, long ngraph, long ny, double time, String method, GameCache gcache, int player1, int player2, int draw, int totalmatchs){

		Double playerVic = 0.0;
		if(player == 1)
			playerVic = Double.parseDouble(""+(player1))*100/Double.parseDouble(""+totalmatchs);
		else if(player == 2)
			playerVic = Double.parseDouble(""+(player2))*100/Double.parseDouble(""+totalmatchs);
		
		Double playerDraw = Double.parseDouble(""+draw)*100/Double.parseDouble(""+totalmatchs);

		if(instances%(ngraph)==0 && instances!=0){
			DecimalFormat df = new 	DecimalFormat("0.00");
			BrainHelper.getInstance().insertTrainingAxisX(id, method, df.format(playerVic+playerDraw));
			
			NumberFormat f = NumberFormat.getNumberInstance();  
			String statistic = id + "," + method + "," + gcache.size() + "," + f.format(instances).replace(",", ".")  + "," + df.format((Double.parseDouble(""+time))) + "," + df.format(playerVic+playerDraw)+ "," + df.format(playerVic)+ "," + df.format(playerDraw);
			BrainHelper.getInstance().insertTrainingHistoryStatistics(id, method, statistic);
			
			BrainHelper.getInstance().insertCacheSize(id, method, new Integer(gcache.size()));
		}
		if(instances%ny==0 && instances!=0){
			BrainHelper.getInstance().insertTrainingAxisY(id, method, instances);
		}

	}
	
	@Deprecated
	public static void generateTestingMemoGraphic(int id, int player, long instances, long epochs, long ngraph, long ny, long initialTime, String method, GameCache gcache, Quality initialQuality, Quality quality){
		long b = new Date().getTime();
		String playerVic ="0.0";
		String playerInitVic ="0.0";
		
		if(initialQuality != null){
			if(player == 1)
				playerInitVic = initialQuality.getPlayer1();
			else if(player == 2)
				playerInitVic = initialQuality.getPlayer2();
		}
		
		if(quality != null){
			if(player == 1)
				playerVic = quality.getPlayer1();
			else if(player == 2)
				playerVic = quality.getPlayer2();
		}
		
		if(instances==0){
			BrainHelper.getInstance().insertTestingAxisX(id, "QLPESSIMISTIC", "0");
			BrainHelper.getInstance().insertTestingAxisY(id, "QLPESSIMISTIC", 0L);

			DecimalFormat df = new 	DecimalFormat("0.00");

			BrainHelper.getInstance().insertTestingAxisX(id, "QLPESSIMISTIC", df.format((Double.parseDouble(playerInitVic) + Double.parseDouble(initialQuality.getDraw()))));
			BrainHelper.getInstance().insertTestingAxisY(id, "QLPESSIMISTIC", 1000);
			
			NumberFormat f = NumberFormat.getNumberInstance();  
			String statistic = id + "," + method + "," + gcache.size() + "," + f.format(1000L).replace(",", ".")  + "," + df.format((Double.parseDouble(""+b)-Double.parseDouble(""+initialTime))/1000) + "," + df.format((Double.parseDouble(playerInitVic) + Double.parseDouble(initialQuality.getDraw()))) + "," + df.format(Double.parseDouble(playerInitVic)) + "," + df.format(Double.parseDouble(initialQuality.getDraw()));
			BrainHelper.getInstance().insertTestingHistoryStatistics(id, "QLPESSIMISTIC", statistic);
		}
		
		if(instances%(ngraph)==0 && instances!=0){
			DecimalFormat df = new 	DecimalFormat("0.00");
			BrainHelper.getInstance().insertTestingAxisX(id, "QLPESSIMISTIC", df.format((Double.parseDouble(playerVic) + Double.parseDouble(quality.getDraw()))));
			
			NumberFormat f = NumberFormat.getNumberInstance();  
			String statistic = id + "," + method + "," + gcache.size() + "," + f.format(instances).replace(",", ".")  + "," + df.format((Double.parseDouble(""+b)-Double.parseDouble(""+initialTime))/1000) + "," + df.format((Double.parseDouble(playerVic) + Double.parseDouble(quality.getDraw())))+ "," + df.format(Double.parseDouble(playerVic)) + "," + df.format(Double.parseDouble(quality.getDraw()));
			BrainHelper.getInstance().insertTestingHistoryStatistics(id, "QLPESSIMISTIC", statistic);
			
			BrainHelper.getInstance().insertCacheSize(id, "QLPESSIMISTIC", new Integer(gcache.size()));
		}
		if(instances%ny==0 && instances!=0){
			BrainHelper.getInstance().insertTestingAxisY(id, "QLPESSIMISTIC", instances);
		}

	}
}
