package com.wisebots.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import com.sleepycat.je.*;

public class PersistedQueue {

	 /**
     * Berkley DB environment
     */
    private final Environment dbEnv;

    /**
     * Berkley DB instance for the queue
     */
    private final Database queueDatabase;

    /**
     * Queue cache size - number of element operations it is allowed to loose in case of system crash.
     */
    private final int cacheSize;

    /**
     * This queue name.
     */
    private final String queueName;

    /**
     * Queue operation counter, which is used to sync the queue database to disk periodically.
     */
    private int opsCounter;

    /**
     * Creates instance of persistent queue.
     *
     * @param queueEnvPath   queue database environment directory path
     * @param queueName      descriptive queue name
     * @param cacheSize      how often to sync the queue to disk
     * @throws DatabaseException
     */
    public PersistedQueue(final String queueEnvPath, final String queueName, final int cacheSize) throws DatabaseException {
        // Create parent dirs for queue environment directory
        new File(queueEnvPath).mkdirs();

        // Setup database environment
        final EnvironmentConfig dbEnvConfig = new EnvironmentConfig();
        dbEnvConfig.setTransactional(false);
        dbEnvConfig.setAllowCreate(true);
        this.dbEnv = new Environment(new File(queueEnvPath),
                                  dbEnvConfig);

        // Setup non-transactional deferred-write queue database
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(false);
        dbConfig.setAllowCreate(true);
        dbConfig.setDeferredWrite(true);
        dbConfig.setBtreeComparator(new KeyComparator());
        this.queueDatabase = dbEnv.openDatabase(null,queueName,dbConfig);
        this.queueName = queueName;
        this.cacheSize = cacheSize;
        this.opsCounter = 0;
    }

    /**
     * Retrieves and returns element from the head of this queue.
     *
     * @return element from the head of the queue or null if queue is empty
     *
     * @throws IOException in case of disk IO failure
     * @throws DatabaseException
     */
    public String poll() throws IOException, DatabaseException {
        final DatabaseEntry key = new DatabaseEntry();
        final DatabaseEntry data = new DatabaseEntry();
        final Cursor cursor = queueDatabase.openCursor(null, null);
        try {
            cursor.getFirst(key, data, LockMode.RMW);
            if (data.getData() == null)
                return null;
            final String res = new String(data.getData(), "UTF-8");
            cursor.delete();
            opsCounter++;
            if (opsCounter >= cacheSize) {
                queueDatabase.sync();
                opsCounter = 0;
            }
            return res;
        } finally {
            cursor.close();
        }
    }

    /**
     * Pushes element to the tail of this queue.
     *
     * @param element element
     *
     * @throws IOException in case of disk IO failure
     * @throws DatabaseException
     */
    public synchronized void push(final String element) throws IOException, DatabaseException {
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry data = new DatabaseEntry();
        Cursor cursor = queueDatabase.openCursor(null, null);
        try {
            cursor.getLast(key, data, LockMode.RMW);

            BigInteger prevKeyValue;
            if (key.getData() == null) {
                prevKeyValue = BigInteger.valueOf(-1);
            } else {
                prevKeyValue = new BigInteger(key.getData());
            }
            BigInteger newKeyValue = prevKeyValue.add(BigInteger.ONE);

            try {
                final DatabaseEntry newKey = new DatabaseEntry(newKeyValue.toByteArray());
                final DatabaseEntry newData = new DatabaseEntry(element.getBytes("UTF-8"));
                queueDatabase.put(null, newKey, newData);

                opsCounter++;
                if (opsCounter >= cacheSize) {
                    queueDatabase.sync();
                    opsCounter = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            cursor.close();
        }
    }

   /**
     * Returns the size of this queue.
     *
     * @return the size of the queue
 * @throws DatabaseException
     */
    public long size() throws DatabaseException {
        return queueDatabase.count();
    }

    /**
     * Returns this queue name.
     *
     * @return this queue name
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Closes this queue and frees up all resources associated to it.
     * @throws DatabaseException
     */
    public void close() throws DatabaseException {
        queueDatabase.close();
        dbEnv.close();
    }
}