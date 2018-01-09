package com.wisebots.utils;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.wisebots.core.Meme;
import com.wisebots.core.cache.MemoryCache;

public class QueueExecutor {

    private static final Logger logger = Logger.getLogger(QueueExecutor.class);
    public static final String QUEUE_AUDITING_NAME = "memory-queue";
    public static final int QUEUE_CACHE = 0;

    private static QueueExecutor instance;
    private static ScheduledExecutorService scheduledExecutorService = null;
    private static long CHECK_TIME = 0; 
    private static String QUEUE_DIR = "";

    private QueueExecutor(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay( listen, 0L, CHECK_TIME, TimeUnit.MILLISECONDS );
    }

    public static QueueExecutor getInstance(long ttl, String queuedir){
        if (instance==null){
        	CHECK_TIME = ttl;
        	QUEUE_DIR = queuedir;
            instance = new QueueExecutor();
        }
        return instance;
    }

    private Runnable listen = new Runnable() {
        public void run(){
            System.out.println("Consuming auditing queue ...");

            String queueObj = "";
            try {
            	
                File queueDir = new File(QUEUE_DIR);
                PersistedQueue queue = new PersistedQueue(queueDir.getPath(), QUEUE_AUDITING_NAME, QUEUE_CACHE);
                try {
                    queueObj = queue.poll();
                    logger.info("Obj queue: " + queueObj);
                    Gson gson = new Gson();
                    Meme meme = (Meme)gson.fromJson(queueObj, Meme.class);
                    System.out.println("MEME OBTIDO COM SUCESSO");

                    MemoryCache memory = MemoryCache.getInstance(true);
                    memory.putOnMemory(meme.getKey(), meme.getValue());
                } 
                finally {
                    queue.close();
                }
                if(queueObj == null){
                    logger.info("Nao existem objetos na fila");
                    return;
                }
            }
            catch (Exception e) {
                logger.error("ERROR", e);
            }
        }
    };
}