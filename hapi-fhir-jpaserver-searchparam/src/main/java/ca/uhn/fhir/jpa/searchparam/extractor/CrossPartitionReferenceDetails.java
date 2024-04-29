/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;

public class CrossPartitionReferenceDetails {

	@Nonnull
	private final RequestPartitionId mySourceResourcePartitionId;

	@Nonnull
	private final PathAndRef myPathAndRef;

	@Nonnull
	private final RequestDetails myRequestDetails;

	@Nonnull
	private final TransactionDetails myTransactionDetails;

	@Nonnull
	private final String mySourceResourceName;

	/**
	 * Constructor
	 */
	public CrossPartitionReferenceDetails(
			@Nonnull RequestPartitionId theSourceResourcePartitionId,
			@Nonnull String theSourceResourceName,
			@Nonnull PathAndRef thePathAndRef,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		mySourceResourcePartitionId = theSourceResourcePartitionId;
		mySourceResourceName = theSourceResourceName;
		myPathAndRef = thePathAndRef;
		myRequestDetails = theRequestDetails;
		myTransactionDetails = theTransactionDetails;
	}

	@Nonnull
	public String getSourceResourceName() {
		return mySourceResourceName;
	}

	@Nonnull
	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	@Nonnull
	public TransactionDetails getTransactionDetails() {
		return myTransactionDetails;
	}

	@Nonnull
	public RequestPartitionId getSourceResourcePartitionId() {
		return mySourceResourcePartitionId;
	}

	@Nonnull
	public PathAndRef getPathAndRef() {
		return myPathAndRef;
	}
}
