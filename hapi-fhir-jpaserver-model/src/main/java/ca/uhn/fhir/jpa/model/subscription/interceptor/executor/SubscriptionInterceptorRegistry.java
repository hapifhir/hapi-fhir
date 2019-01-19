package ca.uhn.fhir.jpa.model.subscription.interceptor.executor;

import ca.uhn.fhir.jpa.model.subscription.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.subscription.interceptor.api.SubscriptionHook;
import ca.uhn.fhir.jpa.model.subscription.interceptor.api.SubscriptionInterceptor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

@Component
public class SubscriptionInterceptorRegistry implements ISubscriptionInterceptorRegistry, ApplicationContextAware {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionInterceptorRegistry.class);
	private ApplicationContext myAppCtx;
	private List<Object> myGlobalInterceptors = new ArrayList<>();
	private ListMultimap<Pointcut, Invoker> myInvokers = ArrayListMultimap.create();

	List<Object> getGlobalInterceptors() {
		return myGlobalInterceptors;
	}

	@PostConstruct
	public void start() {

		// Grab the global interceptors
		String[] globalInterceptorNames = myAppCtx.getBeanNamesForAnnotation(SubscriptionInterceptor.class);
		for (String nextName : globalInterceptorNames) {
			Object nextGlobalInterceptor = myAppCtx.getBean(nextName);
			myGlobalInterceptors.add(nextGlobalInterceptor);
		}

		// Sort them
		sortByOrderAnnotation(myGlobalInterceptors);

		// Pull out the hook methods
		for (Object nextInterceptor : myGlobalInterceptors) {
			for (Method nextMethod : nextInterceptor.getClass().getDeclaredMethods()) {
				SubscriptionHook hook = AnnotationUtils.findAnnotation(nextMethod, SubscriptionHook.class);
				if (hook != null) {
					Invoker invoker = new Invoker(nextInterceptor, nextMethod);
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

		/*
		 * Call each hook in order
		 */
		List<Invoker> invokers = myInvokers.get(thePointcut);
		for (Invoker nextInvoker : invokers) {
			boolean shouldContinue = nextInvoker.invoke(theParams);
			if (!shouldContinue) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, Object... theParams) {
		return callHooks(thePointcut, new HookParams(theParams));
	}

	private class Invoker {

		private final Object myInterceptor;
		private final boolean myReturnsBoolean;
		private final Method myMethod;
		private final Class<?>[] myParameterTypes;

		/**
		 * Constructor
		 */
		private Invoker(@Nonnull Object theInterceptor, @Nonnull Method theHookMethod) {
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
		}

		boolean invoke(HookParams theParams) {
			Object[] args = new Object[myParameterTypes.length];
			for (int i = 0; i < myParameterTypes.length; i++) {
				Class<?> nextParamType = myParameterTypes[i];
				Object nextParamValue = theParams.get(nextParamType);
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

	private static <T> boolean equals(Collection<T> theLhs, Collection<T> theRhs) {
		return theLhs.size() == theRhs.size() && theLhs.containsAll(theRhs) && theRhs.containsAll(theLhs);
	}
}
