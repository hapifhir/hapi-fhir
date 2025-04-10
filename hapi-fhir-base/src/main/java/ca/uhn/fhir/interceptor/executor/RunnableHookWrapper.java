package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IPointcut;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps a runnable with a filter hook.
 */
public class RunnableHookWrapper implements Runnable {
	private final IInterceptorBroadcaster.IInterceptorFilterHook myAdvice;
	private final Runnable myTarget;

	static Runnable wrapRunnable(IInterceptorBroadcaster.IInterceptorFilterHook filter, Runnable runnable) {
		return new RunnableHookWrapper(filter, runnable);
	}

	RunnableHookWrapper(IInterceptorBroadcaster.IInterceptorFilterHook theAdvice, Runnable theTarget) {
		myAdvice = theAdvice;
		myTarget = theTarget;
	}

	public static <POINTCUT extends IPointcut> void callWrapAndRun(
			IBaseInterceptorBroadcaster<POINTCUT> theInterceptorBroadcaster,
			POINTCUT thePointcut,
			HookParams theHookParams,
			Runnable theCallerRunnable) {

		var filters = theInterceptorBroadcaster.getInvokersForPointcut(thePointcut).stream()
				.map(i -> (IInterceptorBroadcaster.IInterceptorFilterHook) i.invoke(theHookParams))
				.collect(Collectors.toList());

		Runnable runnable = wrap(theCallerRunnable, filters);

		runnable.run();
	}

	public static Runnable wrap(Runnable theTargetRunnable, List<IInterceptorBroadcaster.IInterceptorFilterHook> theFilters) {
		Runnable runnable = theTargetRunnable;

		// traverse the invokers in reverse order because the first wrapper will be called last in sequence.
		for (IInterceptorBroadcaster.IInterceptorFilterHook filter : Lists.reverse(theFilters)) {
			runnable = wrapRunnable(filter, runnable);
		}
		return runnable;
	}

	@Override
	public void run() {
		myAdvice.accept(myTarget);
	}
}
