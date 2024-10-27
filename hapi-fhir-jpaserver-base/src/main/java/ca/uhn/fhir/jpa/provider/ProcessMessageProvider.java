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

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.endRequest;
import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.startRequest;

public class ProcessMessageProvider {

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	/**
	 * $process-message
	 */
	@Description("Accept a FHIR Message Bundle for processing")
	@Operation(name = JpaConstants.OPERATION_PROCESS_MESSAGE, idempotent = false)
	public IBaseBundle processMessage(
			HttpServletRequest theServletRequest,
			RequestDetails theRequestDetails,
			@OperationParam(name = "content", min = 1, max = 1, typeName = "Bundle")
					@Description(
							shortDefinition =
									"The message to process (or, if using asynchronous messaging, it may be a response message to accept)")
					IBaseBundle theMessageToProcess) {

		startRequest(theServletRequest);
		try {
			return mySystemDao.processMessage(theRequestDetails, theMessageToProcess);
		} finally {
			endRequest(theServletRequest);
		}
	}
}
