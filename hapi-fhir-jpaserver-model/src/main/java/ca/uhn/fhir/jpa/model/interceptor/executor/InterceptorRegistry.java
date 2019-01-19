package ca.uhn.fhir.jpa.model.interceptor.executor;

import ca.uhn.fhir.jpa.model.interceptor.api.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InterceptorRegistry implements IInterceptorRegistry, ApplicationContextAware {
	private static final Logger ourLog = LoggerFactory.getLogger(InterceptorRegistry.class);
	private ApplicationContext myAppCtx;
	private List<Object> myGlobalInterceptors = new ArrayList<>();
	private ListMultimap<Pointcut, BaseInvoker> myInvokers = ArrayListMultimap.create();
	private ListMultimap<Pointcut, BaseInvoker> myAnonymousInvokers = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

	/**
	 * Constructor
	 */
	public InterceptorRegistry() {
		super();
	}

	@VisibleForTesting
	public List<Object> getGlobalInterceptorsForUnitTest() {
		return myGlobalInterceptors;
	}


	@Override
	@VisibleForTesting
	public void registerAnonymousHookForUnitTest(Pointcut thePointcut, IAnonymousLambdaHook theHook) {
		Validate.notNull(thePointcut);
		Validate.notNull(theHook);

		myAnonymousInvokers.put(thePointcut, new AnonymousLambdaInvoker(theHook));
	}

	@Override
	@VisibleForTesting
	public void clearAnonymousHookForUnitTest() {
		myAnonymousInvokers.clear();
	}

	@PostConstruct
	public void start() {

		// Grab the global interceptors
		String[] globalInterceptorNames = myAppCtx.getBeanNamesForAnnotation(Interceptor.class);
		for (String nextName : globalInterceptorNames) {
			Object nextGlobalInterceptor = myAppCtx.getBean(nextName);
			myGlobalInterceptors.add(nextGlobalInterceptor);
		}

		// Sort them
		sortByOrderAnnotation(myGlobalInterceptors);

		// Pull out the hook methods
		for (Object nextInterceptor : myGlobalInterceptors) {
			for (Method nextMethod : nextInterceptor.getClass().getDeclaredMethods()) {
				Hook hook = AnnotationUtils.findAnnotation(nextMethod, Hook.class);
				if (hook != null) {
					HookInvoker invoker = new HookInvoker(hook, nextInterceptor, nextMethod);
					for (Pointcut nextPointcut : hook.value()) {
						myInvokers.put(nextPointcut, invoker);
					}
				}
			}
		}

	}

	private void sortByOrderAnnotation(List<Object> theObjects) {
		IdentityHashMap<Object, Integer> interceptorToOrder = new IdentityHashMap<>();
		for (Object next : theObjects) {
			Order orderAnnotation = next.getClass().getAnnotation(Order.class);
			int order = orderAnnotation != null ? orderAnnotation.value() : 0;
			interceptorToOrder.put(next, order);
		}

		theObjects.sort((a, b) -> {
			Integer orderA = interceptorToOrder.get(a);
			Integer orderB = interceptorToOrder.get(b);
			return orderA - orderB;
		});
	}

	@Override
	public void setApplicationContext(@Nonnull ApplicationContext theApplicationContext) throws BeansException {
		myAppCtx = theApplicationContext;
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
		assert haveAppropriateParams(thePointcut, theParams);

		// Anonymous hooks first
		List<BaseInvoker> invokers = ListUtils.union(
			myAnonymousInvokers.get(thePointcut),
			myInvokers.get(thePointcut));

		/*
		 * Call each hook in order
		 */
		for (BaseInvoker nextInvoker : invokers) {
			boolean shouldContinue = nextInvoker.invoke(theParams);
			if (!shouldContinue) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Only call this when assertions are enabled, it's expensive
	 */
	private boolean haveAppropriateParams(Pointcut thePointcut, HookParams theParams) {
		List<String> givenTypes = theParams.getTypesAsSimpleName();
		List<String> wantedTypes = new ArrayList<>(thePointcut.getParameterTypes());
		givenTypes.sort(Comparator.naturalOrder());
		wantedTypes.sort(Comparator.naturalOrder());
		if (!givenTypes.equals(wantedTypes)) {
			throw new AssertionError("Wrong hook parameters, wanted " + wantedTypes + " and found " + givenTypes);
		}
		return true;
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, Object... theParams) {
		return callHooks(thePointcut, new HookParams(theParams));
	}

	private abstract class BaseInvoker {
		abstract boolean invoke(HookParams theParams);
	}

	private class AnonymousLambdaInvoker extends BaseInvoker {
		private final IAnonymousLambdaHook myHook;

		public AnonymousLambdaInvoker(IAnonymousLambdaHook theHook) {
			myHook = theHook;
		}

		@Override
		boolean invoke(HookParams theParams) {
			myHook.invoke(theParams);
			return true;
		}
	}

	private class HookInvoker extends BaseInvoker {

		private final Object myInterceptor;
		private final boolean myReturnsBoolean;
		private final Method myMethod;
		private final Class<?>[] myParameterTypes;
		private final int[] myParameterIndexes;

		/**
		 * Constructor
		 */
		private HookInvoker(Hook theHook, @Nonnull Object theInterceptor, @Nonnull Method theHookMethod) {
			myInterceptor = theInterceptor;
			myParameterTypes = theHookMethod.getParameterTypes();
			myMethod = theHookMethod;

			Class<?> returnType = theHookMethod.getReturnType();
			if (returnType.equals(boolean.class) || returnType.equals(Boolean.class)) {
				myReturnsBoolean = true;
			} else {
				Validate.isTrue(Void.class.equals(returnType), "Method does not return boolean or void: %s", theHookMethod);
				myReturnsBoolean = false;
			}

			myParameterIndexes = new int[myParameterTypes.length];
			Map<Class<?>, AtomicInteger> typeToCount = new HashMap<>();
			for (int i = 0; i < myParameterTypes.length; i++) {
				AtomicInteger counter = typeToCount.computeIfAbsent(myParameterTypes[i], t -> new AtomicInteger(0));
				myParameterIndexes[i] = counter.getAndIncrement();
			}
		}

		/**
		 * @return Returns true/false if the hook method returns a boolean, returns true otherwise
		 */
		@Override
		boolean invoke(HookParams theParams) {

			Object[] args = new Object[myParameterTypes.length];
			for (int i = 0; i < myParameterTypes.length; i++) {
				Class<?> nextParamType = myParameterTypes[i];
				int nextParamIndex = myParameterIndexes[i];
				Object nextParamValue = theParams.get(nextParamType, nextParamIndex);
				args[i] = nextParamValue;
			}

			// Invoke the method
			try {
				Object returnValue = myMethod.invoke(myInterceptor, args);
				if (myReturnsBoolean) {
					return (boolean) returnValue;
				} else {
					return true;
				}
			} catch (Exception e) {
				ourLog.error("Failure executing interceptor method[{}]: {}", myMethod, e.toString(), e);
				return true;
			}

		}

	}

}
