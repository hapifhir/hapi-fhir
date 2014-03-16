package ca.uhn.fhir.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtil {

	public static Class<?> getGenericCollectionTypeOfField(Field next) {
		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) next.getGenericType();
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			type = (Class<?>) pt.getRawType();
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

	public static Class<?> getGenericCollectionTypeOfMethodParameter(Method theMethod, int theParamIndex) {
		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) theMethod.getGenericParameterTypes()[theParamIndex];
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			type = (Class<?>) pt.getRawType();
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

	public static Class<?> getGenericCollectionTypeOfMethodReturnType(Method theMethod) {
		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) theMethod.getGenericReturnType();
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			type = (Class<?>) pt.getRawType();
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

}
