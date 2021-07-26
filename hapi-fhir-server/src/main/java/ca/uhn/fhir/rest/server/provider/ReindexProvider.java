package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IReindexJobSubmitter;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ReindexProvider extends BaseMultiUrlProcessor {
	public ReindexProvider(FhirContext theFhirContext, IReindexJobSubmitter theReindexJobSubmitter) {
		super(theFhirContext, theReindexJobSubmitter);
	}

	// FIXME KHS test
	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false)
	public IBaseParameters Reindex(
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_URL, typeName = "string", min = 0, max = 1) List<IPrimitiveType<String>> theUrlsToReindex,
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_EVERYTHING, typeName = "boolean", min = 0, max = 1) IPrimitiveType<Boolean> theEverything,
		RequestDetails theRequestDetails
	) {
		Boolean everything = theEverything != null && theEverything.getValue();
		@Nullable Integer batchSize = getBatchSize(theBatchSize);
		if (everything) {
			return super.processEverything(batchSize, theRequestDetails);
		} else {
			List<String> urls = theUrlsToReindex.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
			return super.processUrls(urls, batchSize, theRequestDetails);
		}
	}
}
