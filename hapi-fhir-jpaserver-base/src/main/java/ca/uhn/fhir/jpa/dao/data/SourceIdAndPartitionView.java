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

import ca.uhn.fhir.model.primitive.IdDt;
import jakarta.annotation.Nullable;

/**
 * Projection of a referencing (source) resource discovered via the HFJ_RES_LINK index: its
 * type + FHIR id, plus the partition id of the source resource. The partition id lets callers
 * pin reads to the correct shard rather than relying on id-based partition resolution, which
 * fails for client-assigned (non-partition-decodable) ids in MegaScale Patient ID mode.
 * <p>
 * {@code partitionId} may be {@code null} for default-partition rows.
 */
// Created by claude-opus
public record SourceIdAndPartitionView(@Nullable Integer partitionId, String resourceType, String fhirId) {
	public IdDt toIdDt() {
		return new IdDt(resourceType, fhirId);
	}
}
