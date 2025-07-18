/*
 * #%L
 * HAPI FHIR - Core Library
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
package org.hl7.fhir.instance.model.api;

public interface IBaseReference extends ICompositeType {

	IBaseResource getResource();

	IBaseReference setResource(IBaseResource theResource);

	IIdType getReferenceElement();

	IBaseReference setReference(String theReference);

	IBase setDisplay(String theValue);

	IPrimitiveType<String> getDisplayElement();

	default boolean hasIdentifier() {
		return false;
	}

	default IBaseReference setIdentifier(ICompositeType theIdentifier) {
		throw new UnsupportedOperationException("This reference does not support identifiers");
	}

	default ICompositeType getIdentifier() {
		return null;
	}
}
