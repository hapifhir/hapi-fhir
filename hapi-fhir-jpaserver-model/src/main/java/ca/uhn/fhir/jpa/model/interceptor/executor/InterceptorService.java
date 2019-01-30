package ca.uhn.fhir.jpa.model.interceptor.executor;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.model.interceptor.api.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InterceptorService implements IInterceptorRegistry, IInterceptorBroadcaster {
	private static final Logger ourLog = LoggerFactory.getLogger(InterceptorService.class);
	private final List<Object> myInterceptors = new ArrayList<>();
	private final ListMultimap<Pointcut, BaseInvoker> myInvokers = ArrayListMultimap.create();
	private final ListMultimap<Pointcut, BaseInvoker> myAnonymousInvokers = ArrayListMultimap.create();
	private final Object myRegistryMutex = new Object();

	/**
	 * Constructor
	 */
	public InterceptorService() {
		super();
	}

	@VisibleForTesting
	List<Object> getGlobalInterceptorsForUnitTest() {
		return myInterceptors;
	}


	@Override
	@VisibleForTesting
	public void registerAnonymousHookForUnitTest(Pointcut thePointcut, IAnonymousLambdaHook theHook) {
		registerAnonymousHookForUnitTest(thePointcut, DEFAULT_ORDER, theHook);
	}

	@Override
	public void registerAnonymousHookForUnitTest(Pointcut thePointcut, int theOrder, IAnonymousLambdaHook theHook) {
		Validate.notNull(thePointcut);
		Validate.notNull(theHook);

		myAnonymousInvokers.put(thePointcut, new AnonymousLambdaInvoker(theHook, theOrder));
	}

	@Override
	@VisibleForTesting
	public void clearAnonymousHookForUnitTest() {
		myAnonymousInvokers.clear();
	}

	@Override
	public boolean registerInterceptor(Object theInterceptor) {
		synchronized (myRegistryMutex) {

			if (isInterceptorAlreadyRegistered(theInterceptor)) {
				return false;
			}

			Class<?> interceptorClass = theInterceptor.getClass();
			int typeOrder = determineOrder(interceptorClass);

			if (!scanInterceptorForHookMethodsAndAddThem(theInterceptor, typeOrder)) {
				return false;
			}

			myInterceptors.add(theInterceptor);

			// Make sure we're always sorted according to the order declared in
			// @Order
			sortByOrderAnnotation(myInterceptors);
			for (Pointcut nextPointcut : myInvokers.keys()) {
				List<BaseInvoker> nextInvokerList = myInvokers.get(nextPointcut);
				nextInvokerList.sort(Comparator.naturalOrder());
			}

			return true;
		}
	}

	private boolean scanInterceptorForHookMethodsAndAddThem(Object theInterceptor, int theTypeOrder) {
		boolean retVal = false;
		for (Method nextMethod : theInterceptor.getClass().getDeclaredMethods()) {
			Hook hook = AnnotationUtils.findAnnotation(nextMethod, Hook.class);

			if (hook != null) {

				int methodOrder = theTypeOrder;
				Order methodOrderAnnotation = AnnotationUtils.findAnnotation(nextMethod, Order.class);
				if (methodOrderAnnotation != null) {
					methodOrder = methodOrderAnnotation.value();
				}

				HookInvoker invoker = new HookInvoker(hook, theInterceptor, nextMethod, methodOrder);
				for (Pointcut nextPointcut : hook.value()) {
					myInvokers.put(nextPointcut, invoker);
				}

				retVal = true;
			}
		}
		return retVal;
	}

	private int determineOrder(Class<?> theInterceptorClass) {
		int typeOrder = DEFAULT_ORDER;
		Order typeOrderAnnotation = AnnotationUtils.findAnnotation(theInterceptorClass, Order.class);
		if (typeOrderAnnotation != null) {
			typeOrder = typeOrderAnnotation.value();
		}
		return typeOrder;
	}

	private boolean isInterceptorAlreadyRegistered(Object theInterceptor) {
		for (Object next : myInterceptors) {
			if (next == theInterceptor) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void unregisterInterceptor(Object theInterceptor) {
		synchronized (myRegistryMutex) {
			myInterceptors.removeIf(t -> t == theInterceptor);
			myInvokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
		}
	}

	@Override
	public boolean registerGlobalInterceptor(Object theInterceptor) {
		return registerInterceptor(theInterceptor);
	}

	@Override
	public void unregisterGlobalInterceptor(Object theInterceptor) {
		unregisterInterceptor(theInterceptor);
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
	public boolean callHooks(Pointcut thePointcut, Object... theParams) {
		return callHooks(thePointcut, new HookParams(theParams));
	}

	@Override
	public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
		assert haveAppropriateParams(thePointcut, theParams);

		List<BaseInvoker> invokers = getInvokersForPointcut(thePointcut);

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

	@VisibleForTesting
	List<Object> getInterceptorsWithInvokersForPointcut(Pointcut thePointcut) {
		return getInvokersForPointcut(thePointcut)
			.stream()
			.map(BaseInvoker::getInterceptor)
			.collect(Collectors.toList());
	}

	/**
	 * Returns an ordered list of invokers for the given pointcut. Note that
	 * a new and stable list is returned to.. do whatever you want with it.
	 */
	private List<BaseInvoker> getInvokersForPointcut(Pointcut thePointcut) {
		List<BaseInvoker> invokers;
		boolean haveAnonymousInvokers;
		synchronized (myRegistryMutex) {
			List<BaseInvoker> globalInvokers = myInvokers.get(thePointcut);
			List<BaseInvoker> anonymousInvokers = myAnonymousInvokers.get(thePointcut);
			invokers = ListUtils.union(anonymousInvokers, globalInvokers);
			haveAnonymousInvokers = anonymousInvokers.isEmpty() == false;
		}

		if (haveAnonymousInvokers) {
			invokers.sort(Comparator.naturalOrder());
		}
		return invokers;
	}

	/**
	 * Only call this when assertions are enabled, it's expensive
	 */
	boolean haveAppropriateParams(Pointcut thePointcut, HookParams theParams) {
		Validate.isTrue(theParams.getParamsForType().values().size() == thePointcut.getParameterTypes().size(), "Wrong number of params for pointcut %s - Wanted %s but found %s", thePointcut.name(), toErrorString(thePointcut.getParameterTypes()), theParams.getParamsForType().values().stream().map(t -> t.getClass().getSimpleName()).sorted().collect(Collectors.toList()));

		List<String> wantedTypes = new ArrayList<>(thePointcut.getParameterTypes());

		ListMultimap<Class<?>, Object> givenTypes = theParams.getParamsForType();
		for (Class<?> nextTypeClass : givenTypes.keySet()) {
			String nextTypeName = nextTypeClass.getName();
			for (Object nextParamValue : givenTypes.get(nextTypeClass)) {
				Validate.isTrue(nextTypeClass.isAssignableFrom(nextParamValue.getClass()), "Invalid params for pointcut %s - %s is not of type %s", thePointcut.name(), nextParamValue.getClass(), nextTypeClass);
				Validate.isTrue(wantedTypes.remove(nextTypeName), "Invalid params for pointcut %s - Wanted %s but missing %s", thePointcut.name(), toErrorString(thePointcut.getParameterTypes()), nextTypeName);
			}
		}

		return true;
	}

	private abstract class BaseInvoker implements Comparable<BaseInvoker> {

		private final int myOrder;
		private final Object myInterceptor;

		BaseInvoker(Object theInterceptor, int theOrder) {
			myInterceptor = theInterceptor;
			myOrder = theOrder;
		}

		public Object getInterceptor() {
			return myInterceptor;
		}

		abstract boolean invoke(HookParams theParams);

		@Override
		public int compareTo(BaseInvoker o) {
			return myOrder - o.myOrder;
		}
	}

	private class AnonymousLambdaInvoker extends BaseInvoker {
		private final IAnonymousLambdaHook myHook;

		public AnonymousLambdaInvoker(IAnonymousLambdaHook theHook, int theOrder) {
			super(theHook, theOrder);
			myHook = theHook;
		}

		@Override
		boolean invoke(HookParams theParams) {
			myHook.invoke(theParams);
			return true;
		}
	}

	private class HookInvoker extends BaseInvoker {

		private final boolean myReturnsBoolean;
		private final Method myMethod;
		private final Class<?>[] myParameterTypes;
		private final int[] myParameterIndexes;

		/**
		 * Constructor
		 */
		private HookInvoker(Hook theHook, @Nonnull Object theInterceptor, @Nonnull Method theHookMethod, int theOrder) {
			super(theInterceptor, theOrder);
			myParameterTypes = theHookMethod.getParameterTypes();
			myMethod = theHookMethod;

			Class<?> returnType = theHookMethod.getReturnType();
			if (returnType.equals(boolean.class)) {
				myReturnsBoolean = true;
			} else {
				Validate.isTrue(void.class.equals(returnType), "Method does not return boolean or void: %s", theHookMethod);
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
				Object returnValue = myMethod.invoke(getInterceptor(), args);
				if (myReturnsBoolean) {
					return (boolean) returnValue;
				} else {
					return true;
				}
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (targetException instanceof RuntimeException) {
					throw ((RuntimeException) targetException);
				} else {
					throw new InternalErrorException(targetException);
				}
			} catch (Exception e) {
				throw new InternalErrorException(e);
			}

		}

	}

	private static String toErrorString(List<String> theParameterTypes) {
		return theParameterTypes
			.stream()
			.sorted()
			.collect(Collectors.joining(","));
	}

}
