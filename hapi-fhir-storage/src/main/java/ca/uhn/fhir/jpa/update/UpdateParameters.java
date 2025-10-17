/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.update;

import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class UpdateParameters<T extends IBaseResource> {

	private RequestDetails myRequestDetails;
	private IIdType myResourceIdToUpdate;
	private String myMatchUrl;
	private boolean myShouldPerformIndexing;
	private boolean myShouldForceUpdateVersion;
	private T myExistingResource;
	private IBasePersistedResource myExistingEntity;
	private RestOperationTypeEnum myOperationType;
	private TransactionDetails myTransactionDetails;

	/**
	 * In the update methods, we have a local variable to keep track of the old resource before performing the update
	 * This old resource may be useful for processing/performing checks (eg. enforcing Patient compartment changes with {@link ca.uhn.fhir.jpa.interceptor.PatientCompartmentEnforcingInterceptor}
	 * However, populating this can be expensive, and is skipped in Mass Ingestion mode
	 *
	 * If this is set to true, the old resource will be forcefully populated.
	 */
	private boolean myShouldForcePopulateOldResourceForProcessing;

	public RequestDetails getRequest() {
		return myRequestDetails;
	}

	public UpdateParameters<T> setRequestDetails(RequestDetails theRequest) {
		this.myRequestDetails = theRequest;
		return this;
	}

	public IIdType getResourceIdToUpdate() {
		return myResourceIdToUpdate;
	}

	public UpdateParameters<T> setResourceIdToUpdate(IIdType theResourceId) {
		this.myResourceIdToUpdate = theResourceId;
		return this;
	}

	public String getMatchUrl() {
		return myMatchUrl;
	}

	public UpdateParameters<T> setMatchUrl(String theMatchUrl) {
		this.myMatchUrl = theMatchUrl;
		return this;
	}

	public boolean shouldPerformIndexing() {
		return myShouldPerformIndexing;
	}

	public UpdateParameters<T> setShouldPerformIndexing(boolean thePerformIndexing) {
		this.myShouldPerformIndexing = thePerformIndexing;
		return this;
	}

	public boolean shouldForceUpdateVersion() {
		return myShouldForceUpdateVersion;
	}

	public UpdateParameters<T> setShouldForceUpdateVersion(boolean theForceUpdateVersion) {
		this.myShouldForceUpdateVersion = theForceUpdateVersion;
		return this;
	}

	public T getResource() {
		return myExistingResource;
	}

	public UpdateParameters<T> setResource(T theResource) {
		this.myExistingResource = theResource;
		return this;
	}

	public IBasePersistedResource getEntity() {
		return myExistingEntity;
	}

	public UpdateParameters<T> setEntity(IBasePersistedResource theEntity) {
		this.myExistingEntity = theEntity;
		return this;
	}

	public RestOperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public UpdateParameters<T> setOperationType(RestOperationTypeEnum myOperationType) {
		this.myOperationType = myOperationType;
		return this;
	}

	public TransactionDetails getTransactionDetails() {
		return myTransactionDetails;
	}

	public UpdateParameters<T> setTransactionDetails(TransactionDetails myTransactionDetails) {
		this.myTransactionDetails = myTransactionDetails;
		return this;
	}

	public boolean shouldForcePopulateOldResourceForProcessing() {
		return myShouldForcePopulateOldResourceForProcessing;
	}

	public UpdateParameters<T> setShouldForcePopulateOldResourceForProcessing(
			boolean myShouldForcePopulateOldResourceForProcessing) {
		this.myShouldForcePopulateOldResourceForProcessing = myShouldForcePopulateOldResourceForProcessing;
		return this;
	}
}
