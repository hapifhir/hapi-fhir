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
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InterceptorService implements IInterceptorRegistry, IInterceptorBroadcaster {
	private final List<Object> myInterceptors = new ArrayList<>();
	private final ListMultimap<Pointcut, BaseInvoker> myGlobalInvokers = ArrayListMultimap.create();
	private final ListMultimap<Pointcut, BaseInvoker> myAnonymousInvokers = ArrayListMultimap.create();
	private final Object myRegistryMutex = new Object();
	private final ThreadLocal<ListMultimap<Pointcut, BaseInvoker>> myThreadlocalInvokers = new ThreadLocal<>();
	private String myName;
	private boolean myThreadlocalInvokersEnabled = true;

	/**
	 * Constructor which uses a default name of "default"
	 */
	public InterceptorService() {
		this("default");
	}

	/**
	 * Constructor
	 *
	 * @param theName The name for this registry (useful for troubleshooting)
	 */
	public InterceptorService(String theName) {
		super();
		myName = theName;
	}

	/**
	 * Are threadlocal interceptors enabled on this registry (defaults to true)
	 */
	public boolean isThreadlocalInvokersEnabled() {
		return myThreadlocalInvokersEnabled;
	}

	/**
	 * Are threadlocal interceptors enabled on this registry (defaults to true)
	 */
	public void setThreadlocalInvokersEnabled(boolean theThreadlocalInvokersEnabled) {
		myThreadlocalInvokersEnabled = theThreadlocalInvokersEnabled;
	}

	@VisibleForTesting
	List<Object> getGlobalInterceptorsForUnitTest() {
		return myInterceptors;
	}


	@Override
	@VisibleForTesting
	public void registerAnonymousInterceptor(Pointcut thePointcut, IAnonymousInterceptor theInterceptor) {
		registerAnonymousInterceptor(thePointcut, DEFAULT_ORDER, theInterceptor);
	}

	public void setName(String theName) {
		myName = theName;
	}

	@Override
	public void registerAnonymousInterceptor(Pointcut thePointcut, int theOrder, IAnonymousInterceptor theInterceptor) {
		Validate.notNull(thePointcut);
		Validate.notNull(theInterceptor);
		synchronized (myRegistryMutex) {
			myAnonymousInvokers.put(thePointcut, new AnonymousLambdaInvoker(thePointcut, theInterceptor, theOrder));
		}
	}

	@Override
	@VisibleForTesting
	public void clearAnonymousHookForUnitTest() {
		synchronized (myRegistryMutex) {
			myAnonymousInvokers.clear();
		}
	}

	@Override
	public void unregisterInterceptors(@Nullable Collection<?> theInterceptors) {
		if (theInterceptors != null) {
			theInterceptors.forEach(t -> unregisterInterceptor(t));
		}
	}

	@Override
	public boolean registerThreadLocalInterceptor(Object theInterceptor) {
		if (!myThreadlocalInvokersEnabled) {
			return false;
		}
		ListMultimap<Pointcut, BaseInvoker> invokers = getThreadLocalInvokerMultimap();
		scanInterceptorAndAddToInvokerMultimap(theInterceptor, invokers);
		return !invokers.isEmpty();

	}

	@Override
	public void unregisterThreadLocalInterceptor(Object theInterceptor) {
		if (myThreadlocalInvokersEnabled) {
			ListMultimap<Pointcut, BaseInvoker> invokers = getThreadLocalInvokerMultimap();
			invokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
			if (invokers.isEmpty()) {
				myThreadlocalInvokers.remove();
			}
		}
	}

	private ListMultimap<Pointcut, BaseInvoker> getThreadLocalInvokerMultimap() {
		ListMultimap<Pointcut, BaseInvoker> invokers = myThreadlocalInvokers.get();
		if (invokers == null) {
			invokers = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
			myThreadlocalInvokers.set(invokers);
		}
		return invokers;
	}

	@Override
	public boolean registerInterceptor(Object theInterceptor) {
		synchronized (myRegistryMutex) {

			if (isInterceptorAlreadyRegistered(theInterceptor)) {
				return false;
			}

			List<HookInvoker> addedInvokers = scanInterceptorAndAddToInvokerMultimap(theInterceptor, myGlobalInvokers);
			if (addedInvokers.isEmpty()) {
				return false;
			}

			// Add to the global list
			myInterceptors.add(theInterceptor);
			sortByOrderAnnotation(myInterceptors);

			return true;
		}
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
			myGlobalInvokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
			myAnonymousInvokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
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

		synchronized (myRegistryMutex) {
			List<BaseInvoker> globalInvokers = myGlobalInvokers.get(thePointcut);
			List<BaseInvoker> anonymousInvokers = myAnonymousInvokers.get(thePointcut);
			List<BaseInvoker> threadLocalInvokers = null;
			if (myThreadlocalInvokersEnabled) {
				ListMultimap<Pointcut, BaseInvoker> pointcutToInvokers = myThreadlocalInvokers.get();
				if (pointcutToInvokers != null) {
					threadLocalInvokers = pointcutToInvokers.get(thePointcut);
				}
			}
			invokers = union(globalInvokers, anonymousInvokers, threadLocalInvokers);
		}

		return invokers;
	}

	/**
	 * First argument must be the global invoker list!!
	 */
	private List<BaseInvoker> union(List<BaseInvoker>... theInvokersLists) {
		List<BaseInvoker> haveOne = null;
		boolean haveMultiple = false;
		for (List<BaseInvoker> nextInvokerList : theInvokersLists) {
			if (nextInvokerList == null || nextInvokerList.isEmpty()) {
				continue;
			}

			if (haveOne == null) {
				haveOne = nextInvokerList;
			} else {
				haveMultiple = true;
			}
		}

		if (haveOne == null) {
			return Collections.emptyList();
		}

		List<BaseInvoker> retVal;

		if (haveMultiple == false) {

			// The global list doesn't need to be sorted every time since it's sorted on
			// insertion each time. Doing so is a waste of cycles..
			if (haveOne == theInvokersLists[0]) {
				retVal = haveOne;
			} else {
				retVal = new ArrayList<>(haveOne);
				retVal.sort(Comparator.naturalOrder());
			}

		} else {

			retVal = Arrays
				.stream(theInvokersLists)
				.filter(t -> t != null)
				.flatMap(t -> t.stream())
				.sorted()
				.collect(Collectors.toList());

		}

		return retVal;
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

	private class AnonymousLambdaInvoker extends BaseInvoker {
		private final IAnonymousInterceptor myHook;
		private final Pointcut myPointcut;

		public AnonymousLambdaInvoker(Pointcut thePointcut, IAnonymousInterceptor theHook, int theOrder) {
			super(theHook, theOrder);
			myHook = theHook;
			myPointcut = thePointcut;
		}

		@Override
		boolean invoke(HookParams theParams) {
			myHook.invoke(myPointcut, theParams);
			return true;
		}
	}

	private abstract static class BaseInvoker implements Comparable<BaseInvoker> {

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
		public int compareTo(BaseInvoker theInvoker) {
			return myOrder - theInvoker.myOrder;
		}
	}

	private static class HookInvoker extends BaseInvoker {

		private final boolean myReturnsBoolean;
		private final Method myMethod;
		private final Class<?>[] myParameterTypes;
		private final int[] myParameterIndexes;
		private final Set<Pointcut> myPointcuts;

		/**
		 * Constructor
		 */
		private HookInvoker(Hook theHook, @Nonnull Object theInterceptor, @Nonnull Method theHookMethod, int theOrder) {
			super(theInterceptor, theOrder);
			myPointcuts = Collections.unmodifiableSet(Sets.newHashSet(theHook.value()));
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

		public Set<Pointcut> getPointcuts() {
			return myPointcuts;
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
					throw new InternalErrorException("Failure invoking interceptor for pointcut(s) " + getPointcuts(), targetException);
				}
			} catch (Exception e) {
				throw new InternalErrorException(e);
			}

		}

	}

	private static List<HookInvoker> scanInterceptorAndAddToInvokerMultimap(Object theInterceptor, ListMultimap<Pointcut, BaseInvoker> theInvokers) {
		Class<?> interceptorClass = theInterceptor.getClass();
		int typeOrder = determineOrder(interceptorClass);

		List<HookInvoker> addedInvokers = scanInterceptorForHookMethods(theInterceptor, typeOrder);

		// Invoke the REGISTERED pointcut for any added hooks
		addedInvokers.stream()
			.filter(t -> t.getPointcuts().contains(Pointcut.REGISTERED))
			.forEach(t -> t.invoke(new HookParams()));

		// Register the interceptor and its various hooks
		for (HookInvoker nextAddedHook : addedInvokers) {
			for (Pointcut nextPointcut : nextAddedHook.getPointcuts()) {
				if (nextPointcut.equals(Pointcut.REGISTERED)) {
					continue;
				}
				theInvokers.put(nextPointcut, nextAddedHook);
			}
		}

		// Make sure we're always sorted according to the order declared in
		// @Order
		for (Pointcut nextPointcut : theInvokers.keys()) {
			List<BaseInvoker> nextInvokerList = theInvokers.get(nextPointcut);
			nextInvokerList.sort(Comparator.naturalOrder());
		}

		return addedInvokers;
	}

	/**
	 * @return Returns a list of any added invokers
	 */
	private static List<HookInvoker> scanInterceptorForHookMethods(Object theInterceptor, int theTypeOrder) {
		ArrayList<HookInvoker> retVal = new ArrayList<>();
		for (Method nextMethod : theInterceptor.getClass().getMethods()) {
			Hook hook = AnnotationUtils.findAnnotation(nextMethod, Hook.class);

			if (hook != null) {
				int methodOrder = theTypeOrder;
				Order methodOrderAnnotation = AnnotationUtils.findAnnotation(nextMethod, Order.class);
				if (methodOrderAnnotation != null) {
					methodOrder = methodOrderAnnotation.value();
				}

				retVal.add(new HookInvoker(hook, theInterceptor, nextMethod, methodOrder));
			}
		}

		return retVal;
	}

	private static int determineOrder(Class<?> theInterceptorClass) {
		int typeOrder = DEFAULT_ORDER;
		Order typeOrderAnnotation = AnnotationUtils.findAnnotation(theInterceptorClass, Order.class);
		if (typeOrderAnnotation != null) {
			typeOrder = typeOrderAnnotation.value();
		}
		return typeOrder;
	}

	private static String toErrorString(List<String> theParameterTypes) {
		return theParameterTypes
			.stream()
			.sorted()
			.collect(Collectors.joining(","));
	}

}
