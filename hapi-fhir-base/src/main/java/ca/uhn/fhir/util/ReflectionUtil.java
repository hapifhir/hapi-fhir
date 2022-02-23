package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtil {

	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	private static final ConcurrentHashMap<String, Object> ourFhirServerVersions = new ConcurrentHashMap<>();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReflectionUtil.class);

	/**
	 * Non instantiable
	 */
	private ReflectionUtil() {
		super();
	}

	/**
	 * Returns all methods declared against {@literal theClazz}. This method returns a predictable order, which is
	 * sorted by method name and then by parameters.
	 * <p>
	 * This method does not include superclass methods (see {@link #getDeclaredMethods(Class, boolean)} if you
	 * want to include those.
	 * </p>
	 */
	public static List<Method> getDeclaredMethods(Class<?> theClazz) {
		return getDeclaredMethods(theClazz, false);
	}

	/**
	 * Returns all methods declared against {@literal theClazz}. This method returns a predictable order, which is
	 * sorted by method name and then by parameters.
	 */
	public static List<Method> getDeclaredMethods(Class<?> theClazz, boolean theIncludeMethodsFromSuperclasses) {
		HashMap<String, Method> foundMethods = new HashMap<>();

		populateDeclaredMethodsMap(theClazz, foundMethods, theIncludeMethodsFromSuperclasses);

		List<Method> retVal = new ArrayList<>(foundMethods.values());
		retVal.sort((Comparator.comparing(ReflectionUtil::describeMethodInSortFriendlyWay)));
		return retVal;
	}

	private static void populateDeclaredMethodsMap(Class<?> theClazz, HashMap<String, Method> foundMethods, boolean theIncludeMethodsFromSuperclasses) {
		Method[] declaredMethods = theClazz.getDeclaredMethods();
		for (Method next : declaredMethods) {

			if (Modifier.isAbstract(next.getModifiers()) ||
				Modifier.isStatic(next.getModifiers()) ||
				Modifier.isPrivate(next.getModifiers())) {
				continue;
			}

			String description = next.getName() + Arrays.asList(next.getParameterTypes());

			if (!foundMethods.containsKey(description)) {
				try {
					Method method = theClazz.getMethod(next.getName(), next.getParameterTypes());
					foundMethods.put(description, method);
				} catch (NoSuchMethodException | SecurityException e) {
					foundMethods.put(description, next);
				}
			}
		}

		if (theIncludeMethodsFromSuperclasses && !theClazz.getSuperclass().equals(Object.class)) {
			populateDeclaredMethodsMap(theClazz.getSuperclass(), foundMethods, theIncludeMethodsFromSuperclasses);
		}
	}

	/**
	 * Returns a description like <code>startsWith params(java.lang.String, int) returns(boolean)</code>.
	 * The format is chosen in order to provide a predictable and useful sorting order.
	 */
	public static String describeMethodInSortFriendlyWay(Method theMethod) {
		StringBuilder b = new StringBuilder();
		b.append(theMethod.getName());
		b.append(" returns(");
		b.append(theMethod.getReturnType().getName());
		b.append(") params(");
		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		for (int i = 0, parameterTypesLength = parameterTypes.length; i < parameterTypesLength; i++) {
			if (i > 0) {
				b.append(", ");
			}
			Class<?> next = parameterTypes[i];
			b.append(next.getName());
		}
		b.append(")");
		return b.toString();
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

	@SuppressWarnings({"rawtypes"})
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
	public static <T> T newInstance(Class<T> theType) {
		Validate.notNull(theType, "theType must not be null");
		try {
			return theType.getConstructor().newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1784) + "Failed to instantiate " + theType.getName(), e);
		}
	}

	public static <T> T newInstance(Class<T> theType, Class<?> theArgumentType, Object theArgument) {
		Validate.notNull(theType, "theType must not be null");
		try {
			Constructor<T> constructor = theType.getConstructor(theArgumentType);
			return constructor.newInstance(theArgument);
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1785) + "Failed to instantiate " + theType.getName(), e);
		}
	}

	public static Object newInstanceOfFhirServerType(String theType) {
		String errorMessage = "Unable to instantiate server framework. Please make sure that hapi-fhir-server library is on your classpath!";
		String wantedType = "ca.uhn.fhir.rest.api.server.IFhirVersionServer";
		return newInstanceOfType(theType, theType, errorMessage, wantedType, new Class[0], new Object[0]);
	}

	private static Object newInstanceOfType(String theKey, String theType, String errorMessage, String wantedType, Class<?>[] theParameterArgTypes, Object[] theConstructorArgs) {
		Object fhirServerVersion = ourFhirServerVersions.get(theKey);
		if (fhirServerVersion == null) {
			try {
				Class<?> type = Class.forName(theType);
				Class<?> serverType = Class.forName(wantedType);
				Validate.isTrue(serverType.isAssignableFrom(type));
				fhirServerVersion = type.getConstructor(theParameterArgTypes).newInstance(theConstructorArgs);
			} catch (Exception e) {
				throw new ConfigurationException(Msg.code(1786) + errorMessage, e);
			}

			ourFhirServerVersions.put(theKey, fhirServerVersion);
		}
		return fhirServerVersion;
	}

	public static <T> T newInstanceOrReturnNull(String theClassName, Class<T> theType) {
		return newInstanceOrReturnNull(theClassName, theType, EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstanceOrReturnNull(String theClassName, Class<T> theType, Class<?>[] theArgTypes, Object[] theArgs) {
		try {
			Class<?> clazz = Class.forName(theClassName);
			if (!theType.isAssignableFrom(clazz)) {
				throw new ConfigurationException(Msg.code(1787) + theClassName + " is not assignable to " + theType);
			}
			return (T) clazz.getConstructor(theArgTypes).newInstance(theArgs);
		} catch (ConfigurationException e) {
			throw e;
		} catch (Exception e) {
			ourLog.info("Failed to instantiate {}: {}", theClassName, e.toString());
			return null;
		}
	}

	public static boolean typeExists(String theName) {
		try {
			Class.forName(theName);
			return true;
		} catch (ClassNotFoundException theE) {
			return false;
		}
	}
}
