package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;

import java.util.function.Supplier;

/**
 * Wraps a Supplier with a filter hook.
 */
public class SupplierFilterHookWrapper<T> implements Supplier<T> {
	private final IInterceptorFilterHook<T> myAdvice;
	private final Supplier<T> myTarget;
	private final Supplier<String> myMessageSupplier;

	public SupplierFilterHookWrapper(
			Supplier<T> theTarget, IInterceptorFilterHook<T> theAdvice, Supplier<String> theCauseDescriptionSupplier) {
		myAdvice = theAdvice;
		myTarget = theTarget;
		myMessageSupplier = theCauseDescriptionSupplier;
	}

	@Override
	public T get() {
		TrackingRunnableWrapper<T> trackingRunnableWrapper = new TrackingRunnableWrapper<>(myTarget);

		T result = myAdvice.apply(trackingRunnableWrapper);

		if (!trackingRunnableWrapper.wasRun()) {
			throw new IllegalStateException("Runnable was not run in filter produced by " + myMessageSupplier.get());
		}

		return result;
	}

	static class TrackingRunnableWrapper<T> implements Supplier<T> {
		private final Supplier<T> myTarget;
		private boolean myRunFlag = false;

		TrackingRunnableWrapper(Supplier<T> theTarget) {
			myTarget = theTarget;
		}

		@Override
		public T get() {
			myRunFlag = true;
			return myTarget.get();
		}

		public boolean wasRun() {
			return myRunFlag;
		}
	}
}
