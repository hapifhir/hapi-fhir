package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

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
		Type genericParameterType = theMethod.getGenericParameterTypes()[theParamIndex];
		if (Class.class.equals(genericParameterType)) {
			return null;
		}
		ParameterizedType collectionType = (ParameterizedType) genericParameterType;
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			type = (Class<?>) pt.getRawType();
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

	@SuppressWarnings({ "rawtypes" })
	public static Class<?> getGenericCollectionTypeOfMethodReturnType(Method theMethod) {
		Class<?> type;
		Type genericReturnType = theMethod.getGenericReturnType();
		if (!(genericReturnType instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType collectionType = (ParameterizedType) genericReturnType;
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			type = (Class<?>) pt.getRawType();
		} else if (firstArg instanceof TypeVariable<?>) {
			Type decl = ((TypeVariable) firstArg).getBounds()[0];
			return (Class<?>) decl;
		} else if (firstArg instanceof WildcardType) {
			Type decl = ((WildcardType) firstArg).getUpperBounds()[0];
			return (Class<?>) decl;
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

}
