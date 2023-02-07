package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;

import javax.annotation.Nonnull;

public class CrossPartitionReferenceDetails {

	@Nonnull
	private final RequestPartitionId mySourceResourcePartitionId;
	@Nonnull
	private final RequestPartitionId myTargetResourcePartitionId;
	@Nonnull
	private final IResourceLookup<JpaPid> myTargetResource;

	/**
	 * Constructor
	 */
	public CrossPartitionReferenceDetails(@Nonnull RequestPartitionId theSourceResourcePartitionId, @Nonnull RequestPartitionId theTargetResourcePartitionId, @Nonnull IResourceLookup<JpaPid> theTargetResource) {
		mySourceResourcePartitionId = theSourceResourcePartitionId;
		myTargetResourcePartitionId = theTargetResourcePartitionId;
		myTargetResource = theTargetResource;
	}

	@Nonnull
	public RequestPartitionId getSourceResourcePartitionId() {
		return mySourceResourcePartitionId;
	}

	@Nonnull
	public RequestPartitionId getTargetResourcePartitionId() {
		return myTargetResourcePartitionId;
	}

	@Nonnull
	public IResourceLookup<JpaPid> getTargetResource() {
		return myTargetResource;
	}

}
