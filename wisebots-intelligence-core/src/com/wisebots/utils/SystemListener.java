package com.wisebots.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


public class SystemListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(SystemListener.class);

	/**
	 * Default constructor.
	 */
	public SystemListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed( final ServletContextEvent arg0 ) {
		logger.info( "Auditing consumer shutting down..." );
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized( final ServletContextEvent arg0 ) {
		logger.info( "Auditing consumer initialized..." );
		QueueExecutor.getInstance(10000, "/usr/local/wisebots/queue");
	}
}