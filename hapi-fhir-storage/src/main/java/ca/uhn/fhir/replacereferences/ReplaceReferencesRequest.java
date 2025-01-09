/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReplaceReferencesRequest {
	/**
	 * Unqualified source id
	 */
	@Nonnull
	public final IIdType sourceId;

	/**
	 * Unqualified target id
	 */
	@Nonnull
	public final IIdType targetId;

	public final int resourceLimit;

	public final RequestPartitionId partitionId;

	public boolean createProvenance = true;

	public ReplaceReferencesRequest(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			int theResourceLimit,
			RequestPartitionId thePartitionId,
			boolean theCreateProvenance) {
		sourceId = theSourceId.toUnqualifiedVersionless();
		targetId = theTargetId.toUnqualifiedVersionless();
		resourceLimit = theResourceLimit;
		partitionId = thePartitionId;
		createProvenance = theCreateProvenance;
	}

	public void validateOrThrowInvalidParameterException() {
		if (isBlank(sourceId.getResourceType())) {
			throw new InvalidRequestException(
					Msg.code(2585) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (isBlank(targetId.getResourceType())) {
			throw new InvalidRequestException(
					Msg.code(2586) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (!targetId.getResourceType().equals(sourceId.getResourceType())) {
			throw new InvalidRequestException(
					Msg.code(2587) + "Source and target id parameters must be for the same resource type");
		}
	}
}
