/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.method;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Simple POJO to capture details of MethodUtil generic type information for method params, if there is any.
 */
class MethodUtilGenericsContext {
	private final Class<?> parameterType;
	private final Class<?> declaredParameterType;
	private final Class<? extends java.util.Collection<?>> outerCollectionType;
	private final Class<? extends java.util.Collection<?>> innerCollectionType;

	public MethodUtilGenericsContext(
			Class<?> theParameterType,
			Class<?> theDeclaredParameterType,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType) {
		parameterType = theParameterType;
		declaredParameterType = theDeclaredParameterType;
		outerCollectionType = theOuterCollectionType;
		innerCollectionType = theInnerCollectionType;
	}

	public Class<?> getParameterType() {
		return parameterType;
	}

	public Class<?> getDeclaredParameterType() {
		return declaredParameterType;
	}

	public Class<? extends Collection<?>> getOuterCollectionType() {
		return outerCollectionType;
	}

	public Class<? extends Collection<?>> getInnerCollectionType() {
		return innerCollectionType;
	}

	@Override
	public boolean equals(Object theO) {
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		MethodUtilGenericsContext that = (MethodUtilGenericsContext) theO;
		return Objects.equals(parameterType, that.parameterType)
				&& Objects.equals(declaredParameterType, that.declaredParameterType)
				&& Objects.equals(outerCollectionType, that.outerCollectionType)
				&& Objects.equals(innerCollectionType, that.innerCollectionType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameterType, declaredParameterType, outerCollectionType, innerCollectionType);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MethodUtilGenericsContext.class.getSimpleName() + "[", "]")
				.add("parameterType=" + parameterType)
				.add("declaredParameterType=" + declaredParameterType)
				.add("outerCollectionType=" + outerCollectionType)
				.add("innerCollectionType=" + innerCollectionType)
				.toString();
	}
}
