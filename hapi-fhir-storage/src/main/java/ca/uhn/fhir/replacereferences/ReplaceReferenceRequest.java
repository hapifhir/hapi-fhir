/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.StringParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;

import java.security.InvalidParameterException;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReplaceReferenceRequest {
	@Nonnull
	public final IIdType sourceId;

	@Nonnull
	public final IIdType targetId;

	public final int batchSize;

	public final RequestPartitionId partitionId;

	private boolean myForceSync = false;

	public ReplaceReferenceRequest(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			int theBatchSize,
			RequestPartitionId thePartitionId) {
		sourceId = theSourceId.toUnqualifiedVersionless();
		targetId = theTargetId.toUnqualifiedVersionless();
		batchSize = theBatchSize;
		partitionId = thePartitionId;
	}

	public void validateOrThrowInvalidParameterException() {
		if (isBlank(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2585) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (isBlank(targetId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2586) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (!targetId.getResourceType().equals(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2587) + "Source and target id parameters must be for the same resource type");
		}
	}

	public boolean isForceSync() {
		return myForceSync;
	}

	public void setForceSync(boolean forceSync) {
		this.myForceSync = forceSync;
	}
}
