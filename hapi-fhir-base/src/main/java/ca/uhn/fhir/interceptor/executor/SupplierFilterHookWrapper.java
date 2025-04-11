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
		TrackingSupplierWrapper<T> trackingSupplierWrapper = new TrackingSupplierWrapper<>(myTarget);

		T result = myAdvice.apply(trackingSupplierWrapper);

		if (!trackingSupplierWrapper.wasExecuted()) {
			throw new IllegalStateException(
					"Supplier was not executed in filter produced by " + myMessageSupplier.get());
		}

		return result;
	}

	static class TrackingSupplierWrapper<T> implements Supplier<T> {
		private final Supplier<T> myTarget;
		private boolean myExecutedFlag = false;

		TrackingSupplierWrapper(Supplier<T> theTarget) {
			myTarget = theTarget;
		}

		@Override
		public T get() {
			myExecutedFlag = true;
			return myTarget.get();
		}

		public boolean wasExecuted() {
			return myExecutedFlag;
		}
	}
}
