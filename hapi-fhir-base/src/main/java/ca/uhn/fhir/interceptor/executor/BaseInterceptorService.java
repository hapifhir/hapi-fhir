package ca.uhn.fhir.interceptor.executor;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorService;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ReflectionUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseInterceptorService<POINTCUT extends IPointcut> implements IBaseInterceptorService<POINTCUT>, IBaseInterceptorBroadcaster<POINTCUT> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseInterceptorService.class);
	private final List<Object> myInterceptors = new ArrayList<>();
	private final ListMultimap<POINTCUT, BaseInvoker> myGlobalInvokers = ArrayListMultimap.create();
	private final ListMultimap<POINTCUT, BaseInvoker> myAnonymousInvokers = ArrayListMultimap.create();
	private final Object myRegistryMutex = new Object();
	private final ThreadLocal<ListMultimap<POINTCUT, BaseInvoker>> myThreadlocalInvokers = new ThreadLocal<>();
	private String myName;
	private boolean myThreadlocalInvokersEnabled = true;
	private boolean myWarnOnInterceptorWithNoHooks = true;

	/**
	 * Constructor which uses a default name of "default"
	 */
	public BaseInterceptorService() {
		this("default");
	}

	/**
	 * Constructor
	 *
	 * @param theName The name for this registry (useful for troubleshooting)
	 */
	public BaseInterceptorService(String theName) {
		super();
		myName = theName;
	}

	/**
	 * Should a warning be issued if an interceptor is registered and it has no hooks
	 */
	public void setWarnOnInterceptorWithNoHooks(boolean theWarnOnInterceptorWithNoHooks) {
		myWarnOnInterceptorWithNoHooks = theWarnOnInterceptorWithNoHooks;
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

	public void setName(String theName) {
		myName = theName;
	}

	protected void registerAnonymousInterceptor(POINTCUT thePointcut, Object theInterceptor, BaseInvoker theInvoker) {
		Validate.notNull(thePointcut);
		Validate.notNull(theInterceptor);
		synchronized (myRegistryMutex) {

			myAnonymousInvokers.put(thePointcut, theInvoker);
			if (!isInterceptorAlreadyRegistered(theInterceptor)) {
				myInterceptors.add(theInterceptor);
			}
		}
	}

	@Override
	public List<Object> getAllRegisteredInterceptors() {
		synchronized (myRegistryMutex) {
			List<Object> retVal = new ArrayList<>();
			retVal.addAll(myInterceptors);
			return Collections.unmodifiableList(retVal);
		}
	}

	@Override
	@VisibleForTesting
	public void unregisterAllInterceptors() {
		synchronized (myRegistryMutex) {
			unregisterInterceptors(myAnonymousInvokers.values());
			unregisterInterceptors(myGlobalInvokers.values());
			unregisterInterceptors(myInterceptors);
		}
	}

	@Override
	public void unregisterInterceptors(@Nullable Collection<?> theInterceptors) {
		if (theInterceptors != null) {
			new ArrayList<>(theInterceptors).forEach(t -> unregisterInterceptor(t));
		}
	}

	@Override
	public void registerInterceptors(@Nullable Collection<?> theInterceptors) {
		if (theInterceptors != null) {
			theInterceptors.forEach(t -> registerInterceptor(t));
		}
	}

	@Override
	public void unregisterAllAnonymousInterceptors() {
		synchronized (myRegistryMutex) {
			unregisterInterceptorsIf(t -> true, myAnonymousInvokers);
		}
	}

	@Override
	public void unregisterInterceptorsIf(Predicate<Object> theShouldUnregisterFunction) {
		unregisterInterceptorsIf(theShouldUnregisterFunction, myGlobalInvokers);
		unregisterInterceptorsIf(theShouldUnregisterFunction, myAnonymousInvokers);
	}

	private void unregisterInterceptorsIf(Predicate<Object> theShouldUnregisterFunction, ListMultimap<POINTCUT, BaseInvoker> theGlobalInvokers) {
		synchronized (myRegistryMutex) {
			theGlobalInvokers.entries().removeIf(t -> theShouldUnregisterFunction.test(t.getValue().getInterceptor()));
		}
	}

	@Override
	public boolean registerThreadLocalInterceptor(Object theInterceptor) {
		if (!myThreadlocalInvokersEnabled) {
			return false;
		}
		ListMultimap<POINTCUT, BaseInvoker> invokers = getThreadLocalInvokerMultimap();
		scanInterceptorAndAddToInvokerMultimap(theInterceptor, invokers);
		return !invokers.isEmpty();

	}

	@Override
	public void unregisterThreadLocalInterceptor(Object theInterceptor) {
		if (myThreadlocalInvokersEnabled) {
			ListMultimap<POINTCUT, BaseInvoker> invokers = getThreadLocalInvokerMultimap();
			invokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
			if (invokers.isEmpty()) {
				myThreadlocalInvokers.remove();
			}
		}
	}

	private ListMultimap<POINTCUT, BaseInvoker> getThreadLocalInvokerMultimap() {
		ListMultimap<POINTCUT, BaseInvoker> invokers = myThreadlocalInvokers.get();
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
				if (myWarnOnInterceptorWithNoHooks) {
					ourLog.warn("Interceptor registered with no valid hooks - Type was: {}", theInterceptor.getClass().getName());
				}
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
	public boolean unregisterInterceptor(Object theInterceptor) {
		synchronized (myRegistryMutex) {
			boolean removed = myInterceptors.removeIf(t -> t == theInterceptor);
			removed |= myGlobalInvokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
			removed |= myAnonymousInvokers.entries().removeIf(t -> t.getValue().getInterceptor() == theInterceptor);
			return removed;
		}
	}

	private void sortByOrderAnnotation(List<Object> theObjects) {
		IdentityHashMap<Object, Integer> interceptorToOrder = new IdentityHashMap<>();
		for (Object next : theObjects) {
			Interceptor orderAnnotation = next.getClass().getAnnotation(Interceptor.class);
			int order = orderAnnotation != null ? orderAnnotation.order() : 0;
			interceptorToOrder.put(next, order);
		}

		theObjects.sort((a, b) -> {
			Integer orderA = interceptorToOrder.get(a);
			Integer orderB = interceptorToOrder.get(b);
			return orderA - orderB;
		});
	}

	@Override
	public Object callHooksAndReturnObject(POINTCUT thePointcut, HookParams theParams) {
		assert haveAppropriateParams(thePointcut, theParams);
		assert thePointcut.getReturnType() != void.class;

		return doCallHooks(thePointcut, theParams, null);
	}

	@Override
	public boolean hasHooks(POINTCUT thePointcut) {
		return myGlobalInvokers.containsKey(thePointcut)
			|| myAnonymousInvokers.containsKey(thePointcut)
			|| hasThreadLocalHooks(thePointcut);
	}

	private boolean hasThreadLocalHooks(POINTCUT thePointcut) {
		ListMultimap<POINTCUT, BaseInvoker> hooks = myThreadlocalInvokersEnabled ? myThreadlocalInvokers.get() : null;
		return hooks != null && hooks.containsKey(thePointcut);
	}

	@Override
	public boolean callHooks(POINTCUT thePointcut, HookParams theParams) {
		assert haveAppropriateParams(thePointcut, theParams);
		assert thePointcut.getReturnType() == void.class || thePointcut.getReturnType() == boolean.class;

		Object retValObj = doCallHooks(thePointcut, theParams, true);
		return (Boolean) retValObj;
	}

	private Object doCallHooks(POINTCUT thePointcut, HookParams theParams, Object theRetVal) {
		// use new list for loop to avoid ConcurrentModificationException in case invoker gets added while looping
		List<BaseInvoker> invokers = new ArrayList<>(getInvokersForPointcut(thePointcut));

		/*
		 * Call each hook in order
		 */
		for (BaseInvoker nextInvoker : invokers) {
			Object nextOutcome = nextInvoker.invoke(theParams);
			Class<?> pointcutReturnType = thePointcut.getReturnType();
			if (pointcutReturnType.equals(boolean.class)) {
				Boolean nextOutcomeAsBoolean = (Boolean) nextOutcome;
				if (Boolean.FALSE.equals(nextOutcomeAsBoolean)) {
					ourLog.trace("callHooks({}) for invoker({}) returned false", thePointcut, nextInvoker);
					theRetVal = false;
					break;
				}
			} else if (pointcutReturnType.equals(void.class) == false) {
				if (nextOutcome != null) {
					theRetVal = nextOutcome;
					break;
				}
			}
		}

		return theRetVal;
	}

	@VisibleForTesting
	List<Object> getInterceptorsWithInvokersForPointcut(POINTCUT thePointcut) {
		return getInvokersForPointcut(thePointcut)
			.stream()
			.map(BaseInvoker::getInterceptor)
			.collect(Collectors.toList());
	}

	/**
	 * Returns an ordered list of invokers for the given pointcut. Note that
	 * a new and stable list is returned to.. do whatever you want with it.
	 */
	private List<BaseInvoker> getInvokersForPointcut(POINTCUT thePointcut) {
		List<BaseInvoker> invokers;

		synchronized (myRegistryMutex) {
			List<BaseInvoker> globalInvokers = myGlobalInvokers.get(thePointcut);
			List<BaseInvoker> anonymousInvokers = myAnonymousInvokers.get(thePointcut);
			List<BaseInvoker> threadLocalInvokers = null;
			if (myThreadlocalInvokersEnabled) {
				ListMultimap<POINTCUT, BaseInvoker> pointcutToInvokers = myThreadlocalInvokers.get();
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
	@SafeVarargs
	private final List<BaseInvoker> union(List<BaseInvoker>... theInvokersLists) {
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
	boolean haveAppropriateParams(POINTCUT thePointcut, HookParams theParams) {
		if (theParams.getParamsForType().values().size() != thePointcut.getParameterTypes().size()) {
			throw new IllegalArgumentException(Msg.code(1909) + String.format("Wrong number of params for pointcut %s - Wanted %s but found %s", thePointcut.name(), toErrorString(thePointcut.getParameterTypes()), theParams.getParamsForType().values().stream().map(t -> t != null ? t.getClass().getSimpleName() : "null").sorted().collect(Collectors.toList())));
		}

		List<String> wantedTypes = new ArrayList<>(thePointcut.getParameterTypes());

		ListMultimap<Class<?>, Object> givenTypes = theParams.getParamsForType();
		for (Class<?> nextTypeClass : givenTypes.keySet()) {
			String nextTypeName = nextTypeClass.getName();
			for (Object nextParamValue : givenTypes.get(nextTypeClass)) {
				Validate.isTrue(nextParamValue == null || nextTypeClass.isAssignableFrom(nextParamValue.getClass()), "Invalid params for pointcut %s - %s is not of type %s", thePointcut.name(), nextParamValue != null ? nextParamValue.getClass() : "null", nextTypeClass);
				Validate.isTrue(wantedTypes.remove(nextTypeName), "Invalid params for pointcut %s - Wanted %s but found %s", thePointcut.name(), toErrorString(thePointcut.getParameterTypes()), nextTypeName);
			}
		}

		return true;
	}

	private List<HookInvoker> scanInterceptorAndAddToInvokerMultimap(Object theInterceptor, ListMultimap<POINTCUT, BaseInvoker> theInvokers) {
		Class<?> interceptorClass = theInterceptor.getClass();
		int typeOrder = determineOrder(interceptorClass);

		List<HookInvoker> addedInvokers = scanInterceptorForHookMethods(theInterceptor, typeOrder);

		// Invoke the REGISTERED pointcut for any added hooks
		addedInvokers.stream()
			.filter(t -> Pointcut.INTERCEPTOR_REGISTERED.equals(t.getPointcut()))
			.forEach(t -> t.invoke(new HookParams()));

		// Register the interceptor and its various hooks
		for (HookInvoker nextAddedHook : addedInvokers) {
			IPointcut nextPointcut = nextAddedHook.getPointcut();
			if (nextPointcut.equals(Pointcut.INTERCEPTOR_REGISTERED)) {
				continue;
			}
			theInvokers.put((POINTCUT) nextPointcut, nextAddedHook);
		}

		// Make sure we're always sorted according to the order declared in
		// @Order
		for (IPointcut nextPointcut : theInvokers.keys()) {
			List<BaseInvoker> nextInvokerList = theInvokers.get((POINTCUT) nextPointcut);
			nextInvokerList.sort(Comparator.naturalOrder());
		}

		return addedInvokers;
	}

	/**
	 * @return Returns a list of any added invokers
	 */
	private List<HookInvoker> scanInterceptorForHookMethods(Object theInterceptor, int theTypeOrder) {
		ArrayList<HookInvoker> retVal = new ArrayList<>();
		for (Method nextMethod : ReflectionUtil.getDeclaredMethods(theInterceptor.getClass(), true)) {
			Optional<HookDescriptor> hook = scanForHook(nextMethod);

			if (hook.isPresent()) {
				int methodOrder = theTypeOrder;
				int methodOrderAnnotation = hook.get().getOrder();
				if (methodOrderAnnotation != Interceptor.DEFAULT_ORDER) {
					methodOrder = methodOrderAnnotation;
				}

				retVal.add(new HookInvoker(hook.get(), theInterceptor, nextMethod, methodOrder));
			}
		}

		return retVal;
	}

	protected abstract Optional<HookDescriptor> scanForHook(Method nextMethod);

	protected static <T extends Annotation> Optional<T> findAnnotation(AnnotatedElement theObject, Class<T> theHookClass) {
		T annotation;
		if (theObject instanceof Method) {
			annotation = MethodUtils.getAnnotation((Method) theObject, theHookClass, true, true);
		} else {
			annotation = theObject.getAnnotation(theHookClass);
		}
		return Optional.ofNullable(annotation);
	}

	private static int determineOrder(Class<?> theInterceptorClass) {
		int typeOrder = Interceptor.DEFAULT_ORDER;
		Optional<Interceptor> typeOrderAnnotation = findAnnotation(theInterceptorClass, Interceptor.class);
		if (typeOrderAnnotation.isPresent()) {
			typeOrder = typeOrderAnnotation.get().order();
		}
		return typeOrder;
	}

	private static String toErrorString(List<String> theParameterTypes) {
		return theParameterTypes
			.stream()
			.sorted()
			.collect(Collectors.joining(","));
	}

	protected abstract static class BaseInvoker implements Comparable<BaseInvoker> {

		private final int myOrder;
		private final Object myInterceptor;

		BaseInvoker(Object theInterceptor, int theOrder) {
			myInterceptor = theInterceptor;
			myOrder = theOrder;
		}

		public Object getInterceptor() {
			return myInterceptor;
		}

		abstract Object invoke(HookParams theParams);

		@Override
		public int compareTo(BaseInvoker theInvoker) {
			return myOrder - theInvoker.myOrder;
		}
	}

	private static class HookInvoker extends BaseInvoker {

		private final Method myMethod;
		private final Class<?>[] myParameterTypes;
		private final int[] myParameterIndexes;
		private final IPointcut myPointcut;

		/**
		 * Constructor
		 */
		private HookInvoker(HookDescriptor theHook, @Nonnull Object theInterceptor, @Nonnull Method theHookMethod, int theOrder) {
			super(theInterceptor, theOrder);
			myPointcut = theHook.getPointcut();
			myParameterTypes = theHookMethod.getParameterTypes();
			myMethod = theHookMethod;

			Class<?> returnType = theHookMethod.getReturnType();
			if (myPointcut.getReturnType().equals(boolean.class)) {
				Validate.isTrue(boolean.class.equals(returnType) || void.class.equals(returnType), "Method does not return boolean or void: %s", theHookMethod);
			} else if (myPointcut.getReturnType().equals(void.class)) {
				Validate.isTrue(void.class.equals(returnType), "Method does not return void: %s", theHookMethod);
			} else {
				Validate.isTrue(myPointcut.getReturnType().isAssignableFrom(returnType) || void.class.equals(returnType), "Method does not return %s or void: %s", myPointcut.getReturnType(), theHookMethod);
			}

			myParameterIndexes = new int[myParameterTypes.length];
			Map<Class<?>, AtomicInteger> typeToCount = new HashMap<>();
			for (int i = 0; i < myParameterTypes.length; i++) {
				AtomicInteger counter = typeToCount.computeIfAbsent(myParameterTypes[i], t -> new AtomicInteger(0));
				myParameterIndexes[i] = counter.getAndIncrement();
			}

			myMethod.setAccessible(true);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("method", myMethod)
				.toString();
		}

		public IPointcut getPointcut() {
			return myPointcut;
		}

		/**
		 * @return Returns true/false if the hook method returns a boolean, returns true otherwise
		 */
		@Override
		Object invoke(HookParams theParams) {

			Object[] args = new Object[myParameterTypes.length];
			for (int i = 0; i < myParameterTypes.length; i++) {
				Class<?> nextParamType = myParameterTypes[i];
				if (nextParamType.equals(Pointcut.class)) {
					args[i] = myPointcut;
				} else {
					int nextParamIndex = myParameterIndexes[i];
					Object nextParamValue = theParams.get(nextParamType, nextParamIndex);
					args[i] = nextParamValue;
				}
			}

			// Invoke the method
			try {
				return myMethod.invoke(getInterceptor(), args);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (myPointcut.isShouldLogAndSwallowException(targetException)) {
					ourLog.error("Exception thrown by interceptor: " + targetException.toString(), targetException);
					return null;
				}

				if (targetException instanceof RuntimeException) {
					throw ((RuntimeException) targetException);
				} else {
					throw new InternalErrorException(Msg.code(1910) + "Failure invoking interceptor for pointcut(s) " + getPointcut(), targetException);
				}
			} catch (Exception e) {
				throw new InternalErrorException(Msg.code(1911) + e);
			}

		}

	}

	protected static class HookDescriptor {

		private final IPointcut myPointcut;
		private final int myOrder;

		public HookDescriptor(IPointcut thePointcut, int theOrder) {
			myPointcut = thePointcut;
			myOrder = theOrder;
		}

		IPointcut getPointcut() {
			return myPointcut;
		}

		int getOrder() {
			return myOrder;
		}

	}

}
