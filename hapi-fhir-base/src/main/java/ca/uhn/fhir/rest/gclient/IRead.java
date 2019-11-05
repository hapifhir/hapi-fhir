package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseResource;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface IRead {
	/**
	 * Construct a read for the given resource type (e.g. Patient.class)
	 */
	<T extends IBaseResource> IReadTyped<T> resource(Class<T> theResourceType);

	/**
	 * Construct a read for the given resource type (e.g. "Patient")
	 */
	IReadTyped<IBaseResource> resource(String theResourceType);
}
