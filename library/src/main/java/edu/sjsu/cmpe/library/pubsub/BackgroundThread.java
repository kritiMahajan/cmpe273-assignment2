/**
 * 
 */
package edu.sjsu.cmpe.library.pubsub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @author Kriti
 *
 */

 
public class BackgroundThread {
 
    public static void startThread(final Listener listener) {
    	int numThreads = 1;
	    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
 
	    Runnable backgroundTask = new Runnable() {
 
    	    @Override
    	    public void run() {
    	    	System.out.println("Starting Listener");
    	    	listener.listenForMessages();
    	    }
    
    	};
 
    	System.out.println("About to submit the background task");
    	executor.execute(backgroundTask);
    	System.out.println("Submitted the background task");
    
    	executor.shutdown();
    	System.out.println("Finished the background task");
    }
}
