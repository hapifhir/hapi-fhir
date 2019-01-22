package ca.uhn.fhir.jpa.model.interceptor.executor;

import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.IAnonymousLambdaHook;
import ca.uhn.fhir.jpa.model.interceptor.api.IInterceptorRegistry;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;

import java.util.ArrayList;
import java.util.List;

public class DeferredInterceptorRegistry implements IInterceptorRegistry {

	private final IInterceptorRegistry myWrap;
	private List<Invocation> myDeferredInvocations = new ArrayList<>();

	public DeferredInterceptorRegistry(IInterceptorRegistry theWrap) {
		myWrap = theWrap;
	}

	@Override
	public void registerAnonymousHookForUnitTest(Pointcut thePointcut, IAnonymousLambdaHook theHook) {
		myWrap.registerAnonymousHookForUnitTest(thePointcut, theHook);
	}

	@Override
	public void registerAnonymousHookForUnitTest(Pointcut thePointcut, int theOrder, IAnonymousLambdaHook theHook) {
		myWrap.registerAnonymousHookForUnitTest(thePointcut, theOrder, theHook);
	}

	@Override
	public void clearAnonymousHookForUnitTest() {
		myWrap.clearAnonymousHookForUnitTest();
	}

	@Override
	public boolean registerGlobalInterceptor(Object theInterceptor) {
		return myWrap.registerGlobalInterceptor(theInterceptor);
	}

	@Override
	public void unregisterGlobalInterceptor(Object theInterceptor) {
		myWrap.unregisterGlobalInterceptor(theInterceptor);
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
		myDeferredInvocations.add(new Invocation(thePointcut, theParams));
		return true;
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, Object... theParams) {
		return callHooks(thePointcut, new HookParams(theParams));
	}

	public void invokeDeferred() {
		for (Invocation next : myDeferredInvocations) {
			myWrap.callHooks(next.getPointcut(), next.getParams());
		}
	}

	private class Invocation {
		private final Pointcut myPointcut;
		private final HookParams myParams;

		public Invocation(Pointcut thePointcut, HookParams theParams) {
			myPointcut = thePointcut;
			myParams = theParams;
		}

		public Pointcut getPointcut() {
			return myPointcut;
		}

		public HookParams getParams() {
			return myParams;
		}
	}
}
