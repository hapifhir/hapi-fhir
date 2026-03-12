/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import org.hl7.fhir.r4.model.Reference;

import java.util.List;

/**
 * Result of moving compartment resources across partitions during a cross-partition merge.
 * Contains references extracted from the combined CREATE+UPDATE response bundle, plus
 * versioned references to the original source copies (for tombstone computation and deferred deletion).
 */
// Created by claude-opus-4-6
public class CrossPartitionMoveResult {
	private final List<Reference> myReferencesToChangedResources;
	private final List<Reference> myReferencesToMovedResourceOriginals;

	public CrossPartitionMoveResult(
			List<Reference> theReferencesToChangedResources, List<Reference> theReferencesToMovedResourceOriginals) {
		myReferencesToChangedResources = theReferencesToChangedResources;
		myReferencesToMovedResourceOriginals = theReferencesToMovedResourceOriginals;
	}

	public List<Reference> getReferencesToChangedResources() {
		return myReferencesToChangedResources;
	}

	public List<Reference> getReferencesToMovedResourceOriginals() {
		return myReferencesToMovedResourceOriginals;
	}
}
