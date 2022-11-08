package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Reasoning
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IAccessor;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides utility methods for doing reflection on FHIR
 * resources. It's specifically focused on knowledge artifact resources since
 * there's not a common interface for those across different Resources (and FHIR
 * versions)
 */
public class Reflections {

	private Reflections() {
	}

	/**
	 * Gets the IAccessor for the given BaseType and child
	 * 
	 * @param <BaseType>       an IBase type
	 * @param theBaseTypeClass the class of the IBase type
	 * @param theChildName     the name of the child property of the
	 *                         BaseType to generate an accessor for
	 * @return an IAccessor for the given child and the BaseType
	 */
	public static <BaseType extends IBase> IAccessor getAccessor(
			final Class<? extends BaseType> theBaseTypeClass, String theChildName) {
		checkNotNull(theBaseTypeClass);
		checkNotNull(theChildName);

		FhirContext fhirContext = FhirContext.forCached(FhirVersions.forClass(theBaseTypeClass));
		if (theBaseTypeClass.isInstance(IBaseResource.class)) {
			Class<? extends IBaseResource> theIBaseResourceClass = theBaseTypeClass.asSubclass(IBaseResource.class);
			RuntimeResourceDefinition resourceDefinition = fhirContext
					.getResourceDefinition(theIBaseResourceClass);
			return resourceDefinition.getChildByName(theChildName).getAccessor();
		} else {
			BaseRuntimeElementDefinition<?> elementDefinition = fhirContext.getElementDefinition(theBaseTypeClass);
			return elementDefinition.getChildByName(theChildName).getAccessor();
		}
	}

	/**
	 * Generates a function to access a primitive property of the given
	 * BaseType.
	 * 
	 * @param <BaseType>       an IBase type
	 * @param <ReturnType>     a return type for the Functions
	 * @param theBaseTypeClass the class of a the IBase type
	 * @param theChildName     to create a function for
	 * @return a function for accessing the "theChildName" property of the
	 *         BaseType
	 */
	public static <BaseType extends IBase, ReturnType> Function<BaseType, ReturnType> getPrimitiveFunction(
			final Class<? extends BaseType> theBaseTypeClass, String theChildName) {
		checkNotNull(theBaseTypeClass);
		checkNotNull(theChildName);

		IAccessor accessor = getAccessor(theBaseTypeClass, theChildName);
		return r -> {
			Optional<IBase> value = accessor.getFirstValueOrNull(r);
			if (!value.isPresent()) {
				return null;
			} else {
				@SuppressWarnings("unchecked")
				ReturnType x = ((IPrimitiveType<ReturnType>) value.get()).getValue();
				return x;
			}
		};
	}

	/**
	 * Generates a function to access a primitive property of the given
	 * BaseType.
	 * 
	 * @param <BaseType>       an IBase type
	 * @param <ReturnType>     a return type for the Functions
	 * @param theBaseTypeClass the class of a the IBase type
	 * @param theChildName     to create a function for
	 * @return a function for accessing the "theChildName" property of the
	 *         BaseType
	 */
	public static <BaseType extends IBase, ReturnType extends List<? extends IBase>> Function<BaseType, ReturnType> getFunction(
			final Class<? extends BaseType> theBaseTypeClass, String theChildName) {
		checkNotNull(theBaseTypeClass);
		checkNotNull(theChildName);

		IAccessor accessor = getAccessor(theBaseTypeClass, theChildName);
		return r -> {
			@SuppressWarnings("unchecked")
			ReturnType x = (ReturnType) accessor.getValues(r);
			return x;
		};
	}

	/**
	 * Generates a function to access the "version" property of the given
	 * BaseType.
	 * 
	 * @param <BaseType>       an IBase type
	 * @param theBaseTypeClass the class of a the IBase type
	 * @return a function for accessing the "version" property of the BaseType
	 */
	public static <BaseType extends IBase> Function<BaseType, String> getVersionFunction(
			final Class<? extends BaseType> theBaseTypeClass) {
		checkNotNull(theBaseTypeClass);

		return getPrimitiveFunction(theBaseTypeClass, "version");
	}

	/**
	 * Generates a function to access the "url" property of the given BaseType.
	 * 
	 * @param <BaseType>       an IBase type
	 * @param theBaseTypeClass the class of a the IBase type
	 * @return a function for accessing the "url" property of the BaseType
	 */
	public static <BaseType extends IBase> Function<BaseType, String> getUrlFunction(
			final Class<? extends BaseType> theBaseTypeClass) {
		checkNotNull(theBaseTypeClass);

		return getPrimitiveFunction(theBaseTypeClass, "url");
	}

	/**
	 * Generates a function to access the "name" property of the given BaseType.
	 * 
	 * @param <BaseType>       an IBase type
	 * @param theBaseTypeClass the class of a the IBase type
	 * @return a function for accessing the "name" property of the BaseType
	 */
	public static <BaseType extends IBase> Function<BaseType, String> getNameFunction(
			final Class<? extends BaseType> theBaseTypeClass) {
		checkNotNull(theBaseTypeClass);

		return getPrimitiveFunction(theBaseTypeClass, "name");
	}
}
