package it.italiangrid.portal.fluka.server;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * Launch a separate thread that check the job status.
 * 
 * @author dmichelotto
 *
 */
public class CheckerService implements ServletContextListener {
	
	/**
	 * Class logger.
	 */
	private static final Logger log = Logger
			.getLogger(CheckerService.class);
	
	/**
	 * The executor for the thread management.
	 */
	private ScheduledExecutorService scheduler  = Executors.newSingleThreadScheduledExecutor();

	/**
	 * Kill the Checker thread.
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
		log.info("Kill the Checker Service.");
		
		try {
			Checker.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Checker.closeConnection();
		
		scheduler.shutdownNow();
	}

	/**
	 * Launch the Checker thread.
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		
		log.info("Start the Checker Service.");
		
		try {
			Checker.load();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		scheduler.scheduleAtFixedRate(new Checker(), 0, 10, TimeUnit.SECONDS);
		
	}

}
