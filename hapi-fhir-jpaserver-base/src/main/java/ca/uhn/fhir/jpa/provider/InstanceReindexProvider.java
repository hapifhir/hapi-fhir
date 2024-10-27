/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.search.reindex.IInstanceReindexService;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InstanceReindexProvider {

	private final IInstanceReindexService myInstanceReindexService;

	/**
	 * Constructor
	 */
	public InstanceReindexProvider(@Nonnull IInstanceReindexService theInstanceReindexService) {
		Validate.notNull(theInstanceReindexService);
		myInstanceReindexService = theInstanceReindexService;
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX_DRYRUN, idempotent = true, global = true)
	public IBaseParameters reindexInstanceDryRun(
			@IdParam IIdType theId,
			@OperationParam(name = "code", typeName = "code", min = 0, max = OperationParam.MAX_UNLIMITED)
					List<IPrimitiveType<String>> theCodes,
			RequestDetails theRequestDetails) {
		Set<String> codes = null;
		if (theCodes != null && theCodes.size() > 0) {
			codes = theCodes.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toSet());
		}

		return myInstanceReindexService.reindexDryRun(theRequestDetails, theId, codes);
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false, global = true)
	public IBaseParameters reindexInstance(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		return myInstanceReindexService.reindex(theRequestDetails, theId);
	}
}
