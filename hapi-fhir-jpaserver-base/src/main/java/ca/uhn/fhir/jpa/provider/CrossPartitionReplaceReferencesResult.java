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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Result of copying compartment resources across partitions during a cross-partition merge.
 * Contains IDs extracted from the combined CREATE+UPDATE response bundle, plus
 * versioned IDs of the original source copies (for deferred deletion).
 */
// Created by claude-opus-4-6
public class CrossPartitionReplaceReferencesResult {
	private final List<IIdType> myChangedResourceIds;
	private final List<IIdType> myCopiedResourceOriginalIds;

	public CrossPartitionReplaceReferencesResult(
			List<IIdType> theChangedResourceIds, List<IIdType> theCopiedResourceOriginalIds) {
		myChangedResourceIds = theChangedResourceIds;
		myCopiedResourceOriginalIds = theCopiedResourceOriginalIds;
	}

	public List<IIdType> getChangedResourceIds() {
		return myChangedResourceIds;
	}

	public List<IIdType> getCopiedResourceOriginalIds() {
		return myCopiedResourceOriginalIds;
	}
}
