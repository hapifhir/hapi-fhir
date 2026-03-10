/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.matcher.models;

// Created by claude-opus-4-6
/**
 * Provider interface for registering custom MDM field matchers.
 * Implementations supply a named {@link IMdmFieldMatcher} that can be
 * registered on an {@link ca.uhn.fhir.mdm.rules.matcher.IMatcherFactory}
 * and referenced by name in MDM rules JSON.
 */
public interface IMdmFieldMatcherProvider {

	/**
	 * @return the unique name for this matcher algorithm, used in MDM rules JSON
	 */
	String getName();

	/**
	 * @return the matcher implementation
	 */
	IMdmFieldMatcher getMatcher();
}
