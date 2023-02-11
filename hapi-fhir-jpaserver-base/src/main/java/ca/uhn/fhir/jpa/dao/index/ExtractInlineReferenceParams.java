package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ExtractInlineReferenceParams {
	private IBaseResource myResource;
	private TransactionDetails myTransactionDetails;
	private RequestDetails myRequestDetails;
	private boolean myFailOnInvalidReferences;

	public ExtractInlineReferenceParams(
		IBaseResource theResource,
		TransactionDetails theTransactionDetails,
		RequestDetails theRequest
	) {
		myResource = theResource;
		myTransactionDetails = theTransactionDetails;
		myRequestDetails = theRequest;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public void setResource(IBaseResource theResource) {
		myResource = theResource;
	}

	public TransactionDetails getTransactionDetails() {
		return myTransactionDetails;
	}

	public void setTransactionDetails(TransactionDetails theTransactionDetails) {
		myTransactionDetails = theTransactionDetails;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}

	public boolean isFailOnInvalidReferences() {
		return myFailOnInvalidReferences;
	}

	public void setFailOnInvalidReferences(boolean theFailOnInvalidReferences) {
		myFailOnInvalidReferences = theFailOnInvalidReferences;
	}
}
