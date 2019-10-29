package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.support.IContextValidationSupport;

public class ReflectionUtil {

	private static final ConcurrentHashMap<String, Object> ourFhirServerVersions = new ConcurrentHashMap<String, Object>();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReflectionUtil.class);

	public static LinkedHashSet<Method> getDeclaredMethods(Class<?> theClazz) {
		LinkedHashSet<Method> retVal = new LinkedHashSet<Method>();

		Collection<Method> methods = getAllMethods(theClazz, false, false);
		for (Method next : methods) {
			try {
				Method method = theClazz.getMethod(next.getName(), next.getParameterTypes());
				retVal.add(method);
			} catch (NoSuchMethodException e) {
				retVal.add(next);
			} catch (SecurityException e) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	public static Class<?> getGenericCollectionTypeOfField(Field next) {
		ParameterizedType collectionType = (ParameterizedType) next.getGenericType();
		return getGenericCollectionTypeOf(collectionType.getActualTypeArguments()[0]);
	}

	/**
	 * For a field of type List<Enumeration<Foo>>, returns Foo
	 */
	public static Class<?> getGenericCollectionTypeOfFieldWithSecondOrderForList(Field next) {
		if (!List.class.isAssignableFrom(next.getType())) {
			return getGenericCollectionTypeOfField(next);
		}

		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) next.getGenericType();
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			Type pt2 = pt.getActualTypeArguments()[0];
			return (Class<?>) pt2;
		}
		type = (Class<?>) firstArg;
		return type;
	}

	public static Class<?> getGenericCollectionTypeOfMethodParameter(Method theMethod, int theParamIndex) {
		Type genericParameterType = theMethod.getGenericParameterTypes()[theParamIndex];
		if (Class.class.equals(genericParameterType) || Class.class.equals(genericParameterType.getClass())) {
			return null;
		}
		ParameterizedType collectionType = (ParameterizedType) genericParameterType;
		return getGenericCollectionTypeOf(collectionType.getActualTypeArguments()[0]);
	}

	public static Class<?> getGenericCollectionTypeOfMethodReturnType(Method theMethod) {
		Type genericReturnType = theMethod.getGenericReturnType();
		if (!(genericReturnType instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType collectionType = (ParameterizedType) genericReturnType;
		return getGenericCollectionTypeOf(collectionType.getActualTypeArguments()[0]);
	}

	@SuppressWarnings({ "rawtypes" })
	private static Class<?> getGenericCollectionTypeOf(Type theType) {
		Class<?> type;
		if (ParameterizedType.class.isAssignableFrom(theType.getClass())) {
			ParameterizedType pt = ((ParameterizedType) theType);
			type = (Class<?>) pt.getRawType();
		} else if (theType instanceof TypeVariable<?>) {
			Type decl = ((TypeVariable) theType).getBounds()[0];
			return (Class<?>) decl;
		} else if (theType instanceof WildcardType) {
			Type decl = ((WildcardType) theType).getUpperBounds()[0];
			return (Class<?>) decl;
		} else {
			type = (Class<?>) theType;
		}
		return type;
	}

	public static boolean isInstantiable(Class<?> theType) {
		return !theType.isInterface() && !Modifier.isAbstract(theType.getModifiers());
	}

	/**
	 * Instantiate a class by no-arg constructor, throw {@link ConfigurationException} if we fail to do so
	 */
	@CoverageIgnore
	public static <T> T newInstance(Class<T> theType) {
		Validate.notNull(theType, "theType must not be null");
		try {
			return theType.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException("Failed to instantiate " + theType.getName(), e);
		}
	}

	public static <T> T newInstance(Class<T> theType, Class<?> theArgumentType, Object theArgument) {
		Validate.notNull(theType, "theType must not be null");
		try {
			Constructor<T> constructor = theType.getConstructor(theArgumentType);
			return constructor.newInstance(theArgument);
		} catch (Exception e) {
			throw new ConfigurationException("Failed to instantiate " + theType.getName(), e);
		}
	}

	public static Object newInstanceOfFhirServerType(String theType) {
		String errorMessage = "Unable to instantiate server framework. Please make sure that hapi-fhir-server library is on your classpath!";
		String wantedType = "ca.uhn.fhir.rest.api.server.IFhirVersionServer";
		return newInstanceOfType(theType, errorMessage, wantedType);
	}

	@SuppressWarnings("unchecked")
	public static <EVS_IN, EVS_OUT, SDT, CST, CDCT, IST> ca.uhn.fhir.context.support.IContextValidationSupport<EVS_IN, EVS_OUT, SDT, CST, CDCT, IST> newInstanceOfFhirProfileValidationSupport(
			String theType) {
		String errorMessage = "Unable to instantiate validation support! Please make sure that hapi-fhir-validation and the appropriate structures JAR are on your classpath!";
		String wantedType = "ca.uhn.fhir.context.support.IContextValidationSupport";
		Object fhirServerVersion = newInstanceOfType(theType, errorMessage, wantedType);
		return (IContextValidationSupport<EVS_IN, EVS_OUT, SDT, CST, CDCT, IST>) fhirServerVersion;
	}

	private static Object newInstanceOfType(String theType, String errorMessage, String wantedType) {
		Object fhirServerVersion = ourFhirServerVersions.get(theType);
		if (fhirServerVersion == null) {
			try {
				Class<?> type = Class.forName(theType);
				Class<?> serverType = Class.forName(wantedType);
				Validate.isTrue(serverType.isAssignableFrom(type));
				fhirServerVersion = type.newInstance();
			} catch (Exception e) {
				throw new ConfigurationException(errorMessage, e);
			}

			ourFhirServerVersions.put(theType, fhirServerVersion);
		}
		return fhirServerVersion;
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstanceOrReturnNull(String theClassName, Class<T> theType) {
		try {
			Class<?> clazz = Class.forName(theClassName);
			if (!theType.isAssignableFrom(clazz)) {
				throw new ConfigurationException(theClassName + " is not assignable to " + theType);
			}
			return (T) clazz.newInstance();
		} catch (ConfigurationException e) {
			throw e;
		} catch (Exception e) {
			ourLog.info("Failed to instantiate {}: {}", theClassName, e.toString());
			return null;
		}
	}

	public static Collection<Method> getAllMethods(Class clazz,
																  boolean includeAllPackageAndPrivateMethodsOfSuperclasses,
																  boolean includeOverridenAndHidden) {

		Predicate<Method> include = m -> !m.isBridge() && !m.isSynthetic() &&
			Character.isJavaIdentifierStart(m.getName().charAt(0))
			&& m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		Set<Method> methods = new LinkedHashSet<>();
		Collections.addAll(methods, clazz.getMethods());
		methods.removeIf(include.negate());
		Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);

		final int access=Modifier.PUBLIC|Modifier.PROTECTED|Modifier.PRIVATE;

		Package p = clazz.getPackage();
		if(!includeAllPackageAndPrivateMethodsOfSuperclasses) {
			int pass = includeOverridenAndHidden?
				Modifier.PUBLIC|Modifier.PROTECTED: Modifier.PROTECTED;
			include = include.and(m -> { int mod = m.getModifiers();
				return (mod&pass)!=0
					|| (mod&access)==0 && m.getDeclaringClass().getPackage()==p;
			});
		}
		if(!includeOverridenAndHidden) {
			Map<Object,Set<Package>> types = new HashMap<>();
			final Set<Package> pkgIndependent = Collections.emptySet();
			for(Method m: methods) {
				int acc=m.getModifiers()&access;
				if(acc==Modifier.PRIVATE) continue;
				if(acc!=0) types.put(methodKey(m), pkgIndependent);
				else types.computeIfAbsent(methodKey(m),x->new HashSet<>()).add(p);
			}
			include = include.and(m -> { int acc = m.getModifiers()&access;
				return acc!=0? acc==Modifier.PRIVATE
					|| types.putIfAbsent(methodKey(m), pkgIndependent)==null:
					noPkgOverride(m, types, pkgIndependent);
			});
		}
		for(clazz=clazz.getSuperclass(); clazz!=null; clazz=clazz.getSuperclass())
			Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);
		return methods;
	}
	static boolean noPkgOverride(
		Method m, Map<Object,Set<Package>> types, Set<Package> pkgIndependent) {
		Set<Package> pkg = types.computeIfAbsent(methodKey(m), key -> new HashSet<>());
		return pkg!=pkgIndependent && pkg.add(m.getDeclaringClass().getPackage());
	}
	private static Object methodKey(Method m) {
		return Arrays.asList(m.getName(),
			MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
	}

}
