package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.util.ArrayList;
import java.util.concurrent.*;

public class SubscriptionChannel implements SubscribableChannel {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionChannel.class);

	private final ExecutorSubscribableChannel mySubscribableChannel;

	public SubscriptionChannel(BlockingQueue<Runnable> theQueue, String namingPattern) {

		ThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern(namingPattern)
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
			ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", theQueue.size());
			StopWatch sw = new StopWatch();
			try {
				theQueue.put(theRunnable);
			} catch (InterruptedException theE) {
				throw new RejectedExecutionException("Task " + theRunnable.toString() +
					" rejected from " + theE.toString());
			}
			ourLog.info("Slot become available after {}ms", sw.getMillis());
		};
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
			1,
			SubscriptionConstants.EXECUTOR_THREAD_COUNT,
			0L,
			TimeUnit.MILLISECONDS,
			theQueue,
			threadFactory,
			rejectedExecutionHandler);
		mySubscribableChannel = new ExecutorSubscribableChannel(executor);
	}

	@Override
	public boolean subscribe(MessageHandler handler) {
		return mySubscribableChannel.subscribe(handler);
	}

	@Override
	public boolean unsubscribe(MessageHandler handler) {
		return mySubscribableChannel.unsubscribe(handler);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return mySubscribableChannel.send(message, timeout);
	}

	@VisibleForTesting
	public void clearInterceptorsForUnitTest() {
		mySubscribableChannel.setInterceptors(new ArrayList<>());
	}

	@VisibleForTesting
	public void addInterceptorForUnitTest(ChannelInterceptor theInterceptor) {
		mySubscribableChannel.addInterceptor(theInterceptor);
	}
}
