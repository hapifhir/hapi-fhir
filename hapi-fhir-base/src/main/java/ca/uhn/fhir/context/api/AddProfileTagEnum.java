package ca.uhn.fhir.context.api;

import ca.uhn.fhir.context.FhirContext;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * RESTful server behaviour for automatically adding profile tags when serializing resources
 * 
 * @see FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)
 */
public enum AddProfileTagEnum {
	/**
	 * Do not add profile tags automatically
	 */
	NEVER,

	/**
	 * Add any profile tags that returned resources appear to conform to
	 * 
	 * @deprecated This mode causes even FHIR's default profiles to be exported in the
	 * resource metadata section. This is not generally expected behaviour from other
	 * systems and it offers no real benefit, so it will be removed at some point. This
	 * option was deprecated in HAPI 1.5
	 */
	@Deprecated
	ALWAYS,

	/**
	 * Add any profile tags that returned resources appear to conform to if the resource is a non-standard class (e.g.
	 * it is an instance of a class that extends a built in type, but adds or constrains it)
	 */
	ONLY_FOR_CUSTOM
}
