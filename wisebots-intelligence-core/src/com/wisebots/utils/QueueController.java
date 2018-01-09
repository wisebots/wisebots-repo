package com.wisebots.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sleepycat.je.DatabaseException;
import com.wisebots.core.Meme;

public class QueueController {
	
    private static final Logger logger = Logger.getLogger(QueueController.class);
    public static final String QUEUE_AUDITING_NAME = "memory-queue";
    public static final int QUEUE_CACHE = 0;

	public static void add(Meme meme) throws IOException, DatabaseException{

		File queueDir = new File("/Users/adrianobrito/Documents/queue");
        PersistedQueue queue = new PersistedQueue(queueDir.getPath(), QUEUE_AUDITING_NAME, QUEUE_CACHE);
        try { 
    	    Gson gson = new Gson();
    	    String sgson = gson.toJson(meme);
    	    logger.debug("Meme -> "+sgson);
        	queue.push(sgson);
        }
        catch (Exception e) {
        	logger.error("Erro de convers‹o de data", e);
		}
        finally {
            queue.close();
        }
	}
}
