package ca.uhn.fhir.jpa.search.reindex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A handler for rejected tasks that will have the caller block until space is available.
 * This was stolen from old hibernate search(5.X.X), as it has been removed in HS6. We can probably come up with a better solution though.
 */
// TODO KHS consolidate with the other BlockPolicy class this looks like it is a duplicate of
public class BlockPolicy implements RejectedExecutionHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(BlockPolicy.class);

	/**
	 * Puts the Runnable to the blocking queue, effectively blocking the delegating thread until space is available.
	 *
	 * @param r the runnable task requested to be executed
	 * @param e the executor attempting to execute this task
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		try {
			e.getQueue().put(r);
		} catch (InterruptedException e1) {
			ourLog.error("Interrupted Execption for task: {}", r, e1);
			Thread.currentThread().interrupt();
		}
	}
}
