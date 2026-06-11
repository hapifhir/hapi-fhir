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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommon;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommonRes;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenIdentifier;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.Constants;
import jakarta.annotation.Nullable;

/**
 * Maps an extracted {@link ResourceIndexedSearchParamToken} into the compressed token-index entities
 * ({@link ResourceIndexedSearchParamTokenCommon}, {@link ResourceIndexedSearchParamTokenCommonRes} and
 * {@link ResourceIndexedSearchParamTokenIdentifier}).
 */
public final class TokenIndexEntityConverter {

	private TokenIndexEntityConverter() {}

	public static ResourceIndexedSearchParamTokenCommon toCommon(
			ResourceIndexedSearchParamToken theSource, Long theSystemId) {
		return new ResourceIndexedSearchParamTokenCommon()
				.setHashSystemAndValue(theSource.getHashSystemAndValue())
				.setSystemId(theSystemId)
				.setValue(theSource.getValue())
				.setHashIdentity(theSource.getHashIdentity())
				.setHashValue(theSource.getHashValue());
	}

	public static ResourceIndexedSearchParamTokenCommonRes toCommonRes(
			ResourceIndexedSearchParamToken theSource, ResourceTable theEntity) {
		return new ResourceIndexedSearchParamTokenCommonRes(
						theEntity.getId().getId(),
						partitionIdOrNull(theSource.getPartitionId()),
						theSource.getHashSystemAndValue())
				.setResource(theEntity);
	}

	public static ResourceIndexedSearchParamTokenIdentifier toIdentifier(
			ResourceIndexedSearchParamToken theSource, ResourceTable theEntity, Long theSystemUrlId) {
		// TYPE_HASH_SYS_AND_VALUE is only meaningful for :of-type tokens
		Long typeHashSysAndValue = null;
		if (theSource.getParamName().endsWith(Constants.PARAMQUALIFIER_TOKEN_OF_TYPE)) {
			typeHashSysAndValue = theSource.getHashSystemAndValue();
		}

		return new ResourceIndexedSearchParamTokenIdentifier(
						partitionIdOrNull(theSource.getPartitionId()),
						theEntity.getId().getId(),
						theSource.getHashIdentity(),
						theSystemUrlId,
						theSource.getValue(),
						theSource.getHashValue(),
						typeHashSysAndValue)
				.setResource(theEntity);
	}

	@Nullable
	private static Integer partitionIdOrNull(@Nullable PartitionablePartitionId thePartitionId) {
		return thePartitionId != null ? thePartitionId.getPartitionId() : null;
	}
}
