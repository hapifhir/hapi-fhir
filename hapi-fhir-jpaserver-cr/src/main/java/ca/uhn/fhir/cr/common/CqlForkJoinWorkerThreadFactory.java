package ca.uhn.fhir.cr.common;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

// This class resolves issues with loading JAXB in a server environment and using CompleteableFutures
// https://stackoverflow.com/questions/49113207/completablefuture-forkjoinpool-set-class-loader
class CqlForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

	@Override
	public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
		return new CqlForkJoinWorkerThread(pool);
	}

	private static class CqlForkJoinWorkerThread extends ForkJoinWorkerThread {

		private CqlForkJoinWorkerThread(final ForkJoinPool pool) {
			super(pool);
			// set the correct classloader here
			setContextClassLoader(Thread.currentThread().getContextClassLoader());
		}
	}
}
