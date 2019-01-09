package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoMessageHeader;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MessageHeader;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import javax.servlet.http.HttpServletRequest;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class BaseJpaResourceProviderMessageHeaderDstu3 extends JpaResourceProviderDstu3<MessageHeader> {


	/**
	 * /MessageHeader/$process-message
	 */
	@Operation(name = JpaConstants.OPERATION_PROCESS_MESSAGE, idempotent = false)
	public IBaseBundle processMessage(
		HttpServletRequest theServletRequest,
		RequestDetails theRequestDetails,

		@OperationParam(name = "content", min = 1, max = 1)
		@Description(formalDefinition = "The message to process (or, if using asynchronous messaging, it may be a response message to accept)")
			Bundle theMessageToProcess
	) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoMessageHeader<MessageHeader>) getDao()).messageHeaderProcessMessage(theRequestDetails, theMessageToProcess);
		} finally {
			endRequest(theServletRequest);
		}

	}


}
