package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Service
public class PartitionRunner {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);
	private static final int MAX_POOL_SIZE = 1000;

	private final DaoConfig myDaoConfig;
	private final PlatformTransactionManager myPlatformTransactionManager;
	private TransactionTemplate myTxTemplate;

	@Autowired
	public PartitionRunner(DaoConfig theDaoConfig, PlatformTransactionManager thePlatformTransactionManager) {
		myDaoConfig = theDaoConfig;
		myPlatformTransactionManager = thePlatformTransactionManager;
	}

	@PostConstruct
	private void setTxTemplate() {
		myTxTemplate = new TransactionTemplate(myPlatformTransactionManager);
		myTxTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
	}

	void runInPartitionedTransactionThreads(Slice<Long> theResourceIds, Consumer<List<Long>> partitionConsumer) {

		List<Callable<Void>> callableTasks = buildCallableTasks(theResourceIds, partitionConsumer);
		if (callableTasks.size() == 0) {
			return;
		}

		ExecutorService executorService = buildExecutor(callableTasks.size());
		try {
			List<Future<Void>> futures = executorService.invokeAll(callableTasks);
			// wait for all the threads to finish
			for (Future<Void> future : futures) {
				future.get();
			}
		} catch (InterruptedException e) {
			ourLog.error("Interrupted while expunging.", e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			ourLog.error("Error while expunging.", e);
			throw new InternalErrorException(e);
		} finally {
			executorService.shutdown();
		}
	}

	private List<Callable<Void>> buildCallableTasks(Slice<Long> theResourceIds, Consumer<List<Long>> partitionConsumer) {
		List<Callable<Void>> retval = new ArrayList<>();

		List<List<Long>> partitions = Lists.partition(theResourceIds.getContent(), myDaoConfig.getExpungeBatchSize());

		for (List<Long> nextPartition : partitions) {
			Callable<Void> callableTask = () -> {
				ourLog.info("Expunging any search results pointing to {} resources", nextPartition.size());
				myTxTemplate.execute(t -> {
					partitionConsumer.accept(nextPartition);
					return null;
				});
				return null;
			};
			retval.add(callableTask);
		}

		return retval;
	}


	private ExecutorService buildExecutor(int numberOfTasks) {
		int threadCount = Math.min(numberOfTasks, myDaoConfig.getExpungeThreadCount());
		assert(threadCount > 0);

		ourLog.info("Expunging with {} threads", threadCount);
		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("expunge-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
			ourLog.info("Note: Expunge executor queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
			StopWatch sw = new StopWatch();
			try {
				executorQueue.put(theRunnable);
			} catch (InterruptedException e) {
				throw new RejectedExecutionException("Task " + theRunnable.toString() +
					" rejected from " + e.toString());
			}
			ourLog.info("Slot become available after {}ms", sw.getMillis());
		};
		return new ThreadPoolExecutor(
			threadCount,
			MAX_POOL_SIZE,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);
	}
}
