package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteExpungeProvider {
	private final MultiUrlProcessor myMultiUrlProcessor;

	public DeleteExpungeProvider(FhirContext theFhirContext, IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		myMultiUrlProcessor = new MultiUrlProcessor(theFhirContext, theDeleteExpungeJobSubmitter);
	}

	@Operation(name = ProviderConstants.OPERATION_DELETE_EXPUNGE, idempotent = false)
	public IBaseParameters deleteExpunge(
		@OperationParam(name = ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, typeName = "string", min = 1) List<IPrimitiveType<String>> theUrlsToDeleteExpunge,
		@OperationParam(name = ProviderConstants.OPERATION_DELETE_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		RequestDetails theRequestDetails
	) {
		if (theUrlsToDeleteExpunge == null) {
			throw new InvalidRequestException(Msg.code(1976) + "At least one `url` parameter to $delete-expunge must be provided.");
		}
		List<String> urls = theUrlsToDeleteExpunge.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
		Integer batchSize = myMultiUrlProcessor.getBatchSize(theBatchSize);
		return myMultiUrlProcessor.processUrls(urls, batchSize, theRequestDetails);
	}
}
