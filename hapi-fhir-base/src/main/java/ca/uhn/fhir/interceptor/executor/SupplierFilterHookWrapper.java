package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;

import java.util.function.Supplier;

/**
 * Wraps a Supplier with advice from a filter hook.
 */
public class SupplierFilterHookWrapper<T> implements Supplier<T> {
	private final IInterceptorFilterHook myAdvice;
	private final Supplier<T> myTarget;
	private final Supplier<String> myMessageSupplier;

	public SupplierFilterHookWrapper(
			Supplier<T> theTarget, IInterceptorFilterHook theAdvice, Supplier<String> theCauseDescriptionSupplier) {
		myAdvice = theAdvice;
		myTarget = theTarget;
		myMessageSupplier = theCauseDescriptionSupplier;
	}

	@Override
	public T get() {
		SupplierRunnable<T> trackingSupplierWrapper = new SupplierRunnable<>(myTarget);

		myAdvice.wrapCall(trackingSupplierWrapper);

		if (!trackingSupplierWrapper.wasExecuted()) {
			throw new IllegalStateException(
					"Supplier was not executed in filter produced by " + myMessageSupplier.get());
		}

		return trackingSupplierWrapper.getResult();
	}

	/**
	 * Adapt a Supplier to Runnable.
	 * We use a Runnable for a simpler api for callers.
	 * @param <T>
	 */
	static class SupplierRunnable<T> implements Runnable {
		private final Supplier<T> myTarget;
		private boolean myExecutedFlag = false;
		private T myResult = null;

		SupplierRunnable(Supplier<T> theTarget) {
			myTarget = theTarget;
		}

		public void run() {
			myExecutedFlag = true;
			myResult = myTarget.get();
		}

		public boolean wasExecuted() {
			return myExecutedFlag;
		}

		public T getResult() {
			return myResult;
		}
	}
}
