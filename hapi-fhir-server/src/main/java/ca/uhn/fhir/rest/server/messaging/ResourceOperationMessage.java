package ca.uhn.fhir.rest.server.messaging;

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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceOperationMessage extends BaseResourceModifiedMessage {

	public ResourceOperationMessage() {
	}

	public ResourceOperationMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		super(theFhirContext, theResource, theOperationType);
	}

	public ResourceOperationMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
		super(theFhirContext, theNewResource, theOperationType, theRequest);
	}

	/**
	 * If you are using a non-fhir-resource payload, you may set the payload directly here instead of using the constructor.
	 * @param thePayload the payload of the message.
	 */
	public void setPayload(String thePayload) {
		this.myPayload = thePayload;
	}
}
