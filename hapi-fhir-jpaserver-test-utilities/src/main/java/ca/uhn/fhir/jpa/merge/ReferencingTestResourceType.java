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
 * Test specification for creating referencing resources in merge operation tests.
 *
 * This class encapsulates the information needed to create resources that reference
 * a merge source resource. It supports flexible test scenarios with multiple
 * referencing resource types.
 *
 * <p>Example usage:</p>
 * <pre>
 * // Create 10 PractitionerRole resources (which could reference a Practitioner)
 * ReferencingTestResourceType spec = ReferencingTestResourceType.of("PractitionerRole", 10);
 *
 * // Create 5 DiagnosticReport resources (which could reference an Observation)
 * ReferencingTestResourceType spec = ReferencingTestResourceType.of("DiagnosticReport", 5);
 * </pre>
 */
public class ReferencingTestResourceType {

	private final String myResourceType;
	private final int myCount;

	private ReferencingTestResourceType(@Nonnull String theResourceType, int theCount) {
		myResourceType = theResourceType;
		myCount = theCount;
	}

	/**
	 * Factory method to create a referencing resource type specification.
	 *
	 * @param theResourceType The FHIR resource type to create (e.g., "PractitionerRole", "Encounter")
	 * @param theCount The number of resources to create
	 * @return A new specification instance
	 */
	@Nonnull
	public static ReferencingTestResourceType of(@Nonnull String theResourceType, int theCount) {
		return new ReferencingTestResourceType(theResourceType, theCount);
	}

	/**
	 * @return The FHIR resource type (e.g., "PractitionerRole")
	 */
	@Nonnull
	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * @return The number of resources to create
	 */
	public int getCount() {
		return myCount;
	}
}
