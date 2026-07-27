/*
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import jakarta.annotation.Nullable;

/**
 * A resource identifier (type and FHIR id) together with the partition it lives in. Currently produced by
 * querying the resource-link table for the sources referencing a given target.
 */
// Created by claude-opus
public record ResourceIdWithPartition(@Nullable Integer partitionId, String resourceType, String fhirId) {
	public IdDt toIdDt() {
		return new IdDt(resourceType, fhirId);
	}

	public RequestPartitionId toRequestPartitionId() {
		return RequestPartitionId.fromPartitionId(partitionId);
	}
}
