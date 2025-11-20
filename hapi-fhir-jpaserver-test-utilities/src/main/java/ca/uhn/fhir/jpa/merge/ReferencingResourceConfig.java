// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import jakarta.annotation.Nonnull;

/**
 * Configuration for creating referencing resources in merge operation tests.
 *
 * This class encapsulates the information needed to create resources that reference
 * a merge source/target resource. It supports flexible test scenarios with multiple
 * referencing resource types.
 *
 * <p>Example usage:</p>
 * <pre>
 * // Create 10 PractitionerRole resources that reference a Practitioner via "practitioner" field
 * ReferencingResourceConfig config = ReferencingResourceConfig.of("PractitionerRole", "practitioner", 10);
 *
 * // Create 5 DiagnosticReport resources that reference an Observation via "result" field
 * ReferencingResourceConfig config = ReferencingResourceConfig.of("DiagnosticReport", "result", 5);
 * </pre>
 */
public class ReferencingResourceConfig {

	private final String myResourceType;
	private final String myReferencePath;
	private final int myCount;

	private ReferencingResourceConfig(@Nonnull String theResourceType, @Nonnull String theReferencePath, int theCount) {
		myResourceType = theResourceType;
		myReferencePath = theReferencePath;
		myCount = theCount;
	}

	/**
	 * Factory method to create a referencing resource configuration.
	 *
	 * @param theResourceType The FHIR resource type to create (e.g., "PractitionerRole", "Encounter")
	 * @param theReferencePath The path in the resource where the reference will be set
	 *                         (e.g., "practitioner", "participant.individual", "result")
	 * @param theCount The number of resources to create
	 * @return A new configuration instance
	 */
	@Nonnull
	public static ReferencingResourceConfig of(
			@Nonnull String theResourceType, @Nonnull String theReferencePath, int theCount) {
		return new ReferencingResourceConfig(theResourceType, theReferencePath, theCount);
	}

	/**
	 * @return The FHIR resource type (e.g., "PractitionerRole")
	 */
	@Nonnull
	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * @return The path in the resource where the reference will be set (e.g., "practitioner")
	 */
	@Nonnull
	public String getReferencePath() {
		return myReferencePath;
	}

	/**
	 * @return The number of resources to create
	 */
	public int getCount() {
		return myCount;
	}
}
