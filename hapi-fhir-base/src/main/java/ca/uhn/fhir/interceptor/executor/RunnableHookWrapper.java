package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;

import java.util.function.Supplier;

/**
 * Wraps a runnable with a filter hook.
 */
public class RunnableHookWrapper implements Runnable {
	private final IInterceptorFilterHook myAdvice;
	private final Runnable myTarget;
	private Supplier<String> myMessageSupplier;

	public RunnableHookWrapper(
			Runnable theTarget, IInterceptorFilterHook theAdvice, Supplier<String> theCauseDescriptionSupplier) {
		myAdvice = theAdvice;
		myTarget = theTarget;
		myMessageSupplier = theCauseDescriptionSupplier;
	}

	@Override
	public void run() {
		TrackingRunnableWrapper trackingRunnableWrapper = new TrackingRunnableWrapper(myTarget);

		myAdvice.accept(trackingRunnableWrapper);

		if (!trackingRunnableWrapper.wasRun()) {
			throw new IllegalStateException("Runnable was not run in filter produced by " + myMessageSupplier.get());
		}
	}

	static class TrackingRunnableWrapper implements Runnable {
		private final Runnable myTarget;
		private boolean myRunFlag = false;

		TrackingRunnableWrapper(Runnable theTarget) {
			myTarget = theTarget;
		}

		@Override
		public void run() {
			myRunFlag = true;
			myTarget.run();
		}

		public boolean wasRun() {
			return myRunFlag;
		}
	}
}
